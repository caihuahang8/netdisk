package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.param.FileRenameRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class FileRenameSaveDiskFile extends Handler {
    @Autowired
    DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRenameRequest){

            FileRenameRequest bean = (FileRenameRequest) request;
            //userid fileid
            diskFileDao.updateFilenameWithUseridAndFileid(bean.getFilename(),bean.getUserid(),bean.getId());
        }else{
            throw new RuntimeException("FileRenameSaveDiskFile参数有问题");
        }
    }
}
