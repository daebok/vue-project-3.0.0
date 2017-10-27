package com.rd.ifaes.core.tpp.model.cbhb.app;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.CbhbAppUtil;
import com.rd.ifaes.common.util.ReflectionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.model.TppModel;
/**
 * 渤海银行APP实体类 基类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年7月18日
 */
@SuppressWarnings("serial")
public class CbhbAppBaseModel extends TppModel{
	/**
	 * 日志
	 */
	private static final Logger logger = LoggerFactory.getLogger(CbhbAppBaseModel.class);
	
	/**
	 * 签名秘钥
	 */
	public static final String KEYSTR ="cbhb&virtu%@)000";
	/**
	 * APP 请求提交Url地址
	 */
	private String submitUrl;
	/**
	 * 固定请求参数一   接口类型
	 */
	private String transid;
	
	/**
	 * 固定请求参数二    密文  
	 */
	private String NetLoanInfo;
	/**
	 * 商户号  
	 */
	private String partnerId;
	/**
	 * 商户流水号   
	 */
	private String merBillNo;
	/**
	 * 商户保留域 
	 */
	private String merPriv;
	/**
	 * 版本号
	 */
	private String versionNo;
	/**
	 * 消息类型   
	 */
	private String bizType;
	/**
	 * 应答返回码    
	 */
	private String rpCode;
	/**
	 * 应答返回码描述信息
	 */
	private String rpDesc;
	/**
	 * 签名值
	 */
	private String mac;
	/**
	 * 签名方式
	 */
	private String signType;
	
	/**
	 * 返回连接
	 */
	private String backUrl;
	
	/**
	 * 前置连接
	 */
	private String frontUrl;
	
	/**
	 * 构造器
	 */
	public CbhbAppBaseModel() {
		super();
		this.setPartnerId(CbhbConstant.PARTNER_ID);
		this.setMerBillNo(this.getMerBillNoStr());
		this.setSubmitUrl("http://mtest.cbhb.com.cn/pmobile/static/index.html");
	}
	
	/**
	 * APP明文加密字段
	 */
	private String[] requestParamNames = new String[] {};

	/**
	 * 响应参数字段数组 包含所有的
	 */
	private String[] responseParamNames = new String[] {};
	
