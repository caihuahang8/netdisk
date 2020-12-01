package com.micro.handler;

import com.micro.config.RedisChunkTemp;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.param.MergeRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import sun.text.CollatorUtilities;

import java.util.List;
@Component
public class MergeFileIsBreakHandler extends Handler {
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if(request instanceof MergeRequest){
            MergeRequest bean = (MergeRequest) request;
            //计算总大小
            List<RedisChunkTemp> temps =  bean.getTemps();
            if (!CollectionUtils.isEmpty(temps)){
                long size = 0L;
                long totalSize = 0L;
            for (int i=0;i<temps.size();i++){
                if (i==0){
                    totalSize = temps.get(0).getSize();
                }
                size=size+temps.get(i).getCurrentsize().longValue();
            }
                if(size!=totalSize){
                    throw new RuntimeException("文件完整性校验不通过（前端计算大小："+totalSize+";后端计算大小："+size+"）");
                }
            }
        }else {
            throw new RuntimeException("MergeFileIsBreakHandler==参数不对");
        }
    }
}
