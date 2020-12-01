package com.micro.handler;


import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.service.UserCapacityService;

import com.micro.param.ShareSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShareSaveCapacityUpdateHandler extends Handler {
	@Autowired
	private UserCapacityService userCapacityService;


	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;

			userCapacityService.updateUserCapacity(0, bean.getCapacity(), bean.getUserid(), bean.getUsername(),"分享文件保存");
		}else{
			throw new RuntimeException("ShareSaveCapacityUpdateHandler==参数不对");
		}
	}
}
