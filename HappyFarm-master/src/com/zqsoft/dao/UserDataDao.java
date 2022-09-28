package com.zqsoft.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.zqsoft.bean.UserData;
import com.zqsoft.utils.FileUtils;


//负责用户数据的读操作
public class UserDataDao {
	//
	public UserData getUserData(int userId) {
		String filepath = "user/userdetails/"+userId+"_data.txt";
//		List<String> str = new ArrayList();
		List<String> list = new ArrayList<String>();
		list=FileUtils.readFile(filepath);//是获得所有用户
		UserData userData;

		if(list.size()==0) {
			userData = new UserData(0,2000,userId);
			userData = new UserData(0,200,userId);
			userData.setUserId(userId);
		}else {
			String str1=list.get(0);
			String strs[]  = str1.split(";");	//用户数据数组
//			System.out.println(Integer.parseInt(strs[0]));
			userData = new UserData(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), userId);
		}
		
		return userData;
	}
	//用于新增或者修改用户数据
	public static void updateUserData(UserData data) {
		String filepath = "user/userdetails/"+data.getUserId()+"_data.txt";
		System.out.println(filepath);
		System.out.println(data.getMoney()+";"+data.getExp());
		FileUtils.writeFile(filepath, data.toString());
	}
	
}
