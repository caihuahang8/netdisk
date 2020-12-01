package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileRubbishDelRequest;
import com.micro.utils.ValidateUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.validation.Validator;

@Component
public class FileRubbishValidatorHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishDelRequest){
            FileRubbishDelRequest bean = new FileRubbishDelRequest();
            ValidateUtils.validate(bean.getUserid(),"userid");
            if (CollectionUtils.isEmpty(bean.getIds())){
                throw new RuntimeException("ids不存在");
            }
        }else {
            throw new RuntimeException("FileRubbishValidatorHandler参数问题");
        }
    }
}
