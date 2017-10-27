/*package com.rd.ifaes.core.core.sms;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.core.sys.service.ConfigService;

*//**
 * 创蓝短信接口
 * @author FangJun
 * @date 2015-11-23
 *//*
public class ClSmsPortalImpl implements SmsPortal {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(ClSmsPortalImpl.class);
			
	@Override
	public String getSPName() {
			return "ChuangLan";
	}

	@Override
	public String send(String mobile, String content) {
		String result = "";
		//ConfigService configService = (ConfigService)BeanUtils.getBean("configService");
		ConfigService configService = SpringContextHolder.getBean("configService");
		String url =  configService.findByCode("sms_notice").getConfigValue();
		String account = configService.findByCode("sms_username").getConfigValue(); //用户名
		String pswd = configService.findByCode("sms_password").getConfigValue(); //密码
		boolean needstatus = true;// 是否需要状态报告，需要true，不需要false
		String product = null;// 产品ID
		String extno = null;// 扩展码
		
		try {
			result=batchSend(url, account, pswd, mobile, content, needstatus, product, extno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(StringUtils.isNotBlank(result)){
			String returnCode=result.split("\\n")[0];
			 if(StringUtils.isNotBlank(returnCode)){
				 result=code2msg(Integer.valueOf(returnCode.split(",")[1]));
			 }
		}
		
		return result;
	}
	*//**
	 * 根据返回状态码解释为提示
	 * @param ret   返回状态码
	 * @return 发送结果提示
	 *//*
	public String code2msg(int ret) {
		String result = "";
		if (ret == 0) {
			result = "ok";
		}
		if(StringUtils.isBlank(result)){
			switch (ret) {
				case 101:
					result = "无此用户";
					break;
				case 102:
					result = "密码错误";
					break;
				case 103:
					result = "提交过快（提交速度超过流速限制）";
					break;
				case 104:
					result = "系统忙（因平台侧原因，暂时无法处理提交的短信）";
					break;
				case 105:
					result = "敏感短信（短信内容包含敏感词）";
					break;
				case 106:
					result = "消息长度错（>536或<=0）";
					break;
				case 107:
					result = "包含错误的手机号码";
					break;
				case 108:
					result = "手机号码个数错（群发>50000或<=0;单发>200或<=0）";
					break;
				case 109:
					result = "无发送额度（该用户可用短信数已使用完）";
					break;
				case 110:
					result = "不在发送时间内";
					break;
				case 111:
					result = "超出该账户当月发送额度限制";
					break;
				case 112:
					result = "无此产品，用户没有订购该产品";
					break;
				case 113:
					result = "extno格式错（非数字或者长度不对）";
					break;
				case 115:
					result = "自动审核驳回";
					break;
				case 116:
					result = "签名不合法，未带签名（用户必须带签名的前提下）";
					break;		
				case 117:
					result = "IP地址认证错,请求调用的IP地址不是系统登记的IP地址";
					break;		
				case 118:
					result = "用户没有相应的发送权限";
					break;		
				case 119:
					result = "用户已过期";
					break;		
				default:
					result = "其他异常错误";
					break;
		    }
		}
		
		return result;
	}
	@Override
	public Map<String, Integer> getUseInfo() {
		return null;
	}
	*//**
	 * 
	 * @param url 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobile 手机号码，多个号码使用","分割
	 * @param msg 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 *//*
	private String send(String url, String account, String pswd, String mobile, String msg,
			boolean needstatus, String product, String extno) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpSendSM", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
					new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needstatus)), 
					new NameValuePair("msg", msg),
					new NameValuePair("product", product), 
					new NameValuePair("extno", extno), 
				});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return EncodeUtils.urlDecode(baos.toString());
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}

	}

	*//**
	 * 
	 * @param url 应用地址，类似于http://ip:port/msg/
	 * @param account 账号
	 * @param pswd 密码
	 * @param mobile 手机号码，多个号码使用","分割
	 * @param msg 短信内容
	 * @param needstatus 是否需要状态报告，需要true，不需要false
	 * @return 返回值定义参见HTTP协议文档
	 * @throws Exception
	 *//*
	private String batchSend(String url, String account, String pswd, String mobile, String msg,
			boolean needstatus, String product, String extno) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		try {
			URI base = new URI(url, false);
			method.setURI(new URI(base, "HttpBatchSendSM", false));
			method.setQueryString(new NameValuePair[] { 
					new NameValuePair("account", account),
					new NameValuePair("pswd", pswd), 
					new NameValuePair("mobile", mobile),
					new NameValuePair("needstatus", String.valueOf(needstatus)), 
					new NameValuePair("msg", msg),
					new NameValuePair("product", product),
					new NameValuePair("extno", extno), 
				});
			int result = client.executeMethod(method);
			if (result == HttpStatus.SC_OK) {
				InputStream in = method.getResponseBodyAsStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = in.read(buffer)) != -1) {
					baos.write(buffer, 0, len);
				}
				return EncodeUtils.urlDecode(baos.toString(), "UTF-8");
			} else {
				throw new Exception("HTTP ERROR Status: " + method.getStatusCode() + ":" + method.getStatusText());
			}
		} finally {
			method.releaseConnection();
		}

	}
}
*/