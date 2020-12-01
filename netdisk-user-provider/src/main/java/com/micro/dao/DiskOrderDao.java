package com.micro.dao;

import com.micro.model.DiskOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DiskOrderDao extends JpaRepository<DiskOrder, String>, JpaSpecificationExecutor<DiskOrder> {
    @Modifying
    @Query("UPDATE DiskOrder SET payStatus='1' , orderStatus='1' WHERE orderid=?1")
    void updatePayStatus(String orderid);
}
