package com.kedacom.model;

import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Test;

public class TestArrayListRemove {
	@Test
	public void test(){
		CopyOnWriteArrayList<User> onlineUserList = new CopyOnWriteArrayList<User>();
		User user1 = new User();
		user1.setId(0);
		user1.setPassword("123");
		user1.setUsername("123");
		user1.setPicpath("erhgerg");
		User user2 = new User();
		user2.setId(1);
		user2.setPassword("234");
		user2.setUsername("234");
		user2.setPicpath("gufu");
		onlineUserList.add(user1);
		onlineUserList.add(user2);
		User user3 = new User();
		user3.setId(0);
		user3.setPassword("123");
		user3.setUsername("123");
		user3.setPicpath("erhgerg");
		//onlineUserList.add(user3);
		System.out.println("删除之前："+onlineUserList);
		onlineUserList.remove(user3);
		System.out.println("删除之后："+onlineUserList);
		
		
	}
}
