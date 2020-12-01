package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.Contanst;
import com.micro.common.DateUtils;
import com.micro.core.Bootstrap;
import com.micro.core.HandlerInitializer;
import com.micro.core.Pipeline;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.jdbc.DiskShareFileJdbc;
import com.micro.db.jdbc.DiskShareJdbc;
import com.micro.disk.bean.*;
import com.micro.disk.service.ShareService;
import com.micro.handler.*;
import com.micro.model.DiskShare;
import com.micro.param.ShareCancelRequest;
import com.micro.param.ShareFriendsRequest;
import com.micro.param.ShareSecretRequest;
import com.micro.param.ShareSecretResponse;
import com.micro.utils.SpringContentUtils;
import com.micro.utils.ValidateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass=ShareService.class)
@Component
@Transactional
public class ShareServiceImpl implements ShareService {
    @Autowired
    private SpringContentUtils springContentUtils;
    @Autowired
    private DiskShareJdbc diskShareJdbc;
    @Autowired
    private DiskShareFileJdbc diskShareFileJdbc;
    @Autowired
    private DiskShareDao diskShareDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public ShareBean shareSecret(List<String> ids, String title, String userid, String username, final Integer sharetype, Integer effect, Integer type){
        ShareSecretRequest bean = new ShareSecretRequest();
        bean.setEffect(effect);
        bean.setIds(ids);
        bean.setSharetype(sharetype);
        bean.setType(type);
        bean.setUsername(username);
        bean.setTitle(title);
        bean.setUserid(userid);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(bean,new ShareSecretResponse()){
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数校验
                pipeline.addLast(springContentUtils.getHandler(ShareSecretValidateHandler.class));
                //2.保存到disk_share
                pipeline.addLast(springContentUtils.getHandler(ShareSaveDiskShareHandler.class));
                //3.保存到disk_share_file
                pipeline.addLast(springContentUtils.getHandler(ShareSaveDiskShareFileHandler.class));
                //4.添加到redis
                pipeline.addLast(springContentUtils.getHandler(ShareSecretRedisSaveHandler.class));
            }
        });
        ShareSecretResponse response = (ShareSecretResponse) bootstrap.execute();
        ShareBean shareBean = new ShareBean();
        shareBean.setCode(response.getCode());
        shareBean.setUrl(response.getUrl());
        return shareBean;
    }
    @Override
    public PageInfo<Share> findMyShare(Integer page,Integer limit,String userid,Integer type,Integer status){
        return diskShareJdbc.findMyShare(page,limit,userid,type,status);
    }
    @Override
    public List<ShareFileBean> findShareFileListBySelf(String shareid,String pid){
        ValidateUtils.validate(shareid, "分享id");
        if (!StringUtils.isEmpty(pid)){
            pid = "0";
        }
        return  diskShareJdbc.findListByPid(shareid,pid);
    }
    @Override
    public void  validateShareIsEffect(String shareid){
        ValidateUtils.validate(shareid,"分享id");
        DiskShare diskShare = diskShareDao.findOne(shareid);
        if (diskShare.getType()==0){
            if (diskShare.getEffect()!=0){
                if(diskShare.getEndtime().before(new Date())){
                    throw new RuntimeException("该分享已经失效");
                }
            }
            if(diskShare.getStatus()==1){
                throw new RuntimeException("该分享已经失效");
            }
            if(diskShare.getStatus()==2){
                throw new RuntimeException("该分享已经被取消了");
            }
        }
    }
    @Override
    public PageInfo<Share> findFriendsShare(Integer page, Integer limit, String userid, Integer status){
        return diskShareJdbc.findFriendsShare(page,limit,userid,status);
    }
    @Override
    public void cancelShare(List<String> ids){
        ShareCancelRequest request = new ShareCancelRequest();
        request.setIds(ids);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(request,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.基本参数校验
                pipeline.addLast(springContentUtils.getHandler(ShareCancelValitatorHandler.class));
                //2.分享状态更改为取消
                pipeline.addLast(springContentUtils.getHandler(ShareCancelStatusHandler.class));
                //3.redis监听取消
                pipeline.addLast(springContentUtils.getHandler(ShareCancelRedisHandler.class));
                //4.推送
            }
        });
        bootstrap.execute();
    }
    @Override
    public List<ShareFriendsBean> findFriends(String shareid){
        return diskShareJdbc.findFriends(shareid);
    }
    @Override
    public List<ShareFileBean> findShareFileListFromFriends(String id,String pid){
        ValidateUtils.validate(id, "分享ID");
        if(StringUtils.isEmpty(pid)){
            pid="0";
        }
        DiskShare share=diskShareDao.findOne(id);
        if(share==null){
            throw new RuntimeException("找不到分享记录");
        }
        if(share.getStatus()==1){
            throw new RuntimeException("该分享已失效");
        }
        if(share.getStatus()==2){
            throw new RuntimeException("该分享已取消");
        }

        //查询列表
        return diskShareFileJdbc.findListByPid(id, pid);
    }
    @Override
    public Share findShareInfo(String id) {
        if (org.springframework.util.StringUtils.isEmpty(id)){
            throw new RuntimeException("分享id为空");
        }
        DiskShare diskShare = diskShareDao.findOne(id);
        Share shareBean = new Share();
        shareBean.setUrl(diskShare.getUrl());
        shareBean.setCode(diskShare.getCode());
        String effectname = diskShare.getEffect().toString();
        if("0".equals(effectname)){
            effectname="永久有效";
        }else{
            effectname=effectname+"天";
        }
        shareBean.setEffectname(effectname);
        shareBean.setId(diskShare.getId());
        shareBean.setType(diskShare.getType());
        shareBean.setTitle(diskShare.getTitle());
        shareBean.setStatus(diskShare.getStatus());
        shareBean.setShareuser(diskShare.getShareusername());
        shareBean.setSharetype(diskShare.getSharetype());
        shareBean.setSavecount(diskShare.getSavecount());
        shareBean.setSharetime(DateUtils.formatDate(diskShare.getSharetime(),"yyyy-MM-dd HH:mm:ss"));
        return shareBean;
    }
    @Override
    public List<ShareFileBean> findShareFileListFromSecret(String id,String pid,String token){
        if (StringUtils.isEmpty(id)){
            throw new RuntimeException("分享id不存在");
        }
        if (StringUtils.isEmpty(pid)){
            pid="0";
        }
        id=id.toLowerCase();
        DiskShare diskShare = diskShareDao.findOne(id);
        if(diskShare.getType()==0&&diskShare.getSharetype()==0){
            String value = stringRedisTemplate.opsForValue().get(Contanst.PREFIX_SHARE_CODE+token);
            if (StringUtils.isEmpty(value)){
                throw new RuntimeException("您尚未认证提取码或提取码已经失效,请重新认证!");
            }else{
                stringRedisTemplate.expire(Contanst.PREFIX_SHARE_CODE+token, 10, TimeUnit.MINUTES);
            }
        }
        //验证是否已否过期
        if(diskShare.getStatus()==1){
            throw new RuntimeException("该分享已失效");
        }
        //验证是否是否取消
        if(diskShare.getStatus()==2){
            throw new RuntimeException("该分享已取消");
        }
        //查询列表
        return diskShareFileJdbc.findListByPid(id, pid);
    }

    @Override
    public String validateCode(String id, String code) {
        ValidateUtils.validate(id, "分享ID");
        ValidateUtils.validate(code, "提取码");
        id=id.toLowerCase();
        DiskShare share=diskShareDao.findOne(id);
        if(share==null){
            throw new RuntimeException("该链接地址无效");
        }
        if(share.getType()!=0){
            throw new RuntimeException("不是私密链接分享");
        }
        if(share.getSharetype()!=0){
            throw new RuntimeException("不是有码提取");
        }
        if(!share.getCode().equals(code)){
            throw new RuntimeException("提取码不正确");
        }
        if(share.getEffect()!=0){
            if(share.getEndtime().before(new Date())){
                throw new RuntimeException("该链接已经失效");
            }
        }
        if(share.getStatus()==1){
            throw new RuntimeException("该链接已经失效");
        }
        if(share.getStatus()==2){
            throw new RuntimeException("该链接已经取消分享");
        }
        String token= UUID.randomUUID().toString();
        if(share.getType()==0&&share.getSharetype()==0){
            //私密链接且有码提取，则生成token
            stringRedisTemplate.opsForValue().set(Contanst.PREFIX_SHARE_CODE+token, token, 10, TimeUnit.MINUTES);
            return token;
        }
        return "";
    }
    @Override
    public void shareFriends(List<String> ids,List<ShareFriendsBean> friends,String title,String userid,String username,Integer type) {
        ShareFriendsRequest request=new ShareFriendsRequest();
        request.setIds(ids);
        request.setFriends(friends);
        request.setTitle(title);
        request.setUserid(userid);
        request.setUsername(username);
        request.setType(type);

        Bootstrap bootstrap=new Bootstrap();
        bootstrap.childHandler(new HandlerInitializer(request,null) {
            @Override
            protected void initChannel(Pipeline pipeline) {
                //1.参数校验
                pipeline.addLast(springContentUtils.getHandler(ShareFriendsValidateHandler.class));
                //2.保存disk_share
                pipeline.addLast(springContentUtils.getHandler(ShareFriendsSaveHandler.class));
                //3.保存disk_share_friends
                pipeline.addLast(springContentUtils.getHandler(ShareFriendsSaveFriendsHandler.class));
                //5.保存disk_share_file（文件分享）
                pipeline.addLast(springContentUtils.getHandler(ShareFriendsFromFileHandler.class));
                //6.保存通知、推送好友
                //pipeline.addLast(springContentUtils.getHandler(ShareFriendsNoticeHandler.class));
                //7.最近操作
            }
        });
        bootstrap.execute();
    }
}
