package com.micro.handler;


import com.micro.common.ValidateUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ShareSaveRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class ShareSaveValidateHandler extends Handler {

	@Override
	public void doHandler(ContextRequest request, ContextResponse response) {
		if(request instanceof ShareSaveRequest){
			ShareSaveRequest bean=(ShareSaveRequest) request;

			ValidateUtils.validate(bean.getUserid(), "用户ID");
			ValidateUtils.validate(bean.getUsername(), "用户姓名");
			ValidateUtils.validate(bean.getFolderid(), "文件夹ID");
			ValidateUtils.validate(bean.getShareid(), "分享ID");
			if(CollectionUtils.isEmpty(bean.getIds())){
				throw new RuntimeException("请勾选需要转存的文件");
			}
		}else{
			throw new RuntimeException("ShareSaveValidateHandler==参数不对");
		}
	}

}
