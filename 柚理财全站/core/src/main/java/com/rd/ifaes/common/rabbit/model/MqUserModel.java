package com.rd.ifaes.common.rabbit.model;

import java.util.List;
import java.util.Map;

import com.rd.account.domain.AccountLog;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
import com.rd.ifaes.core.user.domain.User;

/**
 * 用户
 * @version 3.0
 * @author lh
 * @date 2016年8月6日
 */
public class MqUserModel extends MqBaseModel {
	
	/**
	 * 参数集合
	 */
	private Map<String,Object> params;
	/**
	 * 操作日志列表
	 */
	private List<String> logList; 
	/**
	 * 用户model
	 */
	private User userModel;
	
	/**
	 * 个人注册model
	 */
	private UfxRegisterModel registerModel;
	
	/**
	 * 企业注册model
	 */
	private UfxCompanyRegisterModel companyRegisterModel;	
	
	/**
	 * 无参构造方法
	 */
	public MqUserModel() {
		super();
	}
	/**
	 * 
	 * @param operate
	 */
	public MqUserModel(String operate) {
		this.operate = operate;
	}
	
	/**
	 * 
	 * @param companyRegisterModel
	 */
	public MqUserModel(UfxCompanyRegisterModel companyRegisterModel) {
		super();
		this.operate = MqConstant.OPERATE_COMPANY_REGISGER;
		this.companyRegisterModel = companyRegisterModel;
	}
	
	/**
	 * 
	 * @param registerModel
	 */
	public MqUserModel(UfxRegisterModel registerModel) {
		super();
		this.operate = MqConstant.OPERATE_REGISGER;
		this.registerModel = registerModel;
	}

	/**
	 * 获取个人注册model
	 * @return registerModel
	 */
	public UfxRegisterModel getRegisterModel() {
		return registerModel;
	}
	
	/**
	 * 设置个人注册model
	 * @param  registerModel
	 */
	public void setRegisterModel(UfxRegisterModel registerModel) {
		this.registerModel = registerModel;
	}
	
	/**
	 * 获取企业注册model
	 * @return companyRegisterModel
	 */
	public UfxCompanyRegisterModel getCompanyRegisterModel() {
		return companyRegisterModel;
	}
	
	/**
	 * 设置企业注册model
	 * @param  companyRegisterModel
	 */
	public void setCompanyRegisterModel(UfxCompanyRegisterModel companyRegisterModel) {
		this.companyRegisterModel = companyRegisterModel;
	}

	/**
	 * 获取属性userModel的值
	 * @return userModel属性值
	 */
	public User getUserModel() {
		return userModel;
	}
	/**
	 * 设置属性userModel的值
	 * @param  userModel属性值
	 */
	public void setUserModel(User userModel) {
		this.userModel = userModel;
	}
	
	/**
	 * 获取属性params的值
	 * @return params属性值
	 */
	public Map<String, Object> getParams() {
		return params;
	}
	/**
	 * 设置属性params的值
	 * @param  params属性值
	 */
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	/**
	 * 获取属性logList的值
	 * @return logList属性值
	 */
	public List<String> getLogList() {
		return logList;
	}
	/**
	 * 设置属性logList的值
	 * @param  logList属性值
	 */
	public void setLogList(List<String> logList) {
		this.logList = logList;
	}
	@Override
	public String toString() {
		return "MqUserModel [registerModel=" + registerModel + ", companyRegisterModel=" + companyRegisterModel + "]";
	}
	
}
