package com.kedacom.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class ParaTset {
	@Test
	public void dsfgdh(){
		Para para = new Para();
	     System.out.println(para);
	}
	@Test
	public void date(){
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format = simpleDateFormat.format(date);
		System.out.println(format);
	}
	@Test
	public void date2(){
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
		String format = simpleDateFormat.format(date);
		System.out.println(format);
	}
	@Test
	public void date3(){
		
	}
}
