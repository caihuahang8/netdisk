package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.service.UserCapacityService;
import com.micro.param.FileEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditUpdateDiskCapacityHandler extends Handler {
    @Autowired
    UserCapacityService userCapacityService;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileEditRequest){
            FileEditRequest bean  = (FileEditRequest) request;
            UserCapacityBean userCapacityBean =  userCapacityService.findUserCapacity(bean.getUserid());
            if (userCapacityBean==null){
                userCapacityService.init(bean.getUserid(),bean.getUsername());
            }else {
                userCapacityService.updateUserCapacity(0,bean.getUsercapacity(),bean.getUserid(),bean.getUsername(),"上传文本");
            }
        }else {
            throw new RuntimeException("userCapacityService参数有问题");
        }
    }
}
