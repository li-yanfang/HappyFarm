package com.zqsoft.bean;

import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.FaceHelper;
import com.zqsoft.guiHelper.net.bean.UserItem;

public class UserBean implements UserItem{
	private int userId;//用户编号
	private String userName;//用户登录名
	private String password;//登录密码
	private String nickName;//昵称
	private String userModText;//个性签名
	private String Pic;//用户头像
	private String notice;//用户公告信息
	
	/**
	 * NetUserItem接口定义的方法
	 * 该方法实习功能为返回昵称
	 * @return
	 */
	public String getNickName() {
		
		return this.nickName;
	}
	/**
	 * NetUserItem接口定义的方法
	 * 该方法返回用户头像
	 * @return
	 */
	public String getPic() {
		
		return this.Pic;
	}
	public String getUserModText() {
		
		return this.userModText;
	}
	@Override
	public String getUserName() {
		// TODO Auto-generated method stub
		return this.userName;
	}
	@Override
	public void itemClick() {
		// TODO Auto-generated method stub
		//获取好友信息
		FaceHelper.setBoardText(this.notice);
		GameMember.currentUser=this;
		//停止土地增加线程
		for (int i = 0; i < GameMember.allUserLand.size(); i++) {
			LandItemBean landItemBean=GameMember.allUserLand.get(i);
			landItemBean.getCropBeanThread().stopgrown();
		}
		//加载土地信息
		GameMember.loadUserLand();
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public void setUserModText(String userModText) {
		this.userModText = userModText;
	}
	public void setPic(String pic) {
		Pic = pic;
	}
	

}
