package com.micro.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.micro.common.Contanst;
import com.micro.common.CookieUtils;
import com.micro.common.ValidateUtils;
import com.micro.common.json.JsonJackUtils;
import com.micro.dao.DiskOrderDao;
import com.micro.dao.DiskUserDao;
import com.micro.disk.user.bean.OrderBean;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.bean.UserBean;
import com.micro.disk.user.service.UserService;
import com.micro.model.DiskOrder;
import com.micro.model.DiskUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.management.timer.Timer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass=UserService.class)
@Component
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    DiskUserDao diskUserDao;
    @Autowired
    DiskOrderDao diskOrderDao;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final JsonJackUtils jsonJackUtils = new JsonJackUtils();
    @Override
    public Page findUserPageList(Integer page, Integer limit, String username, String nickname) {
        return null;
    }

    @Override
    public List findUserTree(String pid, String type) {
        return null;
    }

    @Override
    public SessionUserBean login(String username, String password) {
        //去disk_user查找账号
        DiskUser diskUser = diskUserDao.getDiskUserByUsernameAndPassword(username,password);
        if(diskUser==null){
            throw new RuntimeException("登陆错误，账号或者密码不正确");
        }
        /*
        String uuid = UUID.randomUUID().toString();
        String json = jsonJackUtils.objectToJson(diskUser);
        stringRedisTemplate.opsForValue().set(Contanst.SESSION_USER+uuid,json,7, TimeUnit.DAYS);
        CookieUtils.addCookie(response,uuid,60*60*24*7);
         */

        SessionUserBean sessionUserBean = new SessionUserBean();
        sessionUserBean.setId(diskUser.getId());
        sessionUserBean.setNickname(diskUser.getNickname());
        sessionUserBean.setTelephone(diskUser.getTelephone());
        sessionUserBean.setToken(diskUser.getToken());
        sessionUserBean.setUsername(diskUser.getUsername());
        return sessionUserBean;
    }

    @Override
    public SessionUserBean getUserByToken(String token) {
        //1.redis取session
        String jsonbean = stringRedisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(jsonbean)){
            return null;
        }
        SessionUserBean sessionUserBean = jsonJackUtils.jsonToPojo(jsonbean,SessionUserBean.class);
        ValidateUtils.validate(sessionUserBean.getId(),"用户id");
        ValidateUtils.validate(sessionUserBean.getNickname(),"昵称");
        ValidateUtils.validate(sessionUserBean.getUsername(),"用户名");
        return sessionUserBean;
        /*
        SessionUserBean bean=new SessionUserBean();
        if("token-test".equals(token)||"undefined".equals(token)){
            bean.setId("2");
            bean.setNickname("测试账号");
            bean.setUsername("test");
            bean.setTelephone("13307773517");
            bean.setToken("token-test");
            return bean;
        }else if("token-admin".equals(token)){
            bean.setId("1");
            bean.setNickname("超级管理员");
            bean.setUsername("admin");
            bean.setTelephone("15177917744");
            bean.setToken("token-admin");
            return bean;
        }
         */
    }

    /**
     * 登出
     * @param token
     */
    @Override
    public void logout(String token) {

    }

    /**
     * 登陆
     * @param userBean
     */
    @Override
    public void register(UserBean userBean) {
        if (userBean==null){
            throw new RuntimeException("UserBean为null");
        }
        if (userBean.getUsername()==null||userBean.getPassword()==null){
            throw new RuntimeException("UserBean为null");
        }
        try {
            DiskUser diskUser = new DiskUser();
            BeanUtils.copyProperties(userBean, diskUser);
            diskUser.setCreatetime(new Date());
            diskUser.setToken("token-user");
            diskUserDao.save(diskUser);
        }catch (Exception e){
            throw  new RuntimeException("插入错误");
        }
    }

    @Override
    public void saveOrder(OrderBean orderBean) {
        ValidateUtils.validate(orderBean.getOrderid(),"orderid");
        ValidateUtils.validate(orderBean.getOrderAmount(),"amount");
        ValidateUtils.validate(orderBean.getBuymonths(),"butmonths");
        ValidateUtils.validate(orderBean.getUsername(),"username");
        ValidateUtils.validate(orderBean.getTelephone(),"telphone");
        ValidateUtils.validate(orderBean.getUserid(),"userid");
        DiskOrder diskOrder = new DiskOrder();
        BeanUtils.copyProperties(orderBean,diskOrder);
        diskOrder.setCreatetime(new Date());
        diskOrder.setUpdateTime(new Date());
        diskOrder.setOrderAmount(orderBean.getOrderAmount());
        diskOrderDao.save(diskOrder);
    }

    @Override
    public void updateOrder(String orderid) {
        if (StringUtils.isEmpty(orderid)){
            throw new RuntimeException("订单编号不存在");
        }
        diskOrderDao.updatePayStatus(orderid);
        stringRedisTemplate.delete(Contanst.ORDER+orderid);
    }

    @Override
    public void updateToken(String token,String userid) {
        ValidateUtils.validate(token,"token");
        ValidateUtils.validate(userid,"userid");
        diskUserDao.updateToken(token,userid);
        stringRedisTemplate.opsForValue().set(Contanst.VIP+userid,token,30, TimeUnit.DAYS);
    }
    @Override
    public SessionUserBean findOne(String userid){
        ValidateUtils.validate(userid,"userid");
        DiskUser diskUser = diskUserDao.findOne(userid);
        SessionUserBean sessionUserBean = new SessionUserBean();
        sessionUserBean.setId(diskUser.getId());
        sessionUserBean.setNickname(diskUser.getNickname());
        sessionUserBean.setTelephone(diskUser.getTelephone());
        sessionUserBean.setToken(diskUser.getToken());
        sessionUserBean.setUsername(diskUser.getUsername());
        return sessionUserBean;
    }


}
