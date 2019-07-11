package com.kedacom.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;

import com.kedacom.model.User;
@MapperScan
public interface UserDao {
	//插入用户
	void insertUser(User user);
    //删除用户
	void deleteUserById(int id);
	//更新用户
	void updateUser(User user);
    //通过id查找用户
	User selectUserById(int id);
	//通过用户名查找用户
	User selectUserByName(String username);
    //通过用户名和密码查找用户，用户登录
	User selectUserByNameAndPassword(@Param("username")String username, @Param("password")String password);
    //查看全部用户分页
	ArrayList<User> selectUsers(@Param("start")int start, @Param("end")int end);
    //用户总数
	int selectUserCount();
	//模糊搜索相关用户名总数
	int selectUsersBystringCount(@Param("username")String username);
    //用户模糊搜索分页
	ArrayList<User> selectUserByString(@Param("username")String username,@Param("start")int start, @Param("end")int end);
    //通过用户的id更新用户的头像地址
	void updateUserPicPathById(@Param("id")int id, @Param("picpath")String picpath);
	
}
