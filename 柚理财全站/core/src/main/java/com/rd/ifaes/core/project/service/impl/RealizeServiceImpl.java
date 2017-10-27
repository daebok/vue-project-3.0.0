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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.annotation.Cacheable;
import com.rd.ifaes.common.constant.CbhbConstant;
import com.rd.ifaes.common.constant.TokenConstants;
import com.rd.ifaes.common.constant.TppServiceEnum;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.RealizeEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.redisson.RedissonDistributeLock;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.common.validator.BeanValidators;
import com.rd.ifaes.core.base.service.BaseServiceImpl;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.cache.RealizeCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.operate.service.UserRateCouponService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectCollection;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.domain.ProjectRepayment;
import com.rd.ifaes.core.project.domain.ProjectType;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeFreeze;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.mapper.RealizeMapper;
import com.rd.ifaes.core.project.model.RealizeModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.ProjectTypeService;
import com.rd.ifaes.core.project.service.RealizeFreezeService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.protocol.domain.Protocol;
import com.rd.ifaes.core.protocol.model.ProtocolModel;
import com.rd.ifaes.core.protocol.service.ProtocolService;
import com.rd.ifaes.core.risk.domain.LevelConfig;
import com.rd.ifaes.core.risk.service.LevelConfigService;
import com.rd.ifaes.core.sys.service.DictDataService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.tpp.service.tpp.UfxProjectService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 变现实现类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Service("realizeService") 
public class RealizeServiceImpl  extends BaseServiceImpl<RealizeMapper, Realize> implements RealizeService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RealizeServiceImpl.class);
	@Resource
	private transient UserService userService;
	@Resource
	private transient RealizeRuleService realizeRuleService;
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
	private transient AccountService accountService;
	@Resource
	private transient ProjectTypeService projectTypeService;
	@Resource
	private transient UfxProjectService ufxProjectService;
	@Resource
	private transient DictDataService dictDataService;
	@Resource
	private transient LevelConfigService levelConfigService;
	@Resource
	private transient UserRateCouponService userRateCouponService;
	@Resource
	private transient RealizeFreezeService realizeFreezeService;
	@Resource
    private transient ProtocolService protocolService;
	@Resource
    private transient UserCacheService userCacheService;
	@Resource
    private transient PlatformTransactionManager transactionManager;
	/**
	 * 变现项目抬头
	 */
	protected static final String REALIZE_TITTLE = "变现";
		
	@Override
	public Page<Realize> findAllPage(final Realize entity) {
		final Page<Realize> page = entity.getPage();
		if (entity.getPage() != null && StringUtils.isBlank(entity.getPage().getSort())) {
			entity.getPage().setSort("create_time");
			entity.getPage().setOrder("desc");
		}
		page.setRows(dao.findRealizeList(entity));
		return page;
	}
	
	@Override
	
	
	@Cacheable(key = CacheConstant.KEY_REALIZE_LIST , expire=ExpireTime.FIVE_MIN)
	public Page<Realize> findAllPageByCache(final Realize realize) {
		 List<Realize> recordList=dao.findWebRealizeList(realize);
		 //查询缓存中剩余可投
		 for(Realize record : recordList){
			 record.setRemainAccount(ProjectCache.getRemainAccount(record.getUuid()));
		 }
		 realize.getPage().setRows(recordList);
		return  realize.getPage();
	}
	
	@Override
	public Page<RealizeModel> findPageModel(final Realize entity) {
		final Page<RealizeModel> page = new Page<RealizeModel>();
		final List<RealizeModel> newlist = new ArrayList<RealizeModel>();
		final List<Realize> list = dao.findRealizeList(entity);
		if(CollectionUtils.isNotEmpty(list)){
			for (Realize realize : list) {
				final RealizeModel model = RealizeModel.instance(realize);
				final User user = userService.get(model.getUserId());
				final Project oldProject = projectService.get(model.getOldProjectId());
				model.setUserName(user.getUserName());
				model.setOldProjectName(oldProject.getProjectName());
				newlist.add(model);
			}
		}
		page.setPage(entity.getPage().getPage());
		page.setPageSize(entity.getPage().getPageSize());
		page.setCount(entity.getPage().getCount());
		page.setRows(newlist);
		return page;
	}
	
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_REALIZE_INFO , expire =ExpireTime.FIVE_MIN)
	public Map<String, Object> getDetailInfo(final Realize realize) {
		final Map<String, Object> data = new HashMap<String, Object>();
		final LevelConfig levelConfig = levelConfigService.findByRiskLevel(realize.getRiskLevel());
		data.put("result", true);
		data.put("risk", levelConfig.getRiskLevelName());// 安全等级
		data.put("investCondition", levelConfig.getUserRiskLevelName());// 投资条件
		if (LoanEnum.STATUS_REPAY_START.eq(realize.getStatus())) {
			if (realize.getTotalPeriod() != null && realize.getRepayedPeriod() != null) {
				 data.put("remainPeriod", realize.getTotalPeriod() - realize.getRepayedPeriod());
			}else{
				data.put("remainPeriod", realize.getTotalPeriod() );
			}
		}
		RealizeRule rule = realizeRuleService.get(realize.getRuleId());
		if(rule != null){
			data.put("buyStyle", rule.getBuyStyle());
		}
		return data;
	}
	
	@Override
	public Realize findRealize(final String uuid) {
		if(uuid==null){
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
		}
		return dao.findRealize(uuid);
	}
	
	@Override
	@Cacheable(key = CacheConstant.KEY_PREFIX_REALIZE_UUID,expire = ExpireTime.ONE_MIN)
	public Realize findRealizeFromCache(final String uuid) {
		if(uuid==null){
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
		}
		Realize realize = dao.findRealize(uuid);
		if(realize==null){
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		return realize;
	}
	
	/**
	 * 变现发布
	 * @author fxl
	 * @date 2016年7月29日
	 * @param realize
	 */
	@Override
	@Transactional(readOnly = false)
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_REALIZE_LIST,interval=ExpireTime.ONE_SEC)
 	public void publish(final Realize entity) {
		// 根据原始投资ID建立缓存
		final String handleKey = CacheConstant.KEY_REALIZE_INVESTID.concat(entity.getInvestId());
		if (CacheUtils.incr(handleKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
			LOGGER.info("{}----变现正在发布中",StringUtils.isNull(entity.getInvestId()) );
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_PUBLISHING));
		}
		// 设置缓存时长
		CacheUtils.expire(handleKey, ExpireTime.FIVE_MIN);
		try {
			// 获取当前变现规则
			final RealizeRule rule = realizeRuleService.getRule();
			// 原投标记录
			final ProjectInvest oldInvest = projectInvestService.get(entity.getInvestId());
			if(oldInvest==null){
				throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_NOT_FOUND));
			}
			// 被抵押项目
			final Project oldProject = projectService.get(oldInvest.getProjectId());
			// 判断原标是否在还款中 
			if(RedissonDistributeLock.isLocked(oldProject.getUuid())){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_ORGPROJECT_IS_REPAYING));
			}
			// 填充变现标的参数
			fillRealizeParam(entity,oldInvest,oldProject,rule);
			final ProjectType oldType = projectTypeService.get(oldProject.getProjectTypeId());
			LOGGER.info("变现发布进来,ruleId={},investId={}",rule.getUuid(),oldInvest.getUuid());
			//校验数据有效性
			BeanValidators.validate(entity);
			entity.validBase(rule);
			LOGGER.info("变现发布校验结束");
			// 待收本息
			final ProjectWorker worker = new ProjectWorker(entity.prototype());
			final BigDecimal repayAmount = BigDecimalUtils.add(worker.getRepaymentInterest(DateUtils.getNow()),entity.getAccount());
			// 存入应还本息
			entity.setRepayAmount(repayAmount);
			final Map<String,BigDecimal> map = calCapital(entity, oldProject);
			final BigDecimal realizeCapital = map.get("capital");
			final BigDecimal realizeInterest = map.get("interest");
			if(realizeCapital.compareTo(BigDecimal.ONE) < 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_CAPITAL_MUST_GT_ONE));
			}
			entity.setRealizeAmount(repayAmount);
			entity.setRealizeInterest(realizeInterest);
			entity.setProjectNo(projectService.nextProjectNo(DateUtils.getDate()));//项目编号
			entity.setProjectName(REALIZE_TITTLE+"-"+oldType.getTypeName()+"-"+entity.getProjectNo());//项目名称
			//保存借款基本信息
			TokenUtils.validShiroToken(TokenConstants.TOKEN_REALIZE_COMMIT);//录入信息前进行token的校验
			entity.preInsert();
			final Project base = entity.prototype();
			base.preInsert();		
			projectService.insert(base);
			//第三方发布项目
			ufxProjectService.tppProjectApply(base.getUuid());
			entity.setProjectId(base.getUuid());
			dao.insert(entity);
			// 处理原始待还应冻结金额
			doOldCollection(entity,oldProject,oldInvest,realizeCapital,realizeInterest);
			//更新原投资变现状态
			oldInvest.setRealizeAmount(BigDecimalUtils.add(oldInvest.getRealizeAmount(),repayAmount));
			oldInvest.setRealizeInterest(BigDecimalUtils.add(oldInvest.getRealizeInterest(), realizeInterest));
			if(InvestEnum.REALIZE_FLAG_NORMAL.eq(oldInvest.getRealizeFlag())){
				oldInvest.setRealizeId(base.getUuid());
				oldInvest.setRealizeFlag(InvestEnum.REALIZE_FLAG_TENDER.getValue());
			}
			projectInvestService.update(oldInvest);
			// 缓存中添加剩余金额
			RealizeCache.setRemainAccount(entity.getUuid(), entity.getAccount().doubleValue());
		} catch (Exception e) {
			LOGGER.error("变现发布失败",e);
			throw e;
		} finally {
			CacheUtils.del(handleKey);
		}
		LOGGER.info("变现发布结束,id={}",entity.getUuid());
	}
	
	/**
	 * 根据变现规则封装变现参数
	 * @author fxl
	 * @date 2016年7月27日
	 * @param rule
	 */
	private void fillRealizeParam(final Realize entity,final ProjectInvest invest,final Project oldProject,final RealizeRule rule){
		//更改投资属性
		if(!invest.getUserId().equals(entity.getUserId())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_BORROWER_NOT_RIGHT));
		}
		if(projectRepaymentService.checkOverDueByUser(invest.getUserId())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.HAS_OVERDUE_PROJECT));
		}
		if(oldProject==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_ORGPROJECT_NOT_FOUNT));
		}
		int timeLimit = projectCollectionService.getReypayDays(invest.getUuid());
		if(DateUtils.before(DateUtils.getNow(), rule.getIssueTime())){// 变现时间在发布时间之前
			// 用stopTime保存预计筹集结束时间
			entity.setStopTime(DateUtils.after(DateUtils.getNow(), rule.getRaiseEndTime(), 0));
			entity.setRaiseEndTime(DateUtils.after(DateUtils.getNow(), rule.getRaiseEndTime(), 0));
			entity.setRaiseTimeLimit(1); // 有效期为0天
		}else{// 变现时间在发布时间之后
			timeLimit = timeLimit-1;
			entity.setStopTime(DateUtils.after(DateUtils.getNow(), rule.getRaiseEndTime(), 1));
			entity.setRaiseEndTime(DateUtils.after(DateUtils.getNow(), rule.getRaiseEndTime(), 1));
			entity.setRaiseTimeLimit(1); // 有效期为1天
		}
		entity.setTimeLimit(timeLimit);
		// 剩余可变现金额
		final BigDecimal waitMoney = BigDecimalUtils.add(invest.getWaitAmount(), invest.getRaiseInterest().negate(), invest.getRealizeAmount().negate() , invest.getFreezeInterest());
		final BigDecimal mostRealizeAmount = getMostRealizeAmount(waitMoney, entity.getApr(), BigDecimal.valueOf(timeLimit));
		if(rule.getSellStyle().equals(RealizeRule.FULL_REALIZE) ){
			// 全额变现 - 变现金额等于最大可变现金额
			if(mostRealizeAmount.compareTo(entity.getAccount()) != 0){
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_MUST_FULLREALIZE));
			}
		}else{
			// 部分变现  
			if(mostRealizeAmount.compareTo(entity.getAccount()) != 0 && rule.getSellAmountMin().compareTo(entity.getAccount()) > 0){
				if(mostRealizeAmount.compareTo(rule.getSellAmountMin()) < 0){
					throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_LESS_MUST_FULLREALIZE,rule.getSellAmountMin()));
				}else{
					throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_NOT_LESS_SELLAMOUNTMIN,rule.getSellAmountMin()));
				}
			}
		}
		if(mostRealizeAmount.compareTo(entity.getAccount()) < 0){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_NOT_ABOVE_MOSTMONEY));
		}
		if(LoanEnum.REALIZE_FLAG_REALIZE.eq(oldProject.getRealizeFlag())){//原标也是变现
			final Realize oldRealize = dao.get(oldProject.getUuid());
			entity.setOriginalProjectId(oldRealize.getOriginalProjectId());
		}else{
			entity.setOriginalProjectId(oldProject.getUuid());
		}
		//初始化变现属性
		entity.setInvestId(invest.getUuid());
		entity.setOldProjectId(oldProject.getUuid());
		entity.setRuleId(rule.getUuid());
		entity.setDeadline(oldProject.getRepayedPeriod()!=null?oldProject.getRepayedPeriod():0);
		//填充原项目属性
		entity.setRiskLevel(oldProject.getRiskLevel());
		entity.setProjectTypeId(oldProject.getProjectTypeId());
		entity.setRedEnvelopeRate(oldProject.getRedEnvelopeRate());
		entity.setRedEnvelopeUseful(oldProject.getRedEnvelopeUseful());
		entity.setAdditionalRateUseful(oldProject.getAdditionalRateUseful());
		entity.setSaleChannel(oldProject.getSaleChannel());
		//初始化项目属性
		entity.setApr(entity.getApr());//利率
		entity.setAddApr(BigDecimal.ZERO);//加息
		entity.setAccountYes(BigDecimal.ZERO);//已投金额
		entity.setInterestStyle(LoanEnum.INTEREST_STYLE_EV.getValue());//计息方式
		entity.setInterestStartDays(1);
		entity.setInterestManageRate(oldProject.getInterestManageRate());//利息管理费
		entity.setOverdueFeeRate(BigDecimal.ZERO);//oldProject.getOverdueFeeRate());//逾期罚息
		entity.setNovice(CommonEnum.NO.getValue());//新手标
		entity.setChoice(CommonEnum.NO.getValue());//精选标
		entity.setProjectTypeId(oldProject.getProjectTypeId());//产品类型
		entity.setRepayStyle(LoanEnum.STYLE_ONETIME_REPAYMENT.getValue());//还款方式:一次性
		entity.setTimeType(LoanEnum.TIME_TYPE_DAY.getValue());//天标
		entity.setStatus(LoanEnum.STATUS_RAISING.getValue());
		entity.setSaleStyle(LoanEnum.SALE_STYLE_MONEY.getValue());
		entity.setRealizeFlag(LoanEnum.REALIZE_FLAG_REALIZE.getValue());
		entity.setBorrowFlag(LoanEnum.BORROW_FLAG_PRODUCT.getValue());
		entity.setRepayYesAccount(BigDecimal.ZERO);
		entity.setRepayYesInterest(BigDecimal.ZERO);
		entity.setProtocolId(getRealizeProtocolId());
		if(rule.getBuyStyle().equals(RealizeRule.FULL_INVEST)){//全额变现
			entity.setLowestAccount(entity.getAccount());
			entity.setMostAccount(entity.getAccount());
		}else{// 部分变现
			if((rule.getBuyAmountMin()).compareTo(entity.getAccount()) > 0){
				entity.setLowestAccount(entity.getAccount());
			}else{
				entity.setLowestAccount(rule.getBuyAmountMin());
			}
			if((rule.getBuyAmountMax()).compareTo(entity.getAccount()) > 0){
				entity.setMostAccount(entity.getAccount());
			}else{
				entity.setMostAccount(rule.getBuyAmountMax());
			}
		}
	}
	
	@Override
	public void autoDealRealize() {
		final Date nowtime = DateUtils.getNow();
		final Date endTime = DateUtils.rollMinute(nowtime, 1);
		// 过期的变现产品
		final List<Project> realizeIdList = projectService.findExpireRealize(endTime);
		for (Project project : realizeIdList) {
			try {
				if(LoanEnum.STATUS_RAISING.eq(project.getStatus())){// 募集中
					LOGGER.info("变现({})到期撤销............",project.getUuid());
					cancel(project.getUuid());
				}else if(LoanEnum.STATUS_RAISE_FINISHED.eq(project.getStatus())){// 满标待审
					LOGGER.info("变现({})到期放款............",project.getUuid());
					fullSuccess(project.getUuid());
				}else{
					LOGGER.info("变现({})到期时,状态不正确,未进行处理............",project.getUuid());
				}
			} catch (Exception e) {
				LOGGER.error("变现({})到期时,状态不正确,未进行处理............",project.getUuid());
			}
		}
	}
	
	/**
	 * 处理原始待还应冻结金额
	 * @author fxl
	 * @date 2016年8月14日
	 * @param realize
	 */
	private void doOldCollection(final Realize realize, final Project oldProject,final ProjectInvest invest,final BigDecimal capital,BigDecimal interest) {
		final List<ProjectCollection> collectionList = projectCollectionService.findByInvestId(realize.getInvestId(),null);
		final List<RealizeFreeze> realizeFreezeList = new ArrayList<RealizeFreeze>();
		final ProjectWorker projectWorker = new ProjectWorker(oldProject);
		final InterestCalculator interestCalculator = projectWorker.interestCalculator(capital, oldProject.getApr(),
				BigDecimal.ZERO, invest.getCreateTime());
		LOGGER.info("变现对应原始标的本金{}元,利息{}元",capital,interest);
		BigDecimal realizeSum = BigDecimal.ZERO;
		// 从变现起始期数开始计算
		for (int i = realize.getDeadline()!=null?realize.getDeadline():0; i < collectionList.size(); i++) {
			final ProjectCollection collection = collectionList.get(i);
			BigDecimal realizeCapital = interestCalculator.getEachPlan().get(i).getCapital();
			BigDecimal realizeInterest = interestCalculator.getEachPlan().get(i).getInterest();
			realizeSum = BigDecimalUtils.add(realizeSum,realizeCapital,realizeInterest);
			if(i == collectionList.size() - 1){// 最后一期进行本息平衡
				LOGGER.info("反推的总本息{}元,原本息{}",realizeSum,capital.add(interest));
				BigDecimal diffValue = BigDecimalUtils.add(capital,interest,realizeSum.negate());
				if(BigDecimalUtils.validAmount(diffValue) && BigDecimalUtils.validAmount(realizeInterest.add(diffValue))){
					// 最后一期利息进行平衡
					realizeInterest = realizeInterest.add(diffValue);
				}
			}
			if (BigDecimalUtils.validAmount(realizeCapital)) {
				collection.setFreezeCapital(BigDecimalUtils.add(collection.getFreezeCapital(), realizeCapital));
				realizeFreezeList.add(new RealizeFreeze(realize.getUserId(), realize.getUuid(), collection.getUuid(),Constant.COLLECT_CAPITAL, realizeCapital));
			}
			if (BigDecimalUtils.validAmount(realizeInterest)) {
				collection.setFreezeInterest(BigDecimalUtils.add(collection.getFreezeInterest(), realizeInterest));
				realizeFreezeList.add(new RealizeFreeze(realize.getUserId(), realize.getUuid(), collection.getUuid(),Constant.COLLECT_INTEREST, realizeInterest));
			}
			LOGGER.info("原始待还冻结,第{}期,冻结本金{}元,利息{}元",i,realizeCapital,realizeInterest);
		}
		projectCollectionService.updateBatch(collectionList);
		realizeFreezeService.insertBatch(realizeFreezeList);
	}
	
	/**
	 * 退回原待收详情
	 * @author fxl
	 * @date 2016年11月29日
	 * @param realize
	 */
	private void cancelOldCollection(final Realize realize) {
		final Project oldProject = projectService.get(realize.getOldProjectId());
		// 获取所有待收
		final List<ProjectCollection> collectionList = projectCollectionService.findByInvestId(realize.getInvestId(),null);
		if(CollectionUtils.isNotEmpty(collectionList)){
			// 变现对应本金和利息
			final Map<String,BigDecimal> map = calCapital(realize, oldProject);
			final BigDecimal capital = map.get("capital");
			final BigDecimal interest = map.get("interest");
			LOGGER.info("变现对应原始标总本息{}元,的本金{}元,利息{}元",realize.getRepayAmount(),capital,interest);
			final ProjectInvest invest = projectInvestService.get(realize.getInvestId());
			final ProjectWorker projectWorker = new ProjectWorker(oldProject);
			final InterestCalculator interestCalculator = projectWorker.interestCalculator(capital,oldProject.getApr(), BigDecimal.ZERO, invest.getCreateTime());
			// 从变现起始期数开始计算
			BigDecimal realizeSum = BigDecimal.ZERO;
			for (int i = realize.getDeadline()!=null?realize.getDeadline():0; i < collectionList.size(); i++) {
				final ProjectCollection collection = collectionList.get(i);
				BigDecimal realizeCapital = interestCalculator.getEachPlan().get(i).getCapital();
				BigDecimal realizeInterest = interestCalculator.getEachPlan().get(i).getInterest();
				realizeSum = BigDecimalUtils.add(realizeSum,realizeCapital,realizeInterest);
				if(i == collectionList.size() - 1){// 最后一期进行本息平衡
					LOGGER.info("反推的总本息{}元,原本息{}",realizeSum,capital.add(interest));
					BigDecimal diffValue = BigDecimalUtils.add(capital,interest,realizeSum.negate());
					if(BigDecimalUtils.validAmount(diffValue) && BigDecimalUtils.validAmount(realizeInterest.add(diffValue))){
						// 最后一期利息进行平衡
						realizeInterest = realizeInterest.add(diffValue);
					}
				}
				if(BigDecimalUtils.validAmount(realizeCapital)){
					collection.setFreezeCapital(BigDecimalUtils.add(collection.getFreezeCapital(),realizeCapital.negate()));
				}
				if(BigDecimalUtils.validAmount(realizeInterest)){
					collection.setFreezeInterest(BigDecimalUtils.add(collection.getFreezeInterest(),realizeInterest.negate()));
				}
				LOGGER.info("原始待还解冻,第{}期,解冻本金{}元,利息{}元",i,realizeCapital,realizeInterest);
			}
			projectCollectionService.updateBatch(collectionList);
		}
	}
	
	/**
	 * 变现撤回
	 * @author fxl
	 * @date 2016年7月29日
	 * @param uuid
	 */
	@Override
 	public void cancel(final String uuid) {
		LOGGER.info("变现({})撤回,退款处理完毕............",uuid);
		// 三方任务
		final List<TppTrade> taskList = new ArrayList<TppTrade>();
		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			final Realize realize = dao.findRealize(uuid);
			// 撤回校验
			checkWithdraw(realize);
			// 更新变现状态
			projectService.updateStatus(uuid, LoanEnum.STATUS_CANCELLING.getValue(), LoanEnum.STATUS_RAISING.getValue());
			// 查询该项目所有成功的投资记录
			final List<ProjectInvest> investList = projectInvestService.findSuccessInvest(uuid);
			// 生成对应投资退款申请（investFail）
			if(CollectionUtils.isNotEmpty(investList)){
				// 生成对应投资退款申请（investFail）
				for (ProjectInvest invest : investList) {
					if (StringUtils.isBlank(invest.getFreezeNo())) {
						throw new BussinessException(
								ResourceUtils.get(LoanResource.INVEST_FREEZE_NO_EMPTY_ERROR, invest.getUuid()));
					}
					User investUser = userService.get(invest.getUserId());
					TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_PROJECT_CANCEL.getValue(),
							TppEnum.TPP_TYPE_INVEST_FAIL.getValue(), null, null, invest.getUserId(),
							investUser.getTppUserCustId(), invest.getRealAmount(), invest.getUuid());
					tpp.setTradeNo(invest.getFreezeNo());
					tpp.setInvestProjectId(invest.getProjectId());
					taskList.add(tpp);
					//  在投记录-1
					userCacheService.subUserInvestNum(invest.getUserId());
				} // for loop end
				// 修改投资记录状态为：退款处理中
				projectInvestService.refundByProject(uuid);
			}
			// 解冻变现人被冻结的已变现金额
			// 需要调用Account的dubbo服务执行： 借款人、投资人的账户金额处理
			final List<AccountModel> accountList = new ArrayList<AccountModel>();
			// 借款人、投资人的资金日志
			final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
			final User realizeUser = userService.get(realize.getUserId());
			fillUnFreezeTaskList(taskList, realizeUser, realize,accountList,accountLogList);
			if(CollectionUtils.isNotEmpty(accountList) && CollectionUtils.isNotEmpty(accountLogList)){
				accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
			}
			// 未支付成功用户(退回投资用户资金、添加资金日志、退回优惠劵)
			projectInvestService.cancelProjectHandleUnpay(uuid);
			// 修改变现冻结状态
			realizeFreezeService.cancelFreeze(uuid);
			// 取消原标的待冻结利息
			cancelOldCollection(realize);
			// 更改原始投资信息
			if(dao.countSuccessRealize(realize.getInvestId()) > 0){
				projectInvestService.updateOriginalInvest(realize,InvestEnum.REALIZE_FLAG_TENDER.getValue());
			}else{
				projectInvestService.updateOriginalInvest(realize,InvestEnum.REALIZE_FLAG_NORMAL.getValue());
			}
			// 第三方业务入库 
			tppTradeService.insertBatch(taskList);
		 	// 更新变现标的状态
		 	projectService.updateStatus(uuid, LoanEnum.STATUS_NOT_ESTABLISH.getValue(), LoanEnum.STATUS_CANCELLING.getValue());
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		// 清除缓存
		RealizeCache.delRealizeAndList(uuid);
		// 第三方处理
		tppTradeService.sendTppTrade(taskList, MqConstant.ROUTING_KEY_TRADE_INVESTFAIL);
		LOGGER.info("变现({})撤回,退款处理完毕............",uuid);
	}
	
	/**
	 * 变现撤回校验
	 * @author fxl
	 * @date 2016年7月29日
	 * @param realize
	 */
	private void checkWithdraw(final Realize realize){
		if(realize==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_FOUNT));
		}
		if (!LoanEnum.REALIZE_FLAG_REALIZE.eq(realize.getRealizeFlag())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_REALIZE));
		}
		if (LoanEnum.STATUS_ESTABLISH.eq(realize.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_LOANING));
		}
		if (!LoanEnum.STATUS_RAISING.eq(realize.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_NOT_RAISING));
		}
	}

	@Override
	public void fullSuccess(final String uuid) {
		// 三方还款任务
		final List<TppTrade> tppList = new ArrayList<TppTrade>();
		final Realize realize = dao.findRealize(uuid);
		// 放款校验
		checkFullSuccess(realize);
		final ProjectWorker projectWorker = new ProjectWorker(realize.prototype());
		// 借款人
		final User projectUser = userService.get(realize.getUserId());
		// 获取变现规则
		final RealizeRule rule = realizeRuleService.get(realize.getRuleId());
		// 手动控制提交事务，避免关闭队列，同步发送报文太久，TPP未提交，异步回调先到，查询不到TPP
		TransactionDefinition definition=new DefaultTransactionDefinition();
		TransactionStatus status=transactionManager.getTransaction(definition);
		
		final String tppName = ConfigUtils.getTppName();
		List<ProjectCollection> pclist = null;
		//渤海放款接口所需参数
		Map<String, Object> cbhbMap = new HashMap<String, Object>();
		TppTrade fileReleaseTrade = new TppTrade(TppEnum.SERVICE_TYPE_LOAN.getValue(), TppEnum.TPP_TYPE_LOAN.getValue(), null,null, 
				realize.getUserId(), projectUser.getTppUserCustId(), realize.getAccount(), null);
		if(TppServiceEnum.CBHB.getName().equals(tppName)){
			fileReleaseTrade.setProjectId(realize.getProjectNo());// 项目
			fileReleaseTrade.setInvestProjectId(realize.getUuid());
			fileReleaseTrade.setOrderNo(realize.getProjectNo());
			tppList.add(fileReleaseTrade);
			cbhbMap.put("borrowId", realize.getProjectNo());// 录入标的ID
			cbhbMap.put("borrCustId", projectUser.getTppUserCustId());//借款人的存管账号
		}
		try{
			// 变现成立状态
			LOGGER.info("updateStage >>:变现复审");
			projectService.updateStage(LoanEnum.STAGE_REPAY.getInt(), realize.getProjectNo());
			projectService.updateStatus(uuid, LoanEnum.STATUS_ESTABLISH.getValue(), LoanEnum.STATUS_RAISE_FINISHED.getValue());
			// 变现服务费
			BigDecimal realizeManageFee = BigDecimalUtils.round(realize.getAccount().multiply(rule.getFeeRate()).divide(BigDecimal.valueOf(100.00)));
			if (realizeManageFee.compareTo(rule.getFeeSingleMax()) > 0) {
				realizeManageFee = rule.getFeeSingleMax();
			}
			// 生成待还处理投资人资金
			if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
				pclist = createProjectCollection(realize, rule, projectWorker, projectUser, realizeManageFee,tppList,null);
			}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
				pclist = createProjectCollection(realize, rule, projectWorker, projectUser, realizeManageFee,null,cbhbMap);
			}
			// 借款人入账、生成 待还记录
			createProjectRepayment(realize, projectWorker, projectUser, pclist);
			// 修改状态为还款中，记录审核时间
			projectService.updateStatus(uuid, LoanEnum.STATUS_REPAY_START.getValue(), LoanEnum.STATUS_ESTABLISH.getValue());
			// 三方任务入库 --放款
			tppTradeService.insertBatch(tppList);
			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		// 清除缓存
		RealizeCache.delRealizeAndList(uuid);
		// 5 发送第三方 --放款
		if(TppServiceEnum.CHINAPNR.getName().equals(tppName)){
			tppTradeService.sendTppTrade(tppList, MqConstant.ROUTING_KEY_TRADE_LOAN);
		}else if(TppServiceEnum.CBHB.getName().equals(tppName)){
			TppService tppService = (TppService)TppUtil.tppService();
			CbhbFileReleaseModel responseModel = (CbhbFileReleaseModel)tppService.tppLoan(cbhbMap);
			if(CbhbConstant.CBHB_CODE_SUCCESS.equals(responseModel.getRespCode())){//实时回调保存三方流水号，异步回调用
				TppTrade trade = tppTradeService.findByOrderNo(realize.getProjectNo());//根据临时订单号来查
				trade.setServFee(BigDecimalUtils.valueOf(StringUtils.isNull(cbhbMap.get("feeAmt")))); // 借款管理费
				trade.setOrderNo(responseModel.getMerBillNo());
				cbhbMap.remove("detailList");
				trade.setParams(cbhbMap.toString());
				tppTradeService.update(trade);
			}
		}
		// 给变现人发送变现成立通知
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", projectUser);
			params.put("projectName", realize.getProjectName().length() > 10 ? realize.getProjectName().substring(0, 10) + "..."
					: realize.getProjectName());
			BaseMsg baseMsg = new BaseMsg(MessageConstant.REALIZE_FULL_SUCC, params);
			baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("给变现人发送变现成立通知处理失败，realize:{}", uuid, e);
		}
		LOGGER.info("变现({})审核成立,处理完毕............", uuid);
	}
	
	/**
	 * 投资人待收生成
	 * @author fxl
	 * @date 2016年8月1日
	 * @param realize
	 * @param rule
	 * @param projectWorker
	 * @param realizeUser
	 * @param taskList
	 * @param object 
	 */
	private List<ProjectCollection> createProjectCollection(final Realize realize,final  RealizeRule rule, final ProjectWorker projectWorker, final User realizeUser,
			final BigDecimal realizeManageFee, final List<TppTrade> taskList, final Map<String,Object> params) {
		// 查询该项目所有成功的投资记录
		final List<ProjectInvest> investList = projectInvestService.findSuccessInvest(realize.getUuid());
		if(CollectionUtils.isEmpty(investList)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_HAS_NOTSUCCESSINVEST,realize.getProjectName()));
		}
		// 生成对应放款任务数据TPP
		BigDecimal manageFeeTotal = BigDecimal.ZERO; // 存放累计借款管理费
		BigDecimal sumMoney = BigDecimal.ZERO;
		BigDecimal transAmt = BigDecimal.ZERO; // 实际投资金额
		boolean manageFeeMax = true;
		String[][] detailList = new String[investList.size()][];//初始化渤海银行放款所需数组
		final List<ProjectCollection> allCollectionList =new ArrayList<ProjectCollection>();
		for (int i = 0; i < investList.size(); i++) {
			final ProjectInvest invest = investList.get(i);
			// 处理投资待收
			projectService.handleInvestCollection(realize.prototype(), projectWorker, allCollectionList, invest);
			// 投资人		
			final User investUser = userService.get(invest.getUserId());
			BigDecimal manageFee = BigDecimal.ZERO;
			// 最后一个变现服务费子项，做减法运算
			if(manageFeeMax){
				if (i == investList.size() - 1) {// 最后一期
					manageFee = BigDecimalUtils.round(realizeManageFee.subtract(manageFeeTotal));
				} else {
					manageFee = BigDecimalUtils.round(invest.getAmount().multiply(rule.getFeeRate()).divide(BigDecimal.valueOf(100)));
				}
				if((manageFeeTotal.add(manageFee)).compareTo(realizeManageFee) > 0){// 超过了管理费总额  
					manageFee = realizeManageFee.subtract(manageFeeTotal);
					manageFeeMax = false;
				}else{
					manageFeeTotal = manageFeeTotal.add(manageFee);
				}
			}
			
			final BigDecimal realAmount=invest.getRealAmount();//实际投资金额
			//1-- 渤海银行所需数组录入
			String[] detail = new String[4];
			detail[0] = invest.getUuid();
			detail[1] = investUser.getTppUserCustId();
			detail[2] = realAmount.toString(); // 投资实际投资金额 不包含红包
			detail[3] = invest.getFreezeNo();
			detailList[i] = detail;
			transAmt = BigDecimalUtils.add(transAmt,invest.getRealAmount());
			// 三方报文记录
			if(taskList != null){
				taskList.add(projectService.fillTppTrade(investUser, realize.prototype(), realizeUser, invest.getAmount(), invest, invest.getRealAmount(), manageFee));
			}
			sumMoney = BigDecimalUtils.add(sumMoney,invest.getMoney(),invest.getInterest());
		}
		// 生成的待收总本息是否与本息一致
		BigDecimal diffInterest = BigDecimalUtils.add(realize.getRealizeAmount(),sumMoney.negate());
		if(diffInterest.doubleValue() != 0){
			LOGGER.info("生成利息与原利息不一致,误差为{}元",diffInterest);
			// 误差大于 1 说明出现了异常
			if(diffInterest.compareTo(BigDecimal.ONE) > 0 ){
				LOGGER.error("误差过大,不进行平衡,请查看详情,realizeId{}",realize.getUuid());
			}else{
				// 最后一期利息平衡
				ProjectCollection lastCollection = allCollectionList.get(allCollectionList.size()-1);
				lastCollection.setInterest(BigDecimalUtils.add(lastCollection.getInterest(),diffInterest));
			}
		}
		// 批量插入待收
		projectCollectionService.insertBatch(allCollectionList);
		LOGGER.info("变现({})生成待收记录--END",realize.getUuid());
		
		//7-- 录入cbhb所需信息 
		if(params != null){
			params.put("feeAmt", manageFeeTotal);//借款管理费
			params.put("detailList", detailList);
			params.put("transAmt", transAmt);// 实际到账金额
		}
		
		return allCollectionList;
	}
	
	/**
	 * 借款人生成待还
	 * @author fxl
	 * @date 2016年8月1日
	 * @param realize
	 * @param projectWorker
	 * @param realizeUser
	 */
	private void createProjectRepayment(final Realize realize, final ProjectWorker projectWorker, final User realizeUser, final List<ProjectCollection> pclist) {
		final List<ProjectRepayment> repayList = projectWorker.createRepaymentList(pclist);
		if (CollectionUtils.isEmpty(repayList)) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.PROJECT_REPAYMENT_CREATE_FAIL, realize.getProjectName()));
		}else{
			final Project projectTemp=new Project();
			projectTemp.setUuid(realize.getUuid());
			projectTemp.setReviewTime(DateUtils.getNow());
			projectTemp.setTotalPeriod(repayList.size());
			projectTemp.setRepayedPeriod(Constant.INT_ZERO);
			projectTemp.setNextRepayTime(repayList.get(Constant.INT_ZERO).getRepayTime());
			projectTemp.setLastRepayTime(repayList.get(repayList.size()-Constant.INT_ONE).getRepayTime());
			projectService.update(projectTemp);
		}
		// 批量插入还款计划
		projectRepaymentService.insertBatch(repayList);
		LOGGER.info("变现({})生成待还记录--END",realize.getUuid());
	}

	
	
	
	/**
	 * 计算变现对应本金
	 * @author fxl
	 * @date 2016年8月15日
	 * @param realize
	 * @param oldProject
	 * @param collectionSize
	 * @param timelimit
	 * @return
	 */
	public Map<String,BigDecimal> calCapital(final Realize realize,final Project oldProject){
		Integer timeLimit = 0;
		// 剩余期数
		Integer remainPeriod = oldProject.getTotalPeriod() - (realize.getDeadline()!=null?realize.getDeadline():0);
		BigDecimal calcMedian = BigDecimal.ZERO;// 利率 * 未还期限
		BigDecimal calcRate = BigDecimal.ZERO;// (利率 * 未还期限)/(总月份/总天数 * 100)
		if (LoanEnum.STYLE_MONTHLY_INTEREST.eq(oldProject.getRepayStyle())) {
			// 每月还息: 变现对应本金 = 待收总额 / (1+原利率 * 未还月份/12)
			timeLimit = remainPeriod;
			calcMedian = BigDecimalUtils.mul(oldProject.getApr(),BigDecimal.valueOf(timeLimit));
			calcRate = BigDecimalUtils.div(calcMedian,BigDecimal.valueOf(1200), 8);
		} else if (LoanEnum.STYLE_QUARTER_INTEREST.eq(oldProject.getRepayStyle())) {
			// 按季还息: 变现对应本金 = 待收总额 / (1+原利率 * 未还月份*3/12)
			timeLimit = remainPeriod * 3;
			calcMedian = BigDecimalUtils.mul(oldProject.getApr(),BigDecimal.valueOf(timeLimit));
			calcRate = BigDecimalUtils.div(calcMedian,BigDecimal.valueOf(1200), 8);
		} else if (LoanEnum.STYLE_ONETIME_REPAYMENT.eq(oldProject.getRepayStyle())) {
			if(LoanEnum.TIME_TYPE_DAY.eq(oldProject.getTimeType())){
				// 一次性天标: 变现对应本金 = 待收总额 / (1+原利率 * 总天数/360)
				timeLimit = oldProject.getTimeLimit();
				calcMedian = BigDecimalUtils.mul(oldProject.getApr(),BigDecimal.valueOf(timeLimit));
				calcRate = BigDecimalUtils.div(calcMedian, (ConfigUtils.getBigDecimal("days_of_year").multiply(BigDecimal.valueOf(100))), 8);
			}else if(LoanEnum.TIME_TYPE_MONTH.eq(oldProject.getTimeType())){
				// 一次性月标: 变现对应本金 = 待收总额 / (1+原利率 * 总月份/12)
				timeLimit = oldProject.getTimeLimit();
				calcMedian = BigDecimalUtils.mul(oldProject.getApr(),BigDecimal.valueOf(timeLimit));
				calcRate = BigDecimalUtils.div(calcMedian,BigDecimal.valueOf(1200), 8);
			}else{
				throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_REPAYSTYLE_ISNOT_RIGHT));
			}
		}
		final BigDecimal capital = BigDecimalUtils.div(realize.getRepayAmount(), BigDecimalUtils.add(BigDecimal.ONE,calcRate));
		final BigDecimal interest = BigDecimalUtils.sub(realize.getRepayAmount(), capital);
		LOGGER.info("变现总本息{}元,对应原标本金{}元,利息{}元",realize.getRepayAmount(),capital,interest);
		final Map<String,BigDecimal> map = new HashMap<String, BigDecimal>();
		map.put("capital", capital);
		map.put("interest", interest);
		return map; 
	}

	/**
	 * 变现放款校验
	 * @author fxl
	 * @date 2016年7月29日
	 * @param realize
	 */
	private void checkFullSuccess(final Realize realize){
		if(realize==null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_FOUNT));
		}
		if (!LoanEnum.REALIZE_FLAG_REALIZE.eq(realize.getRealizeFlag())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_PROJECT_NOT_REALIZE));
		}
		if (LoanEnum.STATUS_CANCELLING.eq(realize.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_CANCELLING));
		}
		if (!LoanEnum.STATUS_RAISE_FINISHED.eq(realize.getStatus())) {
			throw new BussinessException(ResourceUtils.get(ResourceConstant.REALIZE_IS_NOT_RAISEEND));
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
	private void updateAccount(final User user, final BigDecimal useVar, final BigDecimal nouseVar, final BigDecimal amount,
			final List<AccountModel> accountList) {
		LOGGER.info("用户({})--用户资金修改，金额：{}" ,user.getRealName(), amount.doubleValue());
		accountList.add(new AccountModel(user.getUuid(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),  useVar,nouseVar));
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
	@Override
	public void addAccountLog(final User user,final String toUserId,final BigDecimal amount,final String accountType,final String paymentsType,
			final List<AccountLog> accountLogList,final String remark) {
		LOGGER.info("用户({})--用户资金记录生成，金额：{}" ,user.getRealName(), amount.doubleValue());
		// 保存账户日志
		final AccountLog accountLog = new AccountLog(ConfigUtils.getValue("account_code"), accountType, user.getUuid(), amount, remark);
		accountLog.preInsert();
		accountLog.setToUser(toUserId);
		accountLog.setPaymentsType(paymentsType);
		accountLogList.add(accountLog);
	}
	
	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeService#getMostRealizeAmount(java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal)
	 */
	@Override
	public BigDecimal getMostRealizeAmount(final BigDecimal waitAmount,final BigDecimal apr,final BigDecimal timeLimit) {
		// 最高可变现金额=（待收本息 - 已变现本息 + 已冻结利息 ）/（1+变现利率*变现借款的计息期限/360)
		// 一年的天数
		final BigDecimal daysOfYear = ConfigUtils.getBigDecimal(ConfigConstant.DAYS_OF_YEAR);
		// 天利率
		final BigDecimal dayApr = BigDecimalUtils.div(BigDecimalUtils.mul(BigDecimalUtils.div(apr,BigDecimal.valueOf(100), 6),timeLimit),daysOfYear ,8);
		// 最高可变现金额
		BigDecimal mostRealizeAmount = BigDecimalUtils.div(waitAmount,BigDecimalUtils.add(BigDecimal.ONE,dayApr));
		// 取整
		mostRealizeAmount = BigDecimalUtils.decimal(mostRealizeAmount,0);
		LOGGER.info("一年天数{},天利率{},最高可变现金额{}",daysOfYear,dayApr,mostRealizeAmount);
		return mostRealizeAmount;
	}
	
	/**
	 * 封装第三方冻结列表
	 * @author fxl
	 * @date 2016年8月1日
	 * @param taskList
	 * @param invest
	 * @param investUser
	 * @param money
	 * @param realize
	 */
	@Override
	public BigDecimal fillFreezeTaskList(final List<TppTrade> taskList, final ProjectInvest invest, final String tppUserCustId,
			final  List<RealizeFreeze> realizeFreezeList, final String projectNo,final String collectionId) {
		BigDecimal freezeMoney = BigDecimal.ZERO;
		for (RealizeFreeze realizeFreeze : realizeFreezeList) {
			final TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_FREEZE.getValue(), TppEnum.TPP_TYPE_FREEZE.getValue(), invest.getUserId(),
					tppUserCustId, null, null, realizeFreeze.getMoney(), invest.getUuid());
			tpp.setProjectId(projectNo);// 项目
			tpp.setInvestProjectId(invest.getProjectId());
			tpp.setFreezeType(UfxFreezeModel.FREEZE_NORMAL);
			final StringBuilder buffer=new StringBuilder();
			buffer.append("{")
				  .append("collectionId:").append(collectionId)
				  .append(",realizeId:").append(realizeFreeze.getRealizeId())
				  .append(",type:").append(realizeFreeze.getFreezeType())
				  .append("}");
			tpp.setParams(buffer.toString());
			taskList.add(tpp);
			freezeMoney = freezeMoney.add(realizeFreeze.getMoney());
		}
		return freezeMoney;
	}
	
	/**
	 * 封装第三方解冻列表
	 * @author fxl
	 * @date 2016年8月19日
	 * @param taskList
	 * @param user
	 * @param money
	 * @param realize
	 * @param freezeNo
	 */
	@Override
	public void fillUnFreezeTaskList(final List<TppTrade> taskList, final User user, final Realize realize,
			final List<AccountModel> accountList,final List<AccountLog> accountLogList) {
		List<RealizeFreeze> freezeList = realizeFreezeService.getFreezedListByRealizeId(realize.getUuid(),RealizeEnum.FREEZE_STATUS_FREEZE.getValue());
		BigDecimal freezeCapitalSum = BigDecimal.ZERO;
		BigDecimal freezeInterestSum = BigDecimal.ZERO;
		if(CollectionUtils.isNotEmpty(freezeList)){
			LOGGER.info("变现解冻冻结资金,realizeId:{},freezeList个数{}",realize.getUuid(),freezeList.size());
			for (RealizeFreeze realizeFreeze : freezeList) {
				BigDecimal unFreezeMoney =  realizeFreeze.getMoney();
				if(BigDecimalUtils.validAmount(unFreezeMoney)){
					final TppTrade tpp = new TppTrade(TppEnum.SERVICE_TYPE_UNFREEZE.getValue(), TppEnum.TPP_TYPE_UNFREEZE.getValue(), user.getUuid(),
							user.getTppUserCustId(), null, null, realizeFreeze.getMoney(), null);
					tpp.setParams(realizeFreeze.getFreezeNo());//冻结订单号放入拓展参数
					tpp.setProjectId(realize.getProjectNo());// 项目NO
					tpp.setInvestProjectId(realize.getProjectId());
					tpp.setUnFreezeType(UfxUnFreezeModel.FREEZE_NORMAL);
					taskList.add(tpp);
					if(Constant.COLLECT_CAPITAL.equals(realizeFreeze.getFreezeType())){
						freezeCapitalSum = freezeCapitalSum.add(unFreezeMoney);
					}else if(Constant.COLLECT_INTEREST.equals(realizeFreeze.getFreezeType())){
						freezeInterestSum = freezeInterestSum.add(unFreezeMoney);
					}
					// 记录资金日志
					if(BigDecimalUtils.validAmount(unFreezeMoney)){
						updateAccount(user,  unFreezeMoney, unFreezeMoney.negate(), unFreezeMoney, accountList);
						final String remark = getRemark(Constant.UNFREEZE_REALIZE_MONEY, unFreezeMoney);
						addAccountLog(user, ConfigUtils.getValue(Constant.ADMIN_ID), unFreezeMoney, Constant.UNFREEZE_REALIZE_MONEY,AccountLog.PAYMENTS_TYPE_EXPEND, accountLogList, remark);
					}
				}
			}
			if(BigDecimalUtils.validAmount(freezeCapitalSum) || BigDecimalUtils.validAmount(freezeInterestSum)){
				projectInvestService.updateRealizeFreeze(realize.getInvestId(),freezeCapitalSum.negate(),freezeInterestSum.negate());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeService#queryCondition()
	 */
	@Override
	@Cacheable(key = CacheConstant.KEY_REALIZE_CONDITION ,expire = ExpireTime.ONE_HOUR)
	public Map<String, Object> queryCondition() {
		final Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("realizeAmountCondition", dictDataService.findListByDictType("realizeAmountCondition"));
		conditionMap.put("realizeDayCondition", dictDataService.findListByDictType("realizeDayCondition"));
		conditionMap.put("realizeAprCondition", dictDataService.findListByDictType("realizeAprCondition"));
		return conditionMap;
	}
	
	@Override
	public List<Realize> findRealizeRepay(final Date nowTime) {
		return dao.findRealizeRepay(nowTime);
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
	
	/**
	 * 获取变现协议ID
	 * @author fxl
	 * @date 2016年11月28日
	 * @return
	 */
	private String getRealizeProtocolId(){
		String protocolId = StringUtils.EMPTY;
		final List<Protocol> protocolList = protocolService.getProtocolListByType(Constant.PROTOCOL_TYPE_REALIZE_INVEST);
		if(CollectionUtils.isNotEmpty(protocolList)){
			for(final Protocol protocol:protocolList){
				if(ProtocolModel.STATUS_VALID.equals(protocol.getStatus())){
					protocolId = protocol.getUuid();
					break;
				}
			}
		}
		return protocolId;
	}
	
}