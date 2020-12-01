package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.disk.service.UserCapacityService;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MergeCapacityIsEnoughHandler extends Handler {
    @Autowired
    private UserCapacityService userCapacityService;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean=(MergeRequest) request;
            UserCapacityBean userCapacityBean  = userCapacityService.findUserCapacity(bean.getUserid());
            if(bean.getTotalSize()>(userCapacityBean.getTotalcapacity()-userCapacityBean.getUsedcapacity())){
                throw new RuntimeException("您的存储空间不足,请联系管理员");
            }
        }else{
            throw new RuntimeException("MergeCapacityIsEnoughHandler==参数不对");
        }
    }
}
