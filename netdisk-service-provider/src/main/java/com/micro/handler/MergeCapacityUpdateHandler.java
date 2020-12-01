package com.micro.handler;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.service.UserCapacityService;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MergeCapacityUpdateHandler extends Handler {
    @Autowired
    private UserCapacityService userCapacityService;



    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean = (MergeRequest) request;
            userCapacityService.updateUserCapacity(0,bean.getTotalSize(),bean.getUserid(),bean.getUsername(),"文件上传");
        }else {
            throw new RuntimeException("MergeCapacityUpdateHandler==参数不对");
        }
    }
}
