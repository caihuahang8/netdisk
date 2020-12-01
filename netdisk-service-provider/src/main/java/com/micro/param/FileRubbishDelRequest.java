package com.micro.param;

import com.micro.core.ContextRequest;
import com.micro.modeltree.DiskFileRubbishTree;
import lombok.Data;

import java.util.List;
@Data
public class FileRubbishDelRequest extends ContextRequest {
    List<String> ids;//fileid
    String userid;//用户id
    List<DiskFileRubbishTree> diskFileRubbishTrees;
    List<String> rubbishIds;
}
