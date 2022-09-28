package com.zqsoft.frame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.zqsoft.bean.UserBean;
import com.zqsoft.bean.UserData;
import com.zqsoft.dao.StoreHouseDAO;
import com.zqsoft.dao.UserDao;
import com.zqsoft.dao.UserDataDao;
import com.zqsoft.guiHelper.bean.StoreHouseItem;
import com.zqsoft.guiHelper.net.bean.UserItem;
import com.zqsoft.guiHelper.net.bean.UserWindow;
import com.zqsoft.ncfarm.core.MessageDialogHelper;
import com.zqsoft.utils.FileUtils;

public class UserWin implements UserWindow {
	
	private List<UserBean> allUser;

	
	/**
	 * 
	 */
	public UserWin() {
		// TODO Auto-generated constructor stub
		UserDao  userDao = new UserDao();
		allUser=userDao.getAllUsers();
		
	}

	@Override		//修改密码
	public boolean changePassword(String userName, String oldPassword, String newPassword, String checkPassword) {
		// TODO Auto-generated method stub
		String Lpassword = null;
		for (UserBean userBean : allUser) {
			if(userName.equals(userBean.getUserName())) {
				Lpassword=userBean.getPassword();
			}
		}
		if(!oldPassword.equals(Lpassword)) {
			MessageDialogHelper.showMessageDialog("原密码错误", "修改密码");
			return false;
		}else if(newPassword.length()<6||newPassword.length()>12) {
			MessageDialogHelper.showMessageDialog("新密码必须在6-12位数之间", "修改密码");
			return false;
		}else if(!newPassword.equals(checkPassword)) {
			MessageDialogHelper.showMessageDialog("两次新密码不一样", "修改密码");
			return false;
		}else {
			for (UserBean userBean : allUser) {
				if(userName.equals(userBean.getUserName())) {
					userBean.setPassword(newPassword);
				}
			}
			UserDao userDao = new UserDao();
			userDao.saveUser(allUser);
			return true;
		}
		
		
	}

	@Override//用户信息修改功能
	public boolean changeUserInfo(String userName, String userModText, String nickName, String headPic, String notice) {
		// TODO Auto-generated method stub
		
		GameMember.loginUser.setUserModText(userModText);
		GameMember.loginUser.setNickName(nickName);
		GameMember.loginUser.setPic(headPic);
		GameMember.loginUser.setNotice(notice);
		UserDao.saveUser(allUser);
		return true;
	}

	@Override//获取所有头像文件路径
	public List<String> getUserFaceList() {
		// TODO Auto-generated method stub
		String haedPath = "resources/head";
		List<String> allHeadPath = new ArrayList<String>();
		File fl = new File(haedPath);
		if(fl.isDirectory()) {
			for(int i = 0 ;i<fl.listFiles().length;i++) {
				allHeadPath.add(fl.listFiles()[i].toString());
			}
			
		}
		return allHeadPath;
	}

	@Override
	public List<UserItem> getUserList() {
		// TODO Auto-generated method stub
		List<UserItem> users = new ArrayList<UserItem>();
		users.addAll(allUser);
		users.remove(GameMember.loginUser);
		users.add(0, GameMember.loginUser);
		return users;
	}

	@Override//登录
	public boolean loginCheckUser(String userName, String password) {
		// TODO Auto-generated method stub
		UserDataDao userDataDao = new UserDataDao();
		for(int i=0;i<allUser.size();i++) {
			if(allUser.get(i).getUserName().equals(userName)&&allUser.get(i).getPassword().equals(password)) {
				if(allUser.get(i).getUserName().equals(userName)) {
					MessageDialogHelper.showMessageDialog("用户登录成功", "登录信息");
					GameMember.loginUser=allUser.get(i);
					//表示当前游戏中显示的为登录用户
					GameMember.currentUser = GameMember.loginUser;
					GameMember.userData=userDataDao.getUserData(allUser.get(i).getUserId());
					return true;
				}
			}
		}
			return false;

	}

	@Override//注册用户
	public boolean registerUser(String userName, String password, String checkPassword) {
		// TODO Auto-generated method stub

		if(userName.length()<3||userName.length()>10) {
			System.out.println("用户名长度必须在3-10之间");
			return false;
		}
		else if(password.length()<6||password.length()>12) {
			System.out.println("密码长度必须在3-10之间");
			return false;
		}
		else if(!password.equals(checkPassword)) {
			System.out.println("确认密码和密码必须相同");
			return false;
		}
		else {	
			for (UserBean userBean1 : allUser) {
				if(userName.equals(userBean1.getUserName())) {
					System.out.println("该用户已经被注册");
					return false;
				}
			}
			UserBean userBean = new UserBean();
			userBean.setUserName(userName);	//用户名
			userBean.setPassword(password);	//密码
			userBean.setNickName(userName); //昵称
			userBean.setPic("resources/head/1-1.GIF");
			userBean.setNotice("迅科教育菜地");
			userBean.setUserModText("空");
			userBean.setUserId((allUser.size()+1));
			allUser.add(userBean);
			UserDao.saveUser(allUser);
			
			UserDataDao userDataDao = new UserDataDao();
			
			userDataDao.getUserData(userBean.getUserId());
			//初始化仓库
			StoreHouseDAO.updateUserStore(new ArrayList<StoreHouseItem>(), userBean.getUserId());
//			初始化土地文件
			String path = "user/userdetails/"+userBean.getUserId();
			File file = new File(path+"_land.txt");
			if(!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String str="";
				for (int i = 0; i < 6; i++) {
					str+=(i+1)+","+","+",\n";
				}
				FileUtils.writeFile(path+"_land.txt", str);
			}
			MessageDialogHelper.showMessageDialog("用户注册成功", "注册信息");
			return true;
		}

	}
	

}
