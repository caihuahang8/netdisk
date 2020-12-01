package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileEditRequest;
import com.micro.utils.ValidateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;

@Component
public class FileEditValidateHander extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileEditRequest){
            FileEditRequest bean = (FileEditRequest) request;
            ValidateUtils.validate(bean.getPid(),"文件夹id不存在");
            ValidateUtils.validate(bean.getFilename(),"文件名不存在");
            if(ArrayUtils.isEmpty(bean.getBytes())){
                throw new RuntimeException("基本信息校验有错误");
            }
            ValidateUtils.validate(bean.getUsername(),"用户名不存在");
        }else {
            throw new RuntimeException("基本信息校验有错误");
        }
    }
}
