/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.job;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.account.service.CashService;
import com.rd.ifaes.core.account.service.RechargeService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.operate.service.UserGiftGrantService;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.operate.service.UserVipService;
import com.rd.ifaes.core.project.service.ProjectInvestBespeakService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeRepaymentService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.statistic.service.StatisticInvestService;
import com.rd.ifaes.core.statistic.service.StatisticService;
import com.rd.ifaes.core.sys.service.OperatorService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.service.UserEarnLogService;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 基于quartz实现的定时任务
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年8月15日
 */
@Component
@Lazy(value = false)
public class QuartzJob {
	private static final Logger LOGGER = LoggerFactory.getLogger(QuartzJob.class);
	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserInvestAutoLogService userInvestAutoLogService;
	@Resource
	private UserEarnLogService userEarnLogService;
	@Resource
	private CashService cashService;
	@Resource
	private RechargeService rechargeService;
	@Resource
	private RealizeService realizeService;
	@Resource
	private BondService bondService;
	@Resource
	private BondInvestService bondInvestService;
	@Resource
	private UserRedenvelopeService userRedenvelopeService;
	@Resource
	private UserRateCouponService userRateCouponService;
	@Resource
	private ProjectInvestBespeakService projectInvestBespeakService;
	@Resource
	private ProjectRepaymentService projectRepaymentService;
	@Resource
	private RealizeRepaymentService realizeRepaymentService;
	@Resource
	private UserVipService userVipService;
	@Resource
	private UserService userService;
	@Resource
	private StatisticService statisticService;
	@Resource
	private StatisticInvestService statisticInvestService;
	@Resource
	private TppTradeService tppTradeService;
	@Resource
	private UserGiftGrantService userGiftGrantService;
	@Resource
	private OperatorService operatorService;
	/**
	 * 3天内还款信息提醒处理
	 */
//	@Scheduled(cron = "0 30 10 * * ?")
	public void repaymentTimeoutHandle() {
		LOGGER.info("repaymentTimeoutHandle()  3天内还款信息处理  BEGIN........");

		projectRepaymentService.repaymentTimeoutHandle();

		LOGGER.info("repaymentTimeoutHandle() 3天内还款信息处理   END........");
	}
	

	/**
	 * 投资订单超时处理(订单状态从待支付=》超时取消） 时间间隔(5分钟）
	 */
//	@Scheduled(cron = "0 0/5 * * * ?")
	public void investTimeoutHandle() {
		LOGGER.info("investTimeoutHandle()  投资订单超时处理  BEGIN........");

		projectInvestService.investTimeoutHandle();

		LOGGER.info("investTimeoutHandle() 投资订单超时处理   END........");
	}

	/**
	 * 债权投资订单超时处理(订单状态从待支付=》超时取消） 时间间隔(5分钟）
	 */
//	@Scheduled(cron = "0 0/5 * * * ?")
	public void bondInvestOverTimeHandle() {
		LOGGER.info("bondInvestOverTimeHandle() 债权投资订单超时处理   BEGIN........");

		bondInvestService.bondInvestOverTimeHandle();

		LOGGER.info("bondInvestOverTimeHandle() 债权投资订单超时处理   END........");
	}
	
	/**
	 * 每隔30分钟  搜索前30分钟  已经成立审核的产品和借贷 进行生成协议处理
	 * @author QianPengZhan
	 * @date 2016年11月4日
	 */
//	@Scheduled(cron = "0 0/30 * * * ?")
	public void projectCreateProtocol() {
		LOGGER.info("projectCreateProtocol() 每隔30分钟  搜索前30分钟  已经成立审核的产品和借贷 进行生成协议处理   BEGIN........");

		projectService.projectCreateProtocol();

		LOGGER.info("projectCreateProtocol() 每隔30分钟  搜索前30分钟  已经成立审核的产品和借贷 进行生成协议处理   END........");
	}
	
	/**
	 * 项目自动下架
	 * 
	 * @author FangJun
	 * @date 2016年8月31日摁
	 */
//	@Scheduled(cron = "0/10 * * * * ?")
	public void autoStopSaleProject() {
		LOGGER.info("autoStopSaleProject   开始执行........");
	 
		 int updateNum = projectService.autoStopSale();

	     LOGGER.info("autoStopSaleProject   执行结束.......自动下架条数：{}",updateNum);
	}

	/**
	 * 债权剩余期限每日更新 每日0点10分更新 0 10 0 * * ?
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月17日
	 */
//	@Scheduled(cron = "0 10 0 * * ?")
	public void bondRemainDaysChangeByDay() {
		LOGGER.info("bondRemainDaysChangeByDay() 债权剩余期限每日更新    开始执行........");
		
		bondService.bondRemainDaysChangeByDay();

		LOGGER.info("bondRemainDaysChangeByDay() 债权剩余期限每日更新     执行结束........");
	}
	
