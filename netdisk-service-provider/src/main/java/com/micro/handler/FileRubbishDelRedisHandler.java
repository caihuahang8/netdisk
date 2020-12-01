package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileRubbishDelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class FileRubbishDelRedisHandler extends Handler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof FileRubbishDelRequest){
            FileRubbishDelRequest bean = (FileRubbishDelRequest) request;
            for (String id:bean.getRubbishIds()){
                stringRedisTemplate.delete(id);
            }
        }else {
            throw new RuntimeException("FileRubbishDelRedisHandler参数问题");
        }
    }
}
