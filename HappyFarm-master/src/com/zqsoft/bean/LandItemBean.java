 package com.zqsoft.bean;

import java.security.MessageDigest;
import java.util.Date;
import java.util.Random;
import javax.swing.text.MaskFormatter;
import javax.xml.crypto.Data;
import com.zqsoft.dao.LandDAO;
import com.zqsoft.dao.PackageDAO;
import com.zqsoft.dao.StoreHouseDAO;
import com.zqsoft.dao.UserDataDao;
import com.zqsoft.frame.GameMember;
import com.zqsoft.guiHelper.FaceHelper;
import com.zqsoft.guiHelper.bean.LandItem;
import com.zqsoft.guiHelper.bean.PackageItem;
import com.zqsoft.guiHelper.bean.StoreHouseItem;
import com.zqsoft.ncfarm.core.MessageDialogHelper;
import com.zqsoft.thread.CropBeanThread;
import com.zqsoft.utils.FileUtils;

//一块土地类
public class LandItemBean implements LandItem{
	private CropBean cropBean;//土地上的生长农作物
	private int landId;	//土地编号
	private	int count;	//土地上的农作物数量
	private	Date beginTime;//开始时间
	private CropBeanThread cropBeanThread = new CropBeanThread();//生长线程
	private int num=0;
	/**
	 * @param cropBean
	 * @param landId
	 * @param count
	 * @param beginTime
	 */
	public LandItemBean(CropBean cropBean, int landId, int count, Date beginTime) {
		this.cropBean = cropBean;
		this.landId = landId;
		this.count = count;
		this.beginTime = beginTime;
		if(this.cropBean!=null&&this.count!=0) {
			this.cropBeanThread.setIsrun(true);
			this.cropBeanThread.setCroItemBean(this);
			this.cropBeanThread.start();
		}
	}
	/**
	 * 该方法为土地的铲除方法
	 */
	public void uprootAction() {
		int probability=new Random().nextInt(100);//捡到种子几率
		//判断是否在自己土地上面，如果不在，提示不能在好友土地上铲除
		if(GameMember.loginUser != GameMember.currentUser) {
			MessageDialogHelper.showMessageDialog("不能在好友的土地上面铲除", "提示信息");
			return;
		}
		else if(this.cropBean==null) {
				boolean flag = MessageDialogHelper.showConfirmDialog("是否铲除农作物", "提示信息");
				if(!flag) {
					//用户选择不铲除
					return;
				}else {
					if(this.cropBean==null) {
						MessageDialogHelper.showMessageDialog("土地上没有农作物了",  "提示信息");
						return;
					}
				}
		}
		if(probability>50) {
			MessageDialogHelper.showMessageDialog("没捡到种子", "真可怜");
		}
		//捡到种子
		else if(probability<=50){
			//更新背囊
			for (int i = 0; i < GameMember.allUserPackages.size(); i++) {
				PackageItemBean itBean = (PackageItemBean) GameMember.allUserPackages.get(i);
				//查看背囊是否有种子
				if(itBean.getCropBean().getCropId() == this.cropBean.getCropId()) {
					itBean.setItemCount(itBean.getItemCount()+1);
					break;
				}
				//没有种子，添加
				else if(i+1==GameMember.allUserPackages.size()){
					PackageItemBean pack = new PackageItemBean(this.cropBean, 1);
					GameMember.allUserPackages.add(pack);
					break;
				}
			}
			//如果背囊为0
			if(GameMember.allUserPackages.size()==0) {
				PackageItemBean pack = new PackageItemBean(this.cropBean, 1);
				GameMember.allUserPackages.add(pack);
			}

			MessageDialogHelper.showMessageDialog("恭喜！！！您捡到本农作物的种子了", "捡到种子");
			PackageDAO.updateUserPack(GameMember.allUserPackages, GameMember.loginUser.getUserId());
		}
		//当前农作物对象设为null;
		this.cropBean=null;
		//保存当前土地信息入文件
		LandDAO.saveUserLand(GameMember.allUserLand, GameMember.loginUser.getUserId());
		//增加用户经验
		GameMember.userData.setExp(GameMember.userData.getExp()+3);
		//保存用户的游戏数据
		UserDataDao.updateUserData(GameMember.userData);
		//刷新经验值和等级
		FaceHelper.setExp(String.valueOf(GameMember.userData.getExp()));
		FaceHelper.setLevel(String.valueOf(GameMember.userData.getUserLevel()));
	}
	/**
	 * 返回该土地上农作物的图片
	 * @return
	 */
	public String getItemPic() {
//		System.out.println("没有拿到图片");
//		System.out.println(this.cropBean.getEndPic());
//		return this.cropBean.getEndPic();
		if(this.cropBean==null) {
			return null;
		}
		else if(this.count==0) {
//			System.out.println(this.cropBean.getEndPic());
			return this.cropBean.getEndPic();//数量为0 ；返回枯萎状态照片
		}
		else {
			for (int i=0;i<this.cropBean.getAllStagePic().size();i++) {	//遍历所有阶段照片
				if(i+1==this.cropBean.getCurrent()) {
//					System.out.println(this.cropBean.getAllStagePic().get(i));
//					System.out.println(this.cropBean.getCurrent_pic(i));
//					System.out.println(i);
					return this.cropBean.getCurrent_pic(this.cropBean.getCurrent());
				}
			}
		}
		return this.cropBean.getBeginPic();
	}
	/**
	 * 好友土地摘取的方法
	 */
	public void pickAction() {
		int number = new Random().nextInt(100);
		if(this.cropBean==null) {
			return;
		}
		if(this.cropBean.getStage()>this.cropBean.getCurrent()) {
			System.out.println("别心急，还没熟透");
			MessageDialogHelper.showMessageDialog("别心急，还没熟透", "提示信息");
			return;
		}
		if(this.getCount()==0) {
			System.out.println("已经摘取过了，等再播种成熟后再摘取");
			MessageDialogHelper.showMessageDialog("已经摘取过了，等再播种成熟后再摘取", "提示信息");
			return;
		}
		if(GameMember.loginUser!=GameMember.currentUser) {//在好友的土地上
			num++;
			if(num==3) {
				MessageDialogHelper.showMessageDialog("手下留情，给别人留点吧！", "提示消息");
				return;
			}
			else {
				this.count-=1;
				addStore(1);
				GameMember.userData.setExp(GameMember.userData.getExp()+1);
				MessageDialogHelper.showMessageDialog("您已经成功偷取一个果实", "偷菜成功");
			}
			if(number<=34) {
				MessageDialogHelper.showMessageDialog("差点偷菜失败，快跑吧", "偷菜失败");
			}
			else if(number>34&&number<=67) {
				this.count-=1;
				addStore(1);
				GameMember.userData.setExp(GameMember.userData.getExp()+1);
				MessageDialogHelper.showMessageDialog("您已经成功偷取一个果实", "偷菜成功");
			}
			else {
				int runMoney=new Random().nextInt(200)+1;
				MessageDialogHelper.showMessageDialog("被抓住了，跑的过程丢了"+runMoney+"金币", "偷菜惨败");
				MessageDialogHelper.showMessageDialog("被抓住了，跑的过程丢了"+runMoney+"金币", "偷菜成功");
				GameMember.userData.setMoney(runMoney);
			}
		}
		else {
			addStore(this.count);
			GameMember.userData.setExp(GameMember.userData.getExp()+this.count);
			this.setCount(0);	
		}
		System.out.println(GameMember.currentUser.getUserId()+"\t"+GameMember.loginUser.getUserId());
		LandDAO.saveUserLand(GameMember.allUserLand, GameMember.currentUser.getUserId());
		StoreHouseDAO.updateUserStore(GameMember.allUserStore,GameMember.loginUser.getUserId());
		UserDataDao.updateUserData(GameMember.userData);
		GameMember.loadUserData();
		int num = this.count;
		//是否存在植物
		if(this.cropBean==null) {
			return;
		}
		//是否成熟
		else if(this.cropBean.getCurrent()<this.cropBean.getStage()) {
//			System.out.println("我的信息"+this.cropBean.getCurrent());
			MessageDialogHelper.showMessageDialog("农作物没有成熟", "摘取消息");
			return;
		}
		//是否还有果实
		else if(this.count<=0) {
			MessageDialogHelper.showMessageDialog("土地上的农作物已经被摘取", "摘取消息");
			return;
		}
		//是否是自己土地
		else if(GameMember.loginUser!=GameMember.currentUser) {
			num=1;			
			this.count -=num;//偷一个				
		}
		//在自己土地上面
		else {
			boolean isHave=false;
			for (int i = 0; i < GameMember.allUserStore.size(); i++) {
				if(this.cropBean.getCropId()==((StoreHouseItemBean)GameMember.allUserStore.get(i)).getCropBean().getCropId()) {//判断存在
					((StoreHouseItemBean)GameMember.allUserStore.get(i)).addCount(1);
					isHave=true;
					break;
				}
			}
		
			if(!isHave) {
				StoreHouseItemBean storeHouseItemBean = new StoreHouseItemBean(this.cropBean, num);
				GameMember.allUserStore.add(storeHouseItemBean);
			}
			this.count=0;
		}
//		保存到仓库文件
		StoreHouseDAO.updateUserStore(GameMember.allUserStore, GameMember.currentUser.getUserId());
		//将当前农作物的数量设为0
		LandDAO.saveUserLand(GameMember.allUserLand, GameMember.currentUser.getUserId());
		//保存用户的游戏数据
		UserDataDao.updateUserData(GameMember.userData);
		//刷新显示用户的经验值和金币
		FaceHelper.setExp(String.valueOf(GameMember.userData.getExp()));
		FaceHelper.setLevel(String.valueOf(GameMember.userData.getUserLevel()));
		return;
	}
	public void addStore(int count) {
		for(int i=0;i<GameMember.allUserStore.size();i++) {
			if(GameMember.allUserStore.get(i).getItemName().equals(cropBean.getCropName())) {
				StoreHouseItemBean m=(StoreHouseItemBean)GameMember.allUserStore.get(i);
				m.addCount(count);
				break;
			}
			if(i==GameMember.allUserStore.size()-1) {
				StoreHouseItemBean shib=new StoreHouseItemBean(this.cropBean,count);
				GameMember.allUserStore.add(shib);
				break;
			}
		}
		if(GameMember.allUserStore.isEmpty()) {
			StoreHouseItemBean shib=new StoreHouseItemBean(this.cropBean,count);
			GameMember.allUserStore.add(shib);
		}
	}


