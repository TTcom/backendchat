package com.kedacom.model;

import java.util.ArrayList;

public class MainTest {

	public static void main(String[] args) {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Hello");
		arrayList.add("world");
		arrayList.add("dfhh");
		arrayList.add("gsg");
		arrayList.add("rhgrhg");
		for (String string : arrayList) {
			if(string.equals("gsg")){
				arrayList.remove(string);
			}
		}
		/*for (int i = 0; i <=arrayList.size(); i++) {
			arrayList.remove(arrayList.get(1));
		}*/
		System.out.println(arrayList);
	}

}
