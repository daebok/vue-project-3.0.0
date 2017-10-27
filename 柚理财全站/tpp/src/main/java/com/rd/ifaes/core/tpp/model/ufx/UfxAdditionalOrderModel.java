package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 * 补单model
 * @author xhf
 * @version 3.0
 * @date 2016年7月28日 下午2:58:44
 */
@SuppressWarnings("serial")
public class UfxAdditionalOrderModel extends UfxBaseModel {
	
	/**
	 * 发送异步回调
	 */
	public static final String REPEAT_SEND_YES = "01";
	
	/**
	 * 不发送异步回调
	 */
	public static final String REPEAT_SEND_NO = "02";
	
	/**
	 * 补单对应接口请求ufx 时的订单号，格式为：yyMMdd+14 位数字，如：16030713374018301234
	 */
	private String loanNo;
	
	/**
	 * 交易时间，格式为 yyyyMMdd 请求ufx 时的日期
	 */
	private String loanDate;
	
	/**
	 * 是否重新发送异步回调：01：发送，02：不发送
	 */
	private String isRepeatSend;
	
	/**********响应***********************/
	/**
	 * 应答返回码,此应答码表示交易处理结果非接口调用应答
	 */
	private String respCode;
	
	/**
	 * 应答返回码中文描述（返回状态为原始订单的处理状态）
	 */
	private String respDesc;
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo","ufxCustomerId", "businessWay",
			"merPriv", "loanNo", "loanDate", "isRepeatSend","notifyUrl", "signInfo" };

	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo", "ufxCustomerId", "businessWay",
			"respCode", "respDesc", "isRepeatSend","merPriv","signInfo" };

	
    /**
     * 构造函数
     */
	public UfxAdditionalOrderModel() {
		super();
		this.setService(UfxConstant.ADDITIONAL_ORDER);
	}

	/**
	 * 获取补单对应接口请求ufx时的订单号，格式为：yyMMdd+14位数字，如：16030713374018301234
	 * @return loanNo
	 */
	public String getLoanNo() {
		return loanNo;
	}

	/**
	 * 设置补单对应接口请求ufx时的订单号，格式为：yyMMdd+14位数字，如：16030713374018301234
	 * @param  loanNo
	 */
	public void setLoanNo(String loanNo) {
		this.loanNo = loanNo;
	}

	/**
	 * 获取交易时间，格式为yyyyMMdd请求ufx时的日期
	 * @return loanDate
	 */
	public String getLoanDate() {
		return loanDate;
	}

	/**
	 * 设置交易时间，格式为yyyyMMdd请求ufx时的日期
	 * @param  loanDate
	 */
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}

	/**
	 * 获取是否重新发送异步回调：01：发送，02：不发送
	 * @return isRepeatSend
	 */
	public String getIsRepeatSend() {
		return isRepeatSend;
	}

	/**
	 * 设置是否重新发送异步回调：01：发送，02：不发送
	 * @param  isRepeatSend
	 */
	public void setIsRepeatSend(String isRepeatSend) {
		this.isRepeatSend = isRepeatSend;
	}

	/**
	 * 获取响应
	 * @return respCode
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * 设置响应
	 * @param  respCode
	 */
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	/**
	 * 获取应答返回码中文描述（返回状态为原始订单的处理状态）
	 * @return respDesc
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * 设置应答返回码中文描述（返回状态为原始订单的处理状态）
	 * @param  respDesc
	 */
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
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
	 * @param  requestParamNames
	 */
	public void setRequestParamNames(String[] requestParamNames) {
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
	 * @param  responseParamNames
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

}
