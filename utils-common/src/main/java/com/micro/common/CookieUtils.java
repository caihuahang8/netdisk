package com.micro.common;



import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtils {

    public static void addCookie(HttpServletResponse response, String token,Integer expire) {
        Cookie cookie = new Cookie(Contanst.SESSION_USER, token);
        cookie.setMaxAge(expire);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static String getCookieValue(HttpServletRequest request, String cookiName) {
        Cookie[]  cookies = request.getCookies();
        if(cookies == null || cookies.length <= 0){
            return null;
        }
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(cookiName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
    public static String getCookie(HttpServletRequest request){
        String paramtoken = request.getParameter(Contanst.SESSION_USER);
        String cookievalue = getCookieValue(request,Contanst.SESSION_USER);
        if (!StringUtils.isEmpty(paramtoken)){
            return  paramtoken;
        }
        if (!StringUtils.isEmpty(cookievalue)){
            return cookievalue;
        }
        return null;
    }
    public static void removeCookie(String path, HttpServletResponse response) {
        Cookie cookie = new Cookie(Contanst.SESSION_USER, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
