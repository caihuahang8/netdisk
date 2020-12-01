package com.micro.param;

import com.micro.core.ContextRequest;
import com.micro.modeltree.DiskFileRubbishTree;
import com.micro.utils.CapacityBean;
import lombok.Data;

import java.util.List;

@Data
public class FileRubbishRecoverRequest extends ContextRequest {
    private String userid;
    private String createusername;
    private String folderid;
    private List<String> ids;
    //树形结构
    private List<DiskFileRubbishTree> children;
    //容量
    private CapacityBean capacityBean;

    private List<String> rediskeys;

}
