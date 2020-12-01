package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDelDao;
import com.micro.db.dao.DiskUserCapacityDao;
import com.micro.model.DiskUserCapacity;
import com.micro.param.FileRubbishRecoverRequest;
import com.micro.utils.CapacityBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class FileRubbishRecoverCapacityHandler  extends Handler {
    @Autowired
    DiskUserCapacityDao diskUserCapacityDao;
    @Autowired
    DiskFileDelDao diskFileDelDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileRubbishRecoverRequest){
            FileRubbishRecoverRequest bean = (FileRubbishRecoverRequest) request;
            CapacityBean capacityBean = bean.getCapacityBean();
            DiskUserCapacity userCapacity = diskUserCapacityDao.findByUserid(bean.getUserid());
            if (userCapacity.getTotalcapacity()-userCapacity.getUsedcapacity()<capacityBean.getTotalsize()){
                throw new RuntimeException("容量不足");
            }
        }else {
            throw new RuntimeException("FileRubbishRecoverCapacityHandler参数问题");
        }
    }
}
