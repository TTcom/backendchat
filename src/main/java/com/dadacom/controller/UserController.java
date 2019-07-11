//包名
package com.kedacom.controller;

import java.math.BigInteger;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//引入 各种依赖
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kedacom.model.ReturnMessage;
import com.kedacom.model.User;
import com.kedacom.service.UserService;
import com.kedacom.websocket.MyWebSocket;

import lombok.extern.slf4j.Slf4j;

//返回json需要RestController
@RestController
// 客户端请求接口
@RequestMapping("/user")
//允许不同域名请求
@CrossOrigin
//打印日志
@Slf4j
//定义user控制类
public class UserController {
	//引入userService实体类
	@Autowired
	private UserService userService;

	// 设定注册接口以及接口请求方法
	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException 
	 * @throws UnknownHostException
	 */
	@PostMapping("/register")
	// 返回ReturnMessage数据实体格式
	public ReturnMessage<Object> register(String username, String password) throws NoSuchAlgorithmException {
		//创建returnMessage对象
		ReturnMessage<Object> returnMessage = new ReturnMessage<Object>();
		//setmd5对密码进行加密
		String setmd = setmd5(password);
		//创建user实体对象并插入对象属性值
		User user = new User(username, setmd);
		user.setPicpath("/res/picture/" + "default.jpg");
		// 通过userservice中的addUser方法向数据库插入数据
		userService.addUser(user);
		//设置返回参数
		returnMessage.setCode(1);
		returnMessage.setData("注册成功");
		return returnMessage;
	}
	//创建私有setmd5方法对行密码md5加密
	private String setmd5(String text) throws NoSuchAlgorithmException {
		 // 生成一个MD5加密计算摘要
        MessageDigest md = MessageDigest.getInstance("MD5");
        // 计算md5函数
        md.update(text.getBytes());
        // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
        // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
        String ss= new BigInteger(1, md.digest()).toString(16);
        System.out.println(ss);
		 return ss;
	}
	/**
	 * service add find remove modify dao insert select delete update
	 * 
	 * @param id
	 * @return
	 */
	// 删除用户
	@PostMapping("/deletuser")
	public ReturnMessage<Object> deletuser(int id,HttpSession session) {
		
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();
		//判断管理员身份
		boolean hasAdminAuthority = userService.hasAdminAuthority(session);
		if(!hasAdminAuthority){
			returnMessage.setCode(4);
			returnMessage.setMessage("session已失效");
			return returnMessage;
		}		
        //根据用户id获取用户数据
		User user = userService.findUserById(id);
		if (user != null) {
			user.setPassword(null);
			log.info("要删除的用户："+user);
			//获取在线用户列表
			Iterator<User> iterator = MyWebSocket.onlineUserList.iterator();
			boolean flag = false;
			while (iterator.hasNext()) {
				User next = iterator.next();
				System.out.println("遍历在线用户："+next);
				if (next.equals(user)) {
					// 不能删除在线用户
					flag = true;
					returnMessage.setCode(3);
					returnMessage.setData("用户删除失败");
					return returnMessage;
				}
			}
			if (!flag) {
				userService.removeUserById(id);
				returnMessage.setCode(0);
				returnMessage.setData("用户删除成功");
			}
		 }else {
			returnMessage.setCode(1);
			returnMessage.setData("操作的用户不存在");
		}
		return returnMessage;
	}
    //用户信息更新
	@PostMapping("/updateuser")
	public ReturnMessage<Object> updateuser(int id, String username, String password,HttpSession session) throws NoSuchAlgorithmException {
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();
		//判断session是否失效
		boolean hasAdminAuthority = userService.hasAdminAuthority(session);
		if(!hasAdminAuthority){
			returnMessage.setCode(3);
			returnMessage.setMessage("session已失效");
			return returnMessage;
		}
		
		String setmd = setmd5(password);
		User user = new User(username, setmd);
		user.setId(id);
		// 判断是否在线
		Iterator<User> iterator = MyWebSocket.onlineUserList.iterator();
		boolean flag = false;
		while (iterator.hasNext()) {
			User next = iterator.next();
			System.out.println("遍历在线用户："+next);
			if (next.getId()==id) {
				// 不能删除在线用户
				flag = true;
				returnMessage.setCode(1);
				returnMessage.setData("用户在线无法更新");
				return returnMessage;
			}
		}
		if (!flag) {
			userService.modifyUser(user);
			System.out.println(id + username + password);
			returnMessage.setCode(0);
			returnMessage.setData("用户修改成功");
		}
		 return returnMessage;
	}

