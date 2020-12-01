package com.micro.handler;

import com.micro.common.ValidateUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.MergeRequest;
import org.springframework.stereotype.Component;

@Component
public class MergeValidateHander extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            //强转
            MergeRequest bean = (MergeRequest) request;

            ValidateUtils.validate(bean.getUserid(), "用户ID");
            ValidateUtils.validate(bean.getUsername(), "用户姓名");
            ValidateUtils.validate(bean.getUsername(), "用户姓名");
            ValidateUtils.validate(bean.getPid(), "PID");
            ValidateUtils.validate(bean.getUuid(), "UUID");
            ValidateUtils.validate(bean.getFileid(), "文件ID");
            ValidateUtils.validate(bean.getFilemd5(), "文件MD5");
            ValidateUtils.validate(bean.getFilename(), "文件名称");
            ValidateUtils.validate(bean.getTotalSize(), "文件大小");
        }else {
            throw new RuntimeException("UploadMergeValidateHander==参数不对");
        }
    }
}
