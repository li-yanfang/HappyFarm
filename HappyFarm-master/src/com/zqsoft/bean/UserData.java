package com.zqsoft.bean;

public class UserData {
	public int exp;//经验
	public int money;//金币
	public int userId;//用户id
	
	/**
	 * @param exp
	 * @param money
	 * @param userId
	 */
	public UserData(int exp, int money, int userId) {
		this.exp = exp;
		this.money = money;
		this.userId = userId;
	}
	/**
	 * 根据用户的经验计算用户的等级
	 * @return grade用户等级
	 */
	public int getUserLevel() {
		int grade=0;
		grade=this.exp/200+1;
		return grade;
	}
	
	public int getExp() {
		return this.exp;
	};
	public void setExp(int SetExp) {
		this.exp=SetExp;
	};
	public int getMoney() {
		return money;
	};
	public void setMoney(int MOney) {
		this.money-=MOney;
	};
	public int getUserId() {
		return userId;
	};
	public void setUserId(int SEtUserid) {
		this.userId=SEtUserid;
	}
	@Override
	public String toString() {
		return this.exp+";"+this.money ;
	};
	
	
}
