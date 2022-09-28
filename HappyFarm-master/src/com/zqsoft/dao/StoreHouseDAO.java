package com.zqsoft.dao;

import java.util.ArrayList;
import java.util.List;

import com.zqsoft.bean.CropBean;
import com.zqsoft.bean.LandItemBean;
import com.zqsoft.bean.ShopItemBean;
import com.zqsoft.bean.StoreHouseItemBean;
import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.GameHelper;
import com.zqsoft.guiHelper.bean.LandItem;
import com.zqsoft.guiHelper.bean.StoreHouseItem;
import com.zqsoft.utils.FileUtils;

//负责用户仓库数据文件的读写操作
public class StoreHouseDAO {
	/**
	 * 解析用户仓库文件，拿到用户仓库所有东西的list对象
	 * @param userId用户id
	 * @return	用户的所有仓库
	 */
	public static List<StoreHouseItem> getUserStoreHouse(int userId) {
		List<StoreHouseItem> allStore = new ArrayList<StoreHouseItem>();
		String path = "user/userdetails/"+userId+"_store.txt";
		List<String> listStr = FileUtils.readFile(path);
		for (String string : listStr) {
			String [] tmp = string.split(":");
			CropBean corBean = new CropBean();
			corBean = GameMember.getCropBean(Integer.parseInt(tmp[0]));
			int count = Integer.parseInt(tmp[1].replace("/n", ""));	
			if(count==0) {			
			}
			else {
				StoreHouseItemBean storeHouseItemBean = new StoreHouseItemBean(corBean,count);
				allStore.add((StoreHouseItem)storeHouseItemBean);
			}	
		}
		return allStore;	
	}
	/**
	 * 保存用户土地信息到文件里面去
	 * @param allLand
	 * @param userid
	 */
	public static void updateUserStore(List<StoreHouseItem> allItems,int userid) {
		String path = "user/userdetails/"+userid+"_store.txt";
		String allline = "";
		for (StoreHouseItem storeHouseItem : allItems) {
			StoreHouseItemBean storeHouseItemBean = (StoreHouseItemBean)storeHouseItem;
			String line = storeHouseItemBean.toString();
			allline+=line+"\n";	
		}
		
		FileUtils.writeFile(path, allline);
	}
	
	
	
	
}
