package com.micro.db.dao;

import com.micro.model.DiskShare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DiskShareDao  extends JpaRepository<DiskShare, String>, JpaSpecificationExecutor<DiskShare> {
    @Modifying
    @Query("update DiskShare set savecount=savecount+1 where id=?1")
    public void updateCount(String id);
}
