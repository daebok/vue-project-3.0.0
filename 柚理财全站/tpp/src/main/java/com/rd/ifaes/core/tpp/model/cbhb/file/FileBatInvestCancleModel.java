/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.cbhb.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;

/**
 * 批量撤销文件的第一行内容
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年3月10日
 */
@SuppressWarnings("serial")
public class FileBatInvestCancleModel extends CbhbBaseModel {
	/**
	 * logger
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileBatInvestCancleModel.class);
	
	/**
	 * 批次号 
	 */
	private String batchNo;
	
	/**
	 * 交易日期
	 */
	private String transDate;
	
	/**
	 * 总笔数
	 */
	private String totalNum;
	
	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"char_set","partner_id", "BatchNo", "TransDate","TotalNum"};
	
	/**
	 * 构造器
	 */
	public FileBatInvestCancleModel(){
		super();
		LOGGER.info("FileBatInvestCancleModel 初始化....");
		this.setTransDate(DateUtils.getDate(DateUtils.DATEFORMAT_STR_012));
	}
	
	/**
	 * 录入txt第一行的内容
	 * @author QianPengZhan
	 * @date 2017年3月8日
	 * @param params
	 * @return
	 */
	public String getFirstFileContent(String batchNo,String totalNum){
		this.setBatchNo(batchNo);//批次号
		this.setTotalNum(totalNum);
		return this.getFileContent();
	}
	
	/**
	 * 根据内容获取对象
	 * @author QianPengZhan
	 * @date 2017年3月14日
	 * @return
	 */
	public FileBatInvestCancleModel getModelByContent(String firstContent){
		FileBatInvestCancleModel model = new FileBatInvestCancleModel();
		String[] content = firstContent.split(StringUtils.VERTICAL_LINE);
		for (int i = 0; i < content.length; i++) {
			model.setCharSet(content[0]);
			model.setPartnerId(content[1]);
			model.setBatchNo(content[2]);
			model.setTransDate(content[3]);
			model.setTotalNum(content[4]);
		}
		return model;
	}
	
	/**
	 * 获取属性batchNo的值
	 * @return batchNo属性值
	 */
	public String getBatchNo() {
		return batchNo;
	}

	/**
	 * 设置属性batchNo的值
	 * @param  batchNo属性值
	 */
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * 获取属性transDate的值
	 * @return transDate属性值
	 */
	public String getTransDate() {
		return transDate;
	}

	/**
	 * 设置属性transDate的值
	 * @param  transDate属性值
	 */
	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	/**
	 * 获取属性totalNum的值
	 * @return totalNum属性值
	 */
	public String getTotalNum() {
		return totalNum;
	}

	/**
	 * 设置属性totalNum的值
	 * @param  totalNum属性值
	 */
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
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
