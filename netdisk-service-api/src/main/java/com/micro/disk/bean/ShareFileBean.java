package com.micro.disk.bean;

import lombok.Data;

@Data
public class ShareFileBean {
    private String id;
    private String filename;
    private String fileicon;
    private String filesize;
    private Integer filetype;//0文件夹，1文件
    private String createtime;
}
