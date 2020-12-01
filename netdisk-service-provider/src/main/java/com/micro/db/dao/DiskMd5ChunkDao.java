package com.micro.db.dao;

import com.micro.model.DiskMd5Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiskMd5ChunkDao extends JpaRepository<DiskMd5Chunk, String>, JpaSpecificationExecutor<DiskMd5Chunk> {
    //根据md5获取所有切块
    @Query("select t from DiskMd5Chunk t where filemd5=?1 order by chunknumber asc")
    public List<DiskMd5Chunk> findList(String filemd5);

    @Query("select t from DiskMd5Chunk t where filemd5=?1 ")
    public DiskMd5Chunk findChunkByFilemd5(String filemd5);
}
