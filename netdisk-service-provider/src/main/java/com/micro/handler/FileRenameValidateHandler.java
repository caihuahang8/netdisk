package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileRenameRequest;
import com.micro.utils.ValidateUtils;

public class FileRenameValidateHandler extends Handler {

    /**
     * 基本参数验证
     * @param request
     * @param response
     */
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRenameRequest){
            FileRenameRequest bean = (FileRenameRequest) request;
            ValidateUtils.validate(bean.getFilename(),"filename");
            ValidateUtils.validate(bean.getId(),"fileid");
            ValidateUtils.validate(bean.getUserid(),"userid");
        }else {
            throw new RuntimeException("FileRenameValidateHandler参数问题");
        }
    }
}
