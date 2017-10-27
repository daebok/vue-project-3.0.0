package com.rd.ifaes.core.core.sign;

import java.util.Map;

/**
 * 
 *  第三方签署接口类
 * @version 3.0
 * @author jxx
 * @date 2016年8月1日
 */
public interface SignPortal {
	
	/**
	 * 
	 * 开户
	 * @author jxx
	 * @date 2016年7月28日
	 */
	void prepare();
	
	/**
	 * 
	 * 开户
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String register(Map<String,Object> map);
	
	/**
	 * 
	 * 模板生成电子印章数据
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String createSeal(Map<String,Object> map);
	

	/**
	 * 
	 * 图片生成电子印章数据
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String drawSeal(Map<String,Object> map);
	
	/**
	 * 
	 * 发送短信
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String tokenSendCode(Map<String,Object> map);
	
	/**
	 * 
	 * 短信验证
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	String tokenCheckCode(Map<String,Object> map);
	
	/**
	 * 
	 * 签署业务处理
	 * @author jxx
	 * @date 2016年7月28日
	 * @param map
	 */
	void execute(Map<String,Object> map);
}
