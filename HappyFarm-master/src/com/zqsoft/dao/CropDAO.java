package com.zqsoft.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.zqsoft.bean.CropBean;

//解析所有农作物数据文件
public class CropDAO {
	//读取所有农作物文件，将每个农作物数据解析成为一个CropBean对象
	public List<CropBean> getAllCrop() throws IOException {
		List<CropBean> allCrop = new ArrayList<CropBean>();	//对象集合
		File file = new File("resources/crops");	//文件
		String[] cropdris = file.list();	//内部文件名
		Properties properties = new Properties();		
		
		Reader reader;

		for(int i=0;i<cropdris.length;i++) {
			//InputStreamReader字节流转字符流
			//FileInputStream在文件读字节流
//			System.out.println(cropdris[i]);
			reader = new  InputStreamReader(new FileInputStream("resources/crops/"+cropdris[i]+"/cron.properties"));
			properties.load(reader);
			CropBean cropBean = new CropBean();
			cropBean.setCropId(Integer.parseInt(properties.getProperty("ITEM_ID")));//农作物编号
			cropBean.setCropName(properties.getProperty("ITEM_NAME"));	//农作物名称
			cropBean.setSeedPic("resources/crops/cron"+properties.getProperty("ITEM_ID")+"/"+properties.getProperty("ITEM_SEED_PIC"));//农作物种子

			cropBean.setStage(Integer.parseInt(properties.getProperty("ITEM_STAGE")));//农作物总生长
			cropBean.setSellPrice(Integer.parseInt(properties.getProperty("ITEM_SELL_MONEY")));//农作物果实价格
			cropBean.setPrice(Integer.parseInt(properties.getProperty("ITEM_PRICE")));//种子价格
			cropBean.setBuyLevel(Integer.parseInt(properties.getProperty("ITEM_NEED_LEVEL")));//购买等级
			cropBean.setBeginPic("resources/crops/cron"+properties.getProperty("ITEM_ID")+"/"+properties.getProperty("ITEM_STAGE_SEED"));
//			System.out.println("resources/crops/cron"+properties.getProperty("ITEM_ID")+cropBean.getBeginPic());
			cropBean.setEndPic("resources/crops/cron"+properties.getProperty("ITEM_ID")+"/"+properties.getProperty("ITEM_STAGE_END"));
			List<String> AllStagePicList=new ArrayList<String>();//存放农作物所有阶段的图片的集合
			List<String> AllStageTimeList=new ArrayList<String>();//用于存放每个阶段需要的时间的集合				
			for(int j=0;j<cropBean.getStage();j++) {
				AllStageTimeList.add(properties.getProperty("ITEM_STAGE_NEXT_TIME_"+(j+1)));//时间
				AllStagePicList.add("resources/crops/cron"+properties.getProperty("ITEM_ID")+"/"+properties.getProperty("ITEM_STAGE_"+(j+1)));
				
//				System.out.println(AllStageTimeList.get(j));
//				System.out.println(AllStagePicList.get(j));
			}
			cropBean.setAllStagePic(AllStagePicList);//农作物所有阶段的图片，从开始1阶段到N阶段
			cropBean.setAllStageTime(AllStageTimeList);//用于存放每个阶段需要的时间，要求按照顺序存放
			allCrop.add(cropBean);
		}
		
//		System.out.println(allCrop.size());
		return allCrop;
	}
	
	
}
