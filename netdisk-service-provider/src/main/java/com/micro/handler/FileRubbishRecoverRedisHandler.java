package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileRubbishRecoverRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FileRubbishRecoverRedisHandler extends Handler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileRubbishRecoverRequest){
            FileRubbishRecoverRequest bean = (FileRubbishRecoverRequest) request;
            for (String redisid:bean.getRediskeys()){
                stringRedisTemplate.delete(redisid);
            }
        }else {
            throw new RuntimeException("FileRubbishRecoverRedisHandler参数问题");
        }
    }
}
