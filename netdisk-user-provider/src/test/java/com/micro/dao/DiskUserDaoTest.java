package com.micro.dao;


import com.micro.SellApplicationTests;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.model.DiskUser;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiskUserDaoTest  extends SellApplicationTests {

    @Autowired
    DiskUserDao diskUserDao;
    @Test
    public void getDiskUserByUsernameAndPassword() {
        DiskUser diskUser = diskUserDao.getDiskUserByUsernameAndPassword("caihuahang","8858321700");
        Assert.assertNotNull(diskUser);
    }
    @Test
    public void updateToken(){
        Integer result = diskUserDao.updateToken("token-vip","40289181738572100173857224980000");
        System.out.println(result);
    }
}
