package com.micro.db.dao;

import com.micro.model.DiskUserCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DiskUserCapacityDao  extends JpaRepository<DiskUserCapacity, String>, JpaSpecificationExecutor<DiskUserCapacity> {
    //查询容量
    @Query("select t from DiskUserCapacity t where userid=?1")
    public DiskUserCapacity findByUserid(String userid);
    //新增已用容量
    @Modifying
    @Query("update DiskUserCapacity set usedcapacity=usedcapacity+?1 where userid=?2")
    public void addUsedCapacity(Long capacity,String userid);
    @Modifying
    @Query("update DiskUserCapacity set usedcapacity=usedcapacity-?1 where userid=?2")
    public void reduceUsedCapacity(Long capacity,String userid);
}
