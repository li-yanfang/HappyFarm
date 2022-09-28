package com.zqsoft.thread;

import com.zqsoft.bean.LandItemBean;

public class CropBeanThread extends Thread{
	
	private LandItemBean croItemBean;//土地明细对象
	private Boolean isrun=true;	//开关变量，是否需要运行，该参数为停止线程使用
	
	
	
	
	
	
	//停止线程
	public void stopgrown() {
		this.isrun=false;//设置开关为关闭状态
	}
	/**
	 * 重写Run
	 */
	public void run() {
		while (this.isrun) {
			this.croItemBean.growing(this);//执行自动农作物自动生长
			try {
				Thread.sleep(300);//休眠300毫秒;让出cpu
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public LandItemBean getCroItemBean() {
		return croItemBean;
	}
	public void setCroItemBean(LandItemBean croItemBean) {
		this.croItemBean = croItemBean;
	}
	public Boolean getIsrun() {
		return isrun;
	}
	public void setIsrun(Boolean isrun) {
		isrun = isrun;
	}
	
	
	
	
	
}
