package com.micro.dao;

import com.micro.model.DiskUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface DiskUserDao extends JpaRepository<DiskUser, String>, JpaSpecificationExecutor<DiskUser> {


     @Query("select t from DiskUser t  where username=?1 and password=?2")
     public DiskUser getDiskUserByUsernameAndPassword(String username,String password);

     @Modifying
     @Transactional
     @Query("UPDATE DiskUser SET token=?1 WHERE id=?2")
     Integer updateToken(String token,String userid);

}
