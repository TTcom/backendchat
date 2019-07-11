package com.kedacom.model;

public class Coder extends Person {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Coder [name=" + name + "]";
	}

	public Coder(String name) {
		this.name = name;
	}

	public Coder() {
		System.out.println("Coder()");
	}
	
}
