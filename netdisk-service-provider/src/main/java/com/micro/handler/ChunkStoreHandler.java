package com.micro.handler;

import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ChunkRequest;
import com.micro.store.context.StoreContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChunkStoreHandler extends Handler {
    @Autowired
    private StoreContext storeContext;
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof ChunkRequest){
            ChunkRequest chunk  = (ChunkRequest) request;
            //强转
            String storepath = storeContext.upload("",chunk.getBytes(),chunk.getName());
            //写到下一个
            chunk.setStorepath(storepath);
            this.updateRequest(chunk);
        }else{
            throw new RuntimeException("UploadChunkStoreHandler==参数不对");
        }
    }
}
