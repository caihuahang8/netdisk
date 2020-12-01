package com.micro.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.common.CookieUtils;
import com.micro.common.Result;
import com.micro.common.ResultUtils;
import com.micro.disk.service.UserCapacityService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import com.micro.disk.user.service.UserServiceRedis;
import com.micro.mvc.UserInfoUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping("/security")
public class LoginController {
    @Reference(check=false)
    private UserService userService;
    @Reference(check=false)
    private UserCapacityService userCapacityService;
    @Reference(check=false)
    private UserServiceRedis userServiceRedis;
    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        try{
            //TODO 登陆缓存
            //1.先去登陆
            SessionUserBean bean = userService.login(username,password);
            //SessionUserBean bean=userService.getUserByToken("token-test");
            // SessionUserBean bean = (SessionUserBean) request.getAttribute("userInfo");
            if(bean!=null){
                //设置Cookie
                String uuid = bean.getId();
                CookieUtils.addCookie(response,uuid,60*60*24*7);
                //设置Redis
                userServiceRedis.setSessionUser(uuid,bean);
                //设置用户信息
                request.setAttribute("userid",bean.getId());
                request.setAttribute("username",bean.getNickname());

                //初始化容量
                userCapacityService.init(bean.getId(), bean.getNickname());
            }else{
                throw new RuntimeException("用户名或密码错误");
            }
            return ResultUtils.success("认证成功", bean);
        }catch (Exception e ){
            return ResultUtils.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request){
        try {
            SessionUserBean sessionUserBean = UserInfoUtils.getBean(request);
            userServiceRedis.removeSessionUser(sessionUserBean.getId());
            return ResultUtils.success("退出成功",null);
        }catch (Exception e){
            return ResultUtils.error("退出失败");
        }
    }
}
