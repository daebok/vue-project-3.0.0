package com.rd.ifaes.core.cache;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.dict.CacheKeys;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ProjectConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * 投资缓存处理
 * 
 * @author lh
 * @verison 3.0
 * @since 2016-7-19
 */
public class InvestCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(InvestCache.class);

	/**
	 * 校验是否超投
	 * 
	 * @param projectNo 项目编码
	 * @param amount 投资金额
	 * @return
	 */
	public static double checkOverInvest(String projectNo, double amount) {
		double  currentProjectAmount=Constant.DOUBLE_ZERO;
		String key = String.format(ProjectConstant.KEY_PROJECT_REMAIN_ACCOUNT, projectNo);

		if ((currentProjectAmount=CacheUtils.decr(key, amount)) < 0) { // 超投
			CacheUtils.incr(key, amount);
			LOGGER.error("---overInvest---projectNo: {},amount: {}" ,projectNo, amount);
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ACCOUNT_NOT_ENOUGH));
		}
		return  currentProjectAmount;
	}
	

	/**
	 * 检查用户未支付投资订单数是否超额 
	 * 
	 * @author FangJun
	 * @date 2016年8月14日
	 * @param userCustId 用户资金存管平台账户(uid)
	 * @return 未超额 true,超额 false
	 */
	public static boolean checkUserInvestUnpay(String userCustId) {
		if (StringUtils.isBlank(userCustId)) {
			return false;
		}
		int unpayMax = ConfigUtils.getInt(ConfigConstant.INVEST_UNPAY_MAX);
		if (unpayMax < Constant.INT_ONE) {
			unpayMax = Constant.INT_ONE;
		}
		String key = String.format(CacheKeys.PREFIX_USER_INVEST_UNPAY_NUM.getKey(), userCustId,DateUtils.dateStr7(new Date()));
		int currentNum=CacheUtils.getInt(key);
		if ( currentNum>= unpayMax) {
			return false;
		} 
		return true;
	}
	
	/**
	 *  增加用户未支付投资订单数,未超额会+1处理
	 * 
	 * @author FangJun
	 * @date 2016年8月14日
	 * @param userCustId 用户资金存管平台账户(uid)
	 * @return 未超额 true,超额 false
	 */
	public static boolean incrUserInvestUnpay(String userCustId) {
		if (StringUtils.isBlank(userCustId)) {
			return false;
		}
		int unpayMax = ConfigUtils.getInt(ConfigConstant.INVEST_UNPAY_MAX);
		if (unpayMax < Constant.INT_ONE) {
			unpayMax = Constant.INT_ONE;
		}
		String key = String.format(CacheKeys.PREFIX_USER_INVEST_UNPAY_NUM.getKey(), userCustId,DateUtils.dateStr7(new Date()));
		LOGGER.info("用户：{},unpayMax：{},key：{}",userCustId,unpayMax,key);
		double currentNum=CacheUtils.incr(key, Constant.DOUBLE_ONE);
		 if( currentNum <= Constant.DOUBLE_ONE){
			 CacheUtils.expire(key, ExpireTime.ONE_DAY);
		 }
		 LOGGER.info("(增加用户未支付投资订单数,未超额会+1处理)用户：{},unpayMax：{},key：{},currentNum:{}",userCustId,unpayMax,key,currentNum);
		if ( currentNum> unpayMax) {
			CacheUtils.decr(key, Constant.DOUBLE_ONE);
			LOGGER.error("---Invest Unpay Too Much ---userId: {}" , userCustId);
			return false;
		} 
		
		return true;
	}
    /**
     * 支付成功，减少用户未支付投资订单数
     * @author  FangJun
     * @date 2016年8月14日
     * @param userCustId 用户资金存管平台账户(uid)
     * @return true成功，false更新失败
     */
	public static boolean decrUserInvestUnpay(String userCustId) {
		if (StringUtils.isBlank(userCustId)) {
			return false;
		}
		String key = String.format(CacheKeys.PREFIX_USER_INVEST_UNPAY_NUM.getKey(), userCustId,DateUtils.dateStr7(new Date()));
		if (CacheUtils.decr(key, Constant.DOUBLE_ONE) < Constant.DOUBLE_ZERO) {
			CacheUtils.incr(key, Constant.DOUBLE_ONE);
			LOGGER.error("---Invest Unpay Num decr fail ---userCustId: {}" , userCustId);
			return false;
		}
		return true;
	}
}
