package com.micro.disk.bean;

import lombok.Data;

import java.io.Serializable;
@Data
public class Chunk implements Serializable {
    //插件内置参数
    private String id; //文件ID（注意，它只是前端生成的）
    private String name; //文件名称
    private Integer chunk;//当前切块数
    private Integer chunks;//总切块数
    private Long size;//文件大小
    private byte[] bytes;//字节数组，对应上图的file（binary）

    //下面是业务需要加的参数
    private String uuid; //前端生成的uuid（上一章节，介绍过）
    private String userid;//上传人ID（这个不是前端传递过来的）
    private String username;//上传人姓名（这个不是前端传递过来的）
}
