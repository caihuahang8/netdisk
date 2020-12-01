package com.micro.db.dao;


import com.micro.model.DiskShareSave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiskShareSaveDao extends JpaRepository<DiskShareSave, String>, JpaSpecificationExecutor<DiskShareSave> {
}
