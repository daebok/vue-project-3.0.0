package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.RealizeEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.RealizeCache;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeFreeze;
import com.rd.ifaes.core.project.mapper.ProjectRepaymentMapper;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeFreezeService;
import com.rd.ifaes.core.project.service.RealizeRepaymentService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 变现还款实现类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Service("realizeRepaymentService") 
public class RealizeRepaymentServiceImpl  extends BaseServiceImpl<ProjectRepaymentMapper, ProjectRepayment> implements RealizeRepaymentService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RealizeRepaymentServiceImpl.class);
	@Resource
	private transient UserService userService;
	@Resource
	private transient AccountService accountService;
	@Resource
	private transient RealizeService realizeService;
	@Resource
	private transient ProjectService projectService;
	@Resource
	private transient ProjectInvestService projectInvestService;
	@Resource
	private transient ProjectCollectionService projectCollectionService;
	@Resource
	private transient ProjectRepaymentService projectRepaymentService;
	@Resource
	private transient TppTradeService tppTradeService;
	@Resource
	private transient RealizeFreezeService realizeFreezeService;
	@Resource
    private PlatformTransactionManager transactionManager;
	
	@Override
	public void autoRepay(){
		final Date nowTime = DateUtils.trunc(DateUtils.getNow());
		// 获取当日到期的变现
		final List<Realize> realizeRepayList = realizeService.findRealizeRepay(nowTime);
		if(CollectionUtils.isNotEmpty(realizeRepayList)){
			for (Realize realize : realizeRepayList) {
				try { 
					//执行还款
					repay(realize);
				} catch (Exception e) {
					LOGGER.error("变现{}自动还款失败,原因",realize.getUuid(),e.getMessage(),e);
				}
			}
		}else{
			LOGGER.info("今日没有到期的变现");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeService#repay(com.rd.ifaes.core.project.domain.Realize)
	 */
	@Override
	public void repay(final Realize entity) {
		LOGGER.info("变现还款开始............");
        // 初始化第三方列表
		final List<TppTrade> taskList = new ArrayList<TppTrade>(); 
		// 还款状态校验
		final Realize realize = checkRepay(entity);
		// 查询待收列表(产品都为一次性故期数为1)
		final List<ProjectCollection> projectCollectionList = projectCollectionService.findByProjectIdAndPeriod(realize.getUuid(),
				CommonConstants.PROJECT_PERIODS_START,null);
		// 查询待还信息(产品都为一次性故期数为1)
		final ProjectRepayment projectRepayment = projectRepaymentService.findByProjectIdAndPeriod(realize.getUuid(),
				CommonConstants.PROJECT_PERIODS_START);
		// 变现借款人
		final User realizeUser = userService.get(realize.getUserId());
		// 校验借款人账户信息
		validBeforeRepayment(projectCollectionList, projectRepayment, realize, realizeUser);
		// 需要调用Account的dubbo服务执行： 借款人账户金额处理
		final List<AccountModel> accountList = new ArrayList<AccountModel>();
		// 借款人资金日志
		final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			// 更新还款记录状态
			projectRepaymentService.updateStatus(projectRepayment.getUuid(), RepaymentEnum.STATUS_REPAID.getValue(),
					RepaymentEnum.STATUS_NOT_REPAY.getValue());
			// 处理变现状态以及待还
			handleProjectAndRepayment(projectRepayment, realize);
			// 解冻借款人资金
			realizeService.fillUnFreezeTaskList(taskList, realizeUser, realize,accountList,accountLogList);
			// 封装投资人本金,利息,逾期罚息
			handleInvest(projectCollectionList, realize, realizeUser, taskList);
			// 任务入库
			tppTradeService.insertBatch(taskList);
			// 处理变现状态以及待还
			handleBorrowAccount(projectRepayment, realizeUser,accountList,accountLogList);
			// 资金变动提交
			if(CollectionUtils.isNotEmpty(accountList) && CollectionUtils.isNotEmpty(accountLogList)){
				accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
			}
			// 事物提交
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		// 清除缓存
		RealizeCache.delRealizeAndList(realize.getUuid());
		tppTradeService.sendTppTrade(taskList, MqConstant.ROUTING_KEY_TRADE_REPAY);
		LOGGER.info("变现({})还款成功,处理完毕............",realize.getUuid());
	}
	
	/**
	 * 处理变现状态以及待还
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectRepayment
	 * @param realize
	 * @param realizeUser
	 */
	private void handleProjectAndRepayment(final ProjectRepayment projectRepayment,final Realize realize) {
		final BigDecimal capital = projectRepayment.getCapital();// 本金
		final BigDecimal interest = projectRepayment.getInterest();// 利息
		final BigDecimal payedAmount = BigDecimalUtils.add(capital, interest);// 本息
		// 修改repayment属性
		final ProjectRepayment repaymentTemp = new ProjectRepayment();
		repaymentTemp.setUuid(projectRepayment.getUuid());
		repaymentTemp.setRealRepayTime(DateUtils.getNow());
		repaymentTemp.setRepayUserId(projectRepayment.getUserId());
		repaymentTemp.setPayedAmount(BigDecimalUtils.add(projectRepayment.getCapital(),projectRepayment.getInterest()));//projectRepayment.getLateInterest()));
		repaymentTemp.setRepayType(RepaymentEnum.REPAY_TYPE_NORMAL.getValue());
		dao.update(repaymentTemp);
		// 更新项目已还金额
		final Project projectTemp = new Project();
		projectTemp.setUuid(realize.getUuid());
		projectTemp.setRepayYesAccount(BigDecimalUtils.add(realize.getRepayYesAccount(),payedAmount));
		projectTemp.setRepayYesInterest(BigDecimalUtils.add(realize.getRepayYesInterest(),interest));
		// 变现只有一次性还款因此直接置为还款完成
		projectTemp.setStatus(LoanEnum.STATUS_REPAYED_SUCCESS.getValue());
		projectTemp.setRealLastRepayTime(repaymentTemp.getRealRepayTime());
		projectService.update(projectTemp);
	}
	
	/**
	 * 处理变现借款人资金
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectRepayment
	 * @param realizeUser
	 */
	private void handleBorrowAccount(final ProjectRepayment projectRepayment, final User realizeUser,
			final List<AccountModel> accountList, final List<AccountLog> accountLogList) {
		final BigDecimal capital = projectRepayment.getCapital();// 本金
		final BigDecimal interest = projectRepayment.getInterest();// 利息
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("用户(" + realizeUser.getRealName() + ")还款,所付本金："+capital+"元,利息"+interest+"元");
		}
		// 扣除还款本金
		if (BigDecimalUtils.validAmount(capital)) {
			updateAccount(realizeUser, capital.negate(), BigDecimal.ZERO, capital, accountList);
			final String capitalRemark = getRemark(Constant.REPAID_CAPITAL, capital);//"扣除还款本金" + capital.doubleValue() + "元";
			addAccountLog(realizeUser, ConfigUtils.getValue(Constant.ADMIN_ID), capital, Constant.REPAID_CAPITAL, AccountLog.PAYMENTS_TYPE_EXPEND, accountLogList, capitalRemark);
		}
		// 扣除还款利息
		if (BigDecimalUtils.validAmount(interest)) {
			updateAccount(realizeUser, interest.negate(), BigDecimal.ZERO, interest, accountList);
			final String interestRemark = getRemark(Constant.REPAID_INTEREST, interest);//"扣除还款利息" + interest.doubleValue() + "元";
			addAccountLog(realizeUser, ConfigUtils.getValue(Constant.ADMIN_ID), interest, Constant.REPAID_INTEREST, AccountLog.PAYMENTS_TYPE_EXPEND, accountLogList, interestRemark);
		}
	}
	
	/**
	 * 处理变现投资人资金
	 * @author fxl
	 * @date 2016年8月1日
	 * @param projectCollectionList
	 * @param projectRepayment
	 * @param realize
	 * @param realizeUser
	 * @param taskList
	 */
	private void handleInvest(final List<ProjectCollection> projectCollectionList, final Realize realize,
			final User realizeUser,final List<TppTrade> taskList) {
		
		List<ProjectInvest> investList = new ArrayList<>();
		for (ProjectCollection collection : projectCollectionList) {
			final BigDecimal capital = collection.getCapital();// 本金
			final BigDecimal interest = collection.getInterest();// 利息
			final BigDecimal raiseInterest = collection.getRaiseInterest();// 加息
			final BigDecimal interestFee = BigDecimal.ZERO;
			final ProjectInvest invest = projectInvestService.get(collection.getInvestId());
			// 归还投资人本金+利息
			if (BigDecimalUtils.validAmount(capital)) {
				taskList.add(projectRepaymentService.fillTppTrade(realize.prototype(), invest, realizeUser, BigDecimalUtils.add(capital,interest),
						Constant.COLLECT_CAPITAL_INTEREST, interestFee, false, false, collection));
			}
			// 平台归还投资加息券利息
			if (BigDecimalUtils.validAmount(raiseInterest)) {
				taskList.add(projectRepaymentService.fillTppTrade(realize.prototype(), invest, realizeUser, raiseInterest,
						Constant.COLLECT_ADD_INTEREST, BigDecimal.ZERO, false, false, collection));
			}
			if (InvestEnum.REALIZE_FLAG_TENDER.eq(invest.getRealizeFlag())) {
				invest.setFreezeCapital(BigDecimalUtils.add(invest.getFreezeCapital(),collection.getFreezeCapital()));
				invest.setFreezeInterest(BigDecimalUtils.add(invest.getFreezeInterest(),collection.getFreezeInterest()));
			}
			collection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());
			collection.setManageFee(interestFee);
			collection.setLastRepayTime(DateUtils.getNow());
			collection.setRepayedAmount(BigDecimalUtils.add(capital,interest,raiseInterest));//lateInterest
			// 更新invest
			invest.setRepayedAmount(BigDecimalUtils.add(invest.getRepayedAmount(), collection.getRepayedAmount()));
			invest.setRepayedInterest(BigDecimalUtils.add(invest.getRepayedInterest(),interest,raiseInterest,interestFee.negate()));//lateInterest
			invest.setWaitAmount(BigDecimalUtils.add(invest.getWaitAmount(),capital.negate(),interest.negate(),raiseInterest.negate()));
			invest.setWaitInterest(BigDecimalUtils.add(invest.getWaitInterest(), interest.negate(),raiseInterest.negate()));
			invest.setWaitRaiseInterest(BigDecimalUtils.add(invest.getWaitRaiseInterest(), raiseInterest.negate()));
			if(collection.getPeriod() == collection.getPeriods()){
				invest.setEndDate(DateUtils.getNow());
			}
			investList.add(invest);
		}
		if(CollectionUtils.isNotEmpty(investList)){
			projectInvestService.updateBatch(investList);
		}
		if(CollectionUtils.isNotEmpty(projectCollectionList)){
			projectCollectionService.updateBatch(projectCollectionList);
		}
	}
	
	/**
	 * 变现还款校验
	 * @author fxl
	 * @date 2016年8月01日
	 * @param realize
	 */
	private Realize checkRepay(final Realize entity){
		if(entity==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_REPAY_ISNOT_RIGHT));
		}
		if(entity.getUuid()==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_REPAY_ISNOT_RIGHT));
		}
		LOGGER.info("变现({})进入还款校验............",entity.getUuid());
		final Realize realize = realizeService.findRealize(entity.getUuid());
		if(realize==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_FOUNT));
		}
		if (!LoanEnum.REALIZE_FLAG_REALIZE.eq(realize.getRealizeFlag())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_REALIZE));
		}
		if (LoanEnum.STATUS_REPAYED_SUCCESS.eq(realize.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_REPAYED));
		}
		if (!(LoanEnum.STATUS_REPAY_START.eq(realize.getStatus()) || LoanEnum.STATUS_REPAY_OVERDUE.eq(realize.getStatus()))) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_NOT_REPAYING));
		}
		final Project oldProject = projectService.get(realize.getOldProjectId());
		if( (!LoanEnum.STATUS_REPAYED_SUCCESS.eq(oldProject.getStatus())) && (!LoanEnum.STATUS_REPAYED_LATE.eq(oldProject.getStatus()))){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_ORGPROJECT_NOT_REPAYED));
		}
		List<RealizeFreeze> freezeList = realizeFreezeService.getFreezedListByRealizeId(realize.getUuid(),RealizeEnum.FREEZE_STATUS_NEW.getValue());
		if(CollectionUtils.isNotEmpty(freezeList)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_ORGPROJECT_IS_REPAYING));
		}
		return realize;
	}
	
	/**
	 * 还款前校验
	 * @author fxl
	 * @date 2016年8月01日
	 * @param realize
	 */
	private void validBeforeRepayment(final List<ProjectCollection> projectCollectionList,
			final ProjectRepayment projectRepayment, final Realize realize, final User realizeUser) {
		LOGGER.info("变现还款进入还款前金额校验.................");
		if(projectRepayment==null){
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_NOEXISTS), BussinessException.TYPE_JSON);
		}
		if(CollectionUtils.isEmpty(projectCollectionList)){
			throw new BussinessException("变现待收信息异常");
		}
		// 还款记录状态校验
		if (!RepaymentEnum.STATUS_NOT_REPAY.eq(projectRepayment.getStatus())) {
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_HASREPAID), BussinessException.TYPE_JSON);
		}
		// 应还总额
		final BigDecimal repayMoney = BigDecimalUtils.add(projectRepayment.getCapital(),projectRepayment.getInterest());
		// 变现借款人账户
		final Account realizeAccount = accountService.getMoneyByUserId(new AccountQueryModel(realize.getUserId(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
		BigDecimal noUseMoney = realizeAccount.getNoUseMoney();
		// 查询校验提现账户第三方可用和平台可用是否一致
		final Map<String, Object> ufxMap = new HashMap<String, Object>();
		ufxMap.put("userCustId", realizeUser.getTppUserCustId());
		ufxMap.put("userType", UfxQueryBalanceModel.USER_TYPE_PERSONAL);
		BigDecimal tppFrzMoney = BigDecimal.ZERO;
		try {
			TppService tppService = (TppService)TppUtil.tppService();
			final UfxQueryBalanceModel queryBalanceModel = (UfxQueryBalanceModel)tppService.tppQueryBalance(ufxMap);
			tppFrzMoney = new BigDecimal(queryBalanceModel.getFrzBal());
		} catch (Exception e) {
			throw new BussinessException("还款时查询第三方资金异常");
		}
		LOGGER.info("变现还款应还总额{},应还本金{},应还利息{},本地冻结金额{},第三方冻结金额{}",repayMoney,projectRepayment.getCapital(),projectRepayment.getInterest(),noUseMoney,tppFrzMoney);
		if(noUseMoney.compareTo(repayMoney) < 0){
			throw new BussinessException("变现还款冻结金额不足");
		}
		if (tppFrzMoney.compareTo(repayMoney) < 0) {
			throw new BussinessException("变现还款第三方冻结金额不足");
		}
	}
	
	/**
	 * 修改账户
	 * @author fxl
	 * @date 2016年8月01日
	 * @param user
	 * @param toUserId
	 * @param useVar
	 * @param nouseVar
	 * @param amount
	 * @param accountList
	 */
	private void updateAccount(final User user,final BigDecimal useVar,final BigDecimal nouseVar,
			final BigDecimal amount,final List<AccountModel> accountList) {
		LOGGER.info("用户({})--用户资金修改，金额：{}" ,user.getRealName(), amount.doubleValue());
		accountList.add(new AccountModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), useVar,nouseVar));
	}
	
	/**
	 * 生成账户资金日志
	 * @author fxl
	 * @date 2016年8月01日
	 * @param user
	 * @param toUserId
	 * @param amount
	 * @param accountType
	 * @param paymentsType
	 * @param accountLogList
	 * @param remark
	 */
	private void addAccountLog(final User user, final String toUserId, final BigDecimal amount, final String accountType,
			final String paymentsType, final List<AccountLog> accountLogList, final String remark) {
		LOGGER.info("用户({})--用户资金记录生成，金额：" ,user.getRealName(), amount.doubleValue());
		// 保存账户日志
		final AccountLog accountLog = new AccountLog(ConfigUtils.getValue("account_code"), accountType, user.getUuid(), amount, remark);
		accountLog.preInsert();
		accountLog.setToUser(toUserId);
		accountLog.setPaymentsType(paymentsType);
		accountLogList.add(accountLog);
	}

	/**
	 * 获取模板注释
	 * @author fxl
	 * @date 2016年8月30日
	 * @param code
	 * @param amount
	 * @return
	 */
	private String getRemark(final String code,final BigDecimal amount){
		final Map<String, Object> remarkData = new HashMap<String, Object>(1);
		remarkData.put("amount", amount.doubleValue());
		return LogTemplateUtils.getAccountTemplate(code, remarkData);
	}

	@Override
	public void repayCbhb(Realize entity) {
		LOGGER.info("渤海银行 --- 变现还款开始............");
		// 还款状态校验
		final Realize realize = checkRepay(entity);
		// 查询待收列表(产品都为一次性故期数为1)
		final List<ProjectCollection> projectCollectionList = projectCollectionService.findByProjectIdAndPeriod(realize.getUuid(),
				CommonConstants.PROJECT_PERIODS_START,null);
		// 查询待还信息(产品都为一次性故期数为1)
		final ProjectRepayment projectRepayment = projectRepaymentService.findByProjectIdAndPeriod(realize.getUuid(),
				CommonConstants.PROJECT_PERIODS_START);
		// 变现借款人
		final User realizeUser = userService.get(realize.getUserId());
		// 校验借款人账户信息
		validBeforeRepayment(projectCollectionList, projectRepayment, realize, realizeUser);
		// 需要调用Account的dubbo服务执行： 借款人账户金额处理
		final List<AccountModel> accountList = new ArrayList<AccountModel>();
		// 借款人资金日志
		final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		
		Map<String,Object> map = new HashMap<String,Object>(); // 还款
		Map<String,Object> mapExperCap = new HashMap<String,Object>(); // 加息发放
		Map<String,Object> mapUnHoldAmt = new HashMap<String,Object>(); // 解冻
		try {
			// 更新还款记录状态
			projectRepaymentService.updateStatus(projectRepayment.getUuid(), RepaymentEnum.STATUS_REPAID.getValue(),
					RepaymentEnum.STATUS_NOT_REPAY.getValue());
			// 处理变现状态以及待还
			handleProjectAndRepayment(projectRepayment, realize);
			// 解冻借款人资金
			realizeService.fillUnFreezeTaskList(null, realizeUser, realize,accountList,accountLogList);
			// 封装投资人本金,利息,逾期罚息
			handleInvest(projectCollectionList, realize, realizeUser, null);
			// 处理变现状态以及待还
			handleBorrowAccount(projectRepayment, realizeUser,accountList,accountLogList);
			// 资金变动提交
			if(CollectionUtils.isNotEmpty(accountList) && CollectionUtils.isNotEmpty(accountLogList)){
				accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
			}
			// 事物提交
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		// 清除缓存
		RealizeCache.delRealizeAndList(realize.getUuid());
		LOGGER.info("变现({})还款成功,处理完毕............",realize.getUuid());
	}
	
}