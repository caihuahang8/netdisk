package com.micro.disk.user.service;

import com.github.pagehelper.Page;
import com.micro.disk.user.bean.OrderBean;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.UserBean;
import java.util.List;

public interface UserService {
    //用户列表：分页
    public Page findUserPageList(Integer page, Integer limit, String username, String nickname);
    //用户树
    public List findUserTree(String pid, String type);
    //登录，需要生成token给客户端
    public SessionUserBean login(String username, String password);
    //根据token获取用户信息
    public SessionUserBean getUserByToken(String token);
    //登出
    public void logout(String token);
    void register(UserBean UserBean );
    void saveOrder(OrderBean orderBean);
    void updateOrder(String orderid);
    void updateToken(String token,String userid);
    SessionUserBean findOne(String userid);
}
