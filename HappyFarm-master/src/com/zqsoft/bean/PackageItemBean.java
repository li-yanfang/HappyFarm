package com.zqsoft.bean;

import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.bean.PackageItem;

//包裹明细类
public class PackageItemBean  implements PackageItem{
	private CropBean cropBean = new CropBean();//包裹明细需要显示的种子信息来源
	private int ItemCount;//包裹中该种子的数量
	public CropBean getCropBean() {
		return cropBean;
	}
	public void setCropBean(CropBean cropBean) {
		this.cropBean = cropBean;
	}
	/**
	 * 
	 * @param cropBean种子
	 * @param ItemCount数量
	 */
	public PackageItemBean(CropBean cropBean, int ItemCount) {
		this.cropBean=cropBean;
		this.ItemCount=ItemCount;
		// TODO Auto-generated constructor stub
	}
	public int getItemCount() {//返回包裹中该种子的数量
		return ItemCount;
	}
	public String getItemName() {//返回包裹中该种子的名称
		return this.cropBean.getCropName();
	}
	public String getItemPic() {//返回包裹中该种子的图片
		return this.cropBean.getSeedPic();
	}
	public int getCropId() {//返回包裹中该种子的农作物ID
		return this.cropBean.getCropId();
	}
	public void setCropId(int id) {//返回包裹中该种子的农作物ID
		this.cropBean.setCropId(id);
	}
	public void setItemCount(int itemCount) {//修改包裹种子数量
		this.ItemCount = itemCount;
	}
	public boolean itemClick() {//点击选中需要播种的种子信息，
		GameMember.mouseState=1;
		GameMember.selectdCropid=this.cropBean.getCropId();
		return true;
	}
	public void cancelClick() {//取消选中的种子
		GameMember.mouseState=0;
		GameMember.selectdCropid=cropBean.getCropId();
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
	
	
}