	// 用户信息模糊查询
	@PostMapping("/findusers")
	public ReturnMessage<Object> finduser(String username, int pageSize, int currentPage,HttpSession session) {
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();
		boolean hasAdminAuthority = userService.hasAdminAuthority(session);
				if(!hasAdminAuthority){
					returnMessage.setCode(3);
					returnMessage.setMessage("session已失效");
					return returnMessage;
				}
	    //获取符合条件的用户对象并生成数组userList对象
		ArrayList<User> userList = userService.findUserByString(username, pageSize, currentPage);
		if (userList != null) {
			returnMessage.setCode(0);
			returnMessage.setData(userList);
			//获取符合条件的用户总数
			int findUsersCount = userService.findUserByStringCounts(username);
			int totalPage = 0;
			if (findUsersCount % pageSize == 0) {
				totalPage = findUsersCount / pageSize;
			} else {
				totalPage = findUsersCount / pageSize + 1;
			}
			returnMessage.setMessage(totalPage + "");
		} else {
			returnMessage.setCode(1);
			returnMessage.setData("无用户信息");
		}
		return returnMessage;
	}
    //用户注册判断用户名是否重复
	@PostMapping("/repeatuser")
	public ReturnMessage<Object> repeatuser(String username) {
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();

		// 获取通过数据库查找的数据并按照user的模型赋值
		System.out.println(username);
		User user = userService.findRepeatUser(username);
		if (user != null) {
			returnMessage.setCode(1);
			returnMessage.setData("用户名已经注册");
		} else {
			returnMessage.setCode(0);
		}
		return returnMessage;
	}
    //用户登录和管理员登录
	@PostMapping("/userlogin")
	public ReturnMessage<Object> userlogin(String username, String password, HttpServletRequest request) throws NoSuchAlgorithmException {
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();

		// 获取通过数据库查找的数据并按照user的模型赋值
		String setmd = setmd5(password);
		
		User user = userService.findUserByNameAndPassword(username, setmd);
		if (user != null) {
			//判断用户是否已经登录
			HttpSession session = request.getSession();
			User hasUser = (User) session.getAttribute("user");
			
			if(hasUser != null){     //判断当前浏览器有用户的session
				if(hasUser.getAuthority()==1){
					returnMessage.setCode(2);
					returnMessage.setData("管理员登录成功");
					return returnMessage;
				}else{
					returnMessage.setCode(0);
					returnMessage.setData("登陆成功");
					return returnMessage;
				}
			}else{    //如果当前浏览器没有session 判断用户是否在别的浏览器登录
				Iterator<User> iterator = MyWebSocket.onlineUserList.iterator();
				while(iterator.hasNext()){
					User next = iterator.next();
					if(user.getUsername().equals(next.getUsername())){
						returnMessage.setCode(4);
						returnMessage.setData("账号已在别处登录");
						return returnMessage;
					}
				}
			}
			 //如果当前浏览器没有session
			if (user.getAuthority()==1) {  
				returnMessage.setCode(2);
				returnMessage.setData("管理员登录成功");
				user.setPassword(null);
				session.setAttribute("user", user);
				return returnMessage;
			}
			returnMessage.setCode(0);
			returnMessage.setData("登陆成功");
			// 保存当前用户信息
			user.setPassword(null);
			session.setAttribute("user", user);
			System.out.println("登陆成功后存储用户信息，时间：" + new Date());
		} else { //数据库没有该用户
			returnMessage.setCode(1);
			returnMessage.setData("用户密码不正确");
			
		}
		return returnMessage;
	}

	// 后台分页用户列表查询
	@PostMapping("/findUsersByPage")
	public ReturnMessage<ArrayList<User>> findUsersByPage(int pageSize, int currentPage,HttpSession session) {
	
		ReturnMessage<ArrayList<User>> returnMessage = new ReturnMessage<>();
            
		boolean hasAdminAuthority = userService.hasAdminAuthority(session);
		System.out.println("hasAdminAuthority"+hasAdminAuthority);
		if(!hasAdminAuthority){
			returnMessage.setCode(3);
			returnMessage.setMessage("session已失效");
			return returnMessage;
		}
		
		// 获取通过数据库查找的数据并按照user的模型赋值
		ArrayList<User> userList = userService.findUsers(pageSize, currentPage);
		
		if (userList.size() > 0) {
			returnMessage.setData(userList);
		}
		// 获取共有多少一条数据
		int findUserCount = userService.findUserCount();
		int totalPage = 0;
		if (findUserCount % pageSize == 0) {
			totalPage = findUserCount / pageSize;
		} else {
			totalPage = findUserCount / pageSize + 1;
		}
		returnMessage.setMessage(totalPage + "");
		return returnMessage;
	}

}
