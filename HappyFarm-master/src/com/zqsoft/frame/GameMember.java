package com.zqsoft.frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import com.zqsoft.bean.CropBean;
import com.zqsoft.bean.LandItemBean;
import com.zqsoft.bean.PackageItemBean;
import com.zqsoft.bean.ShopItemBean;
import com.zqsoft.bean.StoreHouseItemBean;
import com.zqsoft.bean.UserBean;
import com.zqsoft.bean.UserData;
import com.zqsoft.dao.CropDAO;
import com.zqsoft.dao.LandDAO;
import com.zqsoft.dao.PackageDAO;
import com.zqsoft.dao.StoreHouseDAO;
import com.zqsoft.dao.UserDao;
import com.zqsoft.guiHelper.FaceHelper;
import com.zqsoft.guiHelper.GameHelper;
import com.zqsoft.guiHelper.bean.LandItem;
import com.zqsoft.guiHelper.bean.PackageItem;
import com.zqsoft.guiHelper.bean.ShopItem;
import com.zqsoft.guiHelper.bean.StoreHouseItem;
import com.zqsoft.guiHelper.net.bean.UserWindow;
import com.zqsoft.ncfarm.core.MessageDialogHelper;
import com.zqsoft.utils.FileUtils;

public class GameMember {
	public static GameHelper gameHelper = new GameHelper();
	public static UserBean loginUser;//保存登录账户信息
	public static UserData userData;	//保存账户信息
	public static List<CropBean> allcropBean=new ArrayList<CropBean>();//存放所有农作物的集合
	public static List<PackageItem> allUserPackages=null;//包裹东西
	public static List<LandItemBean> allUserLand=null;//用于存放用户的土地类型
	public static List<StoreHouseItem> allUserStore;//存放用户仓库数据
	public static int mouseState=0;//0未点击，1点击
	public static int selectdCropid=0;//表示包囊中选中种子的编号；
	public static UserBean currentUser;//表示当前登陆的用户
	
	public static void main (String[] args) {
		loadUserWin();
		loadUserData();
		loadBackGround();	
		loadShop();
		loadUserPackage();
		loadUserLand();//土地数据
		LandDAO landDAO = new LandDAO();
		landDAO.getUserLandBean(loginUser.getUserId());
		loadUserStoreHouse();//加载仓库信息
		
	}
	
	//在界面上展现用户数据
	public static void loadUserData() {
		// TODO Auto-generated method stub
		FaceHelper.setExp(String.valueOf(userData.getExp()));
		FaceHelper.setLevel(String.valueOf(userData.getUserLevel()));
		FaceHelper.setMoney(String.valueOf(userData.getMoney()));
		FaceHelper.setBoardText(loginUser.getNotice());
	}


	/**
	 * 用于加载登录窗口
	 */
	public static void loadUserWin() {
		UserWin userWin = new UserWin();
		gameHelper.loadMod(userWin);
	}

	//加载背景
	public static void loadBackGround() {
		List<String> list = new ArrayList<String>();
		list.add("resources/background/1.png");
		list.add("resources/background/2.png");
		list.add("resources/background/3.png");
		list.add("resources/background/4.png");
		gameHelper.setBackground(list);
	}
	
