package com.micro.service.impl;

import com.micro.db.jdbc.SellApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class UserCapacityServiceTest extends SellApplicationTests {
    @Autowired
    private UserCapacityService userCapacityService;

    @Test
    public void addCapacity() {
        userCapacityService.addCapacity(1,100,"4028918173852be7017385306e8d0000","小菜","VIP容量增加");
    }

    @Test
    public void reduceCapacity() {
        userCapacityService.reduceCapacity(0,100,"4028918173852be7017385306e8d0000","小菜","VIP容量减少");
    }
}
