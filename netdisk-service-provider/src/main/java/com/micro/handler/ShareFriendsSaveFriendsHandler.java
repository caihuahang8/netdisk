package com.micro.handler;


import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareFriendsDao;
import com.micro.disk.bean.ShareFriendsBean;
import com.micro.model.DiskShareFriends;
import com.micro.param.ShareFriendsRequest;
import com.micro.param.ShareSecretRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShareFriendsSaveFriendsHandler extends Handler {
	@Autowired
	private DiskShareFriendsDao diskShareFriendsDao;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(true){
			ShareFriendsRequest bean=(ShareFriendsRequest) request;

			List<ShareFriendsBean> friends=bean.getFriends();
			for(ShareFriendsBean friend:friends){
				DiskShareFriends dsf=new DiskShareFriends();
				dsf.setShareid(bean.getShareid());
				dsf.setUserid(friend.getUserid());
				dsf.setUsername(friend.getUsername());
				diskShareFriendsDao.save(dsf);
			}
		}else{
			throw new RuntimeException("ShareFriendsSaveFriendsHandler==参数不对");
		}
	}
}
