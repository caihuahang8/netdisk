package com.micro.disk.service;

import java.util.List;

public interface FilePreviewService {

    /**
     * 根据md5下载
     * @param filemd5
     * @return
     */
    public List<String> getChunksByFilemd5(String filemd5);
    /**
     * 缩略图
     * @param url
     * @return
     */
    public byte[] getBytesByUrl(String url);

    String getChunkByFilemd5(String filemd5);
}
