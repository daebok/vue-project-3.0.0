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
 *  4.5	投标对账(ftp文件)
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月14日
 */
@SuppressWarnings("serial")
public class CbhbInvestChkModel extends CbhbChkBaseModel{
	/**
	 * 当日生成上一日的对账文件，每日下午三点通过批量方式生成，预计两小时内可生成完成。如今天是20150403，则网贷公司通过ftp直接在./CHK/20150403目录下获取。
	 */
	
	/**
	 * 订单号  用户投标时，后台生成。
	 */
	private String ordNo;
	
	/**
	 * 商户号
	 */
	private String mercId;
	
	/**
	 * 账户存管平台ID
	 */
	private String plaCustId;
	
	/**
	 * 交易金额
	 */
	private String transAmt;
	
	/**
	 * 标的ID
	 */
	private String borrowId;
	
	/**
	 * 订单日期   yyyyMMdd
	 */
	private String creDt;
	
	/**
	 * 订单时间  HHmmss
	 */
	private String creTm;
	
	/**
	 * 订单状态  0-其他异常
			1-投资冻结成功
			2-投资冻结失败
			3-投资撤销解冻成功
			4-投资撤销解冻失败
			5-投资部分冻结
			6-放款解冻
	 */
	private String ordSts;
	
	/**
	 * 商户流水号
	 */
	private String merBillNo;
	/**
	 * 构造器
	 */
	public CbhbInvestChkModel(){
		
	}
	
	/**
	 * 下载的文件名称
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @param date
	 * @return
	 */
	public String getResultFileName(Date time){
		String date = DateUtils.formatDate(DateUtils.rollDay(time, Constant.INT_ONE_NEGA), DateUtils.DATEFORMAT_STR_012);
		return ConfigUtils.getValue(CbhbConstant.CBHB_CUSTOMID)+"_"+date+"INVEST_chk.txt";
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
	 * 获取属性mercId的值
	 * @return mercId属性值
	 */
	public String getMercId() {
		return mercId;
	}

	/**
	 * 设置属性mercId的值
	 * @param  mercId属性值
	 */
	public void setMercId(String mercId) {
		this.mercId = mercId;
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
	 * 获取属性borrowId的值
	 * @return borrowId属性值
	 */
	public String getBorrowId() {
		return borrowId;
	}

	/**
	 * 设置属性borrowId的值
	 * @param  borrowId属性值
	 */
	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
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
	 * 获取属性creTm的值
	 * @return creTm属性值
	 */
	public String getCreTm() {
		return creTm;
	}

	/**
	 * 设置属性creTm的值
	 * @param  creTm属性值
	 */
	public void setCreTm(String creTm) {
		this.creTm = creTm;
	}

	/**
	 * 获取属性ordSts的值
	 * @return ordSts属性值
	 */
	public String getOrdSts() {
		return ordSts;
	}

	/**
	 * 设置属性ordSts的值
	 * @param  ordSts属性值
	 */
	public void setOrdSts(String ordSts) {
		this.ordSts = ordSts;
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
		return "{OrdNo="+this.getOrdNo()+",MercId="+this.getMercId()+",PlaCustId="+this.getPlaCustId()
				+",transAmt="+this.getTransAmt()+",borrowId="+this.getBorrowId()+",Credt="+this.getCreDt()
				+",CreTm="+this.getCreTm()+",OrdSts="+this.getOrdSts()+",MerbillNo="+this.getMerBillNo()+"}";
	}
	
	
}
