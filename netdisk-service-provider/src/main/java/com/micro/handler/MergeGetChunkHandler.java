package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.config.RedisChunkTemp;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.MergeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Component
public class MergeGetChunkHandler extends Handler {
    @Autowired
    private  StringRedisTemplate stringRedisTemplate;

    private JsonJackUtils jsonJackUtils = new JsonJackUtils();
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean = (MergeRequest) request;

            String userid=bean.getUserid();
            String uuid=bean.getUuid();
            String fileid=bean.getFileid();
            String filename=bean.getFilename();
            List<RedisChunkTemp> temps=new ArrayList<>();

            String key= Contanst.PREFIX_CHUNK_TEMP+"-"+userid+"-"+uuid+"-"+fileid+"-"+filename+"-*";
            Set<String> keys = stringRedisTemplate.keys(key);
            for (String k : keys){
                String temp = stringRedisTemplate.opsForValue().get(k);
                RedisChunkTemp chunkTemp = jsonJackUtils.jsonToPojo(temp,RedisChunkTemp.class);
                temps.add(chunkTemp);
            }
            bean.setTemps(temps);
            this.updateRequest(bean);
        }else{
            throw new RuntimeException("UploadMergeGetChunkHandler==参数不对");
        }
    }
}
