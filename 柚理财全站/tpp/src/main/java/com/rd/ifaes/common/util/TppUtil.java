/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util;

import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.tpp.service.TppService;

/**
 * tpp工具类
 * @version 3.0
 * @author QianPengZhan
 * @date 2017年2月22日
 */
public class TppUtil {
	
	/**
	 * 调用不同的service
	 * @author QianPengZhan
	 * @date 2017年2月22日
	 * @return
	 */
	public static Object tppService(){
//		TppService tppService = null;
//		String tppName = ConfigUtils.getTppName();
//		switch (TppServiceEnum.nameOf(tppName)) {
//			case CHINAPNR:
//				tppService = SpringContextHolder.getBean("tppUfxServiceImpl");
//				break;
//			case CZBANK:
//			case HSBANK:
//			case CIB:
//			case UMP:
//			case MMM:
//			case CBHB:
//				tppService = SpringContextHolder.getBean("tppCbhbServiceImpl");
//				break;
//			case JXBANK:
//				tppService = SpringContextHolder.getBean("tppJxbankServiceImpl");
//				break;
//		}
		return SpringContextHolder.getBean("tppJxbankServiceImpl");
	}
	
	/**
	 * 第三方支付接口标识
	 * @return
	 */
	public static String getTppName(){
		return ConfigUtils.getTppName();
	}
		
	/**
	 * 商户号
	 * @return
	 */
	public static String getTppCustomerId(){
		return ConfigUtils.getValue(ConfigConstant.UFX_CUSTOMERID);
	}
	
}
