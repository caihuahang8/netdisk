package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.common.CapacityUtils;
import com.micro.common.Contanst;
import com.micro.common.DateUtils;
import com.micro.core.Bootstrap;
import com.micro.core.ContextResponse;
import com.micro.core.HandlerInitializer;
import com.micro.core.Pipeline;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.db.dialect.IJdbcTemplate;
import com.micro.db.jdbc.DiskFileJdbc;
import com.micro.disk.bean.*;
import com.micro.disk.service.FileService;
import com.micro.handler.*;
import com.micro.lock.LockContext;
import com.micro.model.DiskFile;
import com.micro.model.DiskMd5;
import com.micro.param.*;
import com.micro.store.utils.FileUtils;
import com.micro.utils.SpringContentUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;


import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Service(interfaceClass=FileService.class,timeout=120000) //Dubbo注解
@Component //Spring注解
@Transactional
public class FileServiceImpl implements FileService {
    @NacosValue(value="${locktype}",autoRefreshed=true)
    private String locktype;
    @Autowired
    SpringContentUtils springContentUtils;
    @NacosValue(value="${lockhost}",autoRefreshed=true)
    private String lockhost;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DiskFileDao diskFileDao;
    @Autowired
    DiskFileJdbc diskFileJdbc;
    @Autowired
    DiskMd5Dao diskMd5Dao;
    @Override
    public void uploadChunk(Chunk chunk) {
        ChunkRequest request = new ChunkRequest();
        BeanUtils.copyProperties(chunk,request);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(request,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数验证
                pipeline.addLast(springContentUtils.getHandler(ChunkValidateHandler.class));
                //2.校验是否支持格式
                pipeline.addLast(springContentUtils.getHandler(ChunkFileSuffixHandler.class));
                //3.切块存储
                pipeline.addLast(springContentUtils.getHandler(ChunkStoreHandler.class));
                //4.把记录存在Redis
                pipeline.addLast(springContentUtils.getHandler(ChunkRedisHandler.class));
            }
        });
        bootstrap.execute();
    }

    @Override
    public void mergeChunk(MergeFileBean bean) {
        String lockname=bean.getFilemd5();
        LockContext lockContext = new LockContext(locktype,lockhost);
        try{
            if (!StringUtils.isEmpty(bean.getRelativepath())){
                String[] names = bean.getRelativepath().split("/");
                String userid = bean.getUserid();
                String folderid=bean.getPid();
                lockname="CREATEFOLDER"+userid+"-"+folderid+"-"+names[0];
            }
            //获取锁
            lockContext.getLock(lockname);
            MergeRequest request = new MergeRequest();
            BeanUtils.copyProperties(bean,request);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.childHandler(new HandlerInitializer(request,null) {
                @Override
                protected void initChannel(Pipeline pipeline) {
                    //1.基本参数校验
                    pipeline.addLast(springContentUtils.getHandler(MergeValidateHander.class));
                    //2.校验容量是否足够
                    pipeline.addLast(springContentUtils.getHandler(MergeCapacityIsEnoughHandler.class));
                    //3.从redis获取q切块记录
                    pipeline.addLast(springContentUtils.getHandler(MergeGetChunkHandler.class));
                    //4.校验文件的大小
                    pipeline.addLast(springContentUtils.getHandler(MergeFileIsBreakHandler.class));
                    //5.查询文件是否已经在md5存在了
                    pipeline.addLast(springContentUtils.getHandler(MergeFileIsExistHandler.class));
                    //6.保存disk_md5表
                    pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskMd5Handler.class));
                    //7.保存disk_chunk表
                    pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskMd5ChunkHandler.class));
                    //8.如果是文件夹上传，则先创建文件夹
                    pipeline.addLast(springContentUtils.getHandler(MergeCreateFolderHandler.class));
                    //9.保存disk_file表
                    pipeline.addLast(springContentUtils.getHandler(MergeSaveDiskFileHandler.class));
                    //10.如果是图片则裁剪；如果是视频则截图
                    pipeline.addLast(springContentUtils.getHandler(MergeSpecialDealHandler.class));
                    //11.如果是相册上传图片，则关联相册
                    pipeline.addLast(springContentUtils.getHandler(MergeGlThumnailHandler.class));
                    //12.更新容量、推送容量
                    pipeline.addLast(springContentUtils.getHandler(MergeCapacityUpdateHandler.class));
                    //13.新增Solr
                    pipeline.addLast(springContentUtils.getHandler(MergeSolrHandler.class));
                }
            });
            bootstrap.execute();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }finally {
            try {
                String key= Contanst.PREFIX_CHUNK_TEMP+"-"+bean.getUserid()+"-"+bean.getUuid()+"-"+bean.getFileid()+"-"+bean.getFilename()+"-*";
                Set<String> keys = stringRedisTemplate.keys(key);
                stringRedisTemplate.delete(keys);
            }catch (Exception e){

            }finally {
                lockContext.unLock(lockname);
            }
        }
    }

    //工具类，作用是从Spring容器获取对象
    @Override
    public FileBean findOne(String id){
        DiskFile file = diskFileDao.findOne(id);
        FileBean fileBean = new FileBean();
        BeanUtils.copyProperties(file,fileBean);
        fileBean.setCreatetime(DateUtils.formatDate(file.getCreatetime(), "yyyy-MM-dd HH:mm:ss"));
        fileBean.setFilesize(CapacityUtils.convert(file.getFilesize()));
        return fileBean;
    }

    @Override
    public PageInfo<FileListBean> findFileList(Integer page, Integer limit, String userid, String pid, String typecode, String orderfield, String ordertype) {
        return diskFileJdbc.findAllList(page, limit, userid, pid, typecode, orderfield, ordertype);
    }

    @Override
    public FileBean addFile(final String pid, String filename, byte[] bytes, String username, String userid) {
        String fileMd5  =  DigestUtils.md5DigestAsHex(bytes);
        LockContext lockContext = new LockContext(locktype,lockhost);
        try{
        lockContext.getLock(fileMd5);
        FileEditRequest fileEditRequest = new FileEditRequest();
        fileEditRequest.setBytes(bytes);
        fileEditRequest.setFilename(filename);
        fileEditRequest.setPid(pid);
        fileEditRequest.setUsername(username);
        fileEditRequest.setUserid(userid);
        fileEditRequest.setFileMd5(fileMd5);
        fileEditRequest.setFilesuffix(FileUtils.getFileSuffix(filename));
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(fileEditRequest,new FileEditResponse()) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.基本信息校验
                pipeline.addLast(springContentUtils.getHandler(FileEditValidateHander.class));
                //2.检验容量是否足够
                pipeline.addLast(springContentUtils.getHandler(FileEditCapacityIsEnoughHandler.class));
                //3.判断md5是否存在
                pipeline.addLast(springContentUtils.getHandler(FileEditMd5IsExist.class));
                //4.判断是文件还是文件夹
                pipeline.addLast(springContentUtils.getHandler(FileEditCreateFolderHandler.class));
                //4.下载到本地
                pipeline.addLast(springContentUtils.getHandler(FileEditDownloadHandler.class));
                //5.保存到disk_md5
                pipeline.addLast(springContentUtils.getHandler(FileEditSaveDiskMd5Handler.class));
                //7.保存disk_chunk表
                pipeline.addLast(springContentUtils.getHandler(FileEditSaveDiskMd5ChunkHandler.class));
                //6.保存到disk_file
                pipeline.addLast(springContentUtils.getHandler(FileEditSaveDiskFileHandler.class));
                //7.更新扩容
                pipeline.addLast(springContentUtils.getHandler(FileEditUpdateDiskCapacityHandler.class));
            }
        });
        FileEditResponse fileEditResponse = (FileEditResponse) bootstrap.execute();
        FileBean fileBean = new FileBean();
        fileBean.setId(fileEditResponse.getId());
        fileBean.setFilename(filename);
        fileBean.setFilemd5(fileMd5);
        return fileBean;
        }catch (Exception e ){
            throw new RuntimeException(e.getMessage());
        }finally {
            lockContext.unLock(fileMd5);
        }
    }

    @Override
    public void delete(String createuserid, String createusername, List<String> ids) {
        FileDelRequest fileDelRequest = new FileDelRequest();
        fileDelRequest.setCreateuserid(createuserid);
        fileDelRequest.setCreateusername(createusername);
        fileDelRequest.setIds(ids);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(fileDelRequest,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数验证
                pipeline.addLast(springContentUtils.getHandler(FileDelValidateHandler.class));
                //2.查找删除的文件，保存到rubbish集合
                pipeline.addLast(springContentUtils.getHandler(FileDelGetDataHandler.class));
                //3.删除文件
                pipeline.addLast(springContentUtils.getHandler(FileDelRubbishHandler.class));
                //4.设置redis,进行监听，回收站过期自动删除
                pipeline.addLast(springContentUtils.getHandler(FileDelRedisHandler.class));
                //5.更新容量，新增容量
                pipeline.addLast(springContentUtils.getHandler(FileDelUptateCapacityHandler.class));
            }
        });
        bootstrap.execute();
    }

    @Override
    public void rename(String fileid, String filename, String userid) {
        FileRenameRequest fileRenameRequest = new FileRenameRequest();
        fileRenameRequest.setFilename(filename);
        fileRenameRequest.setId(fileid);
        fileRenameRequest.setUserid(userid);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(fileRenameRequest,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数校验
                pipeline.addLast(springContentUtils.getHandler(FileRenameValidateHandler.class));
                //2.是否存在文件
                pipeline.addLast(springContentUtils.getHandler(FileRenameIsExistHandler.class));
                //3.重命名disk_file
                pipeline.addLast(springContentUtils.getHandler(FileRenameSaveDiskFile.class));
                //4.更新solr
            }
        });
        bootstrap.execute();
    }

    @Override
    public FolderPropertyBean findFolderProperty(String pid, String userid) {
        FolderPropertyBean fpb = new FolderPropertyBean();
        fpb.setFilenum(0);
        fpb.setFoldernum(0);
        fpb.setTotalsize(0L);

        diSearchFolderProperty(pid,userid,fpb);
        fpb.setTotalsizename(CapacityUtils.convert(fpb.getTotalsize()));
        return fpb;
    }
    private void diSearchFolderProperty(String pid,String userid,FolderPropertyBean fpb){
      List<DiskFile> dfs =   diskFileDao.findListByPid(userid,pid);
      if (!CollectionUtils.isEmpty(dfs)){
          for (DiskFile df : dfs ){
              fpb.setTotalsize(fpb.getTotalsize()+df.getFilesize());
              if (df.getFilesize()==1){
                  //文件数量+1
                  fpb.setFilenum(fpb.getFilenum()+1);
              }else if (df.getFiletype()==0){
                  fpb.setFoldernum(fpb.getFoldernum()+1);
              }
              //递归查找文件夹属性
              diSearchFolderProperty(df.getId(),userid,fpb);
          }
      }
    }
    @Override
    public void saveFromShare(String userid,String username, String folderid,String shareid, List<String> ids) {
        String lockname=shareid;
        LockContext lockContext = new LockContext(locktype,lockhost);
        try {
            lockContext.getLock(lockname);
            ShareSaveRequest request = new ShareSaveRequest();
            shareid = shareid.toLowerCase();
            request.setUserid(userid);
            request.setUsername(username);
            request.setFolderid(folderid);
            request.setShareid(shareid);
            request.setIds(ids);

            Bootstrap bootstrap = new Bootstrap();
            bootstrap.childHandler(new HandlerInitializer(request, null) {
                @Override
                protected void initChannel(Pipeline pipeline) {
                    //1.参数校验
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveValidateHandler.class));
                    //2.校验分享是否有效
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveEffectHandler.class));
                    //3.递归获取数据
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveGetDataHandler.class));
                    //4.判断容量是否足够
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveCapacityEnoughHandler.class));
                    //5.保存
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveInsertHandler.class));
                    //6.保存Solr
                    //7.更新容量、推送
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveCapacityUpdateHandler.class));
                    //8.分享日志（转存数量、转存明细）
                    pipeline.addLast(springContentUtils.getHandler(ShareSaveLogHandler.class));
                    //9.最近操作
                }
            });
            bootstrap.execute();
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }finally {
            lockContext.unLock(lockname);
        }
    }

    @Override
    public void addFolder(String filename, String userid, String username, final String pid) {
        FileAddFolderRequest fileAddFolderRequest = new FileAddFolderRequest();
        fileAddFolderRequest.setFilename(filename);
        fileAddFolderRequest.setPid(pid);
        fileAddFolderRequest.setUserid(userid);
        fileAddFolderRequest.setUsername(username);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(fileAddFolderRequest,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.基本参数校验
                pipeline.addLast(springContentUtils.getHandler(FileAddFolderValitatorHandler.class));
                //2.保存到disk_file
                pipeline.addLast(springContentUtils.getHandler(FileAddFolderSaveDiskFile.class));
            }
        });
        bootstrap.execute();
    }
    @Override
    public Integer checkFile(String filemd5) {
        String lockname=filemd5;
        try{
            //lockContext.getLock(lockname);
            DiskMd5 diskMd5=diskMd5Dao.findMd5IsExist(filemd5);
            return diskMd5==null?0:1;
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }finally{
            //lockContext.unLock(lockname);
        }
    }
}
