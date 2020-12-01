package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskFileDao;
import com.micro.model.DiskFile;
import com.micro.param.FileEditRequest;
import com.micro.param.FileEditResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class FileEditSaveDiskFileHandler extends Handler {
    @Autowired
    DiskFileDao diskFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileEditRequest){
            FileEditRequest bean = (FileEditRequest) request;
            DiskFile diskFile  =  diskFileDao.findFile(bean.getUserid(),bean.getPid(),bean.getFilename(),bean.getFileMd5());
            String diskfileid="";
            if (bean.isIsmd5exist()){
                if (diskFile==null){
                    diskFile = new DiskFile();
                    diskFile.setPid(bean.getPid());
                    diskFile.setFilename(bean.getFilename());
                    diskFile.setFilemd5(bean.getFileMd5());
                    diskFile.setFilesize(bean.getBytes().length);
                    diskFile.setCreateuserid(bean.getUserid());
                    diskFile.setCreateusername(bean.getUsername());
                    diskFile.setFiletype(1);
                    diskFile.setTypecode(bean.getTypecode());
                    diskFile.setCreatetime(new Date());
                    diskFile.setFilesuffix(bean.getFilesuffix());
                    diskFile.setTypecode("document");
                    diskFileDao.save(diskFile);
                    diskfileid = diskFile.getId();
                }else {
                    diskfileid = diskFile.getId();
                }
            }else {
                if (diskFile==null){
                    diskFile = new DiskFile();
                    diskFile.setPid(bean.getPid());
                    diskFile.setFilename(bean.getFilename());
                    diskFile.setFilemd5(bean.getFileMd5());
                    diskFile.setFilesize(bean.getBytes().length);
                    diskFile.setCreateuserid(bean.getUserid());
                    diskFile.setCreateusername(bean.getUsername());
                    diskFile.setFiletype(1);
                    diskFile.setTypecode(bean.getTypecode());
                    diskFile.setCreatetime(new Date());
                    diskFile.setFilesuffix(bean.getFilesuffix());
                    diskFile.setTypecode("document");
                    diskFileDao.save(diskFile);
                    diskfileid = diskFile.getId();
                }else {
                    throw new RuntimeException("保存到disk_file错误");
                }
            }
            bean.setDiskfileid(diskfileid);
            this.updateRequest(bean);
            if (response instanceof FileEditResponse){
                FileEditResponse res = (FileEditResponse) response;
                res.setId(diskfileid);
                this.updateResponse(res);
            }
        }
    }
}
