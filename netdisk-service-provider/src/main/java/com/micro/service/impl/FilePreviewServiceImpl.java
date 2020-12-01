package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.disk.service.FilePreviewService;
import com.micro.model.DiskMd5Chunk;
import com.micro.store.context.StoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
@Service(interfaceClass= FilePreviewService.class,timeout=120000) //Dubbo注解
@Component //Spring注解
@Transactional
public class FilePreviewServiceImpl implements FilePreviewService {
    @Autowired
    DiskMd5ChunkDao diskMd5ChunkDao;
    @Autowired
    StoreContext storeContext;
    @Override
    public List<String> getChunksByFilemd5(String filemd5) {
        List<DiskMd5Chunk>  diskMd5ChunkList =  diskMd5ChunkDao.findList(filemd5);
        List<String> urls = new ArrayList<>();
        if (!CollectionUtils.isEmpty(diskMd5ChunkList)) {
            for (DiskMd5Chunk md5Chunk : diskMd5ChunkList){
                urls.add(md5Chunk.getStorepath());
            }
        }
        return urls;
    }

    @Override
    public byte[] getBytesByUrl(String url) {
        return storeContext.download(url);
    }
    @Override
    public String getChunkByFilemd5(String filemd5){
        DiskMd5Chunk diskMd5Chunk =  diskMd5ChunkDao.findChunkByFilemd5(filemd5);
        if (diskMd5Chunk==null){
            return "";
        }
        return diskMd5Chunk.getStorepath();
    }
}
