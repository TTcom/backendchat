package com.kedacom.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Component("taskJob") 
public class TimerTaskService {
	@Scheduled(cron = "0 0 1 * * ?")
	@Test
	public void test(){     //对文件进行定时清理
		//获取指定路径下的所有文件
		File file = new File("E://resouces//file");
		if(file.isDirectory()){    //如果file是一个目录
			File[] listFiles = file.listFiles();
			if(listFiles.length>0){   //如果目录下存在文件
				//文件遍历
				for (File uploadFile : listFiles) {
					if(!uploadFile.isDirectory()){
						//获取文件名称
						String name = uploadFile.getName();
						//截取文件名称
						String substring = name.substring(0,19);
					
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
						Date parse=null;
						try {
							//将截取到的文件名称转换为时间格式
							parse = simpleDateFormat.parse(substring);
							//获取当前时间并转为时间戳
							Date nowTime=new Date();
							long time=nowTime.getTime();
                            //计算出文件存放的时间和现在的时间相差的天数
							long day=(time-parse.getTime())/(24*60*60*1000);
							
				            if(day>=2){      //大于两天删除文件
				            	uploadFile.delete();
				            }
							
						} catch (ParseException e) {
							e.printStackTrace();
						}
						
						
					}
				}
			}
			
		}
	}
}
