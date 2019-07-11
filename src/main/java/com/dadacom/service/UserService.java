package com.kedacom.service;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import com.kedacom.model.User;
public interface UserService {
	void addUser(User user);
	void removeUserById(int id);
	void modifyUser(User user);
	User findUserById(int id);
	User findRepeatUser(String username);
	User findUserByNameAndPassword(String username, String password);
	ArrayList<User> findUsers(int pageSize,int currentPage);
	int findUserCount();
	ArrayList<User> findUserByString(String username, int pageSize, int currentPage);
	int findUserByStringCounts(String username);
	void modifyUserPicPathById(int id,String picpath);
	boolean hasUserAuthority(HttpSession session);
	boolean hasAdminAuthority(HttpSession session);
    
}
