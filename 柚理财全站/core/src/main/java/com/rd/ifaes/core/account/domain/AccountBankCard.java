/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.account.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.entity.BaseEntity;

/**
 * 银行卡列表
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月1日
 */
@SuppressWarnings("serial")
public class AccountBankCard extends BaseEntity<AccountBankCard>{
	
	/**
	 * 卡号
	 */
	private String bankNo;
	/**
	 * 用户ID
	 */
	private String userId;
	
	/**
	 * 银行卡状态
	 */
	private String status;
	/**
	 * 银行代码
	 */
	private String bankCode;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 银行账号
	 */
	private String bankAccount;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	private Date updateTime;
	
	/**
	 * 添加时候的IP
	 */
	private String createIp;
	
	/**
	 * 修改时候的ip
	 */
	private String updateIp;
	
	
	/**
	 * 附件属性
	 */
	private String hideBankNo;
	
	/**
	 * 图片路径
	 */
	private String picPath;
	
	/**
	 *  获取是否允许解绑
	 *  Y：允许N：不允许  
	 */
	private boolean canDisable;
	
	/**
	 * 是否快捷卡
	 * Y：快捷卡
	 * N：普通卡
	 */
	private String  fastPayFlag;
	
	/**
	 * 用户名称
	 */
	private String userName ;
	
	/**
	 *手机号码
	 */
	private String mobile ;

	/**
	 * 获取属性uuid的值
	 * @return uuid属性值
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * 设置属性uuid的值
	 * @param  uuid属性值
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * 获取属性bankCode的值
	 * @return bankCode属性值
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * 设置属性bankCode的值
	 * @param  bankCode属性值
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * 获取属性bankName的值
	 * @return bankName属性值
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * 设置属性bankName的值
	 * @param  bankName属性值
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * 获取属性bankAccount的值
	 * @return bankAccount属性值
	 */
	public String getBankAccount() {
		return bankAccount;
	}

	/**
	 * 设置属性bankAccount的值
	 * @param  bankAccount属性值
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	/**
	 * 获取属性createTime的值
	 * @return createTime属性值
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置属性createTime的值
	 * @param  createTime属性值
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取属性updateTime的值
	 * @return updateTime属性值
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置属性updateTime的值
	 * @param  updateTime属性值
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 获取属性createIp的值
	 * @return createIp属性值
	 */
	public String getCreateIp() {
		return createIp;
	}

	/**
	 * 设置属性createIp的值
	 * @param  createIp属性值
	 */
	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	/**
	 * 获取属性updateIp的值
	 * @return updateIp属性值
	 */
	public String getUpdateIp() {
		return updateIp;
	}

	/**
	 * 设置属性updateIp的值
	 * @param  updateIp属性值
	 */
	public void setUpdateIp(String updateIp) {
		this.updateIp = updateIp;
	}

	/**
	 * 获取属性userId的值
	 * @return userId属性值
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置属性userId的值
	 * @param  userId属性值
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取属性hideBankNo的值
	 * @return hideBankNo属性值
	 */
	public String getHideBankNo() {
		return hideBankNo;
	}

	/**
	 * 设置属性hideBankNo的值
	 * @param  hideBankNo属性值
	 */
	public void setHideBankNo(String hideBankNo) {
		this.hideBankNo = hideBankNo;
	}


	/**
	 * 获取属性picPath的值
	 * @return picPath属性值
	 */
	public String getPicPath() {
		return picPath;
	}

	/**
	 * 设置属性picPath的值
	 * @param  picPath属性值
	 */
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	
	

	/**
	 * 获取属性canDisable的值
	 * @return canDisable属性值
	 */
	public boolean isCanDisable() {
		return canDisable;
	}

	/**
	 * 设置属性canDisable的值
	 * @param  canDisable属性值
	 */
	public void setCanDisable(boolean canDisable) {
		this.canDisable = canDisable;
	}

	/**
	 * 获取属性fastPayFlag的值
	 * @return fastPayFlag属性值
	 */
	public String getFastPayFlag() {
		return fastPayFlag;
	}

	/**
	 * 设置属性fastPayFlag的值
	 * @param  fastPayFlag属性值
	 */
	public void setFastPayFlag(String fastPayFlag) {
		this.fastPayFlag = fastPayFlag;
	}

	@Override
	public void preInsert() {
		super.preInsert();
		this.setStatus(CommonEnum.YES.getValue());
	}

	@Override
	public void preUpdate() {
		super.preUpdate();
	}

	/**
	 * 获取属性bankNo的值
	 * @return bankNo属性值
	 */
	public String getBankNo() {
		return bankNo;
	}

	/**
	 * 设置属性bankNo的值
	 * @param  bankNo属性值
	 */
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	/**
	 * 获取属性status的值
	 * @return status属性值
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * 设置属性status的值
	 * @param  status属性值
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
}
