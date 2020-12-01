package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.service.UserCapacityService;
import com.micro.param.FileRubbishRecoverRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileRubbishRecoverUpdateCapacityHandler extends Handler {
    @Autowired
    UserCapacityService userCapacityService;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishRecoverRequest){
            FileRubbishRecoverRequest bean = (FileRubbishRecoverRequest) request;
            userCapacityService.updateUserCapacity(0,bean.getCapacityBean().getTotalsize(),bean.getUserid(),bean.getCreateusername(),"文件还原");
        }else {
            throw new RuntimeException("FileRubbishRecoverUpdateCapacityHandler参数问题");
        }
    }
}
