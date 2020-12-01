package com.micro.param;

import com.micro.core.ContextRequest;
import com.micro.disk.bean.ShareFriendsBean;
import lombok.Data;

import java.util.List;

@Data
public class ShareSecretRequest extends ContextRequest {
    private List<String> ids;
    private String title;
    private String userid;
    private String username;
    private Integer sharetype;
    private Integer effect;
    private Integer type;
    private List<ShareFriendsBean> friends;
    //补充
    private String shareid;
}