	/**=====================================请求处理方法  start  ============================================================*/
	/**
	 * 获得加密的明文Json字符串
	 * @return
	 */
	public String getPlainTxtJson() {
		String plainTxt = StringUtils.EMPTY;
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		final String[] paramNames = getRequestParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			String upperName = this.getFirstUpperName(name);
			String value= this.getColumnValue(upperName);
			if(i == paramNames.length-1) {
				sb.append("\"").append(upperName).append("\":").append("\"").append(value).append("\"}");
			}else {
				sb.append("\"").append(upperName).append("\":").append("\"").append(value).append("\",");
			}
		}
		plainTxt = sb.toString();
		logger.info("APP明文:{}",plainTxt);	
		return plainTxt;
	}
	
	/**
	 * 将明文进行加密处理得到密文
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public String handleNetLoanInfo(String plainTxt){
		String cipherText =  StringUtils.EMPTY;
		try {
			cipherText = CbhbAppUtil.AES_Encrypt(KEYSTR , plainTxt);
			cipherText = URLEncoder.encode(cipherText,"utf-8");
			logger.info("密文:{}",cipherText);
		} catch (UnsupportedEncodingException e) {
			logger.info("加密错误:{}",e.toString());
			throw new BussinessException("加密错误");
		}
		return cipherText;
	}
	
	/**
	 * 获取请求地址
	 * @param transId
	 * @param netLoanInfo
	 * @return
	 */
	public String getUrl(String netLoanInfo){
		StringBuilder url = new StringBuilder();
		return url.append(this.getSubmitUrl()).append("?Transid=")
		.append(StringUtils.isNull(this.getTransid())).append("&NetLoanInfo=")
		.append(StringUtils.isNull(netLoanInfo)).toString();
	}
	
	/**=====================================请求处理方法  end  ============================================================*/
	
	/**=====================================回调处理方法  start  ============================================================*/
	/**
	 * 通用回调信息处理
	 * @param map
	 */
	public void response(Map<String, String> map){
		try {
			logger.info("APP通用响应信息处理:{}", map.toString());
			this.setPartnerId(URLDecoder.decode(StringUtils.isNull(map.get("PartnerId")),"GBK"));
			this.setMerBillNo(URLDecoder.decode(StringUtils.isNull(map.get("MerBillNo")),"GBK"));
			this.setMerPriv(URLDecoder.decode(StringUtils.isNull(map.get("MerPriv")),"GBK"));
			this.setVersionNo(URLDecoder.decode(StringUtils.isNull(map.get("Version_No")),"GBK"));
			this.setBizType(URLDecoder.decode(StringUtils.isNull(map.get("Biz_Type")),"GBK"));
			this.setRpCode(URLDecoder.decode(StringUtils.isNull(map.get("RpCode")),"GBK"));
			this.setRpDesc(URLDecoder.decode(StringUtils.isNull(map.get("RpDesc")),"GBK"));
			this.setMac(URLDecoder.decode(StringUtils.isNull(map.get("Mac")),"GBK"));
			this.setSignType(URLDecoder.decode(StringUtils.isNull(map.get("Sign_Type")),"GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE));
			throw new BussinessException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 解密回调中的Mac值
	 * @param mac
	 * @return
	 */
	public String getDecryMac(){
		return CbhbAppUtil.AES_Decrypt(KEYSTR, this.getMac());
	}
	
	/**
	 * 校验mac解密之后的值
	 * @return
	 */
	public boolean verifyMac(String macJson){
		try {
			macJson = URLDecoder.decode(macJson,"GBK");
		} catch (UnsupportedEncodingException e) {
			logger.info("解码失败");
			throw new BussinessException("解码失败");
		}
		boolean flag  = false;
		String plainTxtJson = handleBackParams();
		logger.info("macJson:{},plainTxtJson:{}",macJson,plainTxtJson);
		flag = macJson.equals(plainTxtJson);
		return flag;
	}
	
	/**
	 * 根据回调参数数组  按照字母顺序 拼接字符串成为json 
	 * @return
	 */
	public String handleBackParams(){
		StringBuilder sb = new StringBuilder("{");
		final String[] paramNames = getResponseParamNames();
		Arrays.sort(paramNames);
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			String upperName = this.getFirstUpperName(name);
			String value= this.getColumnValue(upperName);
			if("Mac".equals(upperName)){
				value = StringUtils.EMPTY;
			}
			if(i == paramNames.length-1) {
				sb.append("\"").append(name).append("\":").append("\"").append(value).append("\"}");
			}else {
				sb.append("\"").append(name).append("\":").append("\"").append(value).append("\",");
			}
		}
		return sb.toString(); 
	}
	
	/**=====================================回调处理方法  end  ============================================================*/
	
	public String getTransid() {
		return transid;
	}

	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getNetLoanInfo() {
		return NetLoanInfo;
	}

	public void setNetLoanInfo(String netLoanInfo) {
		NetLoanInfo = netLoanInfo;
	}

	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getMerBillNo() {
		return merBillNo;
	}
	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getBackUrl() {
		return backUrl;
	}
	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}
	public String getFrontUrl() {
		return frontUrl;
	}
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	public String getRpCode() {
		return rpCode;
	}
	public void setRpCode(String rpCode) {
		this.rpCode = rpCode;
	}
	public String getRpDesc() {
		return rpDesc;
	}
	public void setRpDesc(String rpDesc) {
		this.rpDesc = rpDesc;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String[] getRequestParamNames() {
		return requestParamNames;
	}
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}
	public String[] getResponseParamNames() {
		return responseParamNames;
	}
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}
	
	/**
	 * 获取商户流水号
	 * 
	 * @author QianPengZhan
	 * @date 2017年2月21日
	 * @return
	 */
	public String getMerBillNoStr() {
		return CbhbConstant.PARTNER_ID + OrderNoUtils.getSerialNumber();
	}
	
	/**
	 * 将不规则的字段变为驼峰命名
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @param name
	 * @return
	 */
	private String getFirstUpperName(String name){
		String upperName = name;
		if (name.contains("_")) {
			String[] array = name.split("_");
			upperName = array[0] + StringUtils.firstCharUpperCase(array[1]);
			if(array.length > 2){
				upperName = upperName + StringUtils.firstCharUpperCase(array[2]);
			}
			if("FEEAMT".equals(upperName)){//不规则命名处理
				upperName = "FeeAmt";
			}
		}
		if("ID".equals(upperName)){
			upperName = "Id";
		}
		return upperName;
	}
	
	
	/**
	 * 根据name获取属性value值
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @param upperName
	 * @return
	 */
	private String getColumnValue(String upperName){
		final Object result = ReflectionUtils.invokeGetMethod(getClass(),this, upperName);
		final String value = (result == null ? "" : result.toString());
		return value;
	}

	/**
	 * 回调打印，用于响应CBHB异步回调 
	 */
	public void printSuccess500Return(final HttpServletResponse response) {
		try {
			response.reset();
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(CbhbConstant.CBHB_SUCCESS_CODE);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public String getSubmitUrl() {
		return submitUrl;
	}

	public void setSubmitUrl(String submitUrl) {
		this.submitUrl = submitUrl;
	}
	
	
}
