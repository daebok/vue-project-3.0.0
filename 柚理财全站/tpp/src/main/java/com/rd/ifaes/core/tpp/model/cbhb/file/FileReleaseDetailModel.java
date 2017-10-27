/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.file;

import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;



/**
 * 放款投资明细实体类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月7日
 */
@SuppressWarnings("serial")
public class FileReleaseDetailModel extends CbhbBaseModel{
	
	
	/**
	 * 序号
	 */
	private String id;
	
	/**
	 * 账户存管平台客户号
	 */
	private String plaCustId;
	
	/**
	 * 交易金额
	 */
	private String transAmt;
	
	/**
	 * 冻结编号
	 */
	private String freezeId;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"ID","PlaCustId","TransAmt","FreezeId"};
	/**
	 * 获取属性id的值
	 * @return id属性值
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置属性id的值
	 * @param  id属性值
	 */
	public void setId(String id) {
		this.id = id;
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
	 * 获取属性freezeId的值
	 * @return freezeId属性值
	 */
	public String getFreezeId() {
		return freezeId;
	}

	/**
	 * 设置属性freezeId的值
	 * @param  freezeId属性值
	 */
	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
	}

	/**
	 * 获取属性requestParamNames的值
	 * @return requestParamNames属性值
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置属性requestParamNames的值
	 * @param  requestParamNames属性值
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}
	
	
	
}
