package com.micro.handler;


import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.service.ShareService;
import com.micro.param.ShareSaveRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ShareSaveEffectHandler extends Handler {
	@Autowired
	private ShareService shareService;

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;

			shareService.validateShareIsEffect(bean.getShareid());
		}else{
			throw new RuntimeException("ShareSaveEffectHandler==参数不对");
		}
	}
}
