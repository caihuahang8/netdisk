package com.micro.db.jdbc.dao;



import com.micro.db.jdbc.DiskFileJdbc;
import com.micro.db.jdbc.SellApplicationTests;
import com.micro.disk.bean.FileListBean;
import com.micro.disk.bean.PageInfo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
public class DiskFileJdbcTest extends SellApplicationTests {
    @Autowired
    private DiskFileJdbc diskFileJdbc;

    @Test
    public void findAllList() {
        PageInfo<FileListBean> pageInfo =  diskFileJdbc.findAllList(null,null,"2",null,null,null,null);
        Assert.assertEquals(0,pageInfo.getTotalPage());
    }
}
