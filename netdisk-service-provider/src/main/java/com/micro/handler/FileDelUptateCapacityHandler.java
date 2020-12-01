package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.service.UserCapacityService;
import com.micro.param.FileDelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileDelUptateCapacityHandler  extends Handler {
    @Autowired
    UserCapacityService userCapacityService;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileDelRequest){
            FileDelRequest bean = (FileDelRequest) request;
            userCapacityService.updateUserCapacity(1,bean.getCapacity(),bean.getCreateuserid(),bean.getCreateusername(),"文件删除");
        }else {
            throw new RuntimeException("FileDelUptateCapacityHandler参数问题");
        }
    }
}
