package com.rd.ifaes.core.tpp.model.ufx;

import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 查询银行卡接口
 * @author xhf
 * @version 3.0
 * @date 2016年6月6日 上午9:19:16
 */
@SuppressWarnings("serial")
public class UfxQueryBankCardModel extends UfxBaseModel {
	
	/**
	 * 银行卡号
	 */
	private String cardId;
	
	/**
	 * 用户银行卡列表
	 */
	private String userCardList;
	
	/**
	 * 商户平台用户号
	 */
	private String userId;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"userCustId", "userId", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay", "respCode", "respDesc",
			"userCustId", "cardId", "userCardList", "signInfo" };
	
	/**
	 * 构造函数
	 */
	public UfxQueryBankCardModel() {
		super();
		this.setService(UfxConstant.QUERY_BANK_CARD);
	}

	@Override
	public void response(String resp) {
		super.response(resp);
		final JSONObject json = JSONObject.parseObject(resp);
		this.setUserCardList(json.getString("userCardList"));
	}
	
	/**
	 * 获取银行卡号
	 * @return cardId
	 */
	public String getCardId() {
		return cardId;
	}

	/**
	 * 设置银行卡号
	 * @param cardId
	 */
	public void setCardId(final String cardId) {
		this.cardId = cardId;
	}

	/**  
	 * 获取用户银行卡列表  
	 * @return userCardList  
	 */
	public String getUserCardList() {
		return userCardList;
	}

	/**  
	 * 设置用户银行卡列表  
	 * @param userCardList  
	 */
	public void setUserCardList(final String userCardList) {
		this.userCardList = userCardList;
	}

	/**
	 * 获取请求参数数组
	 * @return requestParamNames
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 * @param requestParamNames
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数数组
	 * @return responseParamNames
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 * @param responseParamNames
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取商户平台用户号
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置商户平台用户号
	 * @param userId  商户平台用户号
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}
	
}