	/**
	 * 由于剩余期限的利息每天变化导致最高折溢价率的改变 所以债权的折溢价率等于最高折溢价率的标 需要更新掉他的债权折溢价率
	 * @author QianPengZhan
	 * @date 2016年11月4日    30分钟执行一次
	 */
//	@Scheduled(cron = "* 0/30 * * * ?")
	public void bondMaxBondAprChangeByDay() {
		LOGGER.info("bondMaxBondAprChangeByDay() 由于剩余期限的利息每天变化导致最高折溢价率的改变 所以债权的折溢价率等于最高折溢价率的标 需要更新掉他的债权折溢价率    开始执行........");
		
		bondService.bondMaxBondAprChangeByDay();

		LOGGER.info("bondMaxBondAprChangeByDay() 由于剩余期限的利息每天变化导致最高折溢价率的改变 所以债权的折溢价率等于最高折溢价率的标 需要更新掉他的债权折溢价率     执行结束........");
	}
	
	/**
	 * 债权标超过有效期和还款日 自动撤回 30分钟执行一次 0 0/30 * * * ?
	 * 
	 * @author QianPengZhan
	 * @date 2016年8月18日
	 */
//	@Scheduled(cron = "* 0/30 * * * ?")
	public void autoCancleBond() {
		LOGGER.info("autoCancleBond() 债权标超过有效期和还款日  开始执行........");

		bondService.autoCancleBond();

		LOGGER.info("autoCancleBond()  债权标超过有效期和还款日  执行结束........");
	}

	/**
	 * 变现到期处理
	 * 
	 * @author fxl
	 * @date 2016年8月23日
	 */
	// 每30分钟触发一次
//	@Scheduled(cron = "0 0/30 * * * ?")
//	public void autoDealRealize() {
//		LOGGER.info("变现到期处理开始执行........");
//		
//		realizeService.autoDealRealize();
//
//		LOGGER.info("变现到期处理执行结束........");
//	}

	/**
	 * 变现自动还款
	 * 
	 * @author fxl
	 * @date 2016年8月31日
	 */
//	@Scheduled(cron = "0 */30 * * * ?")
//	public void autoRealizeRepay() {
//		LOGGER.info("变现自动还款开始执行........");
//		realizeRepaymentService.autoRepay();
//		LOGGER.info("变现自动还款执行结束........");
//	}

	/**
	 * 产品自动还款
	 * 
	 * @author fangjun
	 * @date 2016年8月31日
	 */
//	@Scheduled(cron = "0 0 4 * * ?")
//	@Scheduled(cron = "0 */10 * * * ?")
//	public void autoRepay() {
//		LOGGER.info("产品自动还款---autoRepay START........");
//		projectRepaymentService.autoRepay();
//		LOGGER.info("产品自动还款--- autoRepay END........");
//	}

	/**
	 * 
	 * 每5分钟 针对倒计时产品执行自动投标使用
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月18日
	 */
//	@Scheduled(cron = "0 */5 * * * ?")
	public void doAutoInvest() {

		LOGGER.info("doAutoInvest() begin........");
		userInvestAutoLogService.jobAutoInvest();
		LOGGER.info("doAutoInvest() end........");
	}

	/**
	 * 统计昨日收益
	 */
//	@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点整
	public void calcuYesterdayEarn() {
		LOGGER.info("开始统计昨日收益,统计时间：{}", DateUtils.getDateTime());
		userEarnLogService.calcuYesterdayEarn();
		LOGGER.info("开始统计昨日收益 end........");
	}

	/**
	 * 每隔30分钟处理一次提现失效订单
	 */
	/*@Scheduled(cron = "0 0/30 * * * ?")
	public void setAccountCash() {
		LOGGER.info("修改提现状态 begin,统计时间：{}", DateUtils.getDateTime());
		cashService.updateOverTimeOrderStatus();
		LOGGER.info("修改提现状态 end........");
	}*/

	/**
	 * 修改充值状态
	 */
//	@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点整
	public void setAccountRecharge() {
		LOGGER.info("修改充值状态 begin,统计时间：{}", DateUtils.getDateTime());
		rechargeService.updateOverTimeOrderStatus();
		LOGGER.info("修改充值状态 end........");
	}

	/**
	 * 每5分钟 红包加息券过期处理
	 * 
	 * @author wyw
	 * @date 2016年8月25日
	 */
//	@Scheduled(cron = "0 */5 * * * ?")
	public void doCouponExpiredHandle() {
		LOGGER.info("doCouponExpiredHandle() 红包加息券过期处理 开始执行........");
		
		userRedenvelopeService.expiredHandle();// 红包过期处理
		userRateCouponService.expiredHandle();
		// 加息券过期处理

		LOGGER.info("doCouponExpiredHandle() 红包加息券过期处理 执行结束........");
	}

	/**
	 * 每5分钟 投资预约提醒
	 * 
	 * @author wyw
	 * @date 2016年8月24日
	 */
//	@Scheduled(cron = "0 */5 * * * ?")
	public void doInvestBespeakRemind() {
		LOGGER.info("doInvestBespeakRemind()  投资预约提醒 开始执行........");
		
		projectInvestBespeakService.investRemind();

		LOGGER.info("doCouponExpiredHandle()  投资预约提醒 执行结束........");
	}

