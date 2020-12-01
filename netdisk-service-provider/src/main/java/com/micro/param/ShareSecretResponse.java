package com.micro.param;

import com.micro.core.ContextResponse;
import lombok.Data;

@Data
public class ShareSecretResponse extends ContextResponse {
    private String url;
    private String code;
}
