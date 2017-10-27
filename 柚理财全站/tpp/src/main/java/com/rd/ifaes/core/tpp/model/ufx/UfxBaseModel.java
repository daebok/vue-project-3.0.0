/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.model.ufx;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.HttpUtils;
import com.rd.ifaes.common.util.ReflectionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.rsa.RSAUtil;
import com.rd.ifaes.common.util.rsa.SignUtil;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;
import com.rd.ifaes.core.tpp.model.TppModel;

/**
 * 基类model
 * 
 * @author xhf
 * @version 3.0
 * @date 2016年06月16日 
 */
@SuppressWarnings("serial")
public class UfxBaseModel extends TppModel{

	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxBaseModel.class);

	/**
	 * 待加密关键参数
	 */
	private static final HashSet<String> ENCRYPTID = new HashSet<>();

	/**
	 * 接口标识
	 */
	protected String service;

	/**
	 * 订单号
	 */
	protected String orderNo;

	/**
	 * UFX商户号
	 */
	protected String ufxCustomerId;

	/**
	 * 用户客户号(受让人)
	 */
	protected String userCustId;

	/**
	 * 第三方账户ID 用户在第三方的账户ID 银行存管必须填写
	 */
	protected String userAccId;
	/**
	 * 网贷平台用户账号
	 */
	protected String userId;
	/**
	 * 项目发起人/融资人
	 */
	protected String sponsorer;

	/**
	 * 项目编号
	 */
	protected String projectId;

	/**
	 * 项目金额/融资金额
	 */
	private String projectAmount;

	/**
	 * 操作金额 充值/购买/取现等操作金额
	 */
	protected String amount;

	/**
	 * 页面/同步通知地址
	 */
	protected String returnUrl;

	/**
	 * 异步/后台通知地址
	 */
	protected String notifyUrl;

	/**
	 * 应答返回码
	 */
	protected String respCode;

	/**
	 * 应答描述
	 */
	protected String respDesc;

	/**
	 * 业务通道 请求来源通道 00-PC端，01-移动APP端，02-移动微信端，03-iPad/平板电脑端，04-其它
	 */
	protected String businessWay = "00";

	/**
	 * 用户私有域
	 */
	protected String merPriv;

	/**
	 * 签名
	 */
	protected String signInfo;

	/**
	 * 请求参数待加密字符串
	 */
	protected String respSignData;

	/**
	 * 响应参数待加密字符串
	 */
	protected String reqSignData;

	/**
	 * 加密私钥
	 */
	protected String ufxPrivateKey;

	/**
	 * 加密公钥
	 */
	protected String ufxPublicKey;

	/**
	 * UFX提交地址
	 */
	protected String ufxSubmitUrl;

	/**
	 * 扩展参数
	 */
	protected String reqExt;
 
	private static final String TAG_SIGNINFO = "signInfo";

	/**
	 * 请求参数
	 */
	private String[] requestParamNames = new String[] {};

	/**
	 * 响应参数
	 */
	private String[] responseParamNames = new String[] {};

	/**
	 * 构造函数
	 */	
	public UfxBaseModel() {
		super();
		this.ufxCustomerId = ConfigUtils.getValue(Constant.UFX_CUSTOMERID);
		this.ufxPrivateKey = ConfigUtils.getValue("ufx_private_key");
		this.ufxPublicKey = ConfigUtils.getValue("ufx_public_key");
		this.ufxSubmitUrl = Constant.FLAG_YES.equals(ConfigUtils.getValue(ConfigConstant.CONFIG_ONLINE)) ? ConfigUtils.getValue("ufx_url")
				: ConfigUtils.getValue("ufx_test_url");
	}

	static {
		ENCRYPTID.add("realName"); // 姓名
		ENCRYPTID.add("idNo"); // 身份证号
		ENCRYPTID.add("phone"); // 手机号
		ENCRYPTID.add("email"); // 邮箱
		ENCRYPTID.add("cardId"); // 银行卡号
	}

	/**
	 * 商户请求UFX时的签名
	 * @param model
	 */
	public void signReq(final UfxBaseModel model) {
		final String signData = this.getReqSignData(model);
			LOGGER.info("请求加密字符串：{}" , signData);
		
		try {
			final String sign = SignUtil.subsign(signData, ufxPrivateKey);
			 LOGGER.info("商户请求UFX时的签名（测试用，正式环境下无此方法），RSA签名[content = {}] 结果：{}" , signData , sign);
			signInfo = sign;
		} catch (Exception e) {
			LOGGER.error("商户请求UFX时的签名（测试用，正式环境下无此方法），RSA签名[content = {}]时发生异常！",signData, e);
		}
	}

	/**
	 * 验签-用于测试商户验签UFX回调
	 * @param model
	 * @return
	 */
	public boolean validSign(final UfxBaseModel model) {
		boolean result = false;
		String tempReqSignData = null;
		try {
			tempReqSignData = this.getValidRespSignData(model);
			result = SignUtil.checkSign(tempReqSignData, this.getSignInfo(), ufxPublicKey);
		} catch (Exception e) {
			LOGGER.error("验签异常，验签字符串：{}" , tempReqSignData, e);
		}
		return result;
	}

	/**
	 * 加密关键信息
	 * 
	 * @param model
	 * @param privateKeyStr 加密私钥串
	 * @param name 关键词名称
	 * @param value 关键词值
	 */
	private void encryptData(final UfxBaseModel model, final String privateKeyStr, final String name, final String value) {
		if (ENCRYPTID.contains(name) && StringUtils.isNotBlank(value)) {
			try {
				final Field field = getClass().getDeclaredField(name);
				field.setAccessible(true);
				field.set(model, RSAUtil.encrypt(privateKeyStr, value));
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return
	 */
	public String[][] getRequestParam() {
		final StringBuilder sb = new StringBuilder();
		final String[] paramNames = getRequestParamNames();
		String[][] namePair = new String[paramNames.length][2];
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			final String value = (result == null ? "" : result.toString());
			namePair[i][0] = name;
			namePair[i][1] = value;
			sb.append("&" + name + "=" + value);
		}
		LOGGER.info("请求参数：{}" , sb.toString());
		return namePair;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return
	 */
	public String[][] getResponseParam() {
		final String[] paramNames = getResponseParamNames();
		String[][] namePair = new String[paramNames.length][2];
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			final String value = (result == null ? "" : result.toString());
			namePair[i][0] = name;
			namePair[i][1] = value;
		}
		return namePair;
	}
	
	/**
	 * 提交请求
	 * @param model
	 * @throws UnsupportedEncodingException 
	 * @throws Exception
	 */
	public void submit(final UfxBaseModel model) throws UnsupportedEncodingException {
		signReq(model);
		LOGGER.info("orderNo:{},提交路径：{}",model.getOrderNo(), getUfxSubmitUrl());
		String resp = HttpUtils.postClient(getUfxSubmitUrl(), getRequestParam());
		LOGGER.info("orderNo:{},请求返回：{}",model.getOrderNo(), resp);
		if (StringUtils.isNotBlank(resp)) {
			resp = EncodeUtils.urlDecode(resp);
			response(resp);
		}
	}

	/**
	 * 回调信息处理
	 * @param resp
	 */
	public void response(final String resp) {
		try {
			final JSONObject json = JSONObject.parseObject(resp);
			this.setRespCode(json.getString("respCode"));
			this.setRespDesc(json.getString("respDesc"));
			this.setOrderNo(json.getString("orderNo"));
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new UfxException(ResourceUtils.get(ResourceConstant.ORDER_HANDLE_SYSTEM_EXCEPTION));
		}
	}

	/**
	 * @return
	 */
	public boolean checkReturn() {
		boolean result = false;
		if (StringUtils.isNotBlank(getRespCode())) {
			if(UfxConstant.UFX_CODE_SUCCESS.equals(getRespCode())){
				result = true;
			}else if(UfxConstant.UFX_CODE_DOING.equals(getRespCode()) && UfxConstant.CASH_AUDIT.equals(getService())){
				result = true;
			}
			
		}
		return result;
	}

	/**
	 * 获取UFX商户号
	 * 
	 * @return
	 */
	public String getUfxCustomerId() {
		return ufxCustomerId;
	}

	/**
	 * 设置UFX商户号
	 * 
	 * @param ufxCustomerId
	 */
	public void setUfxCustomerId(final String ufxCustomerId) {
		this.ufxCustomerId = ufxCustomerId;
	}

	/**
	 * 获取接口标识
	 * 
	 * @return
	 */
	public String getService() {
		return service;
	}

	/**
	 * 设置接口标识
	 * 
	 * @param service
	 */
	public void setService(final String service) {
		this.service = service;
	}

	/**
	 * 获取订单号
	 * 
	 * @return
	 */
	public String getOrderNo() {
		return orderNo;
	}

	/**
	 * 设置订单号
	 * 
	 * @param orderNo
	 */
	public void setOrderNo(final String orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * 获取用户客户号
	 * 
	 * @return
	 */
	public String getUserCustId() {
		return userCustId;
	}

	/**
	 * 设置用户客户号
	 * 
	 * @param userCustId
	 */
	public void setUserCustId(final String userCustId) {
		this.userCustId = userCustId;
	}

	/**
	 * 获取项目发起人/融资人
	 * 
	 * @return
	 */
	public String getSponsorer() {
		return sponsorer;
	}

	/**
	 * 设置项目发起人/融资人
	 * 
	 * @param sponsorer
	 */
	public void setSponsorer(final String sponsorer) {
		this.sponsorer = sponsorer;
	}

	/**
	 * 获取项目编号
	 * 
	 * @return
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * 设置项目编号
	 * 
	 * @param projectId
	 */
	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	/**
	 * 获取项目金额/融资金额
	 * 
	 * @return
	 */
	public String getProjectAmount() {
		return projectAmount;
	}

	/**
	 * 设置项目金额/融资金额
	 * 
	 * @param projectAmount
	 */
	public void setProjectAmount(final String projectAmount) {
		this.projectAmount = projectAmount;
	}

	/**
	 * 操作金额 充值/购买/取现等操作金额
	 * 
	 * @return
	 */
	public String getAmount() {
		return amount;
	}

	/**
	 * 操作金额 充值/购买/取现等操作金额
	 * 
	 * @param amount
	 */
	public void setAmount(final String amount) {
		this.amount = amount;
	}

	/**
	 * 获取页面回调地址
	 * 
	 * @return
	 */
	public String getReturnUrl() {
		return returnUrl;
	}

	/**
	 * 设置前台页面回调地址
	 * 
	 * @param returnUrl
	 */
	public void setReturnUrl(final String returnUrl) {
		this.returnUrl = StringUtils.stripEnd(ConfigUtils.getValue("web_url"),"/")+ returnUrl;
	}
	
	/**
	 * 
	 * 设置后台页面回调地址
	 * @author xhf
	 * @date 2016年11月1日
	 */
	public void setManageReturnUrl(final String returnUrl){
		this.returnUrl = StringUtils.stripEnd(ConfigUtils.getValue("manage_url"),"/")+ returnUrl;
	}

	/**
	 * 获取异步通知地址
	 * 
	 * @return
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * 设置前台异步通知地址
	 * 
	 * @param notifyUrl
	 */
	public void setNotifyUrl(final String notifyUrl) {
		this.notifyUrl = StringUtils.stripEnd(ConfigUtils.getValue("notify_url"),"/")+ notifyUrl;
	}
	
	/**
	 * 设置后台异步通知地址
	 * 
	 * @param notifyUrl
	 */
	public void setManageNotifyUrl(final String notifyUrl) {
		this.notifyUrl = StringUtils.stripEnd(ConfigUtils.getValue("manage_url"),"/")+ notifyUrl;
	}

	/**
	 * 获取应答返回码
	 * 
	 * @return
	 */
	public String getRespCode() {
		return respCode;
	}

	/**
	 * 设置应答返回码
	 * 
	 * @param respCode
	 */
	public void setRespCode(final String respCode) {
		this.respCode = respCode;
	}

	/**
	 * 获取应答描述
	 * 
	 * @return
	 */
	public String getRespDesc() {
		return respDesc;
	}

	/**
	 * 设置应答描述
	 * 
	 * @param respDesc
	 */
	public void setRespDesc(final String respDesc) {
		this.respDesc = respDesc;
	}

	/**
	 * 获取业务通道请求来源通道00-PC端，01-移动APP端，02-移动微信端，03-iPad平板电脑端，04-其它
	 * 
	 * @return businessWay
	 */
	public String getBusinessWay() {
		return businessWay;
	}

	/**
	 * 设置业务通道请求来源通道00-PC端，01-移动APP端，02-移动微信端，03-iPad平板电脑端，04-其它
	 * 
	 * @param businessWay
	 */
	public void setBusinessWay(final String businessWay) {
		this.businessWay = businessWay;
	}

	/**
	 * 获取用户私有域
	 * 
	 * @return
	 */
	public String getMerPriv() {
		return merPriv;
	}

	/**
	 * 设置用户私有域
	 * 
	 * @param merPiv
	 */
	public void setMerPriv(final String merPriv) {
		this.merPriv = merPriv;
	}

	/**
	 * 获取签名
	 * 
	 * @return
	 */
	public String getSignInfo() {
		return signInfo;
	}

	/**
	 * 设置签名
	 * 
	 * @param signInfo
	 */
	public void setSignInfo(final String signInfo) {
		this.signInfo = signInfo;
	}

	/**
	 * 获取请求参数待加密字符串
	 * 
	 * @param model
	 * @return
	 */
	public String getRespSignData(final UfxBaseModel model) {
		final StringBuilder signData = new StringBuilder();
		final String[] paramNames = getResponseParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			if (TAG_SIGNINFO.equals(name)) {
				continue;
			}
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			final String value = result == null ? "" : result.toString();
			signData.append(value);

			// 处理关键信息加密
			encryptData(model, this.ufxPrivateKey, name, value);
		}
		this.respSignData = signData.toString();
		return respSignData;
	}

	/**
	 * 获取验签参数字符串
	 * 
	 * @param model
	 * @return
	 */
	public String getValidRespSignData(final UfxBaseModel model) {
		final StringBuilder signData = new StringBuilder();
		final String[] paramNames = getResponseParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			if (TAG_SIGNINFO.equals(name)) {
				continue;
			}
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			String value = result == null ? "" : result.toString();
			if (ENCRYPTID.contains(name) && StringUtils.isNotBlank(value)) {
				try {
					final Field field = getClass().getDeclaredField(name);
					field.setAccessible(true);
					value = RSAUtil.decrypt(this.ufxPublicKey, value);
					field.set(model, value);
				} catch (Exception e) {
					LOGGER.error("密文解密异常：{}" , value, e );
				}
			}
			signData.append(value);
		}
		this.respSignData = signData.toString();
		return respSignData;
	}

	/**
	 * 设置请求参数待加密字符串
	 * 
	 * @param respSignData
	 */
	public void setRespSignData(final String respSignData) {
		this.respSignData = respSignData;
	}

	/**
	 * 获取响应参数待加密字符串
	 * 
	 * @param model
	 * @return
	 */
	public String getReqSignData(final UfxBaseModel model) {
		final StringBuilder signData = new StringBuilder();
		final String[] paramNames = getRequestParamNames();
		final StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			if (TAG_SIGNINFO.equals(name)) {
				continue;
			}
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			final String value = result == null ? "" : result.toString();
			signData.append(value);
			buffer.append(name).append("=").append(value).append("&");
			// 处理关键信息加密
			encryptData(model, this.ufxPrivateKey, name, value);
		}
	    LOGGER.info("getReqSignData 待加密字符串:{}",buffer.toString());
		this.reqSignData = signData.toString();
		return reqSignData;
	}
	
	/**
	 * 获取验签参数字符串
	 * 
	 * @param model
	 * @param publicKeyStr
	 * @return
	 */
	public String getValidReqSignData(final UfxBaseModel model, final String publicKeyStr) {
		final StringBuilder signData = new StringBuilder();
		final String[] paramNames = getRequestParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			if (TAG_SIGNINFO.equals(name)) {
				continue;
			}
			final Object result = ReflectionUtils.invokeGetMethod(getClass(), this, name);
			String value = result == null ? "" : result.toString();
			if (ENCRYPTID.contains(name) && StringUtils.isNotBlank(value)) {
				try {
					final Field field = getClass().getDeclaredField(name);
					field.setAccessible(true);
					value = RSAUtil.decrypt(publicKeyStr, value);
					field.set(model, value);
				} catch (Exception e) {
					LOGGER.debug("密文解密异常：{}" , value, e);
				}
			}
			signData.append(value);
		}
		this.reqSignData = signData.toString();
		return reqSignData;
	}

	/**
	 * 设置响应参数待加密字符串
	 * 
	 * @param reqSignData
	 */
	public void setReqSignData(final String reqSignData) {
		this.reqSignData = reqSignData;
	}

	/**
	 * 获取加密私钥
	 * 
	 * @return
	 */
	public String getUfxPrivateKey() {
		return ufxPrivateKey;
	}

	/**
	 * 设置加密私钥
	 * 
	 * @param ufxPrivateKey
	 */
	public void setUfxPrivateKey(final String ufxPrivateKey) {
		this.ufxPrivateKey = ufxPrivateKey;
	}

	/**
	 * 获取加密公钥
	 * 
	 * @return
	 */
	public String getUfxPublicKey() {
		return ufxPublicKey;
	}

	/**
	 * 设置加密公钥
	 * 
	 * @param ufxPublicKey
	 */
	public void setUfxPublicKey(final String ufxPublicKey) {
		this.ufxPublicKey = ufxPublicKey;
	}

	/**
	 * 获取用户第三方账户的ID
	 * 
	 * @return
	 */
	public String getUserAccId() {
		return userAccId;
	}

	/**
	 * 设置用户第三方账户ID
	 * 
	 * @param userAccId
	 */
	public void setUserAccId(final String userAccId) {
		this.userAccId = userAccId;
	}

	/**
	 * 获取第三方平台用户号
	 * 
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置第三方平台用户号
	 * 
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取UFX提交地址
	 * 
	 * @return ufxSubmitUrl
	 */
	public String getUfxSubmitUrl() {
		return ufxSubmitUrl;
	}

	/**
	 * 设置UFX提交地址
	 * 
	 * @param ufxSubmitUrl
	 */
	public void setUfxSubmitUrl(final String ufxSubmitUrl) {
		this.ufxSubmitUrl = ufxSubmitUrl;
	}

	/**
	 * 获取请求参数
	 * 
	 * @return
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数
	 * 
	 * @param requestParamNames
	 */
	public void setRequestParamNames(final String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取响应参数
	 * 
	 * @return
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数
	 * 
	 * @param responseParamNames
	 */
	public void setResponseParamNames(final String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取响应参数
	 * 
	 * @return
	 */
	public String getReqExt() {
		return reqExt;
	}

	/**
	 * 设置扩展参数
	 * 
	 * @param reqExt
	 */
	public void setReqExt(final String reqExt) {
		this.reqExt = reqExt;
	}
}
