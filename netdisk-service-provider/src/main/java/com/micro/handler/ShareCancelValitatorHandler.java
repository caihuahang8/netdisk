package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ShareCancelRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
@Component
public class ShareCancelValitatorHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareCancelRequest){
            ShareCancelRequest bean=(ShareCancelRequest) request;

            if(CollectionUtils.isEmpty(bean.getIds())){
                throw new RuntimeException("请选择取消分享的记录");
            }
        }else{
            throw new RuntimeException("ShareCancelValidateHandler==参数不对");
        }
    }
}
