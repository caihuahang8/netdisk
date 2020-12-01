package com.micro.handler;

import com.micro.config.RedisChunkTemp;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.model.DiskMd5Chunk;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class MergeSaveDiskMd5ChunkHandler  extends Handler {
    @Autowired
    private DiskMd5ChunkDao diskMd5ChunkDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean=(MergeRequest) request;

            if (!bean.isExistindiskmd5()){
                List<RedisChunkTemp> redisChunkTempList = bean.getTemps();
                List<DiskMd5Chunk> diskMd5ChunkList = new ArrayList<>();

                for(RedisChunkTemp temp : redisChunkTempList){
                    DiskMd5Chunk dmc=new DiskMd5Chunk();
                    dmc.setFilemd5(bean.getFilemd5());
                    dmc.setChunkname(temp.getName());
                    dmc.setChunknumber(temp.getChunk());
                    dmc.setChunksize(temp.getCurrentsize().longValue());
                    dmc.setTotalchunks(temp.getChunks());
                    dmc.setTotalsize(temp.getSize());
                    dmc.setStorepath(temp.getStorepath());
                    diskMd5ChunkList.add(dmc);
                }
                diskMd5ChunkDao.save(diskMd5ChunkList);
            }else{
                throw new RuntimeException("MergeDiskMd5ChunkHandler==参数不对");
            }
        }
    }
}
