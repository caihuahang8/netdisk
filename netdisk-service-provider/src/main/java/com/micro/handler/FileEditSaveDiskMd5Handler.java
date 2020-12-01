package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskMd5Dao;
import com.micro.model.DiskMd5;
import com.micro.param.FileEditRequest;
import com.micro.store.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditSaveDiskMd5Handler extends Handler {
    @Autowired
    DiskMd5Dao diskMd5Dao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileEditRequest){
            FileEditRequest bean = (FileEditRequest) request;
            if (!bean.isIsmd5exist()){
                DiskMd5 diskMd5 = new DiskMd5();
                diskMd5.setFilename(bean.getFilename());
                diskMd5.setFilesuffix(FileUtils.getFileSuffix(bean.getFilename()));
                diskMd5.setMd5(bean.getFileMd5());
                if (FileUtils.getFileSuffix(bean.getFilename()).equals("txt")){
                    diskMd5.setTypecode("1");
                }
                diskMd5.setFilesize(bean.getBytes().length);
                diskMd5.setFilenum(0);
                diskMd5Dao.save(diskMd5);
            }
        }else {
            throw new RuntimeException("无法保存到disk_md5表中");
        }
    }
}
