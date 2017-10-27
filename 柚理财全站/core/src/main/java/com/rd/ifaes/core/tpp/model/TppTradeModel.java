package com.rd.ifaes.core.tpp.model;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.core.tpp.domain.TppTrade;



public class TppTradeModel extends TppTrade {
 
	private static final long serialVersionUID = 7570674089967872020L;
	
	/**
	 * 获取model对象
	 * @param tpp
	 * @return
	 */
	public static TppTradeModel instance(TppTrade tpp) {
		TppTradeModel model = new TppTradeModel();
		BeanUtils.copyProperties(tpp, model);
		return model;
	}
	/**
	 * 获取实体
	 * @return
	 */
	public  TppTrade prototype(){
		 TppTrade tpp = new  TppTrade();
		BeanUtils.copyProperties(this, tpp);
		return tpp;
	}
	
	
	
}
