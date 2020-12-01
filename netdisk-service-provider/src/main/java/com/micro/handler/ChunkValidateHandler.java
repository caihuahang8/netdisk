package com.micro.handler;

import com.micro.common.ValidateUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ChunkRequest;
import org.springframework.stereotype.Component;

@Component
public class ChunkValidateHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
            if (request instanceof ChunkRequest){
                    //1.强转
                ChunkRequest chunkRequest = (ChunkRequest) request;
                    //2.基本的校验
                if (chunkRequest.getBytes()==null||chunkRequest.getBytes().length==0){
                    throw new RuntimeException("上传的文件为空");
                }
                ValidateUtils.validate(chunkRequest.getUuid(), "uuid");
                ValidateUtils.validate(chunkRequest.getId(), "文件id");
                ValidateUtils.validate(chunkRequest.getName(), "文件名称");
                ValidateUtils.validate(chunkRequest.getSize(), "文件大小");
                ValidateUtils.validate(chunkRequest.getChunk(), "切块序号");
                ValidateUtils.validate(chunkRequest.getChunks(), "切块数量");
                ValidateUtils.validate(chunkRequest.getUserid(), "用户ID");
                ValidateUtils.validate(chunkRequest.getUsername(), "用户姓名");


            }else {
                throw new RuntimeException("ChunkValidateHandler==参数不对");
            }
    }
}
