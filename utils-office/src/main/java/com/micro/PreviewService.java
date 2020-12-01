package com.micro;

public interface PreviewService {
    /**
     * 转换成pdf
     * @param filename
     * @param bytes
     * @return
     */
    byte[] converToPdf(String filename,byte[] bytes);
}