	/**
	 * 每天凌晨2点整 逾期利息处理
	 * 
	 * @author wyw
	 * @date 2016年8月25日
	 */
//	@Scheduled(cron = "0 0/10 * * * ?") // 测试需要暂时修改 FangJun 20160912 (cron = "0 0 2 * * ?")
	public void overdueInterestHandle() {
		LOGGER.info("overdueInterestHandle()  逾期利息 开始执行........");
		
		projectRepaymentService.overdueInterestHandle();

		LOGGER.info("overdueInterestHandle()  逾期利息 执行结束........");
	}

	/**
	 * 自动清除项目缓存
	 */
//	@Scheduled(cron = "0/10 * * * * ?")
	public void autoRemoveProjectCache() {
		LOGGER.info("autoClearProjectCache() 自动清除项目缓存 开始执行........");

		projectService.autoRemoveProjectCache(ExpireTime.TEN_SEC.getTime());

		LOGGER.info("autoClearProjectCache() 自动清除项目缓存  执行结束........");
	}

	/**
	 * 处理过期VIP专享礼包(每天0点10份)
	 */
	// @Scheduled(cron = "10 0 0 * * ?")
//	@Scheduled(cron = "0 0 */1 * * ?")
	public void autoDealExclusive() {
		LOGGER.info("处理过期VIP专享礼包 begin...........");
		
		userGiftGrantService.doOverDueExclusiveGift();

		LOGGER.info("处理过期VIP专享礼包 end................");
	}

	/**
	 * 处理生日礼包(每个月第一天)
	 */
//	@Scheduled(cron = "10 0 0 01 * ?")
	public void autoDealBirthday() {
		LOGGER.info("处理生日礼包 begin..........");
		
		userGiftGrantService.doOverDueBirthGift();

		LOGGER.info("处理生日礼包 end......");
	}
	
	/**
	 * 扣除VIP成长值
	 */
//	@Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1点整
	public void deductionVipGrowthValue() {
		LOGGER.info("扣除VIP成长值 begin,统计时间：{}", DateUtils.getDateTime());
		userVipService.deductionVipGrowthValue();
		LOGGER.info("扣除VIP成长值 end........");
	}

	/**
	 * 每天下午17点30扫描昨天17点30~今天17点30 所有有资金操作的用户的本地和第三方资金的对比 找出差异的用户 并发短信通知运维人员 0 30
	 * 17 * * ?
	 * 
	 * @author QianPengZhan
	 * @date 2016年10月11日
	 */
//	@Scheduled(cron = "0 30 18 * * ?")
	public void scannerAccountCompareError() {

		LOGGER.info("scannerAccountCompareError()  本地和第三方资金的对比  begin........");

		userService.scannerAccountCompareError();

		LOGGER.info("scannerAccountCompareError()  本地和第三方资金的对比  end........");
	}
	
	/**
	 * 每10分钟 进行一次解冻操作
	 * 
	 * @author wyw
	 * @date 2016年8月24日
	 */
//	@Scheduled(cron = "0 */10 * * * ?")
	public void doUnFreezeAccount() {
		LOGGER.info("doUnFreezeAccount() 自动解冻后台用户 开始执行........");
		operatorService.unFreezeAccountByTimer();
		LOGGER.info("doUnFreezeAccount() 自动解冻后台用户 执行结束........");
		
		LOGGER.info("doUnFreezeAccount() 自动解冻用户 开始执行........");
		userService.unFreezeAccountByTimer();
		LOGGER.info("doUnFreezeAccount() 自动解冻用户 执行结束........");
	}

	/**
	 * 执行统计(凌晨1点)
	 * @author fxl
	 * @date 2017年2月22日
	 */
//	@Scheduled(cron = "0 0 1 * * ?")
	public void doStatistic() {
		statisticService.doStatistic();
	}
	
	/**
	 * 每20分  进行一次提现查询操作
	 * 
	 * @author wyw
	 * @date 2016年8月24日
	 */
//	@Scheduled(cron = "0 */30 * * * ?")
	public void queryCbhbCashStatus(){
		LOGGER.info("queryJxBankCashStatus() 提现查询操作开始执行........");
		cashService.queryCashStatus();
		LOGGER.info("queryJxBankCashStatus() 提现查询操作 执行结束........");
	}
	
//	/**
//	 * 充值的查询交易状态
//	 */
//	@Scheduled(cron = "0 30 18 * * ?")
//	public void queryCbhbRechargeStatus(){
//		LOGGER.info("queryCbhbRechargeStatus() 充值的查询交易状态操作开始执行........");
//		rechargeService.queryCbhbRechargeStatus();
//		LOGGER.info("queryCbhbRechargeStatus() 充值的查询交易状态操作 执行结束........");
//	}
}
