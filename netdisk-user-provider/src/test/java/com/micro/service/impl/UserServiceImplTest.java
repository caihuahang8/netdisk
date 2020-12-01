package com.micro.service.impl;

import com.micro.SellApplicationTests;
import com.micro.common.CookieUtils;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.UserBean;
import com.micro.disk.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class UserServiceImplTest extends SellApplicationTests {
    @Autowired
    private UserService userService;

    @Test
    public void getUserByToken() {
        SessionUserBean sessionUserBean = userService.getUserByToken("session-userecdf1aae-a6a8-43ee-a171-8e2d2926c9a8");
        Assert.assertNotNull(sessionUserBean);
    }
    @Test
    public void login(){
       SessionUserBean sessionUserBean =  userService.login("caihuahang8","885832170a");
       Assert.assertNotNull(sessionUserBean);
    }
    @Test
    public void register(){
        UserBean userBean = new UserBean();
        userBean.setNickname("小hua");
        userBean.setPassword("885832170a");
        userBean.setUsername("caihuahang8");
        userBean.setTelephone("18859581997");
        userService.register(userBean);
    }
    @Test
    public void updateOrder(){
        userService.updateOrder("123456");
    }
}
