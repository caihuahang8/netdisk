package com.micro.disk.user.service;

import com.micro.disk.user.bean.OrderBean;

public interface UserServiceRedis {
    void setSessionUser(String token,Object obj);
    void removeSessionUser(String key);
    void saveSessionOrder(OrderBean orderBean);
    OrderBean getSessionOrder(String orderid);
}
