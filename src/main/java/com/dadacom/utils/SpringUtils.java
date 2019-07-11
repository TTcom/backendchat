package com.kedacom.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext=applicationContext;
	}

	public static <T> T getBean(String beanId) {
		@SuppressWarnings("unchecked")
		T t = (T) applicationContext.getBean(beanId);
		return t;
	}

}
