package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.param.FileAddFolderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class FileAddFolderSaveDiskFile extends Handler {
    @Autowired
    private DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileAddFolderRequest){
            FileAddFolderRequest bean = (FileAddFolderRequest) request;
            //1.保存到disk_file里面
            DiskFile df=new DiskFile();
            df.setFilename(bean.getFilename());
            df.setPid(bean.getPid());
            df.setFiletype(0);
            df.setFilesize(0);
            df.setCreateuserid(bean.getUserid());
            df.setCreateusername(bean.getUsername());
            df.setCreatetime(new Date());
            diskFileDao.save(df);
        }
    }
}
