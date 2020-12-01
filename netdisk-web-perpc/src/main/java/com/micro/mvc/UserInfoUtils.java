package com.micro.mvc;

import com.micro.disk.user.bean.SessionUserBean;

import javax.servlet.http.HttpServletRequest;

public class UserInfoUtils {
public static SessionUserBean getBean(HttpServletRequest request){
    SessionUserBean sessionUserBean = (SessionUserBean) request.getAttribute("userInfo");
    return  sessionUserBean;
}
}
