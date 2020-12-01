package com.micro.disk.bean;

import lombok.Data;

@Data
public class FolderPropertyBean {
    private Integer filenum;
    private Integer foldernum;
    private Long totalsize;
    private String totalsizename;
}
