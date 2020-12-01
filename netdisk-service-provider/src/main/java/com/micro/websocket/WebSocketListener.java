package com.micro.websocket;

import com.micro.utils.SpringContentUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint(value="/websocket/{userid}/{token}")
public class WebSocketListener {


	/**
	 * 建立连接的时候
	 * @param session
	 * @param userid
	 * @param token
	 */
	@OnOpen
	public void onOpen(Session session,@PathParam("userid") String userid,@PathParam("token") String token){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}

		PushService pushService=(PushService) SpringContentUtils.applicationContext.getBean("pushService");
		//		//添加Session
		//		SessionUtils.addChannel(userid, token, session);
		//
		//		//查询容量和通知
		pushService.pushCapacityOnOpen(userid, session);
		pushService.pushNoticeOnOpen(userid, session);

	}


	/**
	 * 关闭的时候
	 * @param session
	 * @param userid
	 * @param token
	 */
	@OnClose
	public void onClose(Session session,@PathParam("userid") String userid,@PathParam("token") String token){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}
    	SessionUtils.removeChannel(userid, token);
	}


	/**
	 * 错误的时候
	 * @param session
	 * @param userid
	 * @param token
	 * @param error
	 */
	@OnError
	public void onError(Session session,@PathParam("userid") String userid,@PathParam("token") String token,Throwable error){
		if(StringUtils.isEmpty(userid)||"null".equals(userid)){
			return;
		}
		if(StringUtils.isEmpty(token)||"null".equals(token)){
			return;
		}

		SessionUtils.removeChannel(userid, token);
	}
}
