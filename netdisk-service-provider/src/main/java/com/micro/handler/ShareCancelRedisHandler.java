package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskShareDao;
import com.micro.model.DiskShare;
import com.micro.param.ShareCancelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShareCancelRedisHandler extends Handler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    DiskShareDao diskShareDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareCancelRequest){
            ShareCancelRequest bean = (ShareCancelRequest) request;
            for (String id : bean.getIds()){
                DiskShare diskShare = diskShareDao.findOne(id);
                if (diskShare.getEffect()!=0&&diskShare.getType()==0){
                    stringRedisTemplate.delete(Contanst.SHARE+id);
                }
            }
        }
    }
}
