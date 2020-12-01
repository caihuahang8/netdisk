package com.micro.handler;

import com.micro.common.DateUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.model.DiskFile;
import com.micro.modeltree.DiskFileRubbishTree;
import com.micro.param.FileRubbishRecoverRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class FileRubbishRecoverSavaDiskFileHandler extends Handler {
    @Autowired
    DiskFileDao diskFileDao;
    @Autowired
    DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishRecoverRequest){
            FileRubbishRecoverRequest bean = (FileRubbishRecoverRequest) request;
            List<DiskFileRubbishTree> files=bean.getChildren();
            List<String> rediskeys=new ArrayList<>();
            String pid=bean.getFolderid();
            String userid=bean.getUserid();
            dgRecover(bean.getChildren(),rediskeys, pid, userid);
            //下一个Handler
            bean.setRediskeys(rediskeys);
            this.updateRequest(request);
        }
    }
    public void dgRecover(List<DiskFileRubbishTree> files,List<String> rediskeys,String pid,String userid){
        if(!CollectionUtils.isEmpty(files)){
            for(DiskFileRubbishTree tree:files){
                DiskFile diskFile=null;
                if(tree.getFiletype()==0){
                    diskFile=diskFileDao.findFolderNameIsExist(userid,pid, tree.getFilename());
                }else if(tree.getFiletype()==1){
                    diskFile=diskFileDao.findFileNameIsExist(userid,pid, tree.getFilemd5(), tree.getFilename());
                }

                //添加disk_file
                String fileid="";
                if(diskFile==null){
                    DiskFile df=new DiskFile();
                    BeanUtils.copyProperties(tree, df);
                    df.setId(null);
                    df.setCreatetime(new Date());
                    df.setPid(pid);
                    diskFileDao.save(df);
                    fileid=df.getId();
                }else{
                    if(tree.getFiletype()==0){
                        DiskFile df=new DiskFile();
                        BeanUtils.copyProperties(tree, df);
                        df.setId(null);
                        df.setFilename(tree.getFilename()+"("+ DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss.S")+")");
                        df.setCreatetime(new Date());
                        df.setPid(pid);
                        diskFileDao.save(df);
                        fileid=df.getId();
                    }else{
                        fileid=diskFile.getId();
                    }
                }

                //收集数据
                rediskeys.add(tree.getId());

                //删除disk_file_del
                diskFileDelDao.deleteById(tree.getId());

                //递归
                dgRecover(tree.getChildren(),rediskeys, fileid,userid);
            }
        }
    }

}
