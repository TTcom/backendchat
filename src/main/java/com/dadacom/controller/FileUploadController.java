package com.kedacom.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kedacom.model.FilePath;
import com.kedacom.model.ReturnMessage;
import com.kedacom.model.User;
import com.kedacom.service.UserService;

import lombok.extern.slf4j.Slf4j;

//0图片上传失败，1是图片上传成功，3文件上传成功
@RestController
@RequestMapping("/file")
@CrossOrigin
@Slf4j    //打印login日志管理
public class FileUploadController {
	@Autowired   //查找元素
	private UserService userService;
     //用户头像上传、聊天图片发送和文件发送
	@PostMapping("/upload")
	public ReturnMessage<Object> upload(@RequestParam("file") MultipartFile formData, Integer id, HttpSession session) {
		
		
		
		System.out.println(id);
		ReturnMessage<Object> returnMessage = new ReturnMessage<>();
		System.out.println(formData);
		if (!formData.isEmpty()) {
			// 创建服务端文件目录
			String filename = formData.getOriginalFilename();
			String filenames = formData.getOriginalFilename();
			Date date = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH-mm-ss");
			String format = simpleDateFormat.format(date);
			filename=format+filename;
			// 限制文件类型，只能上传图片
			if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".gif")) {
				File file = new File("E:\\resouces\\picture\\"+filename);
				try {
					formData.transferTo(file);
					returnMessage.setCode(1);
					returnMessage.setMessage("图片上传成功");
					FilePath filePath = new FilePath();
					filePath.setPath("/res/picture/" + filename);
					if (id != null) {
						userService.modifyUserPicPathById(id, "/res/picture/" + filename);
						User user = (User) session.getAttribute("user");
						user.setPicpath("/res/picture/" +filename);
						session.setAttribute("user", user);
					}

					returnMessage.setData(filePath);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				File file = new File("E:\\resouces\\file\\"+filename);
				try {
					formData.transferTo(file);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				returnMessage.setCode(2);
				returnMessage.setMessage(filenames);
				FilePath filePath = new FilePath();
				filePath.setPath("http://" + getAddress() + ":8081/chat/file/download?fileName="+filename);
				returnMessage.setData(filePath);

			}
		} else {
			returnMessage.setCode(0);
			returnMessage.setMessage("文件对象不存在");
		}
		return returnMessage;
	}
	// 获取本机ip
	private String getAddress() {
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		return addr.getHostAddress().toString(); // 获取本机ip
	}
    //文件下载接口
	@GetMapping("/download")
	public String download(String fileName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if(fileName == null){
		 
		    return null;
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName="+ fileName);
		InputStream inputStream = null;
		OutputStream os = null;
		try {
			System.out.println("E:\\resouces\\file\\"+fileName);
			inputStream = new FileInputStream("E:\\resouces\\file\\"+fileName);
			os = response.getOutputStream();
			byte[] b = new byte[2048];
			int length;
			while ((length = inputStream.read(b)) > 0) {
				os.write(b, 0, length);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			os.close();
			inputStream.close();
		}
		return null;
	}

}
