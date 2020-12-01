package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.disk.bean.UserCapacityBean;
import com.micro.param.FileEditRequest;
import com.micro.service.impl.UserCapacityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditCapacityIsEnoughHandler extends Handler {
    @Autowired
    UserCapacityService userCapacityService;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileEditRequest){
            FileEditRequest bean = (FileEditRequest) request;

            UserCapacityBean userCapacityBean  =   userCapacityService.findUserCapacity(bean.getUserid());
            if (bean.getBytes().length >= userCapacityBean.getTotalcapacity() - userCapacityBean.getUsedcapacity()){
                throw new RuntimeException("您的磁盘大小不够");
            }
            bean.setUsercapacity(userCapacityBean.getUsedcapacity());
            bean.setFilecapacity( Long.valueOf(bean.getBytes().length));
            bean.setTotalcapacity(userCapacityBean.getTotalcapacity());
            this.updateRequest(bean);
        }else {
            throw new RuntimeException("FileEditRequest=参数错误");
        }
    }
}
