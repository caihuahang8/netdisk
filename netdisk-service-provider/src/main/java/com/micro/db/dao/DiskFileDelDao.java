package com.micro.db.dao;

import com.micro.model.DiskFileDel;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiskFileDelDao  extends JpaRepository<DiskFileDel, String>, JpaSpecificationExecutor<DiskFileDel> {

    @Query("select t from DiskFileDel t where createuserid=?1 and pid=?2")
    List<DiskFileDel> findListByPid(String userid, String pid);

    @Modifying
    @Query("delete from DiskFileDel where id=?1")
    public void deleteById(String id);
}
