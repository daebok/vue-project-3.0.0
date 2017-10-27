package com.rd.ifaes.core.core.sms;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.rd.ifaes.common.util.SessionUtils;

import reactor.core.support.Assert;

/**
 * 
 *  短信验证码工具类
 * @version 3.0
 * @author xxb
 * @date 2016年8月9日
 */
public class VerifCodeForSMSUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifCodeForSMSUtils.class);

	private static final String CODE = "0123456789"; //随机生成一个0-9之间的整数
	private static final int LEN = 6;
	
	/**
	 * 
	 *  将生成的随机数拼成一个六位数验证码
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年8月9日
	 */
	public static String buildVerifCode(){
		StringBuffer verifCode = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < LEN; i++) {
			verifCode.append(CODE.charAt(random.nextInt(CODE.length())));
		}
		return verifCode.toString(); 
	}
	
	/**
	 * 
	 * 生成验证码
	 * @param  
	 * @return String
	 * @author xxb
	 * @date 2016年8月9日
	 */
	public static String buildVerifCode(String functionType, String addr){
		Assert.notNull(functionType);
		String verifCode = buildVerifCode();
		SessionUtils.setSessionAttr(functionType + addr, verifCode);
		LOGGER.debug("functionType:{} --> addr:{}--> 验证码：{}" ,functionType, addr, verifCode);
		return verifCode;
	}
	
	/**
	 * 
	 *  核实验证码
	 * @param  
	 * @return boolean
	 * @author xxb
	 * @date 2016年8月9日
	 */
	public static boolean checkVerifCode(String functionType, String addr, String codeForCheck){
		Assert.notNull(functionType);
		Assert.notNull(codeForCheck);
		String verifCode = (String)SessionUtils.getSessionAttr(functionType + addr);
		boolean result = StringUtils.equals(codeForCheck, verifCode);
		if(result){
			SessionUtils.removeAttribute(functionType + addr);
		}
		return result;
	}
	
}
