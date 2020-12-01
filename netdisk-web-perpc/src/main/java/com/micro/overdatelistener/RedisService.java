package com.micro.overdatelistener;


import com.alibaba.dubbo.config.annotation.Reference;
import com.micro.disk.service.NoticeService;
import com.micro.disk.service.UserCapacityService;
import com.micro.disk.user.bean.SessionUserBean;
import com.micro.disk.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Component;

@Component
public class RedisService {
	@Reference(check = false)
	UserService userService;
	@Reference(check = false)
	UserCapacityService userCapacityService;

	public void recoveUser(String userid){
		SessionUserBean sessionUserBean = userService.findOne(userid);
		userService.updateToken("token-user",userid);
		userCapacityService.reduceCapacity(0,1000000L,userid,sessionUserBean.getUsername(),"VIP容量减少");
	}


}
