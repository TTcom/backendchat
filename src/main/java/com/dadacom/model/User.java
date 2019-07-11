package com.kedacom.model;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode
public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private int id;
	private String username;
	private String password;
	private String picpath;
	private int authority;
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public User() {
	}
	
}
