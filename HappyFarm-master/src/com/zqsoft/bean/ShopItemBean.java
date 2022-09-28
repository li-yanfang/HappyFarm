package com.zqsoft.bean;

import java.util.List;

import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.bean.ShopItem;
import com.zqsoft.ncfarm.core.MessageDialogHelper;
import com.zqsoft.utils.FileUtils;

//商店明细类
public class ShopItemBean implements ShopItem{
	CropBean cropBean;
	
	/**
	 *闯入商店需要显示的对象
	 */
	public ShopItemBean(CropBean cropBean) {
		this.cropBean=cropBean;
		// TODO Auto-generated constructor stub
	}
	public int getItemBuyLevel(){	//放回农作物的购买等级
		return this.cropBean.getBuyLevel();
	}
	public String getItemName() {//返回农作物的名称
		return this.cropBean.getCropName();
	}
	public String getItemPic() {//返回农作物种子的图片

		return this.cropBean.getSeedPic();
	}
	public String getItemPrice() {	//返回农作物种子的购买金币数
		return this.cropBean.getPrice()+"";
	}
	public boolean itemClick() {//用户等级足够返回true弹出购买窗口

		if(this.cropBean.getBuyLevel()<=GameMember.userData.getUserLevel()) {
			return true;
		}else {
			MessageDialogHelper.showMessageDialog("您的等级达不到购买种子的要求", "购买提示");
			return false;
		}
		
	}
	/**
	 * 
	 * @param count数量
	 * @return
	 */
	public int buyItem(int count) {	//就算用户输入购买数量需要的金币数
		return count*this.cropBean.getPrice();
			
	}
	/**
	 * 修改用户资料文件
	 * @param money需要购买的金币数
	 * @param count购买的数量
	 * @return 
	 * @return 
	 */
	public void doBuyItem(int money,int count) {
//		System.out.println(count);
		//如果金币不足
		if(money*count>=GameMember.userData.getMoney()) {
			System.out.println("金币数不足");
		}//金币够
		else {
			GameMember.userData.setMoney(money*count);//修改用户金币
			
			//修改用户数据
			String strs="";
			List<String> UserStr = FileUtils.readFile("user/userdetails/"+GameMember.userData.getUserId()+"_data.txt");
			for (String string : UserStr) {
				String[] string2=string.split(";");
				string2[1] = GameMember.userData.getMoney()+"";
				strs+=string2[0]+";"+string2[1];
			}
//			FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_data.txt", strs);//写入文件
			FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_data.txt", strs);//写入文件
			//操作背囊文档
			GameMember.buySeed(this.cropBean, count);		
			//刷新操作
			GameMember.reflashUserMoney();//刷新
		}
	}
	
}
