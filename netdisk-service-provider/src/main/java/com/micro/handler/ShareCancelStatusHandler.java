package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareDao;
import com.micro.db.dao.DiskShareFileDao;
import com.micro.model.DiskShare;
import com.micro.model.DiskShareFile;
import com.micro.param.ShareCancelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShareCancelStatusHandler extends Handler {
    @Autowired
    DiskShareDao diskShareDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof ShareCancelRequest){
            ShareCancelRequest bean = (ShareCancelRequest) request;
            List<String> secretids = new ArrayList<>();
            List<String> friendids = new ArrayList<>();
            for (String id : bean.getIds()){
                DiskShare diskShare = diskShareDao.findOne(id);
                if(diskShare==null){
                    throw new RuntimeException("文件分享"+id+"不存在");
                }
                if (diskShare.getStatus()==1){
                    throw new RuntimeException("有文件已经失效");
                }
                if (diskShare.getStatus()==2){
                    throw new RuntimeException("有文件已经取消分享");
                }
                diskShare.setStatus(2);//0正常，1已失效，2已撤销
                diskShareDao.save(diskShare);

                if(diskShare.getType()==0){
                    secretids.add(id);
                }else if(diskShare.getType()==1){
                    friendids.add(id);
                }
                bean.setFriendIds(friendids);
                bean.setSecretIds(secretids);
                this.updateRequest(bean);
            }
        }else {
            throw new RuntimeException("ShareCancelStatusHandler参数问题");
        }
    }
}
