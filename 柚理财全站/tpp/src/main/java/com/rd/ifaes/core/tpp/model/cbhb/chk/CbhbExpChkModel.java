/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.chk;

import java.util.Date;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;

/**
 * 4.8	实时红包对账(ftp文件)
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月15日
 */
@SuppressWarnings("serial")
public class CbhbExpChkModel extends CbhbChkBaseModel{
	/**
	 * 账户存管平台流水号
	 */
	private String transId;
	
	/**
	 * 商户流水号
	 */
	private String merBillNo;
	
	/**
	 * 订单日期
	 */
	private String creDt;
	
	/**
	 * 交易金额
	 */
	private String transAmt;
	
	/**
	 * 账户存管平台ID
	 */
	private String plaCustId;
	
	/**
	 * 下载的文件名称
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param date
	 * @return
	 */
	public String getResultFileName(Date time){
		String date = DateUtils.formatDate(DateUtils.rollDay(time, Constant.INT_ONE_NEGA), DateUtils.DATEFORMAT_STR_012);
		return ConfigUtils.getValue(CbhbConstant.CBHB_CUSTOMID)+"_"+date+"EXP_chk.txt";
	}
	
	/**
	 * 获取属性transId的值
	 * @return transId属性值
	 */
	public String getTransId() {
		return transId;
	}

	/**
	 * 设置属性transId的值
	 * @param  transId属性值
	 */
	public void setTransId(String transId) {
		this.transId = transId;
	}

	/**
	 * 获取属性merBillNo的值
	 * @return merBillNo属性值
	 */
	public String getMerBillNo() {
		return merBillNo;
	}

	/**
	 * 设置属性merBillNo的值
	 * @param  merBillNo属性值
	 */
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

	/**
	 * 获取属性creDt的值
	 * @return creDt属性值
	 */
	public String getCreDt() {
		return creDt;
	}

	/**
	 * 设置属性creDt的值
	 * @param  creDt属性值
	 */
	public void setCreDt(String creDt) {
		this.creDt = creDt;
	}

	/**
	 * 获取属性transAmt的值
	 * @return transAmt属性值
	 */
	public String getTransAmt() {
		return transAmt;
	}

	/**
	 * 设置属性transAmt的值
	 * @param  transAmt属性值
	 */
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}

	/**
	 * 获取属性plaCustId的值
	 * @return plaCustId属性值
	 */
	public String getPlaCustId() {
		return plaCustId;
	}

	/**
	 * 设置属性plaCustId的值
	 * @param  plaCustId属性值
	 */
	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	@Override
	public String toString() {
		return "{TransId="+this.getTransId()+",MerBillNo="+this.getMerBillNo()+",Credt="+this.getCreDt()
				+",TransAmt="+this.getTransAmt()+",PlaCustId="+this.getPlaCustId()+"}";
	}
	
	
}
