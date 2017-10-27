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
 * 充值对账(ftp文件)
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月15日
 */
@SuppressWarnings("serial")
public class CbhbPpdChkModel extends CbhbChkBaseModel{
	
	/**
	 * 在每天的凌晨四点生成上一日的对账文件 如今天是20150403，则网贷公司通过ftp直接在./CHK/20150403目录下获取。
		充值文件只生成上一日充值成功的记录，失败记录不生成。
	 */
	
	/**
	 * 订单号  用户投标时，后台生成。
	 */
	private String ordNo;
	
	/**
	 * 订单日期   yyyyMMdd
	 */
	private String creDt;
	
	/**
	 * 交易金额
	 */
	private String transAmt;
	
	/**
	 * 手续费
	 */
	private String feeAmt;
	
	/**
	 * 账户存管平台ID
	 */
	private String plaCustId;
	
	/**
	 * 商户流水号
	 */
	private String merBillNo;
	
	/**
	 * 下载的文件名称      商户号_当前系统日期的上一日的日期(yyyymmdd)PPD_chk.txt
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param date   查询的日期
	 * @return
	 */
	public String getResultFileName(Date time){
		//上一日的日期
		String date = DateUtils.formatDate(DateUtils.rollDay(time, Constant.INT_ONE_NEGA), DateUtils.DATEFORMAT_STR_012);
		return ConfigUtils.getValue(CbhbConstant.CBHB_CUSTOMID)+"_"+date+"PPD_chk.txt";
	}
	
	/**
	 * 获取属性ordNo的值
	 * @return ordNo属性值
	 */
	public String getOrdNo() {
		return ordNo;
	}

	/**
	 * 设置属性ordNo的值
	 * @param  ordNo属性值
	 */
	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
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
	 * 获取属性feeAmt的值
	 * @return feeAmt属性值
	 */
	public String getFeeAmt() {
		return feeAmt;
	}

	/**
	 * 设置属性feeAmt的值
	 * @param  feeAmt属性值
	 */
	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
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
	
	@Override
	public String toString() {
		return "{OrdNo="+this.getOrdNo()+",CreDt="+this.getCreDt()+",TransAmt="+this.getTransAmt()+
				",FeeAmt="+this.getFeeAmt()+",PlaCustId="+this.getPlaCustId()+
				 ",MerBillNo="+this.getMerBillNo()+"}";
	}
}