	public CropBeanThread getCropBeanThread() {
		return cropBeanThread;
	}
	public void setCropBeanThread(CropBeanThread cropBeanThread) {
		this.cropBeanThread = cropBeanThread;
	}
	public CropBean getCropBean() {
		return cropBean;
	}
	public void setCropBean(CropBean cropBean) {
		this.cropBean = cropBean;
	}
	public int getLandId() {
		return landId;
	}
	public void setLandId(int landId) {
		this.landId = landId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(this.cropBean != null) {
			return (this.landId+","+cropBean.getCropId()+","+this.count+","+this.beginTime.getTime());
		}
		else {
			return (this.landId+","+","+",");
		}
	}


	/**
	 * 让土地上的农作物生产到当前状态
	 * @param cropBeanThread2 
	 */
	public synchronized void growing(CropBeanThread cropBeanThread2) {		
		//自动成长
		Date date = new Date();//新建时间类
		this.cropBeanThread = cropBeanThread2;
//		System.out.println("??");
		// TODO Auto-generated method stubS
		//判断该土地是否种植农作物
		if (cropBean != null) {	
			    int count = 1 ;//生长限制 
				for (String time : cropBean.getAllStageTime()) {//遍历农作物成长时间集					
					if (cropBean.getCurrent() < cropBean.getStage()) {//判断当前周期是否小于最大周期
//						System.out.println(time +"sss"+ this.landId );
//						System.out.println(date.getTime()-(this.beginTime.getTime() + time * 1000) + "ooo");
						if (date.getTime() < (this.beginTime.getTime() + Integer.parseInt(time) * 1000)) {
							break;							
						} else if (cropBean.getCurrent() < count) {//判断当前周期是否小于循环周期
							cropBean.goNextStage();	//农作物周期+1
						}											
					} else {
						this.cropBeanThread.stopgrown();//停止线程
					}
					count++;
				}			
		}

		return;
	}

