package com.micro.param;

import com.micro.core.ContextRequest;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ShareCancelRequest extends ContextRequest {
    private List<String> ids=new ArrayList<>();

    //补充
    private List<String> secretIds=new ArrayList<>();
    private List<String> friendIds=new ArrayList<>();
}
