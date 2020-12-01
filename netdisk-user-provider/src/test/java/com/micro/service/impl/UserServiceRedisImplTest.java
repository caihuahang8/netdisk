package com.micro.service.impl;

import com.micro.SellApplicationTests;
import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.disk.user.bean.OrderBean;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserServiceRedis;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class UserServiceRedisImplTest extends SellApplicationTests {
    @Autowired
    private UserServiceRedis userServiceRedis;
    private final JsonJackUtils jsonJackUtils = new JsonJackUtils();
    @Test
    public void setSessionUser() {
        SessionUserBean sessionUserBean = new SessionUserBean();
        sessionUserBean.setId("1111111");
        sessionUserBean.setUsername("xiaocaihang");
        sessionUserBean.setTelephone("166561789662");
        sessionUserBean.setNickname("孤独的风");
        userServiceRedis.setSessionUser(Contanst.SESSION_USER+sessionUserBean.getId(),jsonJackUtils.objectToJson(sessionUserBean));
    }

    @Test
    public void removeSessionUser() {
        userServiceRedis.removeSessionUser("-session-usersession-user1111111");
    }

    @Test
    public void setSessionOrder(){
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderid("123456");
        orderBean.setOrderAmount("10");
        orderBean.setBuymonths(1);
        userServiceRedis.saveSessionOrder(orderBean);
    }
    @Test
    public void getSessionOrder(){
        userServiceRedis.getSessionOrder("123456");
    }
    @Test
    public void getSessionOrder2(){
        OrderBean orderBean = userServiceRedis.getSessionOrder("123456");
        Assert.assertNull(orderBean);
    }

}
