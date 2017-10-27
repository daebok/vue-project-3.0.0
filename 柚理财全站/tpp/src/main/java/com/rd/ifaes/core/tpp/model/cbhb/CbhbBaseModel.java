package com.rd.ifaes.core.tpp.model.cbhb;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hisun.merchant.atc.RSASignUtil;
import com.hisun.merchant.utils.CAP12CertTool;
import com.hisun.merchant.utils.HttpSendResult;
import com.hisun.merchant.utils.SimpleHttpsClient;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.resource.CbhbResource;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.RSAUtil;
import com.rd.ifaes.common.util.ReflectionUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.tpp.domain.TppCbhbRequest;
import com.rd.ifaes.core.tpp.domain.TppCbhbResponse;
import com.rd.ifaes.core.tpp.exception.CbhbException;
import com.rd.ifaes.core.tpp.model.TppModel;
import com.rd.ifaes.core.tpp.service.TppCbhbRequestService;
import com.rd.ifaes.core.tpp.service.TppCbhbResponseService;

/**
 * 渤海银行 实体类 基类
 * 
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月27日
 */
public class CbhbBaseModel extends TppModel {

	private static final long serialVersionUID = 8688318324987342468L;

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CbhbBaseModel.class);

	/**
	 * 签名验签工具类
	 */
	private static RSASignUtil util;

	/**
	 * 私钥路径
	 */
	private static final String PATH;
	 
	/**
	 * 本地私钥工具类
	 */
	private static CAP12CertTool tool;
	
	/**
	 * 本地私钥
	 */
	private static PrivateKey privateKey;
	/**
	 * 临时文件目录
	 */
	public static final String TEMPDIR;
	/**
	 * 静态块 加载一次
	 */
	static {
		TEMPDIR = "/data/files/";
		PATH = SpringContextHolder.class.getClassLoader().getResource("/").getPath()+ "cfca/800053400010001.pfx";
		LOGGER.info("cfca路径地址:{},文件路徑：{}", PATH,TEMPDIR);
		util = new RSASignUtil(PATH, ConfigUtils.getValue(CbhbConstant.CBHB_PRIVATE_KEY));
		try {
			tool = new CAP12CertTool(PATH,ConfigUtils.getValue(CbhbConstant.CBHB_PRIVATE_KEY));
			privateKey = tool.getPrivateKey();
		} catch (Exception e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_PRIVATE_KEY_INIT));
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_PRIVATE_KEY_INIT),BussinessException.TYPE_JSON);
		}
	}
	/**
	 * 字符集 String
	 */
	private String charSet;

	/**
	 * 商户号 String(16)
	 */
	private String partnerId;

	/**
	 * 版本号 String(3)
	 */
	private String versionNo;

	/**
	 * 消息类型 String(32)
	 */
	private String bizType;

	/**
	 * 签名方式 String(3)
	 */
	private String signType;

	/**
	 * 商户流水号 String(32)
	 */
	private String merBillNo;

	/**
	 * 页面返回url String(256)
	 */
	private String pageReturnUrl;

	/**
	 * 后台通知url String(256)
	 */
	private String bgRetUrl;

	/**
	 * 商户保留域 String(128)
	 */
	private String merPriv;

	/**
	 * 签名值 String(16)
	 */
	private String mac;

	/**
	 * 账户存管平台客户号 String(16)
	 */
	private String plaCustId;

	/**
	 * 应答返回码 String(16)
	 */
	private String respCode;

	/**
	 * 应答返回码描述信息 String(256)
	 */
	private String respDesc;

	/**
	 * 第三方通用提交地址
	 */
	private String cbhbSubmitUrl;

	/**
	 * 证书信息
	 */
	//private String merchantCert;

	/**
	 * 请求参数字段数组  包含所有的
	 */
	private String[] requestParamNames = new String[] {};

	/**
	 * 响应参数字段数组 包含所有的
	 */
	private String[] responseParamNames = new String[] {};

	/**
	 * http提交参数Map  包含mac 和 merchantCert
	 */
	private Map<String, String> requestParamsMap;

	/**
	 * http响应参数Map  包含mac 和 merchantCert
	 */
	private Map<String, String> responseParamsMap;

	/**
	 * 后台方式回调的参数-- 通信状态 200 通过
	 */
	private int status;

	/**
	 * 后台方式回调的参数-- 返回字符串
	 */
	private String responseBody;
	
	/**
	 * 处理类型： 1 = request 请求   ;不是 1  均为 response 响应
	 */
	private String handleType;
	
	/**
	 * 回调类型   1 实时回调  2同步回调 3 异步回调
	 */
	private String responseType;
	
	/**
	 * 回调流水号
	 */
	private String respTransId;
	
	/**
	 * 构造器
	 */
	public CbhbBaseModel() {
		this.setCharSet(CbhbConstant.CHAR_SET);
		this.setPartnerId(CbhbConstant.PARTNER_ID);
		this.setCbhbSubmitUrl(ConfigUtils.getValue(CbhbConstant.CBHB_SUBMIT_URL));// 测试地址
		this.setSignType(CbhbConstant.SIGN_TYPE_RSA);// RSA签名方式
		this.setVersionNo(CbhbConstant.VERSION_NO);
		this.setMerBillNo(this.getMerBillNoStr());
	}
	
	/**
	 * 通用 回调信息处理
	 * 有子类  每个子类需重写此类
	 * @author QianPengZhan
	 * @date 2017年2月24日
	 */
	public void response(Map<String, String> map) {
		try {
			LOGGER.info("base响应信息处理:{}", map.toString());
			this.setPartnerId(URLDecoder.decode(StringUtils.isNull(map.get("partner_id")),"GBK"));
			this.setBizType(URLDecoder.decode(StringUtils.isNull(map.get("biz_type")),"GBK"));
			this.setVersionNo(URLDecoder.decode(StringUtils.isNull(map.get("version_no")),"GBK"));
			this.setPlaCustId(URLDecoder.decode(StringUtils.isNull(map.get("PlaCustId")),"GBK"));
			this.setSignType(URLDecoder.decode(StringUtils.isNull(map.get("sign_type")),"GBK"));
			this.setMerBillNo(URLDecoder.decode(StringUtils.isNull(map.get("MerBillNo")),"GBK"));
			this.setRespCode(URLDecoder.decode(StringUtils.isNull(map.get("RespCode")),"GBK"));
			this.setRespDesc(URLDecoder.decode(StringUtils.isNull(map.get("RespDesc")),"GBK"));
			this.setMerPriv(URLDecoder.decode(StringUtils.isNull(map.get("MerPriv")),"GBK"));
			this.setMac(URLDecoder.decode(StringUtils.isNull(map.get("mac")),"GBK"));
		} catch (UnsupportedEncodingException e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE));
			throw new BussinessException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_URLDECODE),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 请求参数拼装和签名
	 * 使用参数字段数组通过方法拼接参数
	 * @author QianPengZhan
	 * @date 2017年2月22日
	 */
	public String getRequestSignData() {
		this.setHandleType(CbhbConstant.CBHB_HANDLE_TYPE_REQUEST);
		return this.getRequestAndResponseSignData();
	}
	
	/**
	 * 响应参数拼装和签名
	 * 使用参数字段数组通过方法拼接参数
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @return
	 */
	public String getResponseSignData(){
		this.setHandleType(CbhbConstant.CBHB_HANDLE_TYPE_RESPONSE);
		return this.getRequestAndResponseSignData();
	}
	
	/**
	 * 请求的map包
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @return
	 */
	public Map<String,String> getRequestMap(){
		this.setHandleType(CbhbConstant.CBHB_HANDLE_TYPE_REQUEST);
		return this.getRequestAndResponseMapParams();
	}
	
	/**
	 * 响应的map包
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @return
	 */
	public Map<String,String> getResponseMap(){
		this.setHandleType(CbhbConstant.CBHB_HANDLE_TYPE_RESPONSE);
		return this.getRequestAndResponseMapParams();
	}
	
	/**
	 * 获取请求/响应的参数数据包
	 * @author QianPengZhan
	 * @date 2017年2月28日
	 * @return
	 */
	private String getRequestAndResponseSignData(){
		StringBuilder nameData = new StringBuilder();
		StringBuilder signData = new StringBuilder();
		//获取参数字段数组
		final String[] paramNames =CbhbConstant.CBHB_HANDLE_TYPE_REQUEST.equals(this.getHandleType())?getRequestParamNames():getResponseParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			String name = paramNames[i];
			String upperName = this.getFirstUpperName(name);
			String value= this.getColumnValue(upperName);
			if(name.equals("biz_type")){
				LOGGER.info("biz_type={}",value);
			}
			if(name.equals("mac") || name.equals("merchantCert")){
				continue;//请求参数签名加密的时候不需要mac和merchantCert
			}
			nameData.append(name).append(",");
			signData.append(value);
		}
		String result = signData.toString();
		if(CbhbConstant.CBHB_HANDLE_TYPE_REQUEST.equals(this.getHandleType())){
			LOGGER.info("请求参数加密数据包：[{}],字段顺序为：[{}]",result,nameData.toString());
		}else if(CbhbConstant.CBHB_HANDLE_TYPE_RESPONSE.equals(this.getHandleType())){
			try {
				result = URLDecoder.decode(signData.toString(),"GBK");
			} catch (UnsupportedEncodingException e) {
				LOGGER.error("解码错误");
			}
			LOGGER.info("响应参数加密数据包：[{}],字段顺序为：[{}]",result,nameData.toString());
		}
		return result;
	}
	
	/**
	 * 获取请求/响应的Map包  用于http传输
	 * @author QianPengZhan
	 * @date 2017年2月22日
	 * @return
	 */
	public Map<String, String> getRequestAndResponseMapParams() {
		final Map<String, String> map = new HashMap<String, String>();
		final String[] paramNames = CbhbConstant.CBHB_HANDLE_TYPE_REQUEST.equals(this.getHandleType())?getRequestParamNames():getResponseParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			final String name = paramNames[i];
			String upperName = this.getFirstUpperName(name);
			String value= this.getColumnValue(upperName);
			map.put(name, value);
		}
		if(CbhbConstant.CBHB_HANDLE_TYPE_REQUEST.equals(this.getHandleType())){
			LOGGER.info("请求参数http传输map包：{}", map.toString());
		}else if(CbhbConstant.CBHB_HANDLE_TYPE_RESPONSE.equals(this.getHandleType())){
			LOGGER.info("响应参数http传输map包：{}", map.toString());
		}
		return map;
	}
	
	/**
	 * 请求参数加密签名
	 * @author ZhangBiao
	 * @date 2017年2月22日
	 * @param signData
	 */
	public void sign() {
		try {
			LOGGER.info("报文发送前开始加签");
			this.setMac(util.sign(this.getRequestSignData(), getEncoding()));//基类保存签名
			//this.setMerchantCert(util.getCertInfo());//基类保存证书信息
			this.setRequestParamsMap(this.getRequestMap());//基类保存请求map包
			//签名结束--将请求信息放入请求记录表
			if(CbhbConstant.BIZ_TYPE_QUERY_BALANCE.equals(this.getBizType()) || 
					CbhbConstant.BIZ_TYPE_QUERY_TRANS_STAT.equals(this.getBizType()) || 
					CbhbConstant.BIZ_TYPE_QUERY_MERCHANT_ACCTS.equals(this.getBizType()) ||
					CbhbConstant.BIZ_TYPE_QUERY_USER_INF.equals(this.getBizType())){
				LOGGER.error("查询类不入库");
			}else{
				this.saveTppCbhbRequest();
			}
		} catch (Exception e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PARAMS_SIGN,e.toString()));
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PARAMS_SIGN,e.toString()),BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 请求记录入库
	 * @author QianPengZhan
	 * @date 2017年3月23日
	 */
	private void  saveTppCbhbRequest(){
		TppCbhbRequestService tppCbhbRequestService = (TppCbhbRequestService)SpringContextHolder.getBean("tppCbhbRequestService");
		TppCbhbRequest request = new TppCbhbRequest();
		request.preInsert();
		request.setRequestType(this.getBizType());
		request.setRequestMerbillno(this.getMerBillNo());
		request.setRequestMap(this.getRequestMap().toString());
		request.setRequestFileUrl(this.getFileNamePath());
		request.setRequestTime(DateUtils.getNow());
		tppCbhbRequestService.insert(request);
	}
	
	/**
	 * 响应记录入库
	 * @author QianPengZhan
	 * @date 2017年3月23日
	 */
	private void  saveTppCbhbResponse(final CbhbBaseModel model){
		TppCbhbResponseService tppCbhbResponseService = (TppCbhbResponseService)SpringContextHolder.getBean("tppCbhbResponseService");
		if(CbhbConstant.CBHB_RESPONSE_TYPE_CURRENT.equals(this.getResponseType())){//实时回调
			TppCbhbResponse resp = tppCbhbResponseService.getByMerbillNo(model.getMerBillNo());
			if(resp == null){
				insertResp(resp, model, tppCbhbResponseService,CbhbConstant.CBHB_RESPONSE_TYPE_CURRENT);
			}else{
				resp.setRespTime(DateUtils.getNow());
				resp.setRespCode(model.getRespCode());
				resp.setRespDesc(model.getRespDesc());
				tppCbhbResponseService.update(resp);
			}
		}else if(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN.equals(this.getResponseType())){//同步回调
			TppCbhbResponse resp = tppCbhbResponseService.getByMerbillNo(model.getMerBillNo());
			if(resp == null){
				insertResp(resp, model, tppCbhbResponseService,CbhbConstant.CBHB_RESPONSE_TYPE_RETURN);
			}else{
				resp.setReturnRespCode(model.getRespCode());
				resp.setReturnRespDesc(model.getRespDesc());
				resp.setReturnRespTime(DateUtils.getNow());
				tppCbhbResponseService.update(resp);
			}
		}else if(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY.equals(this.getResponseType())){//异步回调
			TppCbhbResponse resp = tppCbhbResponseService.getByMerbillNo(model.getMerBillNo());
			if(resp == null){
				insertResp(resp, model, tppCbhbResponseService,CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY);
			}else{
				resp.setNotifyRespCode(model.getRespCode());
				resp.setNotifyRespDesc(model.getRespDesc());
				resp.setNotifyRespTime(DateUtils.getNow());
				tppCbhbResponseService.update(resp);
			}
		}
	}
	
	/**
	 * 添加响应信息
	 * @author QianPengZhan
	 * @date 2017年3月23日
	 * @param resp
	 * @param model
	 */
	private void insertResp(TppCbhbResponse resp,CbhbBaseModel model,
			TppCbhbResponseService tppCbhbResponseService,String type){
		resp = new TppCbhbResponse();
		resp.preInsert();
		resp.setRespType(model.getBizType());
		resp.setRequestMerbillno(model.getMerBillNo());
		resp.setRespTransid(StringUtils.isNull(model.getRespTransId()));
		resp.setRespMap(model.getResponseMap().toString());
		resp.setRespFileUrl(model.getResultFileNamePath());
		if(CbhbConstant.CBHB_RESPONSE_TYPE_CURRENT.equals(type)){
			resp.setRespTime(DateUtils.getNow());
			resp.setRespCode(model.getRespCode());
			resp.setRespDesc(model.getRespDesc());
			resp.setReturnRespCode(StringUtils.EMPTY);
			resp.setReturnRespDesc(StringUtils.EMPTY);
			resp.setReturnRespTime(DateUtils.getNow());
			resp.setNotifyRespCode(StringUtils.EMPTY);
			resp.setNotifyRespDesc(StringUtils.EMPTY);
			resp.setNotifyRespTime(DateUtils.getNow());
		}else if(CbhbConstant.CBHB_RESPONSE_TYPE_RETURN.equals(type)){
			resp.setRespTime(DateUtils.getNow());
			resp.setRespCode(StringUtils.EMPTY);
			resp.setRespDesc(StringUtils.EMPTY);
			resp.setReturnRespCode(model.getRespCode());
			resp.setReturnRespDesc(model.getRespDesc());
			resp.setReturnRespTime(DateUtils.getNow());
			resp.setNotifyRespCode(StringUtils.EMPTY);
			resp.setNotifyRespDesc(StringUtils.EMPTY);
			resp.setNotifyRespTime(DateUtils.getNow());
		}else if(CbhbConstant.CBHB_RESPONSE_TYPE_NOTIFY.equals(type)){
			resp.setRespTime(DateUtils.getNow());
			resp.setRespCode(StringUtils.EMPTY);
			resp.setRespDesc(StringUtils.EMPTY);
			resp.setReturnRespCode(StringUtils.EMPTY);
			resp.setReturnRespDesc(StringUtils.EMPTY);
			resp.setReturnRespTime(DateUtils.getNow());
			resp.setNotifyRespCode(model.getRespCode());
			resp.setNotifyRespDesc(model.getRespDesc());
			resp.setNotifyRespTime(DateUtils.getNow());
		}
		tppCbhbResponseService.insert(resp);
	}
	
	/**
	 * 回调验签
	 * @author QianPengZhan
	 * @date 2017年3月1日
	 * @param model  回调的实体类
	 * @return
	 */
	public boolean validSign(CbhbBaseModel model){
		LOGGER.info("响应回调验签...暂时为true,biz_type={}",model.getBizType());
		if(CbhbConstant.BIZ_TYPE_QUERY_BALANCE.equals(this.getBizType()) || 
				CbhbConstant.BIZ_TYPE_QUERY_TRANS_STAT.equals(this.getBizType()) || 
				CbhbConstant.BIZ_TYPE_QUERY_MERCHANT_ACCTS.equals(this.getBizType()) ||
				CbhbConstant.BIZ_TYPE_QUERY_USER_INF.equals(this.getBizType())){
			LOGGER.error("查询类不入库,不验签");
			return true;
		}else{
			//回调即刻入库
			saveTppCbhbResponse(model);
		}
		boolean flag = false;
		try {
			String dataString = RSAUtil.decryptByPrivateKey(model.getMac(), (RSAPrivateKey) privateKey);
			if(CbhbConstant.BIZ_TYPE_QUERY_USER_INF.equals(this.getBizType())){
				dataString = dataString.replace("+", " ").replace("=","");
			}
			LOGGER.info("响应回调参数：{},验证参数：{},验证结果：{}",dataString,model.getResponseSignData(),
					dataString.equals(model.getResponseSignData()));
			flag = dataString.equals(model.getResponseSignData());
		} catch (Exception e) {
			LOGGER.error(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_RESPONSE_PARAMS_SIGN,e.toString()));
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_RESPONSE_PARAMS_SIGN,e.toString()),BussinessException.TYPE_JSON);
		}
		return flag;
	}
	
	/**
	 * 后台方式 http传输map提交基类
	 * @author QianPengZhan
	 * @date 2017年2月27日
	 * @param requestModel提交的基类model
	 * @throws UnsupportedEncodingException
	 */
	public void submit(CbhbBaseModel requestModel){
		//请求提交
		SimpleHttpsClient httpClinet = new SimpleHttpsClient();
		LOGGER.info("http请求参数：{},编码方式：{}", requestModel.getRequestParamsMap().toString(), this.getEncoding());
		HttpSendResult result = httpClinet.postRequest(requestModel.getCbhbSubmitUrl(),
				requestModel.getRequestParamsMap(), CbhbConstant.CBHB_TIME_OUT,this.getEncoding());
		//回调
		LOGGER.info("http响应回调参数：[{}],通信状态：{}", result.getResponseBody(),result.getStatus());
		if(200 == result.getStatus()){
			final Map<String, String> responseMap = this.getResponseBodyString(result.getResponseBody());
			requestModel.setStatus(result.getStatus());
			requestModel.setResponseBody(result.getResponseBody());
			response(responseMap);
			requestModel.setRespTransId(StringUtils.isNull(responseMap.get("TransId")));
			requestModel.setResponseType(CbhbConstant.CBHB_RESPONSE_TYPE_CURRENT);
			//验签
			if(this.validSign(requestModel)){
				LOGGER.info("验签通过");
			}else{
				LOGGER.info("验签失败");
				throw new BussinessException("验签失败",BussinessException.TYPE_JSON);
			}
		}else{
			LOGGER.info("通信失败：{}",result.getStatus());
			throw new BussinessException("通信失败,status={}"+result.getStatus(),BussinessException.TYPE_JSON);
		}
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
	 * getResponseBody 对响应参数进行批处理
	 * @author QianPengZhan
	 * @date 2017年2月24日
	 * @param responseBody
	 */
	private Map<String, String> getResponseBodyString(String responseBody) {
		Map<String, String> map = new HashMap<String, String>();
		String[] arrayOne = responseBody.split("&");
		for (int i = 0; i < arrayOne.length; i++) {
			String[] arrayTwo = arrayOne[i].split("=");
			String name = arrayTwo[0];
			String value = "";
			if(arrayTwo.length > 1){
				value = arrayTwo[1];
			}
			map.put(name, value);
		}
		return map;
	}
	
	/**
	 * 获取编码
	 * @return
	 */
	public String getEncoding() {
		if ("00".equals(this.charSet)) {
			return "GBK";
		} else if ("01".equals(this.charSet)) {
			return "GB2312";
		} else if ("02".equals(this.charSet)) {
			return "UTF-8";
		} else {
			return "GBK";
		}
	}

	/**
	 * 校验返回码是否成功过
	 */
	public boolean checkReturn() {
		boolean result = false;
		if (StringUtils.isNotBlank(getRespCode())) {
			if (CbhbConstant.CBHB_CODE_SUCCESS.equals(getRespCode())) {
				result = true;
			}
		}
		return result;
	}
	
	/**
	 * 商户号_文件生成日期(yyyymmdd)_消息类型_商户流水号.txt
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @return
	 */
	public  String getFileNameOnByRule(){
		String billNo = this.getMerBillNo();//放款、还款用商户流水号
		if(CbhbConstant.BIZ_TYPE_BAT_INVEST_CANCLE.equals(this.getBizType()) || 
				CbhbConstant.BIZ_TYPE_EXIST_USER_REGISTER.equals(this.getBizType())){//批量投资撤销、批量注册用批次号
			billNo = this.getMerBillNo().substring(this.getMerBillNo().length()-10,this.getMerBillNo().length());
		}
		StringBuilder sb = new StringBuilder();
		sb.append(ConfigUtils.getValue(CbhbConstant.CBHB_CUSTOMID))
		.append("_").append(DateUtils.getDate(DateUtils.DATEFORMAT_STR_012))
		.append("_").append(this.getBizType())
		.append("_").append(billNo).append(".txt");
		return sb.toString();
	}
	/**
	 * OKfile名称  
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @return
	 */
	public String getOKFileName(){
		return this.getFileNameOnByRule()+".OK";
		
	}
	
	/**
	 * result File 名称    RESULT_商户号_文件生成日期(yyyymmdd)_消息类型_商户流水号.txt
	 * @author QianPengZhan
	 * @date 2017年3月13日
	 * @return
	 */
	public String getResultFileName(){
		return "RESULT_"+this.getFileNameOnByRule();
	}
	
	/**
	 * result file 本地存放的路径
	 * @author QianPengZhan
	 * @date 2017年3月13日
	 * @return
	 */
	public String getResultFileNamePath(){
		StringBuilder sb = new StringBuilder();
		sb.append(TEMPDIR).append(DateUtils.getDate(DateUtils.DATEFORMAT_STR_012)).append("/result/")
		.append(this.getResultFileName());
		return sb.toString();
	}
	
	/**
	 * TEMP_DIR + 商户号_文件生成日期(yyyymmdd)_消息类型_商户流水号.txt
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @return
	 */
	public  String  getFileNamePath(){
		StringBuilder sb = new StringBuilder();
		sb.append(TEMPDIR).append(this.getFileNameOnByRule());
		return sb.toString();
	}
	
	/**
	 * TEMP_DIR + 商户号_文件生成日期(yyyymmdd)_消息类型_商户流水号.txt.OK
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @return
	 */
	public  String  getOKFileNamePath(){
		return this.getFileNamePath() +".OK";
	}
	
	/**
	 * 拼接文件中的内容
	 * @author QianPengZhan
	 * @date 2017年3月7日
	 * @return
	 */
	public String getFileContent(){
		StringBuilder nameData = new StringBuilder();
		StringBuilder fileData = new StringBuilder();
		//获取文件类请求参数字段数组
		final String[] paramNames = getRequestParamNames();
		for (int i = 0; i < paramNames.length; i++) {
			String name = paramNames[i];
			String upperName = this.getFirstUpperName(name);
			final String value = this.getColumnValue(upperName);
			if(i == paramNames.length -1){
				fileData.append(value);
			}else{
				nameData.append(name).append(",");
				fileData.append(value).append(StringUtils.ONLY_VERTICAL_LINE);
			}
		}
		fileData.append(StringUtils.LF);
		LOGGER.info("文件内容：{}，拼接字段：{}",fileData.toString(),nameData.toString());
		return fileData.toString();
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
			LOGGER.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 每个实体类的提交参数的字段的校验
	 * @author QianPengZhan
	 * @date 2017年3月4日
	 */
	public void requestColumnValid(){
		LOGGER.info("baseValid...");
		if(StringUtils.isBlank(this.getCharSet())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_CHARSET_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getPartnerId())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_PARTNERID_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getCbhbSubmitUrl())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_SUBMITURL_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getSignType())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_SIGNTYPE_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getVersionNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_VERSIONNO_EMPTY),BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(this.getMerBillNo())){
			throw new CbhbException(ResourceUtils.get(CbhbResource.CBHB_RESOURCE_ERROR_REQUEST_MERBILLNO_EMPTY),BussinessException.TYPE_JSON);
		}
	}
	public String getCharSet() {
		return charSet;
	}

	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMerBillNo() {
		return merBillNo;
	}

	public void setMerBillNo(String merBillNo) {
		this.merBillNo = merBillNo;
	}

	public String getPageReturnUrl() {
		return pageReturnUrl;
	}

	public void setPageReturnUrl(String pageReturnUrl) {
		this.pageReturnUrl = pageReturnUrl;
	}

	public String getBgRetUrl() {
		return bgRetUrl;
	}

	public void setBgRetUrl(String bgRetUrl) {
		this.bgRetUrl = bgRetUrl;
	}

	public String getMerPriv() {
		return merPriv;
	}

	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getPlaCustId() {
		return plaCustId;
	}

	public void setPlaCustId(String plaCustId) {
		this.plaCustId = plaCustId;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	/**
	 * 获取属性cbhbSubmitUrl的值
	 * 
	 * @return cbhbSubmitUrl属性值
	 */
	public String getCbhbSubmitUrl() {
		return cbhbSubmitUrl;
	}

	/**
	 * 设置属性cbhbSubmitUrl的值
	 * 
	 * @param cbhbSubmitUrl属性值
	 */
	public void setCbhbSubmitUrl(String cbhbSubmitUrl) {
		this.cbhbSubmitUrl = cbhbSubmitUrl;
	}

	/**
	 * 获取属性merchantCert的值
	 * 
	 * @return merchantCert属性值
	 *//*
	public String getMerchantCert() {
		return merchantCert;
	}

	*//**
	 * 设置属性merchantCert的值
	 * 
	 * @param merchantCert属性值
	 *//*
	public void setMerchantCert(String merchantCert) {
		this.merchantCert = merchantCert;
	}
*/
	/**
	 * 获取属性requestParamNames的值
	 * 
	 * @return requestParamNames属性值
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置属性requestParamNames的值
	 * 
	 * @param requestParamNames属性值
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	/**
	 * 获取属性responseParamNames的值
	 * 
	 * @return responseParamNames属性值
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置属性responseParamNames的值
	 * 
	 * @param responseParamNames属性值
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取属性requestParamsMap的值
	 * 
	 * @return requestParamsMap属性值
	 */
	public Map<String, String> getRequestParamsMap() {
		return requestParamsMap;
	}

	/**
	 * 设置属性requestParamsMap的值
	 * 
	 * @param requestParamsMap属性值
	 */
	public void setRequestParamsMap(Map<String, String> requestParamsMap) {
		this.requestParamsMap = requestParamsMap;
	}

	/**
	 * 获取属性responseParamsMap的值
	 * 
	 * @return responseParamsMap属性值
	 */
	public Map<String, String> getResponseParamsMap() {
		return responseParamsMap;
	}

	/**
	 * 设置属性responseParamsMap的值
	 * 
	 * @param responseParamsMap属性值
	 */
	public void setResponseParamsMap(Map<String, String> responseParamsMap) {
		this.responseParamsMap = responseParamsMap;
	}

	/**
	 * 获取属性status的值
	 * 
	 * @return status属性值
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * 设置属性status的值
	 * 
	 * @param status属性值
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 获取属性responseBody的值
	 * 
	 * @return responseBody属性值
	 */
	public String getResponseBody() {
		return responseBody;
	}

	/**
	 * 设置属性responseBody的值
	 * 
	 * @param responseBody属性值
	 */
	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	/**
	 * 获取属性handleType的值
	 * @return handleType属性值
	 */
	public String getHandleType() {
		return handleType;
	}

	/**
	 * 设置属性handleType的值
	 * @param  handleType属性值
	 */
	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	/**
	 * 获取属性responseType的值
	 * @return responseType属性值
	 */
	public String getResponseType() {
		return responseType;
	}

	/**
	 * 设置属性responseType的值
	 * @param  responseType属性值
	 */
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	/**
	 * 获取属性respTransId的值
	 * @return respTransId属性值
	 */
	public String getRespTransId() {
		return respTransId;
	}

	/**
	 * 设置属性respTransId的值
	 * @param  respTransId属性值
	 */
	public void setRespTransId(String respTransId) {
		this.respTransId = respTransId;
	}

	
}
