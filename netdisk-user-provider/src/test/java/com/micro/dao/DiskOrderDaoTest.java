package com.micro.dao;

import com.micro.SellApplicationTests;
import com.micro.model.DiskOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DiskOrderDaoTest extends SellApplicationTests {
    @Autowired
    private DiskOrderDao diskOrderDao;
    @Test
    public void save(){
        DiskOrder diskOrder = new DiskOrder();
        diskOrder.setOrderid("123456");
        diskOrder.setPayStatus(0);
        diskOrder.setOrderStatus(0);
        diskOrderDao.save(diskOrder);
    }
}
