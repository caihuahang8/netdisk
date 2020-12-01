package com.micro.store.service;

public interface StoreService {
    /**
     * 上传
     * @param group
     * @param bytes
     * @param fileName
     */
    public String upload(String group,byte[] bytes,String fileName);

    /**
     * 下载
     * @param path
     * @return
     */
    public byte[] download(String path);
}
