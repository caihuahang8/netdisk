package com.micro.db.dao;

import com.micro.model.DiskTypeSuffix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface DiskTypeSuffixDao extends JpaRepository<DiskTypeSuffix, String>, JpaSpecificationExecutor<DiskTypeSuffix> {
    /**
     * 根据后缀查询
     * @param suffix
     * @return
     */
    @Query("select t from DiskTypeSuffix t where suffix=?1")
    public DiskTypeSuffix findBySuffix(String suffix);

    /**
     * 根据后缀判断是否已经存在
     * @param suffix
     * @return
     */
    @Query("select count(1) from DiskTypeSuffix t where suffix=?1")
    public Integer findCountAdd(String suffix);
}
