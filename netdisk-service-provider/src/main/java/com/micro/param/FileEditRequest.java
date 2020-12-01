package com.micro.param;

import com.micro.core.ContextRequest;
import lombok.Data;

@Data
public class FileEditRequest extends ContextRequest {
    private String pid;
    private String filename;
    private byte[] bytes;
    private String username;
    private String userid;
    private String fileMd5;
    private Long filecapacity;//文件占用容量
    private Long usercapacity;//用户使用容量
    private Long totalcapacity;//总容量
    private boolean ismd5exist;
    private String folderpath;//文件下载地址
    private String diskfileid;
    private String typecode;
    private String filesuffix;
    private String thumbnailurl;
    private String imgsize;
    private String relativepath; //文件夹上传（路径）【*】
}
