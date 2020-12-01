package com.micro.handler;

import com.micro.common.Contanst;
import com.micro.common.json.JsonJackUtils;
import com.micro.common.json.JsonUtils;
import com.micro.core.ContextRequest;
import com.micro.core.ContextResponse;
import com.micro.core.Handler;
import com.micro.db.dao.DiskTypeSuffixDao;
import com.micro.model.DiskTypeSuffix;
import com.micro.param.ChunkRequest;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ChunkFileSuffixHandler extends Handler {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DiskTypeSuffixDao diskTypeSuffixDao;
    JsonUtils jsonUtils = new JsonJackUtils();
    @Override
    public void doHandler(ContextRequest request, ContextResponse response) {
        if (request instanceof ChunkRequest){
            //1.强转
            ChunkRequest chunk = (ChunkRequest) request;
            //2.后缀
            String suffix = FilenameUtils.getExtension(chunk.getName());

            //先去redis查询，查询不到再去mysql
            DiskTypeSuffix dts =null;
            Object obj =  stringRedisTemplate.opsForValue().get(Contanst.PREFIX_TYPE_SUFFIX+suffix);
            if (obj == null){
                dts=diskTypeSuffixDao.findBySuffix(suffix);
                if (dts==null){
                    throw new RuntimeException("网盘暂时不支持该格式,请联系管理员添加!"+suffix);
                }else {
                    stringRedisTemplate.opsForValue().set(Contanst.PREFIX_TYPE_SUFFIX+suffix,jsonUtils.objectToJson(dts));
                }
            }else {
                dts=jsonUtils.jsonToPojo(obj.toString(),DiskTypeSuffix.class);
            }
                //写到下一个
                chunk.setTypecode(dts.getTypecode());
                this.updateRequest(chunk);
        }else {
            throw new RuntimeException("ChunkFileSuffixHandler==参数不对");
        }
    }
}
