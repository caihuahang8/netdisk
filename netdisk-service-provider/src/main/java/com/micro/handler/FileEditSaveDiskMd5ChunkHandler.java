package com.micro.handler;


import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskMd5ChunkDao;
import com.micro.model.DiskMd5Chunk;
import com.micro.param.FileEditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FileEditSaveDiskMd5ChunkHandler  extends Handler {
    @Autowired
    DiskMd5ChunkDao diskMd5ChunkDao;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof FileEditRequest){
            FileEditRequest bean  = (FileEditRequest) request;
            try{
                if(!bean.isIsmd5exist()){
                    DiskMd5Chunk diskMd5Chunk = new DiskMd5Chunk();
                diskMd5Chunk.setChunkname("txtChunk");
                diskMd5Chunk.setChunknumber(1);
                diskMd5Chunk.setChunksize(bean.getBytes().length);
                diskMd5Chunk.setFilemd5(bean.getFileMd5());
                diskMd5Chunk.setStorepath(bean.getFolderpath());
                diskMd5Chunk.setTotalchunks(1);
                diskMd5ChunkDao.save(diskMd5Chunk);
                }
            }catch (Exception e){
                throw new RuntimeException("diskMd5Chunk保存失败");
            }
        }else {
                throw new RuntimeException("FileEditSaveDiskMd5ChunkHandler参数不对");
        }
    }
}
