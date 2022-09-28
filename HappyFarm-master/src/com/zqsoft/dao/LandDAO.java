package com.zqsoft.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zqsoft.bean.CropBean;
import com.zqsoft.bean.LandItemBean;
import com.zqsoft.frame.GameMember;
import com.zqsoft.utils.FileUtils;

//负责用户土地数据文件的读写操作
public class LandDAO{
	
	/**
	 * 获取用户的所有土地请况
	 * @param userId用户id
	 * @return用户土地明细列表
	 */
	public static List<LandItemBean> getUserLandBean(int userId) {
		 List<LandItemBean> allLand = new ArrayList<LandItemBean>();
		 
		 String landPath = "user/userdetails/"+userId+"_land.txt";
		 List<String> listland=FileUtils.readFile(landPath);
//		 System.out.println(listland.size());
		 for (int i =0;i<6;i++) {
			CropBean cropBean = new CropBean();//农作物
			String strs[] = listland.get(i).split(",");//分割字符串
			Date date=null;
			String st=strs[0];
			//土地id;农作物id;数量;时间戳
			//1;1;30;1597309108660
			if(strs.length<=1) {	//农作物id是否为空
				strs=new String[4];
				strs[0]=st;
				strs[2]="0";
				cropBean=null;
			}else {	
				long long1=Long.parseLong(strs[3]);
				date = new Date(long1);//创建data时间	
//				System.out.println(Integer.parseInt(strs[1]));
				cropBean = GameMember.getCropBean(Integer.parseInt(strs[1]));
//				System.out.println(Integer.parseInt(strs[1]));
			}
//			System.out.println(+Integer.parseInt(strs[0]));
			//LandItemBean（种子，土地，数量，时间戳）
			LandItemBean landItemBean = new LandItemBean(cropBean,Integer.parseInt(strs[0]), Integer.parseInt(strs[2]), date);
//			System.out.println(strs[2]);
			allLand.add(landItemBean);
		}
		 return allLand;
	}
	
	/**
	 * 保存用户土地情况到文件
	 * @param allLand用户土地集合
	 * @param userId用户的id
	 */
	public static void saveUserLand(List<LandItemBean> allLand,int userId) {
		String path = "user/userdetails/"+userId+"_land.txt";	//要保存的文件路径
		String allline="";
		for (LandItemBean landItemBean : allLand) {
			String line = landItemBean.toString();
			allline+=line+"\n";
//			System.out.println(allline);
		}
//		System.out.println(allline);
		FileUtils.writeFile(path, allline);
	}
}