	/**
	 * 为土地上农作物种植的方法//播种
	 */
	@Override
	public void plantAction() {
		// TODO Auto-generated method stub
		//判断当前土地是否有农作物
		if(this.cropBean!=null) {
			MessageDialogHelper.showConfirmDialog("有农作物存在，请铲除后再播种", "提示信息");
			return;
		};
		//如果有农作物为0，减少包裹中种子的种类
		if( GameMember.subPackage(GameMember.selectdCropid)) {	
			//包裹中已经没有种子
			MessageDialogHelper.showConfirmDialog("包囊中已经没有种子了，请到商店购买", "提示信息");
			return;
		};
		//判断背囊是否有种子对象
		if(GameMember.allUserPackages.size()==0) {
			MessageDialogHelper.showMessageDialog("种子不足，请到商店购买", "提示信息");
			return;
		}else {
			CropBean cb=new CropBean(GameMember.getCropBean(GameMember.selectdCropid));
			for(int i=0;i<GameMember.allUserPackages.size();i++) {
				 if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
						break;
				}
				if(i==GameMember.allUserPackages.size()-1){
					System.out.println(GameMember.allUserPackages.get(i).getItemName());
					//有种子，数量不像
					if(!cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
						MessageDialogHelper.showMessageDialog("种子不足，快去商店购买", "提示信息");
						return;
					}
					else if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
						break;
					}
				}
				else {
					break;
				}
			}
		};
		
		//判断是否在自己土地上面
		if(GameMember.loginUser!=GameMember.currentUser){
			MessageDialogHelper.showConfirmDialog("不能在好友土地上种植噢", "提示信息");
			return;
		}
		//在自己土地上面，且包囊有种子
		else {
			CropBean cb=new CropBean(GameMember.getCropBean(GameMember.selectdCropid));
			for(int i=0;i<GameMember.allUserPackages.size();i++) {
				if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
					PackageItemBean pib=(PackageItemBean)GameMember.allUserPackages.get(i);
					pib.setItemCount(pib.getItemCount()-1);
					//如果数量减到0，则减少包囊的种子类型
					if(pib.getItemCount()==0) {
						GameMember.allUserPackages.remove(pib);
					}
				}
			//保存包囊信息
			
			}
			//更新背囊种子数量
			PackageDAO.updateUserPack(GameMember.allUserPackages, GameMember.loginUser.getUserId());
			//增加用户经验值
			GameMember.userData.setExp(GameMember.userData.getExp()+5);
			//刷新显示经验值和等级
			FaceHelper.setExp(String.valueOf(GameMember.userData.getExp()));
			FaceHelper.setLevel(String.valueOf(GameMember.userData.getUserLevel()));
			//保存用户游戏数据
			UserDataDao.updateUserData(GameMember.userData);
			//在土地上增加一个农作物
			CropBean cropBean = GameMember.getCropBean(GameMember.selectdCropid);
			CropBean cropBean2 = new CropBean(cropBean);
			this.cropBean = cropBean2;
			//随机产生一个50以内的数量
			this.count = new Random().nextInt(50)+1;
			//播种时间设置
			this.beginTime=new Date();
			//更新用户土地
			LandDAO.saveUserLand(GameMember.allUserLand, GameMember.loginUser.getUserId());
			CropBeanThread cropBeanThread= new CropBeanThread();
			cropBeanThread.setCroItemBean(this);
			cropBeanThread.start();
		}
		
				//判断是否存在农作物
				if(GameMember.loginUser!=GameMember.currentUser) {
					MessageDialogHelper.showMessageDialog("无法在好友的农场种植", "提示信息");
					return;
				}
				String str="",path="user/userdetails/"+GameMember.loginUser.getUserId()+"_package.txt";
				for(int i=0;i<GameMember.allUserLand.size();i++) {
					if(this.getLandId()==GameMember.allUserLand.get(i).landId) {
						if(GameMember.allUserLand.get(i).cropBean!=null) {
							System.out.println("菜已经种了，等收了再种吧");
							MessageDialogHelper.showMessageDialog("菜已经种了，等收了再种吧", "提示信息");
							return;
						}
					}
				}
				CropBean cb=new CropBean(GameMember.getCropBean(GameMember.selectdCropid));
				//判断背包种子数量是否足够
				if(GameMember.allUserPackages.isEmpty()) {
					System.out.println("种子不足，请到商店购买");
					MessageDialogHelper.showMessageDialog("种子不足，请到商店购买", "提示信息");
					return;
				}
				for(int i=0;i<GameMember.allUserPackages.size();i++) {
					if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
						break;
					}
					else if(i==GameMember.allUserPackages.size()-1){
						if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
							break;
						}
						else {
							System.out.println("种子不足，请到商店购买");
							MessageDialogHelper.showMessageDialog("种子不足，请到商店购买", "提示信息");
							return;
						}
					}
				}
				//减少包裹农作物数量
				
				for(int i=0;i<GameMember.allUserPackages.size();i++) {
					if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
						PackageItemBean pib=(PackageItemBean)GameMember.allUserPackages.get(i);
						pib.setItemCount(pib.getItemCount()-1);
					}
				}
				//保存包裹文件
				
				for(int j=0;j<GameMember.allUserPackages.size();j++) {
					if(GameMember.allUserPackages.get(j) instanceof PackageItemBean) {
						PackageItemBean pi=(PackageItemBean)GameMember.allUserPackages.get(j);
						if(pi.getItemCount()==0) {
							continue;
						}
						str+=pi.getCropId()+":"+pi.getItemCount()+"\n";
					}
				}
				FileUtils.writeFile(path, str);
				GameMember.loadUserPackage();
				/*
				 * 设置当前土地农作物
				 */
				Random random=new Random();
				int count=random.nextInt(40)+10;
				this.setCropBean(cb);
				this.setCount(count);
				this.setBeginTime(new Date());;
				//保存用户土地数据
				LandDAO.saveUserLand(GameMember.allUserLand, GameMember.loginUser.getUserId());
				GameMember.userData.setExp(GameMember.userData.getExp()+10);
				UserDataDao.updateUserData(GameMember.userData);
				GameMember.loadUserData();
				GameMember.reflashUserMoney();
				//开始农作物线程
				CropBeanThread cropBeanThread= new CropBeanThread();
				cropBeanThread.setCroItemBean(this);
				cropBeanThread.start();	
		if(this.cropBean!=null) {
			MessageDialogHelper.showConfirmDialog("以及有农作物存在，请铲除后再播种", "提示信息");
			return;
		}
		//如果没有，减少包裹中种子的种类
		else if( GameMember.subPackage(GameMember.selectdCropid)) {
			//包裹中已经没有种子
			MessageDialogHelper.showConfirmDialog("包囊中已经没有种子了，请到商店购买", "提示信息");
			return;
		}
		else if(GameMember.loginUser!=GameMember.currentUser){
			MessageDialogHelper.showConfirmDialog("不能在好友土地上种植噢", "提示信息");
			return;
		}
		else {
		    cb = new CropBean(GameMember.getCropBean(GameMember.selectdCropid));
			for(int i=0;i<GameMember.allUserPackages.size();i++) {
				if(cb.getCropName().equals(GameMember.allUserPackages.get(i).getItemName())) {
					PackageItemBean pib=(PackageItemBean)GameMember.allUserPackages.get(i);
					pib.setItemCount(pib.getItemCount()-1);
				}
			//保存包囊信息
			}
			PackageDAO.updateUserPack(GameMember.allUserPackages, GameMember.loginUser.getUserId());
			//增加用户经验值
			GameMember.userData.setExp(GameMember.userData.getExp()+5);
			//刷新显示经验值和等级
			FaceHelper.setExp(String.valueOf(GameMember.userData.getExp()));
			FaceHelper.setLevel(String.valueOf(GameMember.userData.getUserLevel()));
			//保存用户游戏数据
			UserDataDao.updateUserData(GameMember.userData);
			//在土地上增加一个农作物
			CropBean cropBean = GameMember.getCropBean(GameMember.selectdCropid);
			CropBean cropBean2 = new CropBean(cropBean);
			this.cropBean = cropBean2;
			//随机产生一个50以内的数量
			this.count = new Random().nextInt(50);
			//播种时间设置
			this.beginTime=new Date();
			//更新用户土地
			LandDAO.saveUserLand(GameMember.allUserLand, GameMember.loginUser.getUserId());
			cropBeanThread= new CropBeanThread();
			cropBeanThread.setCroItemBean(this);
			cropBeanThread.start();
		}
	}
	/**
	 * 摘取好友土地上的农作物
	 */
	public void zaiquAction() {
		if(this.cropBean==null) {
			return;
		}
		else if(this.cropBean.getStage()==this.cropBean.getCurrent()) {
			System.out.println("别心急，还没成熟");
		}
		else if(this.count==0) {
			System.out.println("已经摘取过了，等到播种成熟后再来吧");
		}
		else {
			for (StoreHouseItem sti : GameMember.allUserStore) {
				StoreHouseItemBean storeHouseItem = (StoreHouseItemBean)sti;
				if(this.cropBean.getCropId()==storeHouseItem.getCropBean().getCropId()) {
					storeHouseItem.setCount(this.count);//数量加入仓库
					this.count=this.count-1;
				}
			}
			//以下是写入登录用户的仓库文件
			String string="";
			for (int i = 0; i <GameMember.allUserStore.size(); i++) {
				string+=GameMember.allUserStore.get(i).toString();
			}
			FileUtils.writeFile("user/userdetails/"+GameMember.loginUser.getUserId()+"_store.txt", string);
			//以下是写入登录用户的游戏数据文件（经验）
			GameMember.userData.setExp(GameMember.userData.getExp()+1);
			String strUD = GameMember.userData.getExp()+";"+GameMember.userData.getMoney();
			FileUtils.writeFile("user/userdetails/"+GameMember.loginUser.getUserId()+"_data.txt", strUD);
			GameMember.reflashUserMoney();
		}
	}
	
	
}
