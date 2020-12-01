package com.micro.param;

import com.micro.core.ContextRequest;
import com.micro.disk.bean.ShareFriendsBean;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ShareFriendsRequest extends ContextRequest {
    //java.util.List<String> ids,List<ShareFriendsBean> friends,String title,String userid,String username,Integer type
    private String shareid;
    private List<String> ids;
    private List<ShareFriendsBean> friends;
    private String title;
    private String userid;
    private String username;
    private Integer type;

}
