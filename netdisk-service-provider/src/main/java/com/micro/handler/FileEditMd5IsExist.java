package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;
import com.micro.param.FileEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditMd5IsExist extends Handler {
    @Autowired
    DiskMd5Dao diskMd5Dao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileEditRequest ){
            FileEditRequest bean = (FileEditRequest) request;
            DiskMd5 diskMd5 =  diskMd5Dao.findMd5IsExist(bean.getFileMd5());
            boolean flag = diskMd5==null?false:true;
            bean.setIsmd5exist(flag);
            if (flag){
                bean.setFilesuffix(diskMd5.getFilesuffix());
                bean.setTypecode(diskMd5.getTypecode());
                bean.setThumbnailurl(diskMd5.getThumbnailurl());
                bean.setImgsize(diskMd5.getImgsize());
            }
            this.updateRequest(bean);
        }else {
            throw new RuntimeException();
        }
    }
}
