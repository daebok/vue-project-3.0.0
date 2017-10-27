package com.rd.ifaes.common.rabbit.model;

import com.rd.ifaes.core.operate.model.BatchUserRedModel;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.user.domain.User;

public class MqActPlanModel extends MqBaseModel {
	
	private User user;//注册用户
	private ProjectInvest invest; //投资记录
	private BatchUserRedModel batchUserRed;//用户红包批量发放
	private String extendParm;  //扩展参数
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}	
	public ProjectInvest getInvest() {
		return invest;
	}
	public void setInvest(ProjectInvest invest) {
		this.invest = invest;
	}
	public String getExtendParm() {
		return extendParm;
	}
	public void setExtendParm(String extendParm) {
		this.extendParm = extendParm;
	}
	
	public BatchUserRedModel getBatchUserRed() {
		return batchUserRed;
	}
	public void setBatchUserRed(BatchUserRedModel batchUserRed) {
		this.batchUserRed = batchUserRed;
	}
	public MqActPlanModel() {
		super();
	}
	
	public MqActPlanModel(String operate, BatchUserRedModel batchUserRed) {
		this.operate = operate;
		this.batchUserRed = batchUserRed;
	}
	/**
	 * 构造方法
	 * @param operate 活动大类(MqConstant中取值)
	 * @param user	发放对象
	 * @param invest 投资记录
	 * @param extendParm 扩展参数
	 */
	public MqActPlanModel(String operate,User user, ProjectInvest invest,String extendParm) {
		this.operate = operate;
		this.user = user;
		this.invest=invest;
		this.extendParm=extendParm;
	}

}
