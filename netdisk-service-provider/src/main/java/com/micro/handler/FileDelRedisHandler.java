package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.FileDelRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.TimeUnit;

@Component
public class FileDelRedisHandler extends Handler {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileDelRequest){
            FileDelRequest bean = (FileDelRequest) request;
            if (!CollectionUtils.isEmpty(bean.getRubbishs())){
                for (String id : bean.getRubbishs()){
                    stringRedisTemplate.opsForValue().set(Contanst.RUBBISH+id,id,30, TimeUnit.DAYS);
                }
            }
        }else {
            throw new RuntimeException("FileDelRedisHandler参数错误");
        }
    }
}
