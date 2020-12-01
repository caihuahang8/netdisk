package com.micro.store.service.impl;


import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.common.util.IoUtils;
import com.micro.store.service.StoreService;
import com.micro.store.utils.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

@Component(value="Local")
public class StoreServiceImpl implements StoreService {

    @NacosValue("${uploadlocalpath}")
    private String path;

    @Override
    public String upload(String group, byte[] bytes, String fileName) {
        String folders = FileUtils.getFolder();
        String newName = UUID.randomUUID().toString()+"."+ FilenameUtils.getExtension(fileName);
        try {
            //新建文件夹
            String newPath = path.replace("-", "/");
            File fileFolder = new File(newPath+"/"+folders);
            if (!fileFolder.exists()){
                fileFolder.mkdirs();
            }
            File file = new File(newPath+"/"+folders+"/"+newName);
            //下载文件
            OutputStream output = new FileOutputStream(file);
            InputStream input = new ByteArrayInputStream(bytes);
            IOUtils.copyLarge(input,output);
            output.close();
            input.close();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return folders+"/"+newName;
    }
    @Override
    public byte[] download(String path) {
        try{
            File file = new File(this.path.replace("-", "/")+"/"+path);
            InputStream input = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(input);
            input.close();
            return bytes;
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
