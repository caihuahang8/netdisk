package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.param.FileRenameRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class FileRenameIsExistHandler extends Handler {
    @Autowired
    DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof ContextRequest){
            FileRenameRequest bean = (FileRenameRequest) request;
            DiskFile df =  diskFileDao.findOne(bean.getId());
            if(df==null){
                throw new RuntimeException("文件不存在");
            }
            //userid pid fileid filename filetype
            Integer count =   diskFileDao.findFilenameIsExist(bean.getUserid(),df.getPid(),bean.getId(),bean.getFilename(),df.getFiletype());
            if (count>0){
                throw new RuntimeException("文件命名重复");
            }
        }else {
            throw new RuntimeException("Fi了RenameIsExistHandler参数问题");
        }
    }
}
