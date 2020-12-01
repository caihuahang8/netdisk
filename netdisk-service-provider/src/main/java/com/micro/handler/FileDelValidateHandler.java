package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileDelRequest;
import com.micro.utils.ValidateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
@Component
public class FileDelValidateHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileDelRequest){
            FileDelRequest bean  = (FileDelRequest) request;
            ValidateUtils.validate(bean.getCreateusername(),"username");
            ValidateUtils.validate(bean.getCreateuserid(),"userid");
            if(CollectionUtils.isEmpty(bean.getIds())){
             throw new RuntimeException("ids为空");
            }

        }else {
            throw new RuntimeException("FileDelValidateHandler参数问题");
        }
    }
}
