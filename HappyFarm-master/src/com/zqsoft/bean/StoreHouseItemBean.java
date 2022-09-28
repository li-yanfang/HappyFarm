package com.zqsoft.bean;

import java.io.File;
import java.util.List;

import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.bean.StoreHouseItem;
import com.zqsoft.ncfarm.core.MessageDialogHelper;
import com.zqsoft.utils.FileUtils;

//用于描述用户包裹明细信息
public class StoreHouseItemBean implements StoreHouseItem{
	private CropBean cropBean;//农作物
	private int count;//农作物数量
	
	/**
	 * @param cropBean
	 * @param count
	 */
	public StoreHouseItemBean(CropBean cropBean, int count) {
		this.cropBean = cropBean;
		this.count = count;
	}
	/**
	 * 改方法为用户确认卖出仓库中收获的农作物
	 * money金币
	 * count数量
	 */
	@Override
	public void doSellItem(int money, int count) {
		// TODO Auto-generated method stub
		GameMember.userData.setMoney(-this.sellItem(count));
		GameMember.sellCron(cropBean, count);
		GameMember.reflashUserMoney();
		//保存用户游戏数据
		String str = GameMember.userData.getExp()+";"+GameMember.userData.getMoney();
		FileUtils.writeFile("user/userdetails/"+GameMember.userData.getUserId()+"_data.txt", str);
		System.out.println(GameMember.allUserStore);		
		//数量为0，删除在仓库里本植物 
		if(this.getItemCount()==0) {
			for (int i =0;i<GameMember.allUserStore.size();i++) {
				if(((StoreHouseItemBean)GameMember.allUserStore.get(i)).getCropBean().getCropId()==this.cropBean.getCropId()) {
					GameMember.allUserStore.remove(this);
				}
			}
		}
		
		
	}
	/**
	 * 返回农作物的数量
	 */
	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return this.count;
	}
	/**
	 * 返回农作物的名称
	 */
	@Override
	public String getItemName() {
		// TODO Auto-generated method stub
		return this.cropBean.getCropName();
	}
	
	/**
	 * 返回仓库中农作物种子图片
	 */
	@Override
	public String getItemPic() {
		// TODO Auto-generated method stub
		return this.cropBean.getSeedPic();
	}
	/**
	 * 放回农作物的销售价格
	 */
	@Override
	public String getItemPrice() {
		// TODO Auto-generated method stub
		return this.cropBean.getSellPrice()+"";
	}
	/**
	 * 用户点击仓库中的农作物需要实现的方法
	 */
	@Override
	public boolean itemClick() {
		// TODO Auto-generated method stub
		if(this.getItemCount()<=0) {
			GameMember.allUserStore.remove(this);
			MessageDialogHelper.showMessageDialog("仓库数量不足", "仓库信息");
			return false;
		}
		
		return true;
	}
	/**
	 * 用户点击仓库中的农作物，弹出销售窗口之后输入数量且点击卖出的执行方法
	 * count数量，即卖出的数量
	 */
	@Override
	public int sellItem(int count) {
		// TODO Auto-generated method stub
		return count*this.cropBean.getSellPrice();
	}
	/**
	 * 将仓库对象转换成字符串方式以便写入文件中；
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.cropBean.getCropId()+":"+this.count;
	}
	
	/**
	 * 修改仓库中该农作物的数量
	 * count 收获添加的数量
	 */
	public void addCount(int count) {
		this.count+=count;
	}
	public CropBean getCropBean() {
		return cropBean;
	}
	public void setCropBean(CropBean cropBean) {
		this.cropBean = cropBean;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
