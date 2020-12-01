package com.micro.db.dao;

import com.micro.model.DiskUserCapacityHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DiskUserCapacityHistoryDao extends JpaRepository<DiskUserCapacityHistory, String>, JpaSpecificationExecutor<DiskUserCapacityHistory> {
}
