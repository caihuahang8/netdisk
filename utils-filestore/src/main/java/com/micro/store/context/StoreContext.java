package com.micro.store.context;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.micro.store.service.StoreService;
import com.micro.store.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StoreContext {
    @Autowired
    private SpringUtils springUtils;
    @NacosValue(value="${uploadtype}",autoRefreshed=true)
    private String uploadtype;

    //1.上传
    public String upload(String group,byte[] bytes,String filename){
        Object obj  = springUtils.getBean(uploadtype);
        if (obj!=null){
            StoreService storeService = (StoreService) obj;
           return storeService.upload(group,bytes,filename);
        }else {
            throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
    }
    //下载
    public byte[] download(String path){
        Object obj=springUtils.getBean(uploadtype);
        if(obj!=null){
            StoreService storeService=(StoreService)obj;
            return storeService.download(path);
        }else{
            throw new RuntimeException("找不到标识uploadtype=="+uploadtype);
        }
    }
}
