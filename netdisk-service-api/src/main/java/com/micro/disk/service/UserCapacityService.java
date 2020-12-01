package com.micro.disk.service;


import com.micro.disk.bean.UserCapacityBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


public  interface UserCapacityService {
    /**
     * 获取用户的容量
     * @param userid
     * @return
     */
    public UserCapacityBean findUserCapacity(String userid);
    /**
     * 更新容量
     * @param type 0（新增已用容量，减少总容量），1（减少已用容量，新增总容量）
     * @param capacity
     * @param userid
     * @param username
     * @param remark
     */
    public void updateUserCapacity(int type,long capacity,String userid,String username,String remark);

    /**
     * 初始化（个人登录自动初始化）
     * @param userid
     * @param username
     */
    public void init(String userid,String username);

    public void addCapacity(int type,long capacity,String userid,String username,String remark);
    public void reduceCapacity(int type, long capacity, String userid, String username, String remark);

}
