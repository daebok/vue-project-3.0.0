package com.rd.ifaes.core.core.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.sys.service.ConfigService;


/** 
* 助通科技SMS接口
* @ClassName: ZtSmsPortalImpl 
* @Description 发送 短信
* 发送传递的参数<br>
* http://www.ztsms.cn:8800/sendXSms.do?username=用户名&tkey=tkey&password=密码&mobile=手机号码&content=内容&dstime=&productid=产品ID&xh=留空
* @author henryfang1@163.com   
* @date 2012-9-9 下午03:51:34 
*  
*/ 
public class ZtSmsPortalImpl implements SmsPortal {
	/**
	 * 日志跟踪器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ZtSmsPortalImpl.class);	
	
	/**用户名*/
	private static String username;
	/**密码*/
	private static String password;
	/**产品ID*/
	private Integer productid;
	/**扩展的小号,必须为数字，没有请留空*/
	private static String xh;
	/**发送请求*/
	private static String sendurl;
	/**当前时间（必填,24小时制），格式：yyyyMMddHHmmss*/
	private static String tkey;

	/**验证码*/
	private static final int PRODUCT_ID_AUTH_CODE = 676767;
	/**通知*/
	private static final int PRODUCT_ID_NOTICE = 676766;
	
	private static final String AUTH_CODE_KEYWORDS = "验证码";
	
	/**
	 *  创建一个新的实例 SMSSender,助通科技提供的用户名与地址
	 *  username	用户名（必填）
	 * 	password	密码（必填）
	 * 	mobile	手机号，多个手机号为用半角 , 分开，如13899999999,13688888888(最多100个，必填)
	 * 	content	发送内容（必填）
	 * 	dstime	定时时间，为空时表示立即发送（选填）
	 * 	productid	产品id(必填)
	 * 	xh	扩展号,留空
	 * */
	public ZtSmsPortalImpl() {
		final ConfigService configService = SpringContextHolder.getBean("configService");
	    sendurl = configService.findByCode("sms_notice").getConfigValue(); //助通提供的发送地址
	    username = configService.findByCode("sms_username").getConfigValue();//您的用户名
	  	password = configService.findByCode("sms_password").getConfigValue();////密码
	  	tkey = DateUtils.dateStr3(DateUtils.getNow());            //当前时间
         		
	  	//20160331 助通账号密码使用MD32加密传输，解决安全问题
 		password=getMD5(password);
 		password = getMD5(password+tkey);
		productid = PRODUCT_ID_NOTICE; //产品ID号，默认通知消息
		xh = ""; //扩展号留空		
	}

	/**
	 * 发送短信
	 * @param mobile
	 * @param content
	 * @return
	 */
	public String sendSms(final String mobile, final String content) {
		String sendUrl = null;
		if(StringUtils.isBlank(content)) {
			return "短信内容不能为空";
		}
		if(content.contains(AUTH_CODE_KEYWORDS)){
			productid = PRODUCT_ID_AUTH_CODE;
		}
		try {// 否则发到手机乱码
			sendUrl = sendurl + "?username=" + username + "&tkey="+tkey + "&password="+ password 
			+ "&mobile=" + mobile + "&content="+URLEncoder.encode(content, "UTF-8")
			+ "&productid=" + productid + "&xh="+xh;
		} catch (UnsupportedEncodingException uee) {
			LOGGER.error(uee.getMessage(), uee);
		}

		LOGGER.info("短信内容为------------->:{}" , content);
		return getUrl(sendUrl);
	}

	/**
	 * MD5加密
	 * @param sourceStr
	 * @return
	 */
	 private String getMD5(final String sourceStr) {
	        String result = "";
	        try {
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(sourceStr.getBytes());
	            byte[] b = md.digest();
	            int i;
	            StringBuilder buf = new StringBuilder();
	            for (int offset = 0; offset < b.length; offset++) {
	                i = b[offset];
	                if (i < 0)
	                    i += 256;
	                if (i < 16)
	                    buf.append("0");
	                buf.append(Integer.toHexString(i));
	            }
	            result = buf.toString();
	        } catch (NoSuchAlgorithmException e) {
        		LOGGER.error("短信密码MD5失败：{}" ,e.getMessage(), e);
	        }
	        return result;
	    }
	
	/** 
	 * 获取地址
	 * @param urlString
	 * @return    
	 */ 
	public String getUrl(final String urlString) {
		final StringBuilder buffer = new StringBuilder();
		try {
			final URL url = new URL(urlString);
			final URLConnection conn = url.openConnection();
			conn.setReadTimeout(15000);
			final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			for (String line ; (line = reader.readLine()) != null;) {
				buffer.append(line + "\n");
			}
			reader.close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	 
		return	 EncodeUtils.urlDecode(buffer.toString());
	}
	
	/**
	 * 根据返回码，获取结果内容
	 * @param response
	 * @return
	 */
	public static String getResultName(final String response){
		String result = "";
		if(StringUtils.isBlank(response))
			return result;
		final int resCode = Integer.parseInt(response.split(",")[0]);
		switch (resCode) {
		case -1:
			result = "用户名或者密码不正确或用户禁用或者是管理账户";
			break;
		case 1:
			result = "发送短信成功";
			break;
		case 0:
			result = "发送短信失败:"+response.split(",")[1];
			break;
		case 2:
			result = "余额不够或扣费错误";
			break;
		case 3:
			result = "扣费失败异常（请联系客服）";
			break;
		case 5:
			result = "短信定时成功";
			break;
		case 6:
			result = "有效号码为空";
			break;
		case 7:
			result = "短信内容为空";
			break;
		case 8:
			result = "无签名";
			break;
		case 9:
			result = "没有Url提交权限";
			break;
		case 10:
			result = "发送号码过多,最多支持2000个号码";
			break;
		case 11:
			result = "产品ID异常或产品禁用";
			break;
		case 12:
			result = "参数异常";
			break;
		case 15:
			result = "Ip验证失败";
			break;
		case 19:
			result = "短信内容过长，最多支持500个,或提交编码异常导致";
			break;
		default:
			break;
		}		
		return result;
	}
	
	/**
	 * 获得短信通道名称
	 */
	@Override
	public String getSPName() {
		return "助通科技";
	}

	/**
	 * 发送短信
	 */
	@Override
	public String send(final String phone, final String content) {
		return sendSms(phone, content);
	}

	/**
	 * 获得用户信息
	 */
	@Override
	public Map<String, Integer> getUseInfo() {
		return null;
	}

	@Override
	public String send(String phone, String content, String templateCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReturnMessage(String code) {
		// TODO Auto-generated method stub
		return null;
	}

}
