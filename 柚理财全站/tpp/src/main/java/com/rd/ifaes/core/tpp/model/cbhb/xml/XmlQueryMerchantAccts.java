/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.xml;

import java.io.Serializable;

/**
 * 商户查询xml对象实体
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月6日
 */
@SuppressWarnings("serial")
public class XmlQueryMerchantAccts implements Serializable{
	
	/**
	 * 账户类型 800：手续费收入账户
				810：营销账户
				820：预付费账户
				830：现金账户
				840：信用账户

	 */
	private String acTyp;
	
	/**
	 * 可用余额
	 */
	private String avlBal;
	
	/**
	 * 账户余额
	 */
	private String actBal;
	
	/**
	 * 冻结余额
	 */
	private String frzBal;
	
	/**
	 * 账户名称
	 */
	private String acTypName;

	/**
	 * 获取属性acTyp的值
	 * @return acTyp属性值
	 */
	public String getAcTyp() {
		return acTyp;
	}

	/**
	 * 设置属性acTyp的值
	 * @param  acTyp属性值
	 */
	public void setAcTyp(String acTyp) {
		this.acTyp = acTyp;
	}

	/**
	 * 获取属性avlBal的值
	 * @return avlBal属性值
	 */
	public String getAvlBal() {
		return avlBal;
	}

	/**
	 * 设置属性avlBal的值
	 * @param  avlBal属性值
	 */
	public void setAvlBal(String avlBal) {
		this.avlBal = avlBal;
	}

	/**
	 * 获取属性actBal的值
	 * @return actBal属性值
	 */
	public String getActBal() {
		return actBal;
	}

	/**
	 * 设置属性actBal的值
	 * @param  actBal属性值
	 */
	public void setActBal(String actBal) {
		this.actBal = actBal;
	}

	/**
	 * 获取属性frzBal的值
	 * @return frzBal属性值
	 */
	public String getFrzBal() {
		return frzBal;
	}

	/**
	 * 设置属性frzBal的值
	 * @param  frzBal属性值
	 */
	public void setFrzBal(String frzBal) {
		this.frzBal = frzBal;
	}

	/**
	 * 获取属性acTypName的值
	 * @return acTypName属性值
	 */
	public String getAcTypName() {
		/**
		 *  800：手续费收入账户
				810：营销账户
				820：预付费账户
				830：现金账户
				840：信用账户

		 */
		if("800".equals(this.getAcTyp())){
			return "手续费收入账户";
		}else if("810".equals(this.getAcTyp())){
			return "营销账户";
		}else if("820".equals(this.getAcTyp())){
			return "预付费账户";
		}else if("830".equals(this.getAcTyp())){
			return "现金账户";
		}else if("840".equals(this.getAcTyp())){
			return "信用账户";
		}
		return acTypName;
	}

	/**
	 * 设置属性acTypName的值
	 * @param  acTypName属性值
	 */
	public void setAcTypName(String acTypName) {
		this.acTypName = acTypName;
	}
	
	
}
