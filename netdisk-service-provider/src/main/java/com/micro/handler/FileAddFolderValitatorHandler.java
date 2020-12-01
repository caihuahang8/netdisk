package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileAddFolderRequest;
import com.micro.utils.ValidateUtils;
import org.apache.tomcat.util.descriptor.web.ContextHandler;
import org.springframework.stereotype.Component;

@Component
public class FileAddFolderValitatorHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileAddFolderRequest){
            FileAddFolderRequest bean = (FileAddFolderRequest) request;
            ValidateUtils.validate(bean.getFilename(),"文件名称");
            ValidateUtils.validate(bean.getUsername(),"用户名名称");
            ValidateUtils.validate(bean.getPid(),"文件夹id");
            ValidateUtils.validate(bean.getUserid(),"用户名id");
        }
    }
}
