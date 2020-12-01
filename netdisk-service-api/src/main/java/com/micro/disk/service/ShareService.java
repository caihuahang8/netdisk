package com.micro.disk.service;

import com.micro.disk.bean.*;

import java.util.List;

public interface ShareService {
    PageInfo<Share> findMyShare(Integer page, Integer limit, String userid, Integer type, Integer status);
    List<ShareFileBean> findShareFileListBySelf(String shareid, String pid);
    void cancelShare(List<String> ids);
    Share findShareInfo(String id);
    List<ShareFileBean> findShareFileListFromSecret(String id,String pid,String token);
    String  validateCode(String id,String code);
    void  validateShareIsEffect(String shareid);
    PageInfo<Share> findFriendsShare(Integer page, Integer limit, String userid, Integer status);
    List<ShareFileBean> findShareFileListFromFriends(String id,String pid);
    List<ShareFriendsBean> findFriends(String shareid);
    ShareBean shareSecret(List<String> ids, String title, String userid, String username, final Integer sharetype, Integer effect, Integer type);
    void shareFriends(List<String> ids,List<ShareFriendsBean> friends,String title,String userid,String username,Integer type);
}
