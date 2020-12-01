package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskAlbumFileDao;
import com.micro.model.DiskAlbumFile;
import com.micro.param.MergeRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MergeGlThumnailHandler  extends Handler {
    @Autowired
    private DiskAlbumFileDao diskAlbumFileDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean = new MergeRequest();
            String albumId = bean.getAlbumid();
            String fileId = bean.getFileid();
            String typeCode  =  bean.getTypecode();
            if (!StringUtils.isEmpty(albumId)&&!"undefined".equals(albumId)){
                if (typeCode.equals("picture")){
                    int count  =  diskAlbumFileDao.findFileIsInAlbum(albumId,fileId);
                    if(count==0){
                        DiskAlbumFile daf=new DiskAlbumFile();
                        daf.setAlbumid(albumId);
                        daf.setFileid(fileId);
                        daf.setCreatetime(new Date());
                        diskAlbumFileDao.save(daf);
                    }
                }
            }
        }else {
            throw new RuntimeException("MergeGlThumnailHandler==参数不对");
        }
    }
}
