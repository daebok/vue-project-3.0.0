package com.rd.ifaes.core.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.redisson.RedissonDistributeLock;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.LockConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.domain.UserRateCoupon;
import com.rd.ifaes.core.operate.domain.UserRedenvelope;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.operate.service.UserRedenvelopeService;
import com.rd.ifaes.core.project.domain.Borrow;
import com.rd.ifaes.core.project.domain.Product;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.mapper.ProjectInvestMapper;
import com.rd.ifaes.core.project.service.BorrowService;
import com.rd.ifaes.core.project.service.ProductService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.risk.service.RiskUserLogService;
import com.rd.ifaes.core.tpp.service.tpp.JxbankService;
import com.rd.ifaes.core.tpp.service.tpp.UfxInvestService;
import com.rd.ifaes.core.user.domain.AutoInvestRuleLog;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserFreeze;
import com.rd.ifaes.core.user.domain.UserInvestAutoLog;
import com.rd.ifaes.core.user.mapper.AutoInvestRuleLogMapper;
import com.rd.ifaes.core.user.mapper.UserAutoInvestMapper;
import com.rd.ifaes.core.user.mapper.UserCacheMapper;
import com.rd.ifaes.core.user.mapper.UserFreezeMapper;
import com.rd.ifaes.core.user.mapper.UserInvestAutoLogMapper;
import com.rd.ifaes.core.user.mapper.UserMapper;
import com.rd.ifaes.core.user.model.AutoInvestRuleLogModel;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;

/**
 * ServiceImpl:UserInvestAutoLogServiceImpl
 * @author zb
 * @version 3.0
 * @date 2016-7-25
 */
@Service("userInvestAutoLogService") 
public class UserInvestAutoLogServiceImpl  extends BaseServiceImpl<UserInvestAutoLogMapper, UserInvestAutoLog> implements UserInvestAutoLogService{

	private static final Logger LOGGER = LoggerFactory.getLogger(UserInvestAutoLogServiceImpl.class);
	
    @Resource
    private transient UserAutoInvestMapper userAutoInvestMapper;
    @Resource
    private transient UserMapper userMapper;
    @Resource
    private transient UserFreezeMapper userFreezeMapper;
    @Resource
    private transient UserCacheMapper userCacheMapper;
    @Resource
    private transient AutoInvestRuleLogMapper autoInvestRuleLogMapper;
    @Resource
    private transient ProjectInvestMapper projectInvestMapper;
    @Resource
    private transient AccountService accountService;
    @Resource
	private transient ProjectService projectService;
    @Resource
   	private transient BorrowService borrowService;
    @Resource
   	private transient ProductService productService;
    @Resource
   	private transient UfxInvestService ufxInvestService;
    @Resource
	private RiskUserLogService riskUserLogService;
    @Resource
   	private transient JxbankService jxbankService;
	@Resource
	private transient UserRedenvelopeService userRedenvelopeService;
	@Resource
	private transient UserRateCouponService userRateCouponService;
    
