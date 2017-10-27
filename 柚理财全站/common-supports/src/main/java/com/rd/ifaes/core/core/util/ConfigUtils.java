package com.rd.ifaes.core.core.util;

import java.math.BigDecimal;

import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.sys.domain.Config;
import com.rd.ifaes.core.sys.service.ConfigService;
/**
 * 系统参数配置查询工具类
 * @version 3.0
 * @author FangJun
 * @date 2016年8月11日
 */
public class ConfigUtils {
	/**
	 * 系统参数查询服务
	 */
	private static ConfigService configService = SpringContextHolder.getBean("configService");
	
    /**
     * 根据系统参数编码查询配置值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @return 参数配置值
     */
	public static String getValue(String code){
		String result = "";
		if(StringUtils.isNotBlank(code)){			
			Config config = configService.findByCode(code);
			if(null!=config && Constant.FLAG_YES.equals(config.getStatus())){
				result = config.getConfigValue();
			}
		}
		return result;
	}
  /**
     * 根据系统参数编码查询配置的整数值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @return 参数配置的整数值
     */
	public static int getInt(String key) {
		return StringUtils.toInt(getValue(key));
	}
	  /**
     * 根据系统参数编码查询配置的整数值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @param defaultValue 系统设置值为空时的默认值
     * @return 参数配置的整数值
     */
	public static int getInt(String key,int defaultValue) {
		String val=getValue(key);
		if(StringUtils.isBlank(val)){
			return defaultValue;
		}
		return StringUtils.toInt(val);
	}
	
	  /**
     * 根据系统参数编码查询配置的浮点型值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @return 参数配置的浮点型值
     */
	public static double getDouble(String key) {
		return  StringUtils.toDouble(getValue(key));
	}
	
   /**
     * 根据系统参数编码查询配置的浮点型值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @param defaultValue 系统设置值为空时的默认值
     * @return 参数配置的浮点型值
     */
	public static double getDouble(String key,double defaultValue) {
		String val=getValue(key);
		if(StringUtils.isBlank(val)){
			return defaultValue;
		}
		return  StringUtils.toDouble(getValue(key));
	}
	
	/**
     * 根据系统参数编码查询配置的大整数值
     * @author  FangJun
     * @date 2016年8月11日
     * @param code 系统参数编码
     * @return 参数配置的大整数值
     */
	public static BigDecimal getBigDecimal(String key) {
		return BigDecimalUtils.round(getValue(key),Constant.INT_FOUR);
	}
	
	/**
	 * 根据系统参数编码查询配置的数值
	 * @author fxl
	 * @date 2016年10月18日
	 * @param key 系统参数编码
	 * @param scale 小数点后保留几位
	 * @return
	 */
	public static BigDecimal getBigDecimal(String key, int scale) {
		return BigDecimalUtils.round(getValue(key),scale);
	}
	
	/**
	 *  是否开启队列服务
	 * @author  FangJun
	 * @date 2016年10月21日
	 * @return 是否开启队列服务
	 */
	public static  boolean isOpenMq(){
		return CommonConstants.YES.equals(ConfigUtils.getValue(ConfigConstant.OPEN_RABBIT_MQ));
	}
	
	/**
	 * 获取当前系统使用三方支付标识
	 * @author  FangJun
	 * @date 2016年11月8日
	 * @return 三方支付标识
	 */
	public static String getTppName(){
		return ConfigUtils.getValue(ConfigConstant.TPP_NAME);
	}
	
	/**
	 *  是否支持即息理财
	 * @author  FangJun
	 * @date 2016年11月8日
	 * @return  是否支持
	 */
	public static boolean supportInterestFinancing(){
		return	CommonConstants.YES.equals(ConfigUtils.getValue(ConfigConstant.SUPPORT_INTEREST_FINANCING));
	}
	
	/**
	 *  投资/成立审核之后发放首投奖励
	 * @author  zb
	 * @date 2016年11月8日
	 * @return  true 成立审核之后发放 false 投资之后发放
	 */
	public static boolean firstInvestRewardInVerify(){
		return	CommonConstants.YES.equals(ConfigUtils.getValue(ConfigConstant.OPEN_FIRST_INVEST));
	}
	
}
