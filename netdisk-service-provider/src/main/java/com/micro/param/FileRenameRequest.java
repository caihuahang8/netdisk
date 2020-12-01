package com.micro.param;

import com.micro.core.ContextRequest;
import lombok.Data;

@Data
public class FileRenameRequest extends ContextRequest {
    private String id;//文件id
    private String filename;
    private String userid;//用户id
}
