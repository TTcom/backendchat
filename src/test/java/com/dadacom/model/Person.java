package com.kedacom.model;

public class Person {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person(String name) {
		this.name = name;
	}

	public Person() {
		System.out.println("Person()");
	}

	@Override
	public String toString() {
		return "Person [name=" + name + "]";
	}
	
}
