package com.zqsoft.bean;

import java.awt.image.CropImageFilter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
//农作物类
public class CropBean implements Comparable<CropBean>,Cloneable{
	private int cropId;//农作物编号
	private String cropName;//农作物名称
	private String seedPic;//农作物种子图片
	private	int stage;//农作物总的生长
	private	int sellPrice;//农作物成熟之后的销售金币
	private int price;//农作物种子在商店中购买的金币数
	private int buyLevel;//农作物种子在商店中购买的级别
	private List<String> allStagePic;//农作物所有阶段的图片，从开始1阶段到N阶段
	private	List<String> allStageTime;//用于存放每个阶段需要的时间，要求按照顺序存放
	private String beginPic;//开始播种时在土地上显示的图片
	private	String endPic;//农作物摘取之后显示的荒废图片
	private int current;//描述农作物的当前状态。
	private int allTime;//总时间
	
	
	
	/**
	 * @param cropId
	 * @param cropName
	 * @param seedPic
	 * @param stage
	 * @param sellPrice
	 * @param price
	 * @param buyLevel
	 * @param allStagePic
	 * @param allStageTime
	 * @param beginPic
	 * @param endPic
	 * @param current
	 * @param allTime
	 */
	public CropBean(CropBean cropBean) {
	
		this.cropId = cropBean.cropId;
		this.cropName = cropBean.cropName;
		this.seedPic = cropBean.seedPic;
		this.stage = cropBean.stage;
		this.sellPrice = cropBean.sellPrice;
		this.price = cropBean.price;
		this.buyLevel = cropBean.buyLevel;
		this.allStagePic = cropBean.allStagePic;
		this.allStageTime = cropBean.allStageTime;
		this.beginPic = cropBean.beginPic;
		this.endPic = cropBean.endPic;
		this.current = 0;
		
	}

	/**
	 * 
	 */
	public CropBean() {

		// TODO Auto-generated constructor stub
	}

	public int getCurrent() {
		return current;
	}
	
	/**
	 * @param cropId
	 * @param cropName
	 * @param seedPic
	 * @param stage
	 * @param sellPrice
	 * @param price
	 * @param buyLevel
	 * @param allStagePic
	 * @param allStageTime
	 * @param beginPic
	 * @param endPic
	 * @param current
	 * @param allTime
	 */
	

	/**
	 * 
	 * @param Ltime开始时间
	 */
	public int setCurrent(Date time) {
		int current=0;//当前阶段
		long dt = time.getTime();//开始时间的时间戳
		long nt = System.currentTimeMillis();//当前时间的时间戳
		long jt = 0;//阶段累加时间戳
		for(int i=0;i<this.allStageTime.size();i++) {
//			判断当前阶段
//			现在-开始<=累加  返回本阶段
			jt+=Long.parseLong(this.allStageTime.get(i))*1000;
			if(nt-dt<=jt) {
				this.current=(i+1);
			}else {
				current++;
			}
//			System.out.println(current);
		}
//		System.out.println(this.current);
		return this.current;
	}
	
	public void setCurrent(int curr) {
		this.current=curr;
	}

	public int getAllTime() {
		this.setAllTime();
		return this.allTime;
	}
	//毫秒级
	public void setAllTime() {
		int time=0;
		for (int i = 0; i < allStageTime.size(); i++) {
			time+=Integer.parseInt(allStageTime.get(i));
		}
		this.allTime=(time*100);
	}

	/**
	 * 比大小
	 */
	public int compareTo(CropBean cropBean) {
//		Arrays.sort(CropBean);
		if(this.getBuyLevel()<=cropBean.getBuyLevel()) {
			if(this.getSellPrice()<=cropBean.getSellPrice()) {
				return -1;
			}else {
				return 1;
			}
		}
		else {
			return 1 ;
		}

	}

	public int getCropId() {
		return cropId;
	}

	public void setCropId(int cropId) {
		this.cropId = cropId;
	}

	public String getCropName() {
		return cropName;
	}

	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	public String getSeedPic() {
		return seedPic;
	}

	public void setSeedPic(String seedPic) {
		this.seedPic = seedPic;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getBuyLevel() {
		return buyLevel;
	}

	public void setBuyLevel(int buyLevel) {
		this.buyLevel = buyLevel;
	}

	public List<String> getAllStagePic() {
		return allStagePic;
	}

	public void setAllStagePic(List<String> allStagePic) {
		this.allStagePic = allStagePic;
	}

	public List<String> getAllStageTime() {
		return allStageTime;
	}

	public void setAllStageTime(List<String> allStageTime) {
		this.allStageTime = allStageTime;
	}

	public String getBeginPic() {
		return beginPic;
	}

	public void setBeginPic(String beginPic) {
		this.beginPic = beginPic;
	}

	public String getEndPic() {
		return endPic;
	}

	public void setEndPic(String endPic) {
		this.endPic = endPic;
	}	
	/**
	 * 
	 * @param current当前阶段次数
	 * @return农作物种子的图片
	 */
	public String getCurrentPic(int current) {
		
		return this.endPic;
	}
	/**
	 * 
	 * @param current当前阶段次数
	 * @return当前阶段的照片
	 */
	public String getCurrent_pic(int current) {
		for(int i=0;i<this.allStagePic.size();i++) {
			if(i==(current-1)) {
//				System.out.println(this.current);
				return this.allStagePic.get((i));
			}	
		}
		return this.beginPic;
	}
	/**
	 * 当前农作物对象的生长阶段+1；
	 * @param longTime从种植生长到现在的时间戳
	 */
	public void goNextStage() {
		this.current++;
	}
	
}
