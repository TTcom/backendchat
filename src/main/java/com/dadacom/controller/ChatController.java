package com.kedacom.controller;

import java.net.InetAddress;   //获取本机Ip
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedacom.model.Para;
import com.kedacom.model.ReturnMessage;
import com.kedacom.model.User;
import com.kedacom.model.UserInfo;
import com.kedacom.websocket.MyWebSocket;


@RestController
@RequestMapping("/chat")
@CrossOrigin   //允许跨域访问
public class ChatController {
	
	//退出登录接口
	@PostMapping("/signOut")
	public ReturnMessage<UserInfo> signOut(HttpServletRequest request) {
		ReturnMessage<UserInfo> returnMessage = new ReturnMessage<UserInfo>();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		session.removeAttribute("user");
		MyWebSocket.onlineUserList.remove(user);
		returnMessage.setCode(0);
		returnMessage.setMessage("用户退出成功");
		
		return returnMessage;
	}
	@PostMapping("/keepAlive")
	public Para keepAlive() {
		Para para = new Para();
		System.out.println("刷新成功");
		para.setName("刷新成功");
		return para;
	
	}
     // 获取用户信息、判断用户权限、获取本地ip
	@PostMapping("/getUserInfo")
	public ReturnMessage<User> getUserInfo(HttpServletRequest request) {
		ReturnMessage<User> returnMessage = new ReturnMessage<User>();
		HttpSession session = request.getSession();
		User user =  (User) session.getAttribute("user");
		System.out.println("获取当前用户信息"+user+new Date());
		
		if(user == null){
			//没有user返回3
			returnMessage.setCode(3);
		}else{
			System.out.println(user.getUsername());
			if(!user.getUsername().equals("admin") ){
				//不是管理员返回0
				returnMessage.setCode(0);
			}else{
				//是管理员返回1
				returnMessage.setCode(1);
			}
		}
		//定义地址对象
		InetAddress addr=null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}  
		if(addr != null){
			    String ip=addr.getHostAddress().toString(); //获取本机ip  
				returnMessage.setMessage(ip);
		}
		returnMessage.setData(user);
		return returnMessage;
	}

}