    /**
     * 自动投资业务处理
     * 
     * @author ZhangBiao
     * @date 2016年9月13日
     * @param base
     */
	private void doAutoInvest(final Project base) {
		LOGGER.info("进入自动投资处理，产品编号：{}",base.getUuid());
		try {
			//针对同一项目加锁处理
			RedissonDistributeLock.lock(String.format(LockConstant.LOCK_AUTO_INVEST,base.getUuid()));
			// 标识产品按份销售
			boolean flag = LoanEnum.SALE_STYLE_COPY.getValue().equals(base.getSaleStyle());
		
			// 最大自动投资额度
			final BigDecimal autoInvestMaxApr = ConfigUtils.getBigDecimal(ConfigConstant.AUTO_INVEST_MAX_APR);
			BigDecimal autoAmountMax = BigDecimalUtils.mul(autoInvestMaxApr,base.getAccount());
			if(flag){
				final int autoTotalCopies = (int) (autoInvestMaxApr.doubleValue()*base.getTotalCopies());
				autoAmountMax = BigDecimalUtils.mul(base.getCopyAccount(),BigDecimal.valueOf(autoTotalCopies));
			}
			// 自动投资剩余可投
			ProjectCache.setRemainAccount(base.getUuid()+CacheConstant.KEY_AUTOINVEST_REMAINACCOUNT, autoAmountMax.doubleValue());
			final List<UserInvestAutoLog> autoTenderLogList = new ArrayList<>();
			// 查询并对复核条件的自动投资用户排序
			final AutoInvestRuleLogModel mdoel = getCheckModel(base);
			final String autoInvestVipFlag = ConfigUtils.getValue(ConfigConstant.AUTO_INVEST_VIP_FLAG);
			mdoel.setUserVipStatus(autoInvestVipFlag);
			final List<UserAutoInvest> userAutoInvestList = userAutoInvestMapper.findSortList(mdoel);
			BigDecimal money;
			for(final UserAutoInvest autoInvest : userAutoInvestList){
				autoAmountMax =  BigDecimal.valueOf(ProjectCache.getRemainAccount(base.getUuid()+CacheConstant.KEY_AUTOINVEST_REMAINACCOUNT));
				// 产品剩余可投
				final BigDecimal remainAccount = BigDecimal.valueOf(ProjectCache.getRemainAccount(base.getUuid()));
				autoAmountMax = autoAmountMax.compareTo(remainAccount) > Constant.INT_ZERO ? remainAccount:autoAmountMax;//取项目剩余可投和项目自动投资剩余金额两者的最小值
				
				// 自动投资金额
				money = BigDecimal.ZERO;
				// 产品最小投资金额
				final BigDecimal lowestAccount = base.getLowestAccount();
				if(autoAmountMax == BigDecimal.ZERO || autoAmountMax.compareTo(lowestAccount) < Constant.INT_ZERO){
					break;
				}
				
				// 投资者
				final String userId = autoInvest.getUserId();
				final User user = userMapper.get(userId);
				// 自动投资资格判断
				final UserCache userCache = userCacheMapper.findByUserId(userId);
				if(checkAuto(userId,base,userCache)){
					continue;
				}
				// 判断获取自动投资金额
				money = getMoney(money,lowestAccount,autoAmountMax,autoInvest,userId,base,flag);
				if(StringUtils.isNotBlank(autoInvest.getRemark())){
					// 生成自动投资记录
					final UserInvestAutoLog investLog = new UserInvestAutoLog(base.getUuid(),userId);
					investLog.setStatus(InvestEnum.STATUS_FAIL.getValue());
					investLog.setRemark(autoInvest.getRemark());
					autoTenderLogList.add(investLog);
					// 更新排序时间
					autoInvest.setSortTime(DateUtils.getNow().getTime());
					userAutoInvestMapper.update(autoInvest);
					continue;
				}

//				money = new BigDecimal("1000");
				//自动投资业务处理
				final MqAutoInvestModel autoInvestModel = new MqAutoInvestModel(money,autoAmountMax,lowestAccount,base,user,userCache);
				
				//红包加载处理
				UserRedenvelope redenvelope = null;
				try {
					LOGGER.info("开始使用红包判断-----------------");
					if ("1".equals(base.getRedEnvelopeUseful())) {
						LOGGER.info("项目可以使用红包-----------------");
						 UserRedenvelope userRedenvelope=new UserRedenvelope();
						 userRedenvelope.setUserId(user.getUuid());
						 Page<UserRedenvelope> page=new Page<UserRedenvelope>();
						 
						 List<UserRedenvelope>  list = null;
						 final AutoInvestRuleLog rule = autoInvestRuleLogMapper.get(autoInvest.getRuleId());
						 if (rule.getRedType() == 2) {
							 page.setSort("red.amount");
							 page.setOrder(Constant.DESC);
							 userRedenvelope.setPage(page);
							 list = userRedenvelopeService.findAvailableRedenvelope(userRedenvelope, base, money);
						 } else if (rule.getRedType() == 1 ) {
							 page.setSort("ifnull(red.use_expire_time,'2099-12-31 23:59:59')");
							 page.setOrder(Constant.ASC);
							 userRedenvelope.setPage(page);
							 list = userRedenvelopeService.findAvailableRedenvelope(userRedenvelope, base, money);
						 } else {
							 LOGGER.info("用户不使用红包--------------{}", autoInvest.getRuleId());
						 }  
						 
						 LOGGER.info("用户红包数量-----------------{}", list == null ? 0 : list.size());
						 if (list != null && list.size() > 0) {
							redenvelope = list.get(0);
							LOGGER.info("可以使用红包：{}，金额：{}", redenvelope.getUuid(), redenvelope.getAmount());
							if (!redenvelope.isAvailable(base, money)) {
			                    throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_REDENVELOPE), BussinessException.TYPE_CLOSE);
			                }
							
							//最后一期还款发红包
//							final Map<String, Object> map = new HashMap<String, Object>();
//					        map.put("accountId", ConfigUtils.getValue(ConfigConstant.JXBANK_REDENVELOPE));
//					        map.put("txAmount", BigDecimalUtils.round(redenvelope.getAmount(), 2));
//					        map.put("forAccountId", user.getTppUserCustId());
//					        map.put("desLineFlag", "1");
//					        map.put("desLine", "红包使用");
//					        TppService tppService = (TppService) TppUtil.tppService();
//					        jxVoucherPayModel = (JxVoucherPayModel) tppService.experBonus(map);
//					        redenvelope.setOrderNo(jxVoucherPayModel.getOrderNo());
//					        userRedenvelopeService.update(redenvelope);
					        
//					        autoInvestModel.setRealAmount(BigDecimalUtils.sub(money, redenvelope.getAmount()));
					        autoInvestModel.setRealAmount(money);
					        autoInvestModel.setRedEnvelope(redenvelope.getAmount());
					        autoInvestModel.setRedUuid(redenvelope.getUuid());
					        
						 }
					}
					
				} catch (Exception e) {
					LOGGER.error("使用红包出错：{}", e.getMessage());
				}
				try {
					LOGGER.info("开始使用加息券判断-----------------");
					if ("1".equals(base.getAdditionalRateUseful())) {
						LOGGER.info("项目可以使用加息券-----------------");
						final AutoInvestRuleLog rule = autoInvestRuleLogMapper.get(autoInvest.getRuleId());
						UserRateCoupon userRateCoupon = new UserRateCoupon();
						userRateCoupon.setUserId(user.getUuid());

						List<UserRateCoupon> availableRateList = null;
						Page<UserRateCoupon> page=new Page<UserRateCoupon>();
						if (rule.getIncreaseType() == 2) {
							page.setSort("rate.up_apr");
							page.setOrder(Constant.DESC);
							userRateCoupon.setPage(page);
							availableRateList = userRateCouponService.findAvailableRateCoupon(userRateCoupon, base, money);
						} else if (rule.getIncreaseType() == 1) {
							page.setSort("ifnull(rate.use_expire_time,'2099-12-31 23:59:59')");
							page.setOrder(Constant.ASC);
							userRateCoupon.setPage(page);
							availableRateList = userRateCouponService.findAvailableRateCoupon(userRateCoupon, base, money);
						} else {
							LOGGER.info("用户不使用加息券--------------{}", autoInvest.getRuleId());
						}
						
						LOGGER.info("用户加息券数量-----------------{}", availableRateList == null ? 0 : availableRateList.size());
						if (availableRateList != null && availableRateList.size() > 0) {
							UserRateCoupon rateCoupon = availableRateList.get(0);
							LOGGER.info("可以使用加息券：{}，金额：{}", rateCoupon.getUuid(), rateCoupon.getUpApr());
							if (!rateCoupon.isAvailable(base, money)) {
								throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_USE_INVALID_RATECOUPON), BussinessException.TYPE_CLOSE);
			                }
							autoInvestModel.setUpApr(rateCoupon.getUpApr());
							autoInvestModel.setRateUuid(rateCoupon.getUuid());
						}
					}
				} catch (Exception e) {
					LOGGER.error("加息券使用报错:{}",e.getMessage());
				}
				
				try {
					jxbankService.doAutoInvest(autoInvestModel);
				} catch (Exception e) {
					//最后一期还款发红包
					//退红包
//					if (autoInvestModel.getRealAmount() != null && redenvelope != null) {
//						LOGGER.error("自动投资失败，{}，退红包", redenvelope.getUuid());
//						BigDecimal redAmount = BigDecimalUtils.round(BigDecimalUtils.sub(autoInvestModel.getAmount(), autoInvestModel.getRealAmount()), 2);
//						if (redAmount.compareTo(BigDecimal.ZERO) > 0) {
//							final Map<String, Object> map = new HashMap<String, Object>();
//					        map.put("accountId", ConfigUtils.getValue(ConfigConstant.JXBANK_REDENVELOPE));
//					        map.put("txAmount", redAmount);
//					        map.put("forAccountId", user.getTppUserCustId());
//					        map.put("orgTxDate", jxVoucherPayModel.getTxDate());
//					        map.put("orgTxTime", jxVoucherPayModel.getTxTime());
//					        map.put("orgSeqNo", jxVoucherPayModel.getSeqNo());
//					        map.put("delay", "0");
//					        TppService tppService = (TppService) TppUtil.tppService();
//					        tppService.voucherPayCancel(map);
//						}
//					}
				}
			}
			//保存自动投资记录
			if(autoTenderLogList.size() > Constant.INT_ZERO){
				insertBatch(autoTenderLogList);
			}
			ProjectCache.delRemainAccount(base.getUuid()+CacheConstant.KEY_AUTOINVEST_REMAINACCOUNT);
		}finally {
			RedissonDistributeLock.unlock(String.format(LockConstant.LOCK_AUTO_INVEST,base.getUuid()));
		}
		LOGGER.info("自动投资处理完毕.............");
	}

	/**
	 * 
	 * 判断获取自动投资金额
	 * @author ZhangBiao
	 * @date 2016年9月12日
	 * @param money
	 * @param lowestAccount
	 * @param autoAmountMax
	 * @param autoInvest
	 * @param userId
	 * @param base
	 * @param flag
	 * @return
	 */
	private BigDecimal getMoney(BigDecimal money, BigDecimal lowestAccount,
			final BigDecimal autoAmountMax, final UserAutoInvest autoInvest,
			final String userId, final Project base, final boolean flag) {
		BigDecimal tempMoney = money;
		BigDecimal tempLowestAccount = lowestAccount;
		// 详细规则
		final AutoInvestRuleLog rule = autoInvestRuleLogMapper.get(autoInvest.getRuleId());
		// 获取今日实际投资额度
		BigDecimal dayInvestMoney = projectInvestMapper.getDayInvestMoney(userId);
		if(dayInvestMoney == null){
			dayInvestMoney = BigDecimal.ZERO;
		}
		// 设置的自动投资金额
		final BigDecimal autoMoney = rule.getAmountDayMax();
		// 已经超出单日最高投资金额，进入重新排序
		if(dayInvestMoney.compareTo(autoMoney) >= Constant.INT_ZERO){
			autoInvest.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_INVESTMONEY_OVERLIMIT));
			return tempMoney;
		}
		// 今日还可自动投资额度
		tempMoney = BigDecimalUtils.sub(autoMoney, dayInvestMoney);
		// 产品最大投资金额
		BigDecimal mostAccount = base.getMostAccount();
		// 如果按份数计算最大最小额度
		if(flag){
			tempLowestAccount = BigDecimalUtils.mul(base.getCopyAccount(),BigDecimal.valueOf(base.getLowestCopies()));
			mostAccount = BigDecimalUtils.mul(base.getCopyAccount(),BigDecimal.valueOf(base.getMostCopies()));
		}
		// 账户可用金额
		final Account account = accountService.getMoneyByUserId(new AccountQueryModel(userId,ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		final BigDecimal useMoney = account.getUseMoney();
		//如果用户可用余额不足,小于产品最小限额，那么直接跳出循环
		if (tempLowestAccount.compareTo(useMoney) > Constant.INT_ZERO) {
			autoInvest.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_USEMONEY_LTLOWESTACCOUNT));
			return tempMoney;
		}
		// 投资额小于资金余额，取余额
		if(tempMoney.compareTo(useMoney) > Constant.INT_ZERO){
			tempMoney = useMoney;
		}
		//如果投资金额 大于 产品最大限额，那么投资金额取产品最大限额
		if(mostAccount.compareTo(BigDecimal.ZERO) > Constant.INT_ZERO && tempMoney.compareTo(mostAccount) > Constant.INT_ZERO){
			tempMoney = mostAccount;
		}
		//如果 投资金额 大于最大自动投资金额，那么取最大投资金额
		if(tempMoney.compareTo(autoAmountMax) > Constant.INT_ZERO){
			tempMoney = autoAmountMax;
		}
		// 如果是按份必须为每份金额的整数倍
		if(flag){
			final int num = (int) (tempMoney.doubleValue()/base.getCopyAccount().doubleValue());
			tempMoney = BigDecimalUtils.mul(base.getCopyAccount(),new BigDecimal(num));
		}
		// 存在递增金额
		if(base.getStepAccount().doubleValue() > Constant.INT_ZERO){
			final BigDecimal tempAccount = BigDecimalUtils.sub(tempMoney, tempLowestAccount);
			final int num = (int) (tempAccount.doubleValue()/base.getStepAccount().doubleValue());
			tempMoney = BigDecimalUtils.sub(tempLowestAccount, BigDecimalUtils.mul(new BigDecimal(num),base.getStepAccount()));
		}
		// 转换为整数
		tempMoney = BigDecimal.valueOf(tempMoney.intValue());
		// 可用余额必须大于等于最终投资金额,不足保存记录，更新排序时间
		if (tempMoney.compareTo(useMoney) > Constant.INT_ZERO || tempMoney.equals(BigDecimal.ZERO)) {
			autoInvest.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_USEMONEY_NOTENOUGH));
		}
		return tempMoney;
	}

	/**
	 * 封装查询model
	 * @author ZhangBiao
	 * @date 2016年8月5日
	 * @param base
	 * @return
	 */
	private AutoInvestRuleLogModel getCheckModel(final Project base) {
		final AutoInvestRuleLogModel model = new AutoInvestRuleLogModel(base);
		// 查询是否可变现或者是否可债权标识
		if(LoanEnum.BORROW_FLAG_BORROW.eq(base.getBorrowFlag())){
			final Borrow borrow = borrowService.get(base.getUuid());
			model.setBondUseful(borrow.getBondUseful());
		}else{
			final Product product = productService.get(base.getUuid());
			model.setRealizeUseful(product.getRealizeUseful());
		}
		return model;
	}

	/**
	 * 校验用户是否复核自动投资资格
	 * @author ZhangBiao
	 * @date 2016年8月5日
	 * @param userId
	 * @param base
	 * @return
	 */
	private boolean checkAuto(final String userId,final Project base,final UserCache userCache) {
		//如果投资被冻结 ， 那么自动投资失败
		final UserFreeze userFreeze = userFreezeMapper.getByUserId(userId);
		if (userFreeze != null && userFreeze.getOperation().contains(UserFreezeModel.STATUS_USER_FREEZE_INVEST)) {
			LOGGER.info("投资功能已被冻结，userId:{}",userId);
			return true;
		}
		// 自己不能投资自己发布的产品
		if(userId.equals(base.getUserId())){
			LOGGER.info("自己不能投资自己发布的产品，userId:{}",userId);
			return true;
		}
		// 判断是否经过风险等级
		if(riskUserLogService.getCountByUserId(userId) <= Constant.INT_ZERO){//若评测记录为空 则说明没有做过任何评估
			LOGGER.info("未做过风险等级评估，不能进行自动投资，userId:{}",userId);
			return true;
	    }
		// 风险等级判断
		int userRiskLevel = Constant.INT_ZERO;
		int baseRiskLevel = Constant.INT_ZERO;
		if(StringUtils.isNotBlank(userCache.getRiskLevel())){
			userRiskLevel = Integer.valueOf(userCache.getRiskLevel());
		}
		if(StringUtils.isNotBlank(base.getRiskLevel())){
			baseRiskLevel = Integer.valueOf(base.getRiskLevel());
		}
		if (userRiskLevel + Constant.INT_ONE < baseRiskLevel){
			LOGGER.info("产品风险等级已超出用户的风险承受能力，userId:{}",userId);
			return true;
		}
		return false;
	}

	@Override
	//@Transactional(readOnly=false)
	public void jobAutoInvest() {
		final Date date = DateUtils.getNow();
		// 产品开售时间
		final Date startDate = DateUtils.rollMinute(date, Constant.INT_FIVE);
		final Date endDate = DateUtils.rollMinute(date, Constant.INT_TEN);
		// 获取需要自动投资产品
		final List<Project> projectList = projectService.findListByStatusTime(LoanEnum.STATUS_RAISING.getValue(),startDate,endDate);
		for(final Project project : projectList){
			// 判断产品是否复核自动投资
			if(project.checkOpenAuto()){
				final MqAutoInvestModel autoInvestModel = new MqAutoInvestModel();
				autoInvestModel.setProject(project);
				if (ConfigUtils.isOpenMq()) {
					RabbitUtils.startAutoInvest(autoInvestModel);
				} else {
					startAutoInvest(autoInvestModel);
				}
			}
		}
		
	}

	@Override
	@Transactional(readOnly=false)
	public void startAutoInvest(MqAutoInvestModel autoInvestModel) {
		doAutoInvest(autoInvestModel.getProject());
	}
}