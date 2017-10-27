package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rd.account.domain.Account;
import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.BondCollectionEnum;
import com.rd.ifaes.common.dict.BondEnum;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.ProjectCollectionEnum;
import com.rd.ifaes.common.dict.RepaymentEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.dict.UfxRepaymentModelEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.rabbit.RabbitProducer;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.CommonConstants;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.FreemarkerUtil;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.redisson.RedissonDistributeLock;
import com.rd.ifaes.common.util.resource.BondResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.bond.domain.BondCollection;
import com.rd.ifaes.core.bond.domain.BondInvest;
import com.rd.ifaes.core.bond.service.BondCollectionService;
import com.rd.ifaes.core.bond.service.BondInvestService;
import com.rd.ifaes.core.bond.service.BondService;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.constant.URLConstant;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.core.util.ValidateUtils;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.UrgeRepayLog;
import com.rd.ifaes.core.project.mapper.ProjectRepaymentMapper;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.UrgeRepayLogService;
import com.rd.ifaes.core.sys.domain.Message;
import com.rd.ifaes.core.sys.domain.MessageType;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.service.MessageService;
import com.rd.ifaes.core.sys.service.MessageTypeService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.service.UserService;

/**
 * ServiceImpl:ProjectRepaymentServiceImpl
 *
 * @author FangJun
 * @version 3.0
 * @date 2016-7-13
 */
@Service("projectRepaymentService")
public class ProjectRepaymentServiceImpl extends BaseServiceImpl<ProjectRepaymentMapper, ProjectRepayment> implements
		ProjectRepaymentService {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectRepaymentServiceImpl.class);
	@Resource
	private ProjectTypeService projectTypeService;
	@Resource
	private BondCollectionService bondCollectionService;
	@Resource
	private BondInvestService bondInvestService;
	@Resource
	private AccountService accountService;
	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectCollectionService projectCollectionService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserService userService;
	@Resource
	private TppTradeService tppTradeService;
	@Resource
	private BondService bondService;
	@Resource
	private PlatformTransactionManager transactionManager;
	@Resource
	private TppMerchantLogService tppMerchantLogService;
	/**
	 * 短信类型
	 */
	@Resource
	private transient MessageTypeService msgTypeService;
	@Resource
	private transient UrgeRepayLogService urgeRepayLogService;
	/**
	 * Rabbit操作类
	 */
	@Autowired
	private transient RabbitProducer rabbitProducer;
	/**
	 * 短信业务处理类
	 */
	@Resource
	private transient MessageService messageService;

	@Override
	public BigDecimal getWaitRepayAccountTotal(String userId) {
		return dao.getWaitRepayAccountTotal(userId);
	}

	@Override
	public ProjectRepayment getNextRepayByUserId(String userId) {
		return dao.getNextRepayByUserId(userId);
	}

	@Override
	public BigDecimal getNextRepayAccountByTime(String userId, Date repayTime) {
		ProjectRepayment repayment = new ProjectRepayment();
		repayment.setUserId(userId);
		repayment.setRepayTime(repayTime);
		return dao.getNextRepayAccountByTime(repayment);
	}

	@Override
	public ProjectRepayment findByProjectIdAndPeriod(String projectId, Integer period) {
		return dao.findRepayByProjectIdAndPeriod(projectId, period, RepaymentEnum.STATUS_NOT_REPAY.getValue());
	}

	@Override
	public ProjectRepayment getRepayByProject(String projectId) {
		return dao.getRepayByProject(projectId);
	}

	@Override
	public void repay(String reapaymentId, String repayerPhone, String phoneCode, String repayUserId) {
		// 校验验证码
		if (!ValidateUtils.checkCode(repayerPhone, MessageConstant.MESSAGE_USER_REPAY_PHONECODE, phoneCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(reapaymentId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
		}
		// 投资记录
		final ProjectRepayment repayment = dao.get(reapaymentId);
		if (repayment == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS),
					BussinessException.TYPE_JSON);
		}
		repayment.setRepayUserId(repayUserId);
		this.doRepay(repayment, false, false);
	}

	@Override
	public void overdueRepay(String reapaymentId, String repayerPhone, String phoneCode, String repayUserId) {
		// 校验验证码
		if (!ValidateUtils.checkCode(repayerPhone, MessageConstant.MESSAGE_USER_REPAY_PHONECODE, phoneCode)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(reapaymentId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
		}
		// 投资记录
		final ProjectRepayment repayment = dao.get(reapaymentId);
		if (repayment == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS),
					BussinessException.TYPE_JSON);
		}
		repayment.setRepayUserId(repayUserId);
		this.doRepay(repayment, true, false);
	}

	/**
	 * 渤海银行还款操作
	 *
	 * @param repayment
	 * @param isAuto
	 * @param b
	 * @author ZhangBiao
	 * @date 2017年3月8日
	 */
	private void doCbhbRepay(ProjectRepayment repayment, boolean isOverdue, boolean isAuto) {
		LOGGER.info("渤海银行 -- 还款记录(UUID= {} )本地处理开始-----------", repayment.getUuid());
		// 还款金额的限制 计算本息和加逾期罚息
		final BigDecimal repayMoney = BigDecimalUtils.add(repayment.getCapital(), repayment.getInterest(),
				repayment.getLateInterest(), repayment.getMerchantLateInterest());
		if (!BigDecimalUtils.validAmount(repayMoney)) {// 若这些之和 不是大于0 的值 不允许还款
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_REPAYMENT_MONEY_ZERO));
		}
		// 项目信息
		final Project project = projectService.get(repayment.getProjectId());
		// 真实还款人信息
		final User repayUser = userService.get(project.getUserId());
		// 还款前数据、还款人可用金额校验
		validBeforeRepayment(repayment, repayUser);
		if (!isAuto) {
			TokenUtils.validShiroToken(TokenConstants.TOKEN_REPAY);
		}
		// 针对同一项目加锁处理
		RedissonDistributeLock.lock(project.getUuid());

		Map<String, Object> map = new HashMap<String, Object>(); // 还款
		try {
			// 更新还款记录状态
			this.updateStatus(repayment.getUuid(), RepaymentEnum.STATUS_REPAID.getValue(),
					RepaymentEnum.STATUS_NOT_REPAY.getValue());
			// 修改还款金额、项目已经金额
			this.updateRepayAmount(project, repayment, isOverdue, repayUser);
			// 生成还款TPP任务
			this.createCbhbRepay(project, repayUser, repayment, isOverdue, map);
			this.fillCbhbRepay(map, repayment, project, isOverdue, repayUser);
			// 处理还款人资金---担保垫付不处理担保垫付人资金
			User accountUser = isOverdue ? userService.get(project.getVouchId()) : repayUser;
			this.handleBorrowerAccount(accountUser, repayment, project, isOverdue);
		} finally {
			RedissonDistributeLock.unlock(project.getUuid());
		}

		try {
			// 清理所有项目列表缓存
			CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
			// 还款完毕 需要对债权的列表的缓存 进行清理
			CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
			// 清空项目信息缓存
			CacheUtils.del(CacheConstant.KEY_PROJECT_UUID, project.getUuid());
		} catch (Exception e) {
			LOGGER.error("渤海银行 --还款(UUID={})本地操作结束，清理缓存出错!", repayment.getUuid(), e);
		}

		// 还款
		TppService tppService = (TppService) TppUtil.tppService();
		if (new BigDecimal(map.get("transAmt").toString()).doubleValue() > 0) {
			CbhbFileRepaymentModel model = (CbhbFileRepaymentModel) tppService.tppRepayment(map);
			TppTrade tppTrade = new TppTrade(TppEnum.SERVICE_TYPE_REPAY.getValue(), TppEnum.TPP_TYPE_REPAY.getValue(),
					repayUser.getUuid(), repayUser.getTppUserCustId(), "", "", new BigDecimal(map.get("transAmt").toString()), "");
			tppTrade.setInvestProjectId(project.getUuid());
			tppTrade.setProjectId(project.getUuid());// 项目UUID
			// 判断是否垫付
			if (isOverdue) { // 担保
				tppTrade.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_FOUR.getValue());
			} else {// 逾期利息收回 走借款人还款通道
				tppTrade.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_ONE.getValue());
			}
			map.put("repaymentId", repayment.getUuid());
			map.put("isOverdue", isOverdue);
			map.put("repayType", tppTrade.getRepayType());
			map.put("projectNo", project.getProjectNo());
			map.remove("detailList");
			tppTrade.setParams(JSON.toJSONString(map));
			if (model != null) {
				tppTrade.setOrderNo(model.getMerBillNo());
				tppTrade.setReturnUrl(model.getBgRetUrl());
			}
			tppTradeService.insert(tppTrade);
		}

		LOGGER.info("渤海银行 --还款记录(UUID= {} )本地处理成功-----------", repayment.getUuid());
	}

	private void fillCbhbRepay(Map<String, Object> map, ProjectRepayment repayment, Project project, boolean isOverdue,
			User repayUser) {
		// 还款
		map.put("borrowId", project.getProjectNo());
		if (isOverdue) {// 是否逾期还款
			map.put("repayTyp", CbhbConstant.STRING_TWO);// 逾期
		} else {
			map.put("repayTyp", CbhbConstant.STRING_ONE);// 正常还款
		}
		if (project.getTotalPeriod() > Constant.INT_ONE) { // 是否分期
			map.put("repayInstTot", String.valueOf(project.getTotalPeriod()));
			if (project.getRepayedPeriod() == null) {
				map.put("repayInstCur", String.valueOf(Constant.INT_ONE));
			} else {
				map.put("repayInstCur", String.valueOf(project.getRepayedPeriod() + Constant.INT_ONE));
			}
		} else {
			map.put("repayInstTot", CbhbConstant.STRING_ZERO);
			map.put("repayInstCur", CbhbConstant.STRING_ZERO);
		}
		map.put("borrowerAmt", project.getAccount()); // 标的金额
		map.put("borrCustId", repayUser.getTppUserCustId()); // 借款人第三方账号
	}

	private void createCbhbRepay(Project project, User repayUser, ProjectRepayment repay, boolean isOverdue,
			Map<String, Object> map) {
		// 查询当前待还记录
		final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdAndPeriod(repay.getProjectId(),
				repay.getPeriod(), repay.getInvestId());

		final List<ProjectInvest> investList = new ArrayList<>();
		final List<BondInvest> biList = new ArrayList<>();
		final List<BondCollection> bcList = new ArrayList<>();
		final Date now = DateUtils.getNow();
		String[][] detailList = new String[collectionList.size()][];
		int i = 1;
		BigDecimal transAmt = BigDecimal.ZERO; // 交易金额
		BigDecimal feeAmt = BigDecimal.ZERO; // 逾期罚息
		for (ProjectCollection collection : collectionList) {
			// 本金
			final BigDecimal capital = collection.getCapital();
			// 利息
			final BigDecimal interest = collection.getInterest();
			// 加息
			final BigDecimal raiseInterest = collection.getRaiseInterest();
			// 逾期罚息
			final BigDecimal lateInterest = collection.getLateInterest();
			// 利息管理费
			final BigDecimal interestFee = BigDecimalUtils.div(BigDecimalUtils.mul(interest, project.getInterestManageRate()),
					BigDecimal.valueOf(100), 2);
			// 逾期平台罚息
			final BigDecimal merchantLateInterest = BigDecimalUtils.validAmount(collection.getMerchantLateInterest()) ? collection
					.getMerchantLateInterest() : BigDecimal.ZERO;

			LOGGER.info("借款人给投资人罚息：{},借款人给平台罚息：{}", lateInterest, merchantLateInterest);

			// 对应投资记录
			ProjectInvest invest = projectInvestService.get(collection.getInvestId());

			// 鉴定该笔待收是否是受让人待收
			boolean bondFlag = false;
			if (ProjectCollectionEnum.COLLECTION_TYPE_INVEST.eq(collection.getCollectionType())) {
				bondFlag = true;
			}
			LOGGER.info("【借款人还款-还款ID:{}-是否是受让人待收:{}-】", repay.getUuid(), bondFlag);

			// 封装渤海银行还款参数
			User user = userService.get(collection.getUserId());
			String[] array = new String[5];
			array[0] = String.valueOf(i);
			array[1] = user.getTppUserCustId();
			array[2] = String.valueOf(capital);
			array[3] = String.valueOf(BigDecimalUtils.add(interest, lateInterest));
			array[4] = String.valueOf(interestFee);
			detailList[i - 1] = array;
			transAmt = BigDecimalUtils.add(transAmt, capital, interest, lateInterest, merchantLateInterest);
			feeAmt = BigDecimalUtils.add(feeAmt, merchantLateInterest);

			i++;

			// 已变现投资
			if (InvestEnum.REALIZE_FLAG_TENDER.eq(invest.getRealizeFlag())) {
				invest.setFreezeCapital(BigDecimalUtils.add(invest.getFreezeCapital(), collection.getFreezeCapital()));
				invest.setFreezeInterest(BigDecimalUtils.add(invest.getFreezeInterest(), collection.getFreezeInterest()));
			}// Realize end!
				// 更新待收记录
			collection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());
			collection.setManageFee(interestFee);// 存入利息管理费
			collection.setLastRepayTime(now);
			collection.setRepayedAmount(BigDecimalUtils.add(capital, raiseInterest, interest, lateInterest));

			// 更新投资记录
			invest.setRepayedAmount(BigDecimalUtils.add(invest.getRepayedAmount(), collection.getRepayedAmount()));
			invest.setRepayedInterest(BigDecimalUtils.add(invest.getRepayedInterest(), interest, raiseInterest, lateInterest,
					interestFee.negate()));
			invest.setWaitAmount(BigDecimalUtils.add(invest.getWaitAmount(), capital.negate(), raiseInterest.negate(),
					interest.negate()));
			invest.setWaitInterest(BigDecimalUtils.add(invest.getWaitInterest(), interest.negate(), raiseInterest.negate()));
			invest.setWaitRaiseInterest(BigDecimalUtils.add(invest.getWaitRaiseInterest(), raiseInterest.negate()));
			// 最后一期保存结束日
			LOGGER.info("最后一期保存结束日");
			if (collection.getPeriod() == collection.getPeriods()) {
				invest.setEndDate(now);
				LOGGER.info("还款录入结束日期，{}", invest.getEndDate());
			}
			investList.add(invest);

			// 受让投资时 更新债权投资记录
			BondInvest bondInvest = new BondInvest();
			if (InvestEnum.INVEST_STYLE_BUY.eq(invest.getInvestStyle())) {
				LOGGER.info("【债权还款--更新受让投资--原始标投资记录投资方式:{}--对应的债权投资订单号:{}】", invest.getInvestStyle(), invest.getInvestOrderNo());
				bondInvest = bondInvestService.getBondInvestByOrderNo(invest.getInvestOrderNo());
				bondInvest.setPayedInterest(invest.getRepayedInterest());
				biList.add(bondInvest);
			}

			// 受让待收时 更新债权待收记录
			if (bondFlag) {
				LOGGER.info("【债权还款--collectionId:{}，还款本金：{},还款利息：{},collectionInvestId:{},期数：{}】", collection.getUuid(), capital,
						interest, collection.getInvestId(), collection.getPeriod());
				collection.setBondCapital(capital);
				collection.setBondInterest(interest);
				BondCollection bondCollection = bondCollectionService.getBondCollectionByInvestIdAndPeriod(
						collection.getInvestId(), collection.getPeriod());
				if (bondCollection == null) {
					throw new BussinessException(ResourceUtils.get(BondResource.BOND_COLLECTION_IS_NOT_EXISTS));
				} else {
					bondCollection.setStatus(BondCollectionEnum.STATUS_PAID.getValue());
					bondCollection.setBondInterest(interest);
					bondCollection.setBondCapital(capital);
					bondCollection.setReceivedAmount(BigDecimalUtils.add(capital, interest));
					bondCollection.setReceivedTime(now);
					bcList.add(bondCollection);
				}
			}
		} // for end!

		if (CollectionUtils.isNotEmpty(investList)) {
			projectInvestService.updateBatch(investList);
		}
		if (CollectionUtils.isNotEmpty(biList)) {
			bondInvestService.updateBatch(biList);
		}
		if (CollectionUtils.isNotEmpty(bcList)) {
			bondCollectionService.updateBatch(bcList);
		}
		if (CollectionUtils.isNotEmpty(collectionList)) {
			projectCollectionService.updateBatch(collectionList);
		}
		map.put("feeAmt", feeAmt); // 商户收取借款人手续费---商户收取逾期罚息
		map.put("transAmt", transAmt); // 交易金额
		map.put("detailList", detailList);
	}

	/**
	 * 还款操作
	 *
	 * @param reapayment 待还记录
	 * @param isOverdue 是否垫付
	 * @param isAuto
	 * @author FangJun
	 * @date 2016年8月9日
	 */
	public void doRepay(final ProjectRepayment repayment, final boolean isOverdue, boolean isAuto) {
		LOGGER.info("还款记录(UUID= {} )本地处理开始-----------", repayment.getUuid());
		// 还款金额的限制 计算本息和加逾期罚息
		final BigDecimal repayMoney = BigDecimalUtils.add(repayment.getCapital(), repayment.getInterest(),
				repayment.getLateInterest(), repayment.getMerchantLateInterest());
		if (!BigDecimalUtils.validAmount(repayMoney)) {// 若这些之和 不是大于0 的值 不允许还款
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_REPAYMENT_MONEY_ZERO));
		}
		//判断存管方还款是否结束
		int repayed = tppTradeService.countByTypeAndProjectIdAndStatus(TppEnum.SERVICE_TYPE_BATCH_REPAY.getValue(), repayment.getProjectId());
		LOGGER.info("看放款是否到账:{}" , repayed > 0 ? "否" : "是");
		if (repayed > 0 ) {
			LOGGER.info("还款未结束，可能还款多次操作，前期回调还未处理--projectId={}", repayment.getProjectId());
			throw new BussinessException("前期还款未回调处理");
		}
		final String tppName = ConfigUtils.getTppName();
		// 三方还款任务
		final List<TppTrade> tppList = new ArrayList<TppTrade>();
		// 项目信息
		final Project project = projectService.get(repayment.getProjectId());
		// 真实还款人信息
		final User repayUser = userService.get(repayment.getRepayUserId());
		// 还款前数据、还款人可用金额校验
		validBeforeRepayment(repayment, repayUser);
		if (!isAuto) {
			TokenUtils.validShiroToken(TokenConstants.TOKEN_REPAY);
		}

		// 针对同一项目加锁处理
		RedissonDistributeLock.lock(project.getUuid());

		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionStatus status = null;
		Map<String, Object> jxRepayMap = new HashMap<>();// 江西还款参数
		Map<String, Object> jxVoucherMap = new HashMap<>();// 江西加息参数
		try {
			// 开启事务
			TransactionDefinition definition = new DefaultTransactionDefinition();
			status = transactionManager.getTransaction(definition);
			// 更新还款记录状态
			this.updateStatus(repayment.getUuid(), RepaymentEnum.STATUS_REPAID.getValue(),
					RepaymentEnum.STATUS_NOT_REPAY.getValue());
			// 修改还款金额、项目已经金额
			this.updateRepayAmount(project, repayment, isOverdue, repayUser);
			// 生成还款TPP任务
			this.createRepayTppTask(project, repayUser, repayment, tppList, isOverdue, jxRepayMap, jxVoucherMap);
			// 处理还款人资金
			this.handleBorrowerAccount(repayUser, repayment, project, isOverdue);
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		} finally {
			RedissonDistributeLock.unlock(project.getUuid());
		}

		try {
			// 清理所有项目列表缓存
			CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
			// 还款完毕 需要对债权的列表的缓存 进行清理
			CacheUtils.batchDel(CacheConstant.KEY_PREFIX_BOND_LIST);
			// 清空项目信息缓存
			CacheUtils.del(CacheConstant.KEY_PROJECT_UUID, project.getUuid());
		} catch (Exception e) {
			LOGGER.error("还款(UUID={})本地操作结束，清理缓存出错!", repayment.getUuid(), e);
		}

		if (TppServiceEnum.CHINAPNR.getName().equals(tppName)) {
			tppTradeService.sendTppTrade(tppList, MqConstant.ROUTING_KEY_TRADE_REPAY);
		}
		if (TppServiceEnum.JXBANK.getName().equals(tppName)) {
			TppService tppService = (TppService) TppUtil.tppService();
			Map<String, Object> map = null;
			if (isOverdue) {
				// noinspection unchecked
				map = (Map<String, Object>) tppService.batchBailRepay(jxRepayMap);
			} else {
				// noinspection unchecked
				map = (Map<String, Object>) tppService.tppRepayment(jxRepayMap);
			}
			String received = StringUtils.isNull(map.get("received"));
			if (!"success".equals(received)) {
				String tradeNo = (String) jxRepayMap.get("tradeNo");
				tppTradeService.updateStatusByTradeNo(tradeNo, TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(),
						"存管方接收失败！");
			}

			if (jxVoucherMap.size() > 0) {// 有加息
				// noinspection unchecked
				Map<String, Object> _map = (Map<String, Object>) tppService.batchVoucherPay(jxVoucherMap);
				String _received = StringUtils.isNull(_map.get("received"));
				if (!"success".equals(_received)) {
					String _tradeNo = (String) jxVoucherMap.get("tradeNo");
					tppTradeService.updateStatusByTradeNo(_tradeNo, TppEnum.STATUS_FAIL.getValue(),
							TppEnum.STATUS_UNDO.getValue(), "存管方接收失败！");
				}
			}
		}

		LOGGER.info("还款记录(UUID= {} )本地处理成功-----------", repayment.getUuid());
	}

	/**
	 * 修改还款金额、项目已还金额
	 *
	 * @param project 待还项目
	 * @param repayment 还款记录
	 * @param isOverdue 是否逾期
	 * @param repayUser 实际还款用户
	 * @author FangJun
	 * @date 2016年9月12日
	 */
	private void updateRepayAmount(final Project project, final ProjectRepayment repayment, final boolean isOverdue,
			final User repayUser) {
		// 更新待还记录
		final ProjectRepayment tempRepay = new ProjectRepayment();
		tempRepay.setUuid(repayment.getUuid());
		tempRepay.setRealRepayTime(DateUtils.getNow());
		tempRepay.setRepayUserId(repayUser.getUuid());
		tempRepay.setRepayType(isOverdue ? RepaymentEnum.REPAY_TYPE_GUARANTEE.getValue() : RepaymentEnum.REPAY_TYPE_NORMAL
				.getValue());
		tempRepay.setPayedAmount(BigDecimalUtils.add(repayment.getCapital(), repayment.getInterest(),
				repayment.getLateInterest(), repayment.getMerchantLateInterest()));
		super.update(tempRepay);

		// 修改项目已还本金、利息
		final Project projectTemp = new Project();
		projectTemp.setUuid(repayment.getProjectId());
		projectTemp.setRepayYesAccount(BigDecimalUtils.add(
				project.getRepayYesAccount() == null ? BigDecimal.ZERO : project.getRepayYesAccount(), repayment.getCapital()));
		projectTemp
				.setRepayYesInterest(BigDecimalUtils.add(
						project.getRepayYesInterest() == null ? BigDecimal.ZERO : project.getRepayYesInterest(),
						repayment.getInterest()));
		projectTemp.setRepayedPeriod(repayment.getPeriod());
		if (project.isInterestFinancing()) {
			// 即息理财处理
			ProjectRepayment repaymentTemp = new ProjectRepayment();
			if (repayment.getPeriods().equals(repayment.getPeriod() + 1 - CommonConstants.PROJECT_PERIODS_START)) {
				repaymentTemp.setProjectId(repayment.getProjectId());
				int unpayRecords = dao.countUnpayRecord(repaymentTemp);
				if (unpayRecords < 1) {
					// 最后一期还款，修改项目状态为：还款完成
					projectTemp.setStatus(LoanEnum.STATUS_REPAYED_SUCCESS.getValue());
					projectTemp.setRealLastRepayTime(tempRepay.getRealRepayTime());
				}
			} else {
				// 查询该项目当期是否还有待还记录
				repaymentTemp.setProjectId(repayment.getProjectId());
				repaymentTemp.setRepayTime(repayment.getRepayTime());
				int unpayRecords = dao.countUnpayRecord(repaymentTemp);
				if (unpayRecords < 1) {
					projectTemp.setNextRepayTime(dao.findNextRepayTime(repayment));
				}
			}
		} else if (repayment.getPeriods().equals(repayment.getPeriod() + 1 - CommonConstants.PROJECT_PERIODS_START)) {
			// 最后一期还款，修改项目状态为：还款完成
			projectTemp.setStatus(LoanEnum.STATUS_REPAYED_SUCCESS.getValue());
			projectTemp.setRealLastRepayTime(tempRepay.getRealRepayTime());
		} else {
			// 非最后一期更新下期还款时间
			final ProjectRepayment nextRepayment = dao.findRepayByProjectIdAndPeriod(repayment.getProjectId(),
					repayment.getPeriod() + 1, RepaymentEnum.STATUS_NOT_REPAY.getValue());
			if (nextRepayment != null) {
				projectTemp.setNextRepayTime(nextRepayment.getRepayTime());
			}
		}
		projectService.update(projectTemp);

	}

	/**
	 * 还款-处理借款人资金
	 *
	 * @param repayUser 实际还款人
	 * @param repay 还款记录
	 * @author FangJun
	 * @date 2016年8月9日
	 */
	private void handleBorrowerAccount(final User repayUser, final ProjectRepayment repay, final Project project,
			boolean isOverdue) {
		final StringBuffer infoBuffer = new StringBuffer();
		final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
		infoBuffer.append("【<a href=\"")/* .append(StringUtils.stripEnd(ConfigUtils
										 * .
										 * getValue(ConfigConstant.WEB_URL),"/")
										 * ) */
		.append(URLConstant.INVEST_DETAIL_PAGE_PREFIX).append(project.getUuid()).append("\"  target=\"_blank\">")
				.append(projectType.getTypeName()).append(StringUtils.HYPHEN).append(project.getProjectNo()).append("</a>】");
		final String info = infoBuffer.toString();

		// 需要调用Account的dubbo服务执行： 借款人账户金额处理
		final List<AccountModel> accountList = new ArrayList<AccountModel>();
		// 借款人资金日志
		final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
		// 2.1 资金日志-还款本金
		if (BigDecimalUtils.validAmount(repay.getCapital())) {
			this.createBorrowerAccountLog(repayUser.getUuid(), repay.getCapital(), Constant.REPAID_CAPITAL, accountList,
					accountLogList, info);
		}
		// 2.2 资金日志-还款利息
		if (BigDecimalUtils.validAmount(repay.getInterest())) {
			this.createBorrowerAccountLog(repayUser.getUuid(), repay.getInterest(), Constant.REPAID_INTEREST, accountList,
					accountLogList, info);
		}
		// 2.3 资金日志-还款罚息
		if (BigDecimalUtils.validAmount(repay.getLateInterest())) {
			this.createBorrowerAccountLog(repayUser.getUuid(), repay.getLateInterest(), Constant.REPAID_LATE_INTEREST,
					accountList, accountLogList, info);
		}
		
		BigDecimal borrowManageFee = repay.getBorrowManageFee();
		if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag()) && BigDecimalUtils.validAmount(borrowManageFee)) { 
			accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
					borrowManageFee.negate(), BigDecimal.ZERO));
			Map<String, Object> feeRemarkData = new HashMap<String, Object>(1);
			feeRemarkData.put("fee", borrowManageFee.doubleValue());
			String feeRemark = LogTemplateUtils.getAccountTemplate(Constant.BORROW_FEE, feeRemarkData);
			final AccountLog feeAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
					Constant.BORROW_FEE, project.getUserId(), borrowManageFee, feeRemark);
			feeAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
			feeAccountLog.setOrderNo("");
			feeAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
			accountLogList.add(feeAccountLog);
			
			//平台资金日志- 借款管理费
		    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_BORROW_FEE, project.getUserId(), borrowManageFee, "");
	}
		
		// 2.3 资金日志-还款给平台罚息 如果是垫付的 无法收取罚息
		if (BigDecimalUtils.validAmount(repay.getMerchantLateInterest()) && !isOverdue) {
			this.createBorrowerAccountLog(repayUser.getUuid(), repay.getMerchantLateInterest(),
					Constant.REPAID_MERCHANT_LATE_INTEREST, accountList, accountLogList, info);
		}
		if (CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())
				&& !UserCache.USER_NATURE_PERSON.equals(repayUser.getUserNature())) {
			accountService.saveLogs(new AccountBatchModel(null, accountLogList));
		} else {
			accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
		}
	}

	/**
	 * 创建还款人资金日志
	 *
	 * @param repayUserId 还款人ID
	 * @param money 还款金额
	 * @param accountType 还款资金类型
	 * @param accountList 资金变动列表
	 * @param accountLogList 资金日志列表
	 * @param info 　项目备注信息
	 * @author FangJun
	 * @date 2016年10月26日
	 */
	private void createBorrowerAccountLog(String repayUserId, final BigDecimal money, final String accountType,
			final List<AccountModel> accountList, final List<AccountLog> accountLogList, final String info) {
		accountList.add(new AccountModel(repayUserId, ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), money.negate(),
				BigDecimal.ZERO));
		Map<String, Object> remarkData = new HashMap<String, Object>(1);
		remarkData.put("info", info);
		remarkData.put("amount", money.doubleValue());
		String remark = LogTemplateUtils.getAccountTemplate(accountType, remarkData);
		AccountLog capitalLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), accountType, repayUserId,
				money, remark);
		if (Constant.REPAID_MERCHANT_LATE_INTEREST.equals(accountType)) {
			capitalLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
		} else {
			capitalLog.setToUser(Constant.TO_USER_INVEST);
		}
		capitalLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
		accountLogList.add(capitalLog);
	}

	/**
	 * 生成还款TPP任务
	 *
	 * @param project 项目信息
	 * @param repayUser 实际还款人
	 * @param repay 还款记录
	 * @param tppList 三方任务信息列表
	 * @param isOverdue 是否垫付
	 * @author FangJun
	 * @date 2016年8月9日
	 */
	private void createRepayTppTask(final Project project, final User repayUser, final ProjectRepayment repay,
			final List<TppTrade> tppList, final boolean isOverdue, Map<String, Object> jxRepayMap,
			Map<String, Object> jxVoucherMap) {
		// 查询当前待还记录
		final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdAndPeriod(repay.getProjectId(),
				repay.getPeriod(), repay.getInvestId());

		final List<ProjectInvest> investList = new ArrayList<>();
		final List<BondInvest> biList = new ArrayList<>();
		final List<BondCollection> bcList = new ArrayList<>();
		final Date now = DateUtils.getNow();

		// 江西银行
		BigDecimal txAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);// 还款交易总金额
		BigDecimal interestFeeTotal = BigDecimal.ZERO; // 存放累计利息管理费
		int txCounts = 0;// 还款交易总笔数
		List<Map> subPacks = new ArrayList<>();// 请求数组
		String batchNo = OrderNoUtils.getRandomStr(6);
		String tradeNo = DateUtils.dateStr7(now).concat(batchNo);
		// 加息
		BigDecimal _txAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);// 加息总金额
		int _txCounts = 0;// 加息总笔数
		List<Map> _subPacks = new ArrayList<>();// 加息请求数组
		String _batchNo = OrderNoUtils.getRandomStr(6);
		String _tradeNo = DateUtils.dateStr7(now).concat(_batchNo);
		for (ProjectCollection collection : collectionList) {
			// 本金
			final BigDecimal capital = collection.getCapital();
			// 利息
			final BigDecimal interest = collection.getInterest();
			// 加息
			final BigDecimal raiseInterest = collection.getRaiseInterest();
			// 逾期罚息
			final BigDecimal lateInterest = collection.getLateInterest();
			// 利息管理费
			final BigDecimal interestFee = BigDecimalUtils.div(BigDecimalUtils.mul(interest, project.getInterestManageRate()),
					BigDecimal.valueOf(100), 2);
			// 逾期平台罚息
			final BigDecimal merchantLateInterest = BigDecimalUtils.validAmount(collection.getMerchantLateInterest()) ? collection
					.getMerchantLateInterest() : BigDecimal.ZERO;
			// 投资收回本息（本金+利息+罚息）
			final BigDecimal capitalInterestSum = BigDecimalUtils.add(capital, interest, lateInterest);

			LOGGER.info("借款人给投资人罚息：{},借款人给平台罚息：{}", lateInterest, merchantLateInterest);

			// 对应投资记录
			ProjectInvest invest = projectInvestService.get(collection.getInvestId());
			// 投资人
			User investUser = userService.get(invest.getUserId());
			// 鉴定该笔待收是否是受让人待收
			boolean bondFlag = false;
			if (ProjectCollectionEnum.COLLECTION_TYPE_INVEST.eq(collection.getCollectionType())) {
				bondFlag = true;
			}
			LOGGER.info("【借款人还款-还款ID:{}-是否是受让人待收:{}-】", repay.getUuid(), bondFlag);

			// 1 本金+利息+逾期罚息 （借款人支出）
			// 江西银行
			if (BigDecimalUtils.validAmount(capitalInterestSum)) {
				TppTrade tpp = fillJxRepayTppTrade(project, invest, repayUser, investUser, capitalInterestSum, interestFee,
						collection, Constant.COLLECT_CAPITAL_INTEREST, isOverdue);
				tpp.setTradeNo(tradeNo);
				tpp.setProjectRepaymentId(repay.getUuid());
				Map<String, Object> subPack = new HashMap<>();
				if (isOverdue) {// 如果是担保垫付
					tpp.setServiceType(TppEnum.SERVICE_TYPE_BAIL_REPAY.getValue());
					tpp.setTppType(TppEnum.TPP_TYPE_BAIL_REPAY.getValue());
					subPack.put("orderId", tpp.getOrderNo());
					subPack.put("txCapAmout", capital.setScale(2, BigDecimal.ROUND_HALF_UP));
					subPack.put("txIntAmount", interest.add(lateInterest).setScale(2, BigDecimal.ROUND_HALF_UP));
					subPack.put("txAmount", capitalInterestSum.setScale(2, BigDecimal.ROUND_HALF_UP));
					subPack.put("forAccountId", investUser.getTppUserAccId());
					subPack.put("orgOrderId", invest.getInvestOrderNo());
					subPack.put("orgTxAmount", invest.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));
					if (interestFee.compareTo(BigDecimal.ZERO) > 0) {
						interestFeeTotal = interestFeeTotal.add(interestFee);
						subPack.put("txFee", interestFee.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					txAmount = txAmount.add(capitalInterestSum.setScale(2, BigDecimal.ROUND_HALF_UP));
				} else {
					subPack.put("orderId", tpp.getOrderNo());
					subPack.put("accountId", repayUser.getTppUserAccId());
					subPack.put("txAmount", capital.setScale(2, BigDecimal.ROUND_HALF_UP));
					subPack.put("intAmount", interest.add(lateInterest).setScale(2, BigDecimal.ROUND_HALF_UP));
					//加上借款人管理费
					BigDecimal borrowManageFee = BigDecimalUtils.validAmount(collection.getBorrowManageFee()) ? collection.getBorrowManageFee() : BigDecimal.ZERO;
					if (merchantLateInterest.compareTo(BigDecimal.ZERO) > 0 || borrowManageFee.compareTo(BigDecimal.ZERO) > 0) {
//						subPack.put("txFeeOut", merchantLateInterest.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款罚息
						subPack.put("txFeeOut",
								BigDecimalUtils.round(BigDecimalUtils.add(borrowManageFee, merchantLateInterest)));
						LOGGER.info("borrowManageFee:{},merchantLateInterest:{}", borrowManageFee, merchantLateInterest);
					}
					if (interestFee.compareTo(BigDecimal.ZERO) > 0) {
						interestFeeTotal = interestFeeTotal.add(interestFee);
						subPack.put("txFeeIn", interestFee.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					subPack.put("forAccountId", investUser.getTppUserAccId());
					subPack.put("productId", project.getUuid());
					subPack.put("authCode", invest.getAuthCode());
					txAmount = txAmount.add(capital.setScale(2, BigDecimal.ROUND_HALF_UP));
					tpp.setAuthCode(invest.getAuthCode());
				}
				tppList.add(tpp);
				subPacks.add(subPack);
				txCounts++;
			}
			// 2 资金日志-还款加息（平台支出）
			if (BigDecimalUtils.validAmount(raiseInterest)) {
				TppTrade tpp = fillJxVoucherTppTrade(project, invest, investUser, raiseInterest);
				tpp.setTradeNo(_tradeNo);
				tpp.setProjectRepaymentId(repay.getUuid());
				Map<String, Object> subPack = new HashMap<>();
				subPack.put("orderId", tpp.getOrderNo());
				subPack.put("txAmount", raiseInterest.setScale(2, BigDecimal.ROUND_HALF_UP));
				subPack.put("forAccountId", investUser.getTppUserAccId());
				subPack.put("voucherType", "002");
				subPack.put("name", investUser.getRealName());
				_subPacks.add(subPack);
				_txAmount = _txAmount.add(raiseInterest.setScale(2, BigDecimal.ROUND_HALF_UP));
				_txCounts++;
				tppList.add(tpp);
			}

			// 3 资金日志-还款罚息（还款给平台方罚息）
			// if (BigDecimalUtils.validAmount(lateInterest) &&
			// BigDecimalUtils.validAmount(merchantLateInterest)) {
			// tppList.add(fillTppTrade(project, invest, repayUser,
			// merchantLateInterest,
			// Constant.TYPE_OVERDUE_MERCHANT, merchantLateInterest, bondFlag,
			// isOverdue, collection));
			// }
			// 已变现投资
			if (InvestEnum.REALIZE_FLAG_TENDER.eq(invest.getRealizeFlag())) {
				invest.setFreezeCapital(BigDecimalUtils.add(invest.getFreezeCapital(), collection.getFreezeCapital()));
				invest.setFreezeInterest(BigDecimalUtils.add(invest.getFreezeInterest(), collection.getFreezeInterest()));
			}// Realize end!
				// 更新待收记录
			collection.setStatus(ProjectCollectionEnum.STATUS_PAID.getValue());
			collection.setManageFee(interestFee);// 存入利息管理费
			collection.setLastRepayTime(now);
			collection.setRepayedAmount(BigDecimalUtils.add(capital, raiseInterest, interest, lateInterest));

			// 更新投资记录
			invest.setRepayedAmount(BigDecimalUtils.add(invest.getRepayedAmount(), collection.getRepayedAmount()));
			invest.setRepayedInterest(BigDecimalUtils.add(invest.getRepayedInterest(), interest, raiseInterest, lateInterest,
					interestFee.negate()));
			invest.setWaitAmount(BigDecimalUtils.add(invest.getWaitAmount(), capital.negate(), raiseInterest.negate(),
					interest.negate()));
			invest.setWaitInterest(BigDecimalUtils.add(invest.getWaitInterest(), interest.negate(), raiseInterest.negate()));
			invest.setWaitRaiseInterest(BigDecimalUtils.add(invest.getWaitRaiseInterest(), raiseInterest.negate()));
			// 最后一期保存结束日
			if (collection.getPeriod() == collection.getPeriods()) {
				invest.setEndDate(now);
			}
			investList.add(invest);

			// 受让投资时 更新债权投资记录
			BondInvest bondInvest = new BondInvest();
			if (InvestEnum.INVEST_STYLE_BUY.eq(invest.getInvestStyle())) {
				LOGGER.info("【债权还款--更新受让投资--原始标投资记录投资方式:{}--对应的债权投资订单号:{}】", invest.getInvestStyle(), invest.getInvestOrderNo());
				bondInvest = bondInvestService.getBondInvestByOrderNo(invest.getInvestOrderNo());
				bondInvest.setPayedInterest(invest.getRepayedInterest());//TODO 还是更新在原有债权上
				biList.add(bondInvest);
			}

			// 受让待收时 更新债权待收记录
			if (bondFlag) {
				LOGGER.info("【债权还款--collectionId:{}，还款本金：{},还款利息：{},collectionInvestId:{},期数：{}】", collection.getUuid(), capital,
						interest, collection.getInvestId(), collection.getPeriod());
				collection.setBondCapital(capital);
				collection.setBondInterest(interest);
				BondCollection bondCollection = bondCollectionService.getBondCollectionByInvestIdAndPeriod(
						collection.getInvestId(), collection.getPeriod());
				if (bondCollection == null) {
//					throw new BussinessException(ResourceUtils.get(BondResource.BOND_COLLECTION_IS_NOT_EXISTS));//TODO 多次转让会有问题
				} else {
					bondCollection.setStatus(BondCollectionEnum.STATUS_PAID.getValue());
					bondCollection.setBondInterest(interest);
					bondCollection.setBondCapital(capital);
					bondCollection.setReceivedAmount(BigDecimalUtils.add(capital, interest));
					bondCollection.setReceivedTime(now);
					bcList.add(bondCollection);
				}
			}
		} // for end!

		// 江西银行处理
		if (isOverdue) {
			jxRepayMap.put("accountId", repayUser.getTppUserAccId());
			jxRepayMap.put("productId", project.getUuid());
		}
		jxRepayMap.put("txAmount", txAmount);
		jxRepayMap.put("txCounts", txCounts);
		jxRepayMap.put("subPacks", subPacks);
		jxRepayMap.put("batchNo", batchNo);
		jxRepayMap.put("tradeNo", tradeNo);
		// 保存批量放款记录
		TppTrade tpp = new TppTrade();
		if (isOverdue) {
			tpp.setTppType(TppEnum.TPP_TYPE_BATCH_BAIL_REPAY.getValue());
			tpp.setServiceType(TppEnum.SERVICE_TYPE_BATCH_BAIL_REPAY.getValue());
		} else {
			tpp.setTppType(TppEnum.TPP_TYPE_BATCH_REPAY.getValue());
			tpp.setServiceType(TppEnum.SERVICE_TYPE_BATCH_REPAY.getValue());
		}
		tpp.setUserId(repayUser.getUuid());
		tpp.setTppUserCustId(repayUser.getTppUserCustId());
		tpp.setMoney(txAmount);
		tpp.setCreateTime(now);
		tpp.setStatus(TppEnum.STATUS_UNDO.getValue());
		tpp.setTradeNo(tradeNo);
		tpp.setTradeDate(now);
		tpp.setProjectId(project.getUuid());
		tpp.setInvestProjectId(project.getUuid());
		tpp.setServFee(interestFeeTotal);
		tpp.setProjectRepaymentId(repay.getUuid());
		tppList.add(tpp);
		// 加息记录
		if (_txCounts > 0) {
			jxVoucherMap.put("txAmount", _txAmount);
			jxVoucherMap.put("txCounts", _txCounts);
			jxVoucherMap.put("subPacks", _subPacks);
			jxVoucherMap.put("batchNo", _batchNo);
			jxVoucherMap.put("tradeNo", _tradeNo);
			// 保存批量发红包记录
			TppTrade _tpp = new TppTrade();
			_tpp.setServiceType(TppEnum.SERVICE_TYPE_BATCH_VOUCHER.getValue());
			_tpp.setTppType(TppEnum.TPP_TYPE_BATCH_VOUCHER.getValue());
			_tpp.setUserId(ConfigUtils.getValue(Constant.ADMIN_ID));
			_tpp.setTppUserCustId(ConfigUtils.getValue(Constant.ADMIN_ID));
			_tpp.setMoney(_txAmount);
			_tpp.setCreateTime(now);
			_tpp.setStatus(TppEnum.STATUS_UNDO.getValue());
			_tpp.setTradeNo(_tradeNo);
			_tpp.setTradeDate(now);
			_tpp.setProjectId(project.getUuid());
			_tpp.setInvestProjectId(project.getUuid());
			_tpp.setProjectRepaymentId(repay.getUuid());
			tppList.add(_tpp);
		}

		if (CollectionUtils.isNotEmpty(investList)) {
			projectInvestService.updateBatch(investList);
		}
		if (CollectionUtils.isNotEmpty(biList)) {
			bondInvestService.updateBatch(biList);
		}
		if (CollectionUtils.isNotEmpty(bcList)) {
			bondCollectionService.updateBatch(bcList);
		}
		if (CollectionUtils.isNotEmpty(collectionList)) {
			projectCollectionService.updateBatch(collectionList);
		}
		if (CollectionUtils.isNotEmpty(tppList)) {
			tppTradeService.insertBatch(tppList);
		}

	}

	/**
	 * 江西银行填充还款交易记录
	 *
	 * @param project
	 * @param invest
	 * @param repayUser
	 * @param investUser
	 * @param money
	 * @param fee
	 * @param collection
	 * @param accountType
	 * @param isOverdue
	 * @return
	 */
	private TppTrade fillJxRepayTppTrade(final Project project, final ProjectInvest invest, final User repayUser,
			final User investUser, final BigDecimal money, final BigDecimal fee, final ProjectCollection collection,
			final String accountType, final boolean isOverdue) {
		TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_REPAY.getValue(), TppEnum.TPP_TYPE_REPAY.getValue(),
				repayUser.getUuid(), repayUser.getTppUserCustId(), investUser.getUuid(), investUser.getTppUserCustId(), money,
				invest.getUuid());
		tpp.setInvestProjectId(project.getUuid());// 项目UUID
		tpp.setProjectId(project.getUuid());
		tpp.setServFee(fee); // 利息管理费
		if (Constant.COLLECT_ADD_INTEREST.equals(accountType)) {// 加息
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_TWO.getValue());
		} else if (isOverdue) {// 垫付
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_FOUR.getValue());
		} else if (ProjectCollectionEnum.COLLECTION_TYPE_INVEST.eq(collection.getCollectionType())) {// 债权
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_SIX.getValue());
		} else {// 逾期利息收回 走借款人还款通道
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_ONE.getValue());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("collectionId", collection.getUuid());
		map.put("accountType", accountType);
		map.put("repayType", tpp.getRepayType());
		map.put("capital", collection.getCapital());
		map.put("interest", collection.getInterest());
		map.put("raiseInterest", collection.getRaiseInterest());
		map.put("lateInterest", collection.getLateInterest());
		map.put("merchantLateInterest", collection.getMerchantLateInterest());
		map.put("borrowManageFee", collection.getBorrowManageFee());
		map.put("isOverdue", isOverdue);
		map.put("orgOrderId", invest.getInvestOrderNo());
		map.put("orgTxAmount", invest.getAmount());
		tpp.setParams(JSON.toJSONString(map));
		return tpp;
	}

	/**
	 * 江西加息
	 *
	 * @param project
	 * @param invest
	 * @param investUser
	 * @param money
	 * @return
	 */
	private TppTrade fillJxVoucherTppTrade(final Project project, final ProjectInvest invest, final User investUser,
			final BigDecimal money) {
		TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_VOUCHER.getValue(), TppEnum.TPP_TYPE_VOUCHER.getValue(),
				ConfigUtils.getValue(Constant.ADMIN_ID), ConfigUtils.getValue(Constant.ADMIN_ID), investUser.getUuid(),
				investUser.getTppUserCustId(), money, invest.getUuid());
		tpp.setInvestProjectId(project.getUuid());// 项目UUID
		tpp.setProjectId(project.getUuid());
		tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_TWO.getValue());
		Map<String, Object> map = new HashMap<>();
		map.put("accountType", Constant.COLLECT_ADD_INTEREST);
		map.put("repayType", tpp.getRepayType());
		map.put("voucherType", "002");
		map.put("name", investUser.getRealName());
		tpp.setParams(JSON.toJSONString(map));
		return tpp;
	}

	/**
	 * 填充三方交易记录
	 *
	 * @param project 借款项目
	 * @param invest 投资记录
	 * @param repayUser 实际还款人
	 * @param money 金额
	 * @param accountType 资金类型 collect_capital_interest 本金+利息 +逾期利息
	 *            collect_add_interest 加息
	 * @param fee 手续费
	 * @param isOverdue 是否垫付
	 * @param isAddInterest 是否加息
	 * @return 三方交易记录
	 * @author FangJun
	 * @date 2016年8月9日
	 */
	@Override
	public TppTrade fillTppTrade(final Project project, final ProjectInvest invest, final User repayUser, final BigDecimal money,
			final String accountType, final BigDecimal fee, boolean bondFlag, final boolean isOverdue,
			final ProjectCollection collection) {
		User investUser = userService.get(invest.getUserId());
		final String tppUserCustId = Constant.COLLECT_ADD_INTEREST.equals(accountType) ? ConfigUtils
				.getValue(Constant.UFX_CUSTOMERID) : repayUser.getTppUserCustId();// 加息利息走商户平台代偿
		final String payUserId = Constant.COLLECT_ADD_INTEREST.equals(accountType) ? ConfigUtils.getValue(Constant.ADMIN_ID)
				: repayUser.getUuid();// 加息利息走商户平台代偿
		TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_REPAY.getValue(), TppEnum.TPP_TYPE_REPAY.getValue(), payUserId,
				tppUserCustId, invest.getUserId(), investUser.getTppUserCustId(), money, invest.getUuid());
		tpp.setInvestId(invest.getUuid());
		tpp.setInvestProjectId(project.getUuid());// 项目UUID
		tpp.setProjectId(project.getProjectNo());
		tpp.setTradeDate(DateUtils.valueOf(invest.getInvestDate(), DateUtils.DATEFORMAT_STR_012));
		tpp.setServFee(fee); // 利息管理费
		// 还款类型有顺序的 商户平台优先--然后是垫付---然后是债权还款--最后是融资还款
		if (Constant.COLLECT_ADD_INTEREST.equals(accountType)) {// 加息利息走商户平台代偿
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_TWO.getValue());
		} else if (isOverdue) {
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_FOUR.getValue());
		} else if (bondFlag) {
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_SIX.getValue());
		} else {// 逾期利息收回 走借款人还款通道
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_ONE.getValue());
		}
		final StringBuilder buffer = new StringBuilder();
		buffer.append("{").append("collectionId:").append(collection.getUuid()) // 待还记录ID
				.append(",accountType:").append(accountType) // 资金类型
				.append(",isOverdue:").append(isOverdue) // 是否垫付
				.append(",repayType:").append(tpp.getRepayType()) // 还款类型
				.append(",fundFlow:").append(UfxRepaymentModelEnum.FUND_FLOW_TWO.getValue()) // 资金流向
				.append(",projectNo:").append(project.getProjectNo())// 项目No
				.append(",investNo:").append(invest.getInvestNo()) // 投资流水号
				.append(",userId:").append(investUser.getUserNo()) // 投资人NO
				.append(",realName:").append(investUser.getRealName()) // 投资人真实姓名
				.append(",capital:").append(BigDecimalUtils.round(collection.getCapital()).toString())// 本金
				.append(",interest:").append(BigDecimalUtils.round(collection.getInterest()).toString())// 利息
				.append(",raiseInterest:").append(BigDecimalUtils.round(collection.getRaiseInterest()).toString())// 加息
				.append(",lateInterest:").append(BigDecimalUtils.round(collection.getLateInterest()).toString());// 逾期利息
		if (Constant.COLLECT_CAPITAL_INTEREST.equals(accountType)) {
			buffer.append(",sumIncome:")
					.append(BigDecimalUtils.round(BigDecimalUtils.add(collection.getInterest(), collection.getLateInterest()))
							.toString());
		} else {
			// 加息、平台收取罚息
			buffer.append(",sumIncome:").append(BigDecimalUtils.round(money).toString());// 总收益
		}
		buffer.append(",sponsorer:").append(userService.get(project.getUserId()).getTppUserCustId())// 借款人账户,汇付平台出账必填
				.append("}");
		tpp.setParams(buffer.toString());

		return tpp;
	}

	/**
	 * 还款前的数据校验
	 *
	 * @param repayment 还款记录
	 * @param repayUser 还款人
	 * @author FangJun
	 * @date 2016年8月2日
	 */
	@SuppressWarnings("unchecked")
	private void validBeforeRepayment(final ProjectRepayment repayment, final User repayUser) {
		if (repayment == null) {
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_NOEXISTS), BussinessException.TYPE_JSON);
		}
		// 还款记录状态校验
		if (!RepaymentEnum.STATUS_NOT_REPAY.eq(repayment.getStatus())) {
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_HASREPAID), BussinessException.TYPE_JSON);
		}
		// 是否存在转让中的借款
		int num = bondService.getCountByStatus(repayment.getProjectId(), BondEnum.STATUS_PUBLISH.getValue());
		if (num > 0) {
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_HASBOND), BussinessException.TYPE_JSON);
		}
		// 需支付总额
		final BigDecimal total = BigDecimalUtils.round(BigDecimalUtils.add(repayment.getCapital(), repayment.getInterest(),
				repayment.getLateInterest(), repayment.getMerchantLateInterest()));
		// 渤海银行企业借款 跳过资金校验
		if (CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())
				&& !UserCache.USER_NATURE_PERSON.equals(repayUser.getUserNature())) {
			LOGGER.info("渤海银行企业借款 跳过资金校验");
		} else {
			// 借款人资金校验
			Account localBorrowerAccount = accountService.getMoneyByUserId(new AccountQueryModel(repayUser.getUuid(), ConfigUtils
					.getValue(ConfigConstant.ACCOUNT_CODE)));
			// 查询校验提现账户第三方可用和平台可用是否一致
			final Map<String, Object> jxMap = new HashMap<>();
			jxMap.put("accountId", repayUser.getTppUserCustId());
			BigDecimal tppMoney = BigDecimal.ZERO;
			try {
				TppService tppService = (TppService) TppUtil.tppService();
				Object object = tppService.tppQueryBalance(jxMap);
				Map<String, Object> map = (Map<String, Object>) object;
				tppMoney = new BigDecimal(map.get("avlBal").toString());
			} catch (Exception e) {
				LOGGER.error("还款前，查询第三方资金异常，{}！", e.getMessage());
				throw new BussinessException(ResourceUtils.get(LoanResource.TPP_QUERY_ACCOUNT_FAIL));
			}
			if (localBorrowerAccount.getUseMoney().compareTo(total) < 0) {
				LOGGER.error("待还记录（uuid= {}）还款前校验，本地可用资金不足！", repayment.getUuid());
				throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_USEMONEY_NOT_ENOUGH));
			}
			if (tppMoney.compareTo(total) < 0) {
				LOGGER.error("待还记录（uuid= {}）还款前校验，三方可用资金不足！", repayment.getUuid());
				throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_USEMONEY_NOT_ENOUGH));
			}
		}

		// 非即息理财还款，做还款期数校验
		if (StringUtils.isBlank(repayment.getInvestId())) {
			// 指定还款记录之前还款状态校验（projectId,period,investId,repayTime）
			ProjectRepayment repaymentTemp = new ProjectRepayment();
			repaymentTemp.setProjectId(repayment.getProjectId());
			repaymentTemp.setPeriod(repayment.getPeriod());
			// repaymentTemp.setInvestId(repayment.getInvestId());
			int unpayRecords = dao.countUnpayRecord(repaymentTemp);
			if (unpayRecords > 0) {
				throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_HASUNPAYRECORDS),
						BussinessException.TYPE_CLOSE);
			}
		}
	}

	@Override
	public Page<ProjectRepayment> findByProjectId(ProjectRepayment model) {
		if (StringUtils.isBlank(model.getProjectId())) {
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
		}
		Project project = projectService.get(model.getProjectId());
		if (project == null) {
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		Page<ProjectRepayment> page = model.getPage();
		if (page == null) {
			page = new Page<ProjectRepayment>();
		}
		page.setRows(dao.findByProjectId(model));
		return page;
	}

	@Override
	public Page<ProjectRepayment> findByDate(ProjectRepayment model) {
		Page<ProjectRepayment> page = model.getPage();
		page.setRows(dao.findByDate(model));
		return page;
	}

	@Override
	public ProjectRepayment get(String id) {
		ProjectRepayment repay = super.get(id);

		Project project = projectService.get(repay.getProjectId());
		repay.setProjectName(project.getProjectName());
		repay.setProjectNo(project.getProjectNo());

		User user = userService.get(project.getUserId());
		repay.setUserName(user.getUserName());
		repay.setRealName(user.getRealName());
		repay.setMobile(user.getMobile());
		return super.get(id);
	}

	@Override
	public void updateStatus(String uuid, String status, String preStatus) {
		int result = dao.updateStatus(uuid, status, preStatus);
		if (result == Constant.INT_ZERO) {
			String msg = "还款状态更新失败 [uuid=" + uuid + ", status=" + status + ", preStatus=" + preStatus + "]";
			LOGGER.error(msg);
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
	}

	@Transactional
	@Override
	public void overdueInterestHandle() {
		int overdueDays = Constant.INT_ZERO;
		BigDecimal overdueInterest = BigDecimal.ZERO;
		BigDecimal payment = BigDecimal.ZERO;
		Date now = DateUtils.getNow();// 当前时间
		Integer period = null; // 期数
		// 1、查询所有已经逾期 未还的数据
		ProjectRepayment overdue = new ProjectRepayment();
		overdue.setStatus(RepaymentEnum.STATUS_NOT_REPAY.getValue());
		overdue.setEndTime(DateUtils.dateStr4(now));// 还款时间小于当前时间
		overdue.setBorrowFlag(LoanEnum.BORROW_FLAG_BORROW.getValue());// 只有借款才统计逾期
		List<ProjectRepayment> overdueList = this.findList(overdue);
		if (CollectionUtils.isNotEmpty(overdueList)) {
			final List<ProjectRepayment> needUpdateRepayList = new ArrayList<ProjectRepayment>();
			final List<ProjectCollection> needUpdateCollectionList = new ArrayList<ProjectCollection>();
			boolean feeInterest = false;
			for (final ProjectRepayment repay : overdueList) {
				LOGGER.info("逾期罚息的projectId:{}", repay.getProjectId());
				// a)查询逾期罚息的利率 --随产品
				final BigDecimal overdueFeeRate = repay.getOverdueFeeRate() == null ? BigDecimal.ZERO : repay.getOverdueFeeRate();
				// b）逾期天数 预计还款时间 与当前的时间天数差
				overdueDays = DateUtils.daysBetween(repay.getRepayTime(), now);
				payment = BigDecimalUtils.add(repay.getCapital(), repay.getInterest());// 预还金额
																						// 本金和利息之和
				if (overdueDays > Constant.INT_ZERO) {// 逾期天数大于零 开始计算逾期利息
					final int free_overdue_day = ConfigUtils.getInt(ConfigConstant.FREE_OVERDUE_DAY);
					if (overdueDays <= free_overdue_day) {// 免罚息 逾期罚息为0
															// 但是逾期天数照常统计
						overdueInterest = BigDecimal.ZERO;
						feeInterest = true;
					} else {// c)逾期利息 ： 本金和利息之和 * 逾期利率 * 逾期天数 超过免费天数
							// 按照全部的天数计算逾期天数和罚息
						feeInterest = false;
						overdueInterest = BigDecimalUtils.round(BigDecimalUtils.mul(payment, overdueFeeRate,
								BigDecimal.valueOf(overdueDays)));
					}
					LOGGER.info("-------------逾期罚息,逾期天数：{},逾期免费天数：{}，逾期利息：{}，逾期真实天数:{},是否免息：{}------------------start",
							overdueDays, free_overdue_day, overdueInterest, overdueDays, feeInterest);
				}

				// d)逾期罚息的分配
				final BigDecimal lateInterestRate = ConfigUtils.getBigDecimal(ConfigConstant.LATE_INTEREST_RATE);// 逾期利息划给用户比例
				if (lateInterestRate.compareTo(BigDecimal.ONE) > Constant.INT_ZERO
						|| lateInterestRate.compareTo(BigDecimal.ZERO) < Constant.INT_ZERO) {// 如果比例大于1或者小于0
																								// 则不执行
					LOGGER.info("--------------逾期罚息划给用户的比例超过100%：{}------------------", lateInterestRate);
				} else {
					final BigDecimal toMerchantLateRate = BigDecimal.ONE.subtract(lateInterestRate);
					final BigDecimal toUserOverdueInterest = BigDecimalUtils.round(
							BigDecimalUtils.mul(overdueInterest, lateInterestRate), Constant.INT_TWO);// 计算出给投资人的逾期罚息
																										// 保留2位有效数字
					final BigDecimal toMerchantOverdueInterest = overdueInterest.subtract(toUserOverdueInterest);// 给平台的逾期罚息
					if (overdueDays > Constant.INT_ZERO) {// 逾期天数大于零就是逾期
						// 1、 更新投资人相关collection 逾期天数 和逾期利息
						period = repay.getPeriod();
						final List<ProjectCollection> collectionList = projectCollectionService.findByProjectIdAndPeriod(
								repay.getProjectId(), period, repay.getInvestId());
						BigDecimal sumCollectionInterest = BigDecimal.ZERO;// 投资人罚息统计
						BigDecimal sumMerCollectionInterest = BigDecimal.ZERO;// 平台罚息统计
						if (CollectionUtils.isNotEmpty(collectionList)) {
							LOGGER.info("--------------计算出来的还款记录：{},期数:{},总罚息{},分配比例：{},平台比例：{},是否免息：{}------------------start",
									repay.getUuid(), period, overdueInterest, lateInterestRate, toMerchantLateRate, feeInterest);
							for (int i = Constant.INT_ZERO; i < collectionList.size(); i++) {
								final ProjectCollection collection = collectionList.get(i);
								collection.setLateDays(overdueDays);
								final BigDecimal capitalAndInterest = collection.getCapital().add(collection.getInterest());// 待收本息和
								// 罚息给投资人 免罚息的时候为0
								final BigDecimal lateInterest = feeInterest == true ? BigDecimal.ZERO : BigDecimalUtils.round(
										BigDecimalUtils.mul(capitalAndInterest, overdueFeeRate, BigDecimal.valueOf(overdueDays),
												lateInterestRate), Constant.INT_TWO);// 保留2位有效数字
								// 罚息给平台 免罚息的时候为0
								final BigDecimal merchantLateInterest = feeInterest == true ? BigDecimal.ZERO : BigDecimalUtils
										.round(BigDecimalUtils.mul(capitalAndInterest, overdueFeeRate,
												BigDecimal.valueOf(overdueDays), toMerchantLateRate), Constant.INT_TWO);
								collection.setLateInterest(lateInterest);
								collection.setMerchantLateInterest(merchantLateInterest);

								// 统计所有需要还罚息的 逾期利息之和
								if (ProjectCollectionEnum.COLLECTION_TYPE_COMMON.eq(collection.getCollectionType())
										|| ProjectCollectionEnum.COLLECTION_TYPE_INVEST.eq(collection.getCollectionType())) {// 只有普通待收和受让待收才累加
																																// 以保证总的罚息
									LOGGER.info("-----每期待收本息：{},逾期天数：{},逾期利率：{},投资人罚息:{},平台罚息：{}-----", capitalAndInterest,
											overdueDays, overdueFeeRate, lateInterest, merchantLateInterest);
									sumCollectionInterest = sumCollectionInterest.add(lateInterest);
									sumMerCollectionInterest = sumMerCollectionInterest.add(merchantLateInterest);
								}
								needUpdateCollectionList.add(collection);
							}
							LOGGER.info("--------------计算出来的还款记录：{},期数:{}计算总罚息{}------------------end", repay.getUuid(), period,
									sumCollectionInterest.add(sumMerCollectionInterest));
							// 2、更新借款人和平台将待收计算得出的总和作为投资人还款的的逾期总利息
							// 更新相关repayRepayment 逾期天数 和逾期利息
							repay.setLateDays(overdueDays);
							if (sumCollectionInterest.compareTo(toUserOverdueInterest) == Constant.INT_ZERO) {// 投资人罚息平衡
								repay.setLateInterest(toUserOverdueInterest);
							} else {// 投资人罚息不平衡
								repay.setLateInterest(sumCollectionInterest);
							}
							if (sumMerCollectionInterest.compareTo(toMerchantOverdueInterest) == Constant.INT_ZERO) {// 平台罚息平衡
								repay.setMerchantLateInterest(toMerchantOverdueInterest);
							} else {// 平台罚息不平衡
								repay.setMerchantLateInterest(sumMerCollectionInterest);
							}
							LOGGER.info("--------------借款人还款---投资人罚息：{},平台罚息：{},总罚息：{}------------------end",
									repay.getLateInterest(), repay.getMerchantLateInterest(),
									(repay.getLateInterest().add(repay.getMerchantLateInterest())));
						}
						needUpdateRepayList.add(repay);
					}
				}
			}
			if (CollectionUtils.isNotEmpty(needUpdateRepayList)) {
				dao.updateBatchLateInterest(needUpdateRepayList);
			}
			if (CollectionUtils.isNotEmpty(needUpdateCollectionList)) {
				projectCollectionService.updateBatchLateInterest(needUpdateCollectionList);
			}
		}
	}

	@Override
	public boolean checkOverDueByUser(String userId) {
		if (dao.countOverDueByUser(userId) > 0) {
			return true;
		}
		return false;
	}

	@Override
	public int updateOverdueInfo(ProjectRepayment model) {
		return dao.updateOverdueInfo(model);
	}

	@Override
	public int getRepayPeriodsByProjectIdAndStatus(final String projectId, final String status) {
		return dao.getRepayPeriodsByProjectIdAndStatus(projectId, status);
	}

	@Override
	public Date getLastRepayDay(String projectId) {
		return dao.getLastRepayDay(projectId);
	}

	@Override
	public Page<ProjectRepayment> findOverduePage(ProjectRepayment model) {
		Page<ProjectRepayment> page = model.getPage() == null ? new Page<ProjectRepayment>() : model.getPage();
		page.setRows(dao.findOverdueList(model));
		return page;
	}

	@Override
	public void updateBatchLateInterest(List<ProjectRepayment> list) {
		dao.updateBatchLateInterest(list);
	}

	@Override
	public void autoRepay() {
		// 获取单日需还款的待还记录
		List<ProjectRepayment> needRepayList = dao.findTodayRepay();
		LOGGER.info("自动还款-begin--条数{}", CollectionUtils.size(needRepayList));
		// 逐个还款
		for (ProjectRepayment repay : needRepayList) {
			try {
				if (CbhbConstant.CBHB_TPP_NAME.equals(ConfigUtils.getTppName())) {
					this.doCbhbRepay(repay, false, true);
				} else {
					this.doRepay(repay, false, true);
				}
			} catch (Exception e) {
				LOGGER.error("自动还款失败(repay uuid={})", repay.getUuid(), e);
			}
		}
	}

	@Override
	public Page<ProjectRepayment> findAdvance(ProjectRepayment model) {
		Page<ProjectRepayment> page = model.getPage();
		if (page == null) {
			page = new Page<ProjectRepayment>();
		}
		page.setRows(dao.findAdvance(model));
		return page;
	}

	@Override
	public int offlineRepay(String uuid) {
		return dao.updateRepayType(uuid, RepaymentEnum.REPAY_TYPE_REPAYED_GUARANTEE.getValue(),
				RepaymentEnum.REPAY_TYPE_GUARANTEE.getValue());
	}

	public void repaymentTimeoutHandle() {
		ProjectRepayment model = new ProjectRepayment();
		List<ProjectRepayment> list = findPage(model).getRows();
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		Date nowDate = DateUtils.getToday();
		c2.setTime(nowDate);
		for (ProjectRepayment projectRepayment : list) {
			LOGGER.info("待处理:{}", projectRepayment);
			Date repayDate = projectRepayment.getRepayTime();
			c1.setTime(repayDate);
			LOGGER.info("待处理:{}", nowDate);
			int days = c1.get(Calendar.DAY_OF_YEAR) - c2.get(Calendar.DAY_OF_YEAR);
			LOGGER.info("待处理:{}", days);
			// 三天内还款信息
			if (days <= 2 && days >= 0) {
				repaymentPhoneCode(projectRepayment.getUuid());
			}
		}
	}

	/**
	 * 短信催收还款
	 */
	public void repaymentPhoneCode(String id) {
		final ProjectRepayment pr = get(id);
		String userId = pr.getUserId();
		final User user = userService.get(userId);
		String projectId = pr.getProjectId();
		final Project p = projectService.get(projectId);
		String mobilePhone = user.getMobile();
		pr.setMobile(mobilePhone);
		final String functionType = MessageConstant.URGE_REPAYMENT;
		final Map<String, Object> sendData = new HashMap();
		sendData.put("mobilePhone", mobilePhone);
		sendData.put("user", user);
		sendData.put("createTime", DateUtils.dateStr4(pr.getRepayTime()));
		sendData.put("payment", pr.getPayment());
		sendData.put("projectName", p.getProjectName());
		sendData.put("payedAmount", pr.getPayedAmount());
		final MessageType msgType = msgTypeService.find(functionType, MessageConstant.MESSAGE_SMS);
		if (msgType != null && CommonEnum.YES.eq(msgType.getSend())) {
			final Message msg = new Message();
			msg.setMessageType(functionType);
			msg.setSendType(String.valueOf(MessageConstant.MESSAGE_SMS));
			msg.setSendUser(MessageConstant.USER_ID_ADMIN);
			msg.setReceiveUser(userId);
			msg.setTitle(FreemarkerUtil.renderTemplate(msgType.getMessageTitle(), sendData));
			msg.setContent(FreemarkerUtil.renderTemplate(msgType.getMessageContent(), sendData));
			msg.setReceiveAddr(mobilePhone);
			// 内容不为空
			if (StringUtils.isNotBlank(msg.getContent())) {
				if (ConfigUtils.isOpenMq()) {
					// 加入队列
					rabbitProducer.send(MqConstant.ROUTING_KEY_MESSAGE, msg);
				} else {
					// 不加入队列
					messageService.sendMessage(msg);
				}
			}
		}
		Operator operator = (Operator) PrincipalUtils.getPrincipal();
		/* 添加催收记录 */
		UrgeRepayLog urgeRepayLog = new UrgeRepayLog();
		urgeRepayLog.setRepaymentId(userId);
		urgeRepayLog.setOperatorId(operator.getUuid());
		urgeRepayLog.setCreateTime(DateUtils.getNow());
		urgeRepayLog.setRemark("电话催收");
		urgeRepayLogService.save(urgeRepayLog);
	}

	@Override
	public BigDecimal getTotalCollection() {
		BigDecimal total = dao.getTotalCollection();
		if (total == null) {
			total = BigDecimal.ZERO;
		}
		return total;
	}

	@Override
	public BigDecimal sumTotalCollectionByDate(String startDate, String endDate) {
		BigDecimal total = dao.sumTotalCollectionByDate(startDate, endDate);
		if (total == null) {
			total = BigDecimal.ZERO;
		}
		return total;
	}

	@Override
	public void repayBail(String repaymentId, String mobile, String code, String repayUserId) {
		// 校验验证码
		if (!ValidateUtils.checkCode(mobile, MessageConstant.MESSAGE_USER_REPAY_PHONECODE, code)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.VALID_CODE_ERROR), BussinessException.TYPE_JSON);
		}
		if (StringUtils.isBlank(repaymentId)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_NO_RECORD), BussinessException.TYPE_JSON);
		}
		// 投资记录
		final ProjectRepayment repayment = dao.get(repaymentId);
		if (repayment == null) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS),
					BussinessException.TYPE_JSON);
		}
		final Project project = projectService.get(repayment.getProjectId());
		if (project == null) {
			throw new BussinessException("借款标的不存在！", BussinessException.TYPE_JSON);
		}
		if (!StringUtils.equalsNotNull(repayment.getRepayUserId(), project.getVouchId())) {
			throw new BussinessException("非担保垫付记录，无法还垫付款！", BussinessException.TYPE_JSON);
		}
		this.doRepayBail(repayment);
	}

	private void doRepayBail(ProjectRepayment repayment) {
		User projectUser = userService.get(repayment.getUserId());// 借款人
		// 还款前数据、还款人可用金额校验
		validBeforeRepayBail(repayment, projectUser);
		// 江西银行垫付参数
		Map<String, Object> params = new HashMap<>();
		// 针对同一项目加锁处理
		RedissonDistributeLock.lock(repayment.getUuid());
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionStatus status = null;
		try {
			// 开启事务
			TransactionDefinition definition = new DefaultTransactionDefinition();
			status = transactionManager.getTransaction(definition);
			// 封装第三方请求数据
			Map<String, Object> accountMap = this.doRepayBailTpp(params, repayment, projectUser);
			// 处理借款人的本地资金
			this.handleRepayBailAccount(projectUser, repayment, accountMap);
			// 更新为已还垫付款
			this.updateRepayType(repayment.getUuid(), RepaymentEnum.REPAY_TYPE_REPAYED_GUARANTEE.getValue(),
					RepaymentEnum.REPAY_TYPE_GUARANTEE.getValue());
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		} finally {
			RedissonDistributeLock.unlock(repayment.getUuid());
		}
		// 江西银行交易
		TppService tppService = (TppService) TppUtil.tppService();
		// noinspection unchecked
		Map<String, Object> map = (Map<String, Object>) tppService.batchRepayBail(params);
		String received = StringUtils.isNull(map.get("received"));
		if (!"success".equals(received)) {
			String tradeNo = (String) params.get("tradeNo");
			tppTradeService.updateStatusByTradeNo(tradeNo, TppEnum.STATUS_FAIL.getValue(), TppEnum.STATUS_UNDO.getValue(),
					"存管方接收失败！");
		}
	}

	private void validBeforeRepayBail(ProjectRepayment repayment, User repayUser) {
		if (repayment == null) {
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_NOEXISTS), BussinessException.TYPE_JSON);
		}
		// 还款记录状态校验
		if (!RepaymentEnum.REPAY_TYPE_GUARANTEE.eq(repayment.getRepayType())) {
			throw new BussinessException("非垫付记录，无法还垫付款", BussinessException.TYPE_JSON);
		}
		// 需支付总额
		final BigDecimal total = BigDecimalUtils.round(BigDecimalUtils.add(repayment.getCapital(), repayment.getInterest(),
				repayment.getLateInterest(), repayment.getMerchantLateInterest()));

		// 借款人资金校验
		Account localBorrowerAccount = accountService.getMoneyByUserId(new AccountQueryModel(repayUser.getUuid(), ConfigUtils
				.getValue(ConfigConstant.ACCOUNT_CODE)));
		// 查询校验提现账户第三方可用和平台可用是否一致
		final Map<String, Object> jxMap = new HashMap<>();
		jxMap.put("accountId", repayUser.getTppUserCustId());
		BigDecimal tppMoney = BigDecimal.ZERO;
		try {
			TppService tppService = (TppService) TppUtil.tppService();
			Object object = tppService.tppQueryBalance(jxMap);
			Map<String, Object> map = (Map<String, Object>) object;
			tppMoney = new BigDecimal(map.get("avlBal").toString());
		} catch (Exception e) {
			LOGGER.error("还款前，查询第三方资金异常，{}！", e.getMessage());
			throw new BussinessException(ResourceUtils.get(LoanResource.TPP_QUERY_ACCOUNT_FAIL));
		}
		if (localBorrowerAccount.getUseMoney().compareTo(total) < 0) {
			LOGGER.error("待还记录（uuid= {}）还款前校验，本地可用资金不足！", repayment.getUuid());
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_USEMONEY_NOT_ENOUGH));
		}
		if (tppMoney.compareTo(total) < 0) {
			LOGGER.error("待还记录（uuid= {}）还款前校验，三方可用资金不足！", repayment.getUuid());
			throw new BussinessException(ResourceUtils.get(LoanResource.REPAYMENT_USEMONEY_NOT_ENOUGH));
		}

	}

	private void updateRepayType(String uuid, String status, String preStatus) {
		int result = dao.updateRepayType(uuid, status, preStatus);
		if (result == Constant.INT_ZERO) {
			String msg = "还款状态更新失败 [uuid=" + uuid + ", status=" + status + ", preStatus=" + preStatus + "]";
			LOGGER.error(msg);
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_STATUS_ERROR));
		}
	}

	private void handleRepayBailAccount(User projectUser, ProjectRepayment repayment, Map<String, Object> accountMap) {
		final StringBuffer infoBuffer = new StringBuffer();
		final Project project = projectService.get(repayment.getProjectId());
		final ProjectType projectType = projectTypeService.get(project.getProjectTypeId());
		infoBuffer.append("【<a href=\"").append(URLConstant.INVEST_DETAIL_PAGE_PREFIX).append(project.getUuid())
				.append("\"  target=\"_blank\">").append(projectType.getTypeName()).append(StringUtils.HYPHEN)
				.append(project.getProjectNo()).append("</a>】");
		final String info = infoBuffer.toString();
		BigDecimal money = (BigDecimal) accountMap.get("money");
		// 需要调用Account的dubbo服务执行： 借款人账户金额处理
		AccountModel accountModel = new AccountModel(projectUser.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
				money.negate(), BigDecimal.ZERO);
		// 借款人资金日志
		accountMap.put("info", info);
		accountMap.put("money", money.doubleValue());
		String remark = LogTemplateUtils.getAccountTemplate(Constant.REPAY_BAIL, accountMap);
		AccountLog capitalLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), Constant.REPAY_BAIL,
				projectUser.getUuid(), money, remark);
		capitalLog.setToUser(repayment.getRepayUserId());
		capitalLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
		accountService.saveAccountChange(accountModel, capitalLog);
	}

	private Map<String, Object> doRepayBailTpp(Map<String, Object> params, ProjectRepayment repayment, User projectUser) {
		List<TppTrade> list = tppTradeService.findListByTypeAndRepaymentId(TppEnum.TPP_TYPE_BAIL_REPAY.getValue(),
				repayment.getUuid());
		if (CollectionUtils.isEmpty(list)) {
			throw new BussinessException("找不到垫付记录", BussinessException.TYPE_JSON);
		}
		User vouchUser = userService.get(repayment.getRepayUserId());// 垫付人
		// 第三方tppList
		List<TppTrade> repayBailTppList = new ArrayList<>();
		// 借款人资金变动map
		Map<String, Object> accountMap = new HashMap<>();
		BigDecimal txAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal moneySum = new BigDecimal(0);
		BigDecimal capitalSum = new BigDecimal(0);
		BigDecimal interestSum = new BigDecimal(0);
		BigDecimal lateInterestSum = new BigDecimal(0);
		BigDecimal merchantLateInterestSum = new BigDecimal(0);
		int txCounts = 0;
		List<Map> subPacks = new ArrayList<>();
		Date now = new Date();
		String batchNo = OrderNoUtils.getRandomStr(6);
		String tradeNo = DateUtils.dateStr7(now).concat(batchNo);
		for (TppTrade trade : list) {
			if (!TppEnum.STATUS_SUCEESS.getValue().equals(trade.getStatus())) {
				throw new BussinessException("垫付还未完成", BussinessException.TYPE_JSON);
			}
			JSONObject object = JSON.parseObject(trade.getParams());
			BigDecimal capital = object.getBigDecimal("capital");
			BigDecimal interest = object.getBigDecimal("interest");
			BigDecimal lateInterest = object.getBigDecimal("lateInterest");
			BigDecimal merchantLateInterest = object.getBigDecimal("merchantLateInterest");
			if (merchantLateInterest == null) {
				merchantLateInterest = new BigDecimal(0);
			}
			BigDecimal capitalInterestSum = capital.add(interest).add(lateInterest);
			// 新建还垫付tpp记录
			TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_REPAY_BAIL.getValue(), TppEnum.TPP_TYPE_REPAY_BAIL.getValue(),
					projectUser.getUuid(), projectUser.getTppUserCustId(), vouchUser.getUuid(), vouchUser.getTppUserCustId(),
					capitalInterestSum, null);
			tpp.setProjectRepaymentId(repayment.getUuid());
			tpp.setProjectId(repayment.getProjectId());
			tpp.setRepayType(UfxRepaymentModelEnum.REPAY_TYPE_FIVE.getValue());
			tpp.setTradeNo(tradeNo);
			tpp.setServFee(merchantLateInterest);
			Map<String, Object> subPack = new HashMap<>();
			subPack.put("accountId", tpp.getTppUserCustId());
			subPack.put("orderId", tpp.getOrderNo());
			subPack.put("txAmount", capital.setScale(2, BigDecimal.ROUND_HALF_UP));
			subPack.put("intAmount", interest.add(lateInterest).setScale(2, BigDecimal.ROUND_HALF_UP));
			if (merchantLateInterest.compareTo(BigDecimal.ZERO) > 0) {
				subPack.put("txFeeOut", merchantLateInterest.setScale(2, BigDecimal.ROUND_HALF_UP));// 还款罚息
			}
			subPack.put("forAccountId", tpp.getToTppUserCustId());
			subPack.put("orgOrderId", trade.getOrderNo());
			subPack.put("authCode", trade.getAuthCode());
			tpp.setParams(JSON.toJSONString(subPack));
			txAmount = txAmount.add(capital.setScale(2, BigDecimal.ROUND_HALF_UP));
			txCounts++;
			subPacks.add(subPack);
			repayBailTppList.add(tpp);
			moneySum = moneySum.add(capitalInterestSum).add(merchantLateInterest);
			capitalSum = capitalSum.add(capital);
			interestSum = interestSum.add(interest);
			lateInterestSum = lateInterestSum.add(lateInterest);
			merchantLateInterestSum = merchantLateInterestSum.add(merchantLateInterest);
		}
		// 第三方交易参数
		params.put("txAmount", txAmount);
		params.put("txCounts", txCounts);
		params.put("subPacks", subPacks);
		params.put("batchNo", batchNo);
		params.put("tradeNo", tradeNo);
		// 本地资金记录参数
		accountMap.put("money", moneySum);
		accountMap.put("capital", capitalSum.doubleValue());
		accountMap.put("interest", interestSum.doubleValue());
		accountMap.put("lateInterest", lateInterestSum.doubleValue());
		accountMap.put("merchantLateInterest", merchantLateInterestSum.doubleValue());

		// 保存批量放款记录
		TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_BATCH_REPAY_BAIL.getValue(),
				TppEnum.TPP_TYPE_BATCH_REPAY_BAIL.getValue(), projectUser.getUuid(), projectUser.getTppUserCustId(),
				vouchUser.getUuid(), vouchUser.getTppUserCustId(), txAmount, null);
		tpp.setTradeNo(tradeNo);
		tpp.setTradeDate(now);
		tpp.setProjectId(repayment.getProjectId());
		tpp.setProjectRepaymentId(repayment.getUuid());
		tpp.setParams(JSON.toJSONString(accountMap));
		repayBailTppList.add(tpp);
		if (CollectionUtils.isNotEmpty(repayBailTppList)) {
			tppTradeService.insertBatch(repayBailTppList);
		}
		return accountMap;
	}

	@Override
	public Page<ProjectRepayment> findDetail(ProjectRepayment model) {
		List<ProjectRepayment> projectRepaymentList=dao.findList(model);
		BigDecimal moneySum = new BigDecimal(0);
		for(ProjectRepayment p:projectRepaymentList){
			moneySum.add(p.getPayment());
		}
		CacheUtils.set("repaymentMoneySum", moneySum+"", ExpireTime.NONE);//把预还金额放到redis中
		Page<ProjectRepayment> page=model.getPage();
		page.setRows(projectRepaymentList);
		return page;
	}

}