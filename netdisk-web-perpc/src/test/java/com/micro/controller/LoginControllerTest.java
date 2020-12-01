package com.micro.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.SellApplicationTests;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.UserBean;
import com.micro.disk.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class LoginControllerTest extends SellApplicationTests {

    @Reference(check=false)
    private UserService userService;
    @Test
    public void init(){
        UserBean userBean = new UserBean();
        userBean.setNickname("小菜");
        userBean.setPassword("8858321700");
        userBean.setUsername("caihuahang");
        userBean.setTelephone("18060561619");
        userService.register(userBean);
    }
    @Test
    public void login() {
        SessionUserBean sessionUserBean = userService.login("caihuahang","8858321700");
        Assert.assertNotNull(sessionUserBean);
    }

    @Test
    public void logout(){

    }

}
