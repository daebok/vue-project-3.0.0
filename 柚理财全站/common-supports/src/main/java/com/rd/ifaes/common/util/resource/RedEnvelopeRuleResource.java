/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 *  红包规则资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月23日
 */
public class RedEnvelopeRuleResource {
	/**
	 * 发放开始时间不能小于当前时间!
	 */
	public static final String REDENVELOPERULE_STARTTIME_ERROR = "redEnvelopeRule.message.startTime.error";
	/**
	 * 发放结束时间不能小于当前时间!
	 */
	public static final String REDENVELOPERULE_ENDTIME_ERROR = "redEnvelopeRule.message.endTime.error";
	/**
	 * 发放结束时间不能小于发放开始时间!
	 */
	public static final String REDENVELOPERULE_STARTTIME_ENDTIME_ERROR = "redEnvelopeRule.message.startTime.endTime.error";
	/**
	 * 加息利率不争取
	 */
	public static final String REDENVELOPERULE_STARTTIME_UPRATE_ERROR = "redEnvelopeRule.message.uprate.error";
}
