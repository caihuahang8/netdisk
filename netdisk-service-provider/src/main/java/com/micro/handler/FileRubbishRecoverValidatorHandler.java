package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileRubbishRecoverRequest;
import com.micro.utils.ValidateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class FileRubbishRecoverValidatorHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileRubbishRecoverRequest){
            FileRubbishRecoverRequest bean = new FileRubbishRecoverRequest();
            ValidateUtils.validate(bean.getCreateusername(),"username");
            ValidateUtils.validate(bean.getFolderid(),"folderid");
            ValidateUtils.validate(bean.getUserid(),"userid");
            if (CollectionUtils.isEmpty(bean.getIds())){
                throw new RuntimeException("ids");
            }
        }else {
            throw new RuntimeException("FileRubbishRecoverValidatorHandler参数问题");
        }
    }
}