	//加载商店的明细信息
	public static void loadShop()  {
		List<ShopItem> sib = new ArrayList<ShopItem>();
		CropDAO cropdao = new CropDAO();
		
			try {
				allcropBean=cropdao.getAllCrop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Collections.sort(allcropBean);	
		for (int i = 0;i<allcropBean.size();i++) {
			ShopItemBean shopItemBean = new ShopItemBean(allcropBean.get(i));
			sib.add(shopItemBean);
		}
//		System.out.println(allcropBeans);
//		Collections.sort((List<ShopItem>)sib);
		gameHelper.setShopItemList((List<ShopItem>)sib);
	}
	//通过农作物编号查询农作物对象
	public static CropBean getCropBean(int cropId) {
//		System.out.println(allcropBean.get(3).getCropId());
		for (CropBean cropBean : allcropBean) {
//			System.out.println(cropBean.getCropId());
			if(cropId==cropBean.getCropId()) {
				return cropBean;
			}
		}
		return null;
	}
	//加载背包信息
	public static void loadUserPackage() {
		PackageDAO packageDAO = new PackageDAO();
		allUserPackages=packageDAO.getUserPackage(currentUser.getUserId());
		gameHelper.setPackageItemList(allUserPackages);

	}
	/**
	 * 负责刷新用户金币数量
	 */
	public static void reflashUserMoney() {
		FaceHelper.setMoney(String.valueOf(userData.getMoney()));
	}
	
	
	
	
	/**
	 * 增加用户包囊中的种子数量
	 * @param crop	农作物对象
	 * @param count	//数量
	 */
	public static void buySeed(CropBean cropBean,int count) {
//		System.out.println(count);
		PackageItemBean packageItemBean = new PackageItemBean(cropBean,count);	//建背包种子
		String filePath = "user/userdetails/"+GameMember.userData.getUserId()+"_package.txt";//包裹信息路径		
		List<String> allFilePath = FileUtils.readFile(filePath);//获得文字内容
		String strs = "";	
		if(allUserPackages==null||allUserPackages.size()==0) {
			allFilePath.add(cropBean.getCropId()+":"+count);//包囊数据文档加一条
			for (String string : allFilePath) {	//改为String类型
				String tmp[] = string.split(":");
				tmp[1]=tmp[1]+"\n";
				strs+=tmp[0]+":"+tmp[1];
			}
			allUserPackages.add(packageItemBean);
			FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_package.txt", strs);
			loadUserPackage();
		}else {
			for(int i=0;i<=allUserPackages.size();i++) {	//遍历现有背包种子项
				if(allUserPackages.get(i).getItemName().equals(cropBean.getCropName())) {	//判断背包是否含有新种子名称，如果有
					for (String string : allFilePath) {	//改背包种子
							String tmp[] = string.split(":");
							tmp[1]=tmp[1]+"\n";
							if(tmp[0].equals(cropBean.getCropId()+"")) {
								int tmp1=Integer.parseInt(tmp[1].replaceAll("\n", ""));
								tmp[1]=(tmp1+count)+"\n";
//								System.out.println(tmp[1]);
							}
							strs+=tmp[0]+":"+tmp[1];
					}
//					System.out.println(strs);
					FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_package.txt", strs);
					loadUserPackage();
					break;
				}		
				if(i==(allUserPackages.size()-1)) {
					allFilePath.add(cropBean.getCropId()+":"+count);//包囊数据文档加一条
					for (String string : allFilePath) {	//改为String类型
						String tmp[] = string.split(":");
						tmp[1]=tmp[1]+"\n";
						strs+=tmp[0]+":"+tmp[1];
					}

					allUserPackages.add(packageItemBean);
					FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_package.txt", strs);
					loadUserPackage();
					break;
				}
			}
		}

	}
	
	//初始化显示用户土地数据
	public static void loadUserLand() {

		allUserLand=LandDAO.getUserLandBean(currentUser.getUserId());
//		System.out.println(allUserLand.size());
		for (LandItemBean landItemBean : allUserLand) {
			
//			System.out.println(landItemBean.getCropBean().getCropId());
//			System.out.println(landItemBean.getCropBean().getCropId()+";"+landItemBean.getCount()+";"+landItemBean.getLandId()+";"+landItemBean.getBeginTime());
			gameHelper.addLandItem(landItemBean.getLandId(),(LandItem) landItemBean);
		}	
		
	}
	//用于加载用户的仓库数据
	public static void loadUserStoreHouse() {
		StoreHouseDAO stor= new StoreHouseDAO();
		allUserStore=stor.getUserStoreHouse(loginUser.getUserId());
		gameHelper.setStoreItemList(allUserStore);
	}
	/**
	 * 减少包裹中的种子数，没执行一次减1，当数量减少到0时将移除包裹明细，如果包囊中没有该编号的种子返回false
=======
	 * 减少包裹中的种子书，没执行一次减1，当数量减少到0时将移除包裹明细，如果包囊中没有该编号的种子返回false
	 * @param cropId
	 * @return
	 */
	public static boolean subPackage(int cropId) {
		//如果包囊大小为0，直接true
		if(allUserPackages.size()==0) {
			return true;
		}
		for(int i=0;i<allUserPackages.size();i++) {
			PackageItemBean item = (PackageItemBean) allUserPackages.get(i);
			//背囊有种子，但是数量不为0
			if(item.getCropBean().getCropId()==cropId) {
				if(item.getItemCount()<=0) {
					allUserPackages.remove(item);
//					selectdCropid=0;
					return true;
				}
			}
		}
//			else if() {
//				return true;
//			}
		for(int i=0;i<allUserPackages.size();i++) {
			PackageItemBean item = (PackageItemBean) allUserPackages.get(i);
			if(item.getCropBean().getCropId()==cropId) {
				if(item.getItemCount()==0) {
					allUserPackages.remove(item);
					return true;
				}
			}
		}
		return false;
	}
	public static void sellCron(CropBean crop,int count) {
		for(int i=0;i<allUserStore.size();i++) {
			if(allUserStore.get(i).getItemName().equals(crop.getCropName())) {
				if(allUserStore.get(i).getItemCount()>=count) {
					StoreHouseItemBean shib=(StoreHouseItemBean)allUserStore.get(i);
					shib.setCount(shib.getItemCount()-count);
				}
				else {
					MessageDialogHelper.showMessageDialog("您选择的农作物不足", "提示消息");
					return;
				}
			}
		}
		StoreHouseDAO.updateUserStore(allUserStore, loginUser.getUserId());
	}
	
}
