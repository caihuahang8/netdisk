package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.disk.user.bean.OrderBean;
import com.micro.disk.user.service.UserServiceRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service(interfaceClass= UserServiceRedis.class)
@Component
@Transactional
public class UserServiceRedisImpl implements UserServiceRedis {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final JsonJackUtils jsonJackUtils = new JsonJackUtils();


    @Override
    public void setSessionUser(String token, Object obj) {
        String json = jsonJackUtils.objectToJson(obj);
        stringRedisTemplate.opsForValue().set(Contanst.SESSION_USER+token,json,7, TimeUnit.DAYS);
    }
    @Override
    public void removeSessionUser(String key){
        stringRedisTemplate.delete(Contanst.SESSION_USER+key);
    }

    @Override
    public void saveSessionOrder(OrderBean orderBean) {
        stringRedisTemplate.opsForValue().set(Contanst.ORDER+orderBean.getOrderid(),jsonJackUtils.objectToJson(orderBean));
    }

    @Override
    public OrderBean getSessionOrder(String orderid) {
        String json = stringRedisTemplate.opsForValue().get(Contanst.ORDER+orderid);
        if (json==null){
            return null;
        }
        stringRedisTemplate.opsForValue().set(Contanst.ORDER+orderid,"");
        return jsonJackUtils.jsonToPojo(json,OrderBean.class);
    }
}
