package com.kedacom.websocket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.alibaba.fastjson.JSON;
import com.kedacom.dao.UserDao;
import com.kedacom.model.Message;
import com.kedacom.model.User;
import com.kedacom.utils.SpringUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@ServerEndpoint(value = "/websocket/{userId}")
@Slf4j
@CrossOrigin
public class MyWebSocket {
	// 用户列表使用并发容器解决并发异常 ,set不可放重复的用户，list可以放重复的
	public static CopyOnWriteArraySet<User> onlineUserList = new CopyOnWriteArraySet<User>();

	private Integer userid; // 用户名Id

	private Session session;

	private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();

	private static HashMap<String, Session> userIdToSessionroute = new HashMap<String, Session>(); // 用户名和websocket的session绑定的路由表

	@OnOpen
	public void onOpen(@PathParam("userId") Integer userId, Session session, EndpointConfig config){
		this.userid = userId;
		System.out.println("userId:" + userId);
		// 获取新连接的用户
		// User user = userDao.selectUserById(userId);
		UserDao userDao = SpringUtils.getBean("userDao");
		User user = userDao.selectUserById(userId);
		
		user.setPassword(null);
		// 在线用户列表添加新用户
		onlineUserList.add(user);
		this.session = session;
		webSocketSet.add(this);

		userIdToSessionroute.put(userId.toString(), session);

		log.info("[websoket消息] 有新连接，总数：" + webSocketSet.size());
		// 构建消息进行推送
		Message message = new Message();
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		message.setDate(format);
		message.setMessageType(2);
		message.setMessageBody(JSON.toJSONString(onlineUserList));
		// 广播新连接建立消息推送
		sendMessage(JSON.toJSONString(message));

	}
    //    离线推送
	@OnClose
	public void onClose() {
		webSocketSet.remove(this);
		userIdToSessionroute.remove(userid.toString());
		/*for (User user : onlineUserList) {
			if (user.getId() == userid) {
				onlineUserList.remove(user);
				userIdToSessionroute.remove(userid.toString());
			}
		}*/
		Iterator<User> iterator=onlineUserList.iterator();
		while(iterator.hasNext()){
			User user = iterator.next();
			if(user.getId() == userid){
				onlineUserList.remove(user);
			}
		}
		// 构建消息进行推送
		Message message = new Message();
		Date date = new Date();
		UserDao userDao = SpringUtils.getBean("userDao");
		User user = userDao.selectUserById(userid);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		message.setDate(format);
		message.setOfflineUserId(userid);
		message.setOfflineUserName(user.getUsername());
		message.setMessageType(3);
		message.setMessageBody(JSON.toJSONString(onlineUserList));
		// 广播新连接建立消息推送
		sendMessage(JSON.toJSONString(message));

		log.info("[websoket消息] 连接断开，总数：" + webSocketSet.size());

	}

	@OnMessage
	public void onMessage(String reveiveMessage) {
		log.info("[websoket消息] 收到客户端发来的消息：" + reveiveMessage);
		// this.sendMessage(reveiveMessage);
		Message message = JSON.parseObject(reveiveMessage, Message.class);
		 System.out.println(message);
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		message.setDate(format);
		if (message.getMessageType() == 0) {
			// 广播
			this.sendMessage(JSON.toJSONString(message));
		} else {
			// 私聊
			Integer receiverId = message.getReceiverId();
			Session receiverSession = userIdToSessionroute.get(receiverId.toString());
			Session currentUserSession = userIdToSessionroute.get(this.userid.toString());
			try {
				System.out.println("私聊消息：" + JSON.toJSONString(message));
				receiverSession.getBasicRemote().sendText(JSON.toJSONString(message));
				currentUserSession.getBasicRemote().sendText(JSON.toJSONString(message));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void sendMessage(String message) {
		for (MyWebSocket myWebSocket : webSocketSet) {
			log.info("[websoket消息] 广播消息：" + message);
			try {
				myWebSocket.session.getBasicRemote().sendText(message);
			} catch (IOException e) {
				log.error("[websoket消息] 广播消息异常：" + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	
}
