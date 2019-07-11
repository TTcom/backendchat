package com.kedacom.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class TestPath {
	@Test
	public void test(){
		FilePath picPath = new FilePath();
		picPath.setPath("http://127.0.0.1:8080/chat/File/abc.jpg");
		String path = picPath.getPath();
		System.out.println(path);
		System.out.println(picPath);
		
	}
	@Test
	public void testIp() throws UnknownHostException{
		InetAddress addr = InetAddress.getLocalHost();  
        String ip=addr.getHostAddress().toString(); //获取本机ip  
        String hostName=addr.getHostName().toString(); //获取本机计算机名称  
        System.out.println(ip);
        System.out.println(hostName);
	}
}
