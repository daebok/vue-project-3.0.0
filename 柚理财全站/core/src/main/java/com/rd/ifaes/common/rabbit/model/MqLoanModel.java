/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.rabbit.model;

import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;

/**
 *  放款
 * @version 3.0
 * @author FangJun
 * @date 2016年10月17日
 */
public class MqLoanModel extends MqBaseModel {
	/**
	 * 放款
	 */
	private UfxLoansModel loanModel;

	private TppTrade tppTrade;
	
	
	public MqLoanModel() {
		super();
	}
	

	public MqLoanModel(String operate) {
		 this.operate=operate;
	}


	/**
	 * 获取属性loanModel的值
	 * @return loanModel属性值
	 */
	public UfxLoansModel getLoanModel() {
		return loanModel;
	}

	/**
	 * 设置属性loanModel的值
	 * @param  loanModel属性值
	 */
	public void setLoanModel(UfxLoansModel loanModel) {
		this.loanModel = loanModel;
	}

//	@Override
//	public String toString() {
//		return "MqLoanModel [orderNo=" + loanModel.getOrderNo() +",projectNo="+ loanModel.getProjectId() + ",amount="+loanModel.getAmount()+",investNo="+loanModel.getInvestNo()+",freeNo="+loanModel.getFreezeNo()+"]";
//	}

	public TppTrade getTppTrade() {
		return tppTrade;
	}

	public void setTppTrade(TppTrade tppTrade) {
		this.tppTrade = tppTrade;
	}
}
