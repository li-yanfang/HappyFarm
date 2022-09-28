package com.zqsoft.dao;

import java.util.ArrayList;
import java.util.List;

import com.zqsoft.bean.CropBean;
import com.zqsoft.bean.PackageItemBean;
import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.bean.PackageItem;
import com.zqsoft.utils.FileUtils;

//用于解析用户包裹
public class PackageDAO {

	
	
	public List<PackageItem> getUserPackage(int userId) {
		List<PackageItem> userPackages = new ArrayList<PackageItem>();
		String filePath = "user/userdetails/"+userId+"_package.txt";//包裹信息
		List<String> allFilePath =FileUtils.readFile(filePath);//获得文字内容
		CropBean cropBean ;
		
		for (String string : allFilePath) {
			String tmp[] = string.split(":");
			cropBean = new CropBean();
//			tmp[0]id;tmp[1]数量
			if(Integer.parseInt(tmp[1])==0) {
				continue;
			}
			for (CropBean croBean1 : GameMember.allcropBean) {
				if(croBean1.getCropId()==Integer.parseInt(tmp[0])) {
					cropBean=croBean1;
				}
			}
			PackageItemBean packageItemBean = new PackageItemBean(cropBean,Integer.parseInt(tmp[1]));
			packageItemBean.setCropId(Integer.parseInt(tmp[0]));
			
			userPackages.add((PackageItem)packageItemBean);
			System.out.println();
		}
		return userPackages;
	};
	
	
	public static void updateUserPack(List<PackageItem> userPackage,int userId) {

		String str="";
		for(int j=0;j<userPackage.size();j++) {
			if(userPackage.get(j) instanceof PackageItemBean) {
				PackageItemBean pi=(PackageItemBean)userPackage.get(j);
				if(pi.getItemCount()==0) {
					continue;
				}
				str+=pi.getCropId()+":"+pi.getItemCount()+"\n";
			}
		}
		FileUtils.writeFile("user/userdetails/"+userId+"_package.txt", str);
		
	}

}
