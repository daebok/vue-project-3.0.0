package com.rd.ifaes.common.rabbit.model;

import java.util.List;

import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.tpp.model.TppTradeModel;

/**
 * 交易
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月6日
 */
public class MqTradeModel extends MqBaseModel {

	/**
	 * 交易列表
	 */
	private List<TppTradeModel> tradeList;
	
	/**
	 * 无参构造方法
	 */
	public MqTradeModel() {
		super();
	}

	/**
	 * 
	 * @param operate
	 */
	public MqTradeModel(String operate) {
		this.operate = operate;
	}
	
	/**
	 * 
	 * @param tradeList
	 */
	public MqTradeModel(List<TppTradeModel> tradeList) {
		this.operate = MqConstant.OPERATE_TPP_TASK;
		this.tradeList = tradeList;
	}

	/**
	 * 
	 * @param operate
	 * @param tradeList
	 */
	public MqTradeModel(String operate, List<TppTradeModel> tradeList) {
		this.operate = operate;
		this.tradeList = tradeList;
	}

	public List<TppTradeModel> getTradeList() {
		return tradeList;
	}

	public void setTradeList(List<TppTradeModel> tradeList) {
		this.tradeList = tradeList;
	}

	@Override
	public String toString() {
		return "MqTradeModel [operate="+operate+", tradeList=" + tradeList + "]";
	}
	
	
}
