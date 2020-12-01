package com.micro.db.dao;

import com.micro.model.DiskMd5;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DiskMd5Dao extends JpaRepository<DiskMd5, String>, JpaSpecificationExecutor<DiskMd5> {
    /**
     * 判断md5是否存在
     * @param md5
     * @return
     */
    @Query("select t from DiskMd5 t where md5=?1")
    public DiskMd5 findMd5IsExist(String md5);


    /**
     * 更新字段
     * @param thumbnailurl
     * @param imgsize
     * @param md5
     */
    @Modifying
    @Query("update DiskMd5 set thumbnailurl=?1,imgsize=?2 where md5=?3")
    public void updateField(String thumbnailurl,String imgsize,String md5);
}
