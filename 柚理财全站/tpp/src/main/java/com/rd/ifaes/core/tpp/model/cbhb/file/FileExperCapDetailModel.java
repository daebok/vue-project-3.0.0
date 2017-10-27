package com.rd.ifaes.core.tpp.model.cbhb.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.core.tpp.model.cbhb.CbhbBaseModel;

/**
 * 
 * 3.12.1	红包申请通知（后台方式）
 * @version 3.0
 * @author ZhangBiao
 * @date 2017年2月21日
 */
public class FileExperCapDetailModel extends CbhbBaseModel{
	
	private static final long serialVersionUID = -3639191443537894840L;
	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(FileExperCapDetailModel.class);
	
	/**
	 * 序号	String(8)
	 */
	private String id;
	
	/**
	 * 账户存管平台客户号	String(16)
	 */
	private String plaCustId;
	
	/**
	 * 本金金额	Number(16)
	 */
	private String tasteAmt;
	
	/**
	 * 收益金额	String(10)
	 */
	private String interest;
	
	/**
	 * 投资管理费	Number(16)
	 */
	private String invesFee;


	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {"ID","PlaCustId","tasteAmt","Interest","Inves_fee"};

	/**
	 * 构造器
	 */
	public FileExperCapDetailModel(){
		super();
	}
	
	/**
	 * 请求参数校验
	 */
	@Override
	public void requestColumnValid() {
		super.requestColumnValid();
		LOGGER.info("CbhbExperCapModel valid...");
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getTasteAmt() {
		return tasteAmt;
	}

	public void setTasteAmt(String tasteAmt) {
		this.tasteAmt = tasteAmt;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getInvesFee() {
		return invesFee;
	}

	public void setInvesFee(String invesFee) {
		this.invesFee = invesFee;
	}

	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

}
