package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.config.RedisChunkTemp;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.ChunkRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
@Component
public class ChunkRedisHandler extends Handler {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private JsonJackUtils jsonJackUtils = new JsonJackUtils();

    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof ChunkRequest){
            //1.强转
            ChunkRequest chunk  = (ChunkRequest) request;

            //实体转换
            RedisChunkTemp redisChunkTemp = new RedisChunkTemp();
            RedisChunkTemp temp=new RedisChunkTemp();
            temp.setUserid(chunk.getUserid());
            temp.setId(chunk.getId());
            temp.setName(chunk.getName());
            temp.setStorepath(chunk.getStorepath());
            temp.setTypecode(chunk.getTypecode());
            temp.setFilesuffix(FilenameUtils.getExtension(chunk.getName()));
            temp.setSize(chunk.getSize());
            temp.setChunks(chunk.getChunks());
            temp.setCurrentsize(chunk.getBytes().length); //当前切块大小
            temp.setChunk(chunk.getChunk());

            //保存到Redis，并且设置key过期
            String key= Contanst.PREFIX_CHUNK_TEMP+"-"+chunk.getUserid()+"-"+chunk.getUuid()+"-"+chunk.getId()+"-"+chunk.getName()+"-"+chunk.getChunk();
            stringRedisTemplate.opsForValue().set(key, jsonJackUtils.objectToJson(temp), 30, TimeUnit.MINUTES);
        }else{
            throw new RuntimeException("UploadChunkRedisHandler==参数不对");
        }
    }
}
