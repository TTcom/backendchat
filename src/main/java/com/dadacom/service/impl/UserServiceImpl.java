package com.kedacom.service.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kedacom.dao.UserDao;
import com.kedacom.model.User;
import com.kedacom.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	//插入用户
	@Override
	public void addUser(User user) {
		//判断
		
		userDao.insertUser(user);
	}
	
	//通过id删除用户
	@Override
	public void removeUserById(int id) {
		userDao.deleteUserById(id);
	}
    //更新用户
	@Override
	public void modifyUser(User user) {
		userDao.updateUser(user);
		
	}
    //通过id查找用户
	@Override
	public User findUserById(int id) {
	//	User user = userDao.selectUserById(id);
		return userDao.selectUserById(id);
	}
	
    //判断用户名是否重复
	@Override
	public User findRepeatUser(String username) {

		return userDao.selectUserByName(username);
	}
     //用户登录判断
	@Override
	public User findUserByNameAndPassword(String username, String password) {
		return userDao.selectUserByNameAndPassword(username, password);
	}
    //用户分页查找
	@Override
	public ArrayList<User> findUsers(int pageSize, int currentPage) {
		ArrayList<User> userList = userDao.selectUsers(currentPage*pageSize-pageSize,pageSize);
		return userList;
	}
   //获取用户总数
	@Override
	public int findUserCount() {
		int selectUserCount = userDao.selectUserCount();
		return selectUserCount;
	}
    //获取模糊搜索匹配的用户数量
	@Override
	public int findUserByStringCounts(String username) {
		int selectUserCount = userDao.selectUsersBystringCount(username);
		return selectUserCount;
	}
	//用户模糊搜索
	@Override
	public ArrayList<User> findUserByString(String username,int pageSize, int currentPage) {
		ArrayList<User> userList =userDao.selectUserByString(username,currentPage*pageSize-pageSize,pageSize);
		return userList;
	}

   //更新用户头像
	@Override
	public void modifyUserPicPathById(int id, String picpath) {
		userDao.updateUserPicPathById(id,picpath);
	}

	@Override
	public boolean hasUserAuthority(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user != null && user.getAuthority()==0){
			return true;
		}
		return false;
	}

	@Override
	public boolean hasAdminAuthority(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if(user != null && user.getAuthority()==1){
			return true;
		}
		return false;
	}
	
	

	

}
