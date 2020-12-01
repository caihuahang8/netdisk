package com.micro.handler;

import com.micro.config.RedisChunkTemp;
import com.micro.utils.SpringContentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FiledealContext {
    @Autowired
    private SpringContentUtils springContentUtils;

    public void deal(String typecode, String fileid, String filemd5, List<RedisChunkTemp> temps){
        Object obj=springContentUtils.getBean(typecode);
        if(obj!=null){
            FiledealStrategy strategy=(FiledealStrategy) obj;
            strategy.deal(fileid,filemd5, temps);
        }
    }
    public void deal(String typecode,String filemd5,List<RedisChunkTemp> temps){
        Object obj=springContentUtils.getBean(typecode);
        if(obj!=null){
            FiledealStrategy strategy=(FiledealStrategy) obj;
            strategy.deal(filemd5, temps);
        }
    }
}
