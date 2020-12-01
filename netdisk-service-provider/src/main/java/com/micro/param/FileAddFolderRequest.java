package com.micro.param;

import com.micro.core.ContextRequest;
import lombok.Data;

@Data
public class FileAddFolderRequest extends ContextRequest {
    private String filename;
    private String userid;
    private String username;
    private String pid;
}
