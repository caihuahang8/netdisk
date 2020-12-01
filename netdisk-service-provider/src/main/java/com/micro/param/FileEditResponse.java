package com.micro.param;

import com.micro.core.ContextResponse;
import lombok.Data;

@Data
public class FileEditResponse extends ContextResponse {
 private String id;
 private String filename;
 private String filemd5;
}
