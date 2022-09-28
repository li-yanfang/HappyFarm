package com.zqsoft.dao;

//游戏中，用户存在的基本信息，进行读写操作
import java.util.ArrayList;
import java.util.List;

import com.zqsoft.bean.UserBean;
import com.zqsoft.frame.GameMember;
import com.zqsoft.utils.FileUtils;

public class UserDao {
	
	//读取用户基本信息文件，获取用户全部信息
	public List<UserBean> getAllUsers() {
		String filename = "user/userbase.txt";	//游戏用户数据
		List<UserBean> allUsers = new ArrayList<UserBean>();	//所有用户信息
		List<String> allLine = FileUtils.readFile(filename);	//读取用户文件内容
		for(int i= 0;i<allLine.size();i++) {
			String line = allLine.get(i);
			String [] tmp = line.split(";");	//拆分信息
			//获取用户信息
			int userId = Integer.parseInt(tmp[0]);
			String userName = tmp[1];
			String password = tmp[2];			
			String nickName = tmp[3];
			String useModText = tmp[4];
			String notice = tmp[5];
			String pic = tmp[6];
			//构建UserbBean对象
			UserBean userBean = new UserBean();
			userBean.setUserId(userId);
			userBean.setUserName(userName);
			userBean.setPassword(password);
			userBean.setNotice(notice);
			userBean.setPic(pic);
			userBean.setUserModText(useModText);
			userBean.setNickName(nickName);
			//将UserBean添加到集合中
			allUsers.add(userBean);	
		}
		return allUsers;
	}
		
	/**
	 * 将所有用户对象写到文件中
	 * @param allUsers所有用户对象
	 */
	public static void saveUser(List<UserBean> allUsers) {
		String filename = "user/userbase.txt";
		String str= "";
		for(int i = 0; i <allUsers.size();i++) {
			UserBean user = allUsers.get(i);
			String pic = user.getPic();
			pic = pic.substring(pic.lastIndexOf("/")+1);
			str+=(user.getUserId()+";"+user.getUserName()+";"+user.getPassword()+";"+user.getNickName()+";"+user.getUserModText()+";"+user.getNotice()+";"+user.getPic());
			if(i!=allUsers.size()-1) {
				str+="\n";
			}
		}
		FileUtils.writeFile(filename,str);
	}
}
