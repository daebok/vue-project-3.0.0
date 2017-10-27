/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.rabbit.model;

import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.ufx.UfxRepaymentModel;

/**
 *  还款信息队列MODEL
 * @version 3.0
 * @author FangJun
 * @date 2016年10月17日
 */
public class MqRepayModel extends MqBaseModel {
	/**
	 * 还款
	 */
	private UfxRepaymentModel repayModel;

	private TppTrade tppTrade;

	private String projectId;

	
	public MqRepayModel() {
		super();
	}
	

	public MqRepayModel(String operate) {
		 this.operate=operate;
	}

	/**
	 * 获取属性repayModel的值
	 * @return repayModel属性值
	 */
	public UfxRepaymentModel getRepayModel() {
		return repayModel;
	}

	/**
	 * 设置属性repayModel的值
	 * @param  repayModel属性值
	 */
	public void setRepayModel(UfxRepaymentModel repayModel) {
		this.repayModel = repayModel;
	}

	public TppTrade getTppTrade() {
		return tppTrade;
	}

	public void setTppTrade(TppTrade tppTrade) {
		this.tppTrade = tppTrade;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	//	@Override
//	public String toString() {
//		return "MqRepayModel [orderNo=" + repayModel.getOrderNo() + ",amount:"+repayModel.getAmount()+",projectNo:"+repayModel.getProjectId()+ "]";
//	}

}
