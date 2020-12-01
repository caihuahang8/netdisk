package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ShareSecretRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ShareSecretRedisSaveHandler extends Handler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ShareSecretRequest){
            ShareSecretRequest bean = (ShareSecretRequest) request;
            stringRedisTemplate.opsForValue().set(Contanst.SHARE+bean.getShareid(),bean.getShareid(),bean.getEffect(), TimeUnit.DAYS);
        }else {
            throw new RuntimeException("ShareSecretRedisSaveHandler参数问题");
        }
    }
}
