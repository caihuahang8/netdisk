package com.micro.param;

import com.micro.core.ContextRequest;
import com.micro.modeltree.DiskFileTree;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class FileDelRequest extends ContextRequest {
    private String createuserid;
    private String createusername;
    private List<String> ids=new ArrayList<>();

    //补充
    private List<DiskFileTree> files=new ArrayList<>();
    private List<String> rubbishs=new ArrayList<>();
    private long capacity;
}
