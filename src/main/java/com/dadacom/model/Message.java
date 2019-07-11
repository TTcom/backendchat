package com.kedacom.model;

import lombok.Data;

@Data
public class Message {
	/*0:广播，1私聊,2上线信息，3离线信息*/
	/*fileType 1:图片，2:非图片文件*/
	private int messageType;
	private int fileType;
	private int senderId;
	private int receiverId;
	private int offlineUserId;
	private String offlineUserName;
	private String filenames;
	private String senderName;
	private String receiverName;
	private String filePath;
	private String date;
	private String messageBody;
}
