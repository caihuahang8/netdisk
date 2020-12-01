package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.dao.DiskShareFriendsDao;
import com.micro.model.DiskShare;
import com.micro.param.ShareFriendsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ShareFriendsSaveHandler extends Handler {
    @Autowired
    private DiskShareDao diskShareDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof ShareFriendsRequest){
            ShareFriendsRequest bean=(ShareFriendsRequest) request;

            DiskShare share=new DiskShare();
            share.setShareuserid(bean.getUserid());
            share.setShareusername(bean.getUsername());
            share.setSharetime(new Date());
            share.setTitle(bean.getTitle());
            share.setType(1);//0私密链接分享，1好友分享
            share.setStatus(0);
            share.setSavecount(0);
            diskShareDao.save(share);

            bean.setShareid(share.getId());
            this.updateRequest(bean);
        }else{
            throw new RuntimeException("ShareFriendsSaveHandler==参数不对");
        }
    }
}
