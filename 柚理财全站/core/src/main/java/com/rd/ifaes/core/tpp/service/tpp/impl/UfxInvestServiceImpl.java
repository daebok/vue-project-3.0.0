package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.rd.account.domain.AccountLog;
import com.rd.account.log.AccountLogback;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountLogService;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.constant.JxbankConstant;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.InvestEnum;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.UserEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.rabbit.RabbitUtils;
import com.rd.ifaes.common.rabbit.model.MqActPlanModel;
import com.rd.ifaes.common.rabbit.model.MqAutoInvestModel;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TppUtil;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.cache.RealizeCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.MqConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.CacheUtils;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.interest.InterestCalculator;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.worker.ProjectWorker;
import com.rd.ifaes.core.statistic.service.StatisticUserService;
import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxAutoInvestModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestFailModel;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.tpp.service.TppService;
import com.rd.ifaes.core.tpp.service.tpp.UfxInvestService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.domain.UserAutoInvest;
import com.rd.ifaes.core.user.domain.UserCache;
import com.rd.ifaes.core.user.domain.UserInvestAutoLog;
import com.rd.ifaes.core.user.mapper.UserAutoInvestMapper;
import com.rd.ifaes.core.user.service.UserAuthSignLogService;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserInvestAutoLogService;
import com.rd.ifaes.core.user.service.UserService;

@Service("ufxInvestService")
public class UfxInvestServiceImpl implements UfxInvestService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxInvestServiceImpl.class);
	@Resource
	private ProjectCollectionService projectCollectionService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private UserService userService;
	@Resource
	private UserCacheService userCacheService;
	@Resource
	private ProjectService projectService;
	@Resource
	private UserAutoInvestMapper userAutoInvestMapper;
	@Resource
	private UserInvestAutoLogService userInvestAutoLogService;
	@Resource
	private AccountService accountService;
	@Resource
	private AccountLogService accountLogService;
	@Resource
	private ActivityPlanService activityPlanService;
	@Resource
    private PlatformTransactionManager transactionManager;
	@Resource
    private StatisticUserService statisticUserService;
	@Resource
	private UserAuthSignLogService userAuthSignLogService;
	
	@Override
	public void ufxInvestHandle(UfxInvestModel model) {
		if (StringUtils.isBlank(model.getOrderNo())) {
			throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_ORDER_NO_NOT_EMPTY));
		}
		ProjectInvest cachedInvest = projectInvestService.getInvestByOrderNo(model.getOrderNo());
		Project pmodel = new Project();
		pmodel.setUuid(cachedInvest.getProjectId());
		pmodel.setStatus(LoanEnum.STATUS_RAISING.getValue());// 募集中

		if (InvestEnum.STATUS_TIMEOUT.eq(cachedInvest.getStatus())) {
			// 被定时器处理为超时
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				ufxInvalidInvestHandle(model, cachedInvest.getUuid(), InvestEnum.STATUS_TIMEOUT.getValue()); // 超时-支付成功
			} else {
				// 超时-支付失败
				projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
						InvestEnum.STATUS_TIMEOUT.getValue());
			}
		} else if (InvestEnum.STATUS_CANCEL.eq(cachedInvest.getStatus())) {
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				// 再支付，取消的前一订单 -支付成功
				ufxInvalidInvestHandle(model, cachedInvest.getUuid(), InvestEnum.STATUS_CANCEL.getValue());
			} else {
				// 再支付，取消的前一订单 -支付失败
				projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
						InvestEnum.STATUS_CANCEL.getValue());
			}
		} else {
			if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
				if (projectService.getCount(pmodel) < Constant.INT_ONE) {
					// 项目不在募集中状态（撤销、下架等处理），成功投资的视为超时处理
					projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_TIMEOUT.getValue(),
							InvestEnum.STATUS_NEW.getValue());
					if (ConfigUtils.isOpenMq()) {
						RabbitUtils.investTimeout(model);
					} else {
						this.ufxInvestTimeout(model);
					}
				} else { // 成功投资
					if (ConfigUtils.isOpenMq()) {
						RabbitUtils.investSuccess(model);
					} else {
						this.ufxInvestSuccess(model);
					}
				}
			} else {
				LOGGER.error("投标汇付处理失败，原因：{}", EncodeUtils.urlDecode(model.getRespDesc()));
				// 未超时-支付失败
				projectInvestService.updateStatus(cachedInvest.getUuid(), InvestEnum.STATUS_FAIL.getValue(),
						InvestEnum.STATUS_NEW.getValue());
				// 失败业务处理
				if (ConfigUtils.isOpenMq()) {
					RabbitUtils.investPayFail(model);
				} else {
					this.investPayFail(model);
				}
			}
		}
	}
	
	
	/**
	 * 投资超时后支付成功-业务处理 
	 * 1）投资记录修改（退款中） 
	 * 2）发送投资失败请求（InvestFail)
	 * 3）回退优惠劵
	 * 4）退回用户投资金额（本地）
	 * 5）退回项目可投金额 
	 * @param model
	 * @param invest
	 */
	@Override
	@Transactional
	public void ufxInvestTimeout(UfxInvestModel model) {
     	LOGGER.info("投资回调处理 -超时（非定时器处理）-orderNo={}----",  model.getOrderNo() );
 
		// 1更新投资记录
		ProjectInvest invest = projectInvestService.getInvestByOrderNo(model.getOrderNo());
		invest.setInvestNo(model.getInvestNo());
		invest.setInvestDate(model.getInvestDate());
		invest.setFreezeNo(model.getFreezeNo());
		invest.setStatus(InvestEnum.STATUS_REFUND.getValue());
		projectInvestService.update(invest);
		
		 // 2 调用接口： investFail ,回调处理优惠劵、修改资金
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderNo", model.getTppOrderNo());
		map.put("projectId", model.getProjectId());
		map.put("projectAmount", model.getSponsorer());
		map.put("userCustId", model.getUserCustId());
		map.put("amount", model.getAmount());
		map.put("investNo", model.getInvestNo());
		map.put("investDate", model.getInvestDate());
		map.put("freezeNo", model.getFreezeNo());
		TppService tppService = (TppService)TppUtil.tppService();
		tppService.tppInvestFail(map);
	 
		//3 归项目还剩余可投金额 (redis)
		double investAmount = Double.valueOf(model.getAmount());
		double remainAmount = ProjectCache.incrRemainAccount( model.getProjectId(), investAmount);
		if(remainAmount == investAmount){//退回之前无剩余可投，项目的投资阶段改为可投
			LOGGER.info("updateStage >>:投资超时后支付成功");
			projectService.updateStage(LoanEnum.STAGE_SALE.getInt(), model.getProjectId());
		}
	}

	/**
	 * 投资支付失败-业务处理
	 *   1）退回项目可投金额
 	 * 	 2）回退用户投资金额
	 * 	 3）回退优惠劵
	 * @param model
	 * @param invest
	 */
	@Transactional
	public void investPayFail(UfxInvestModel model) {
		ProjectInvest cachedInvest=	projectInvestService.getInvestByOrderNo(model.getOrderNo());
		// 1退还红包、加息券
		projectInvestService.backVoucher(cachedInvest.getUuid());
		// 1.2 在投记录-1
		userCacheService.subUserInvestNum(cachedInvest.getUserId());
		
		// 2 退还用户投资实际支付金额 (dubbo)
		if(BigDecimalUtils.validAmount(cachedInvest.getRealAmount())){
			projectInvestService.handleFailInvesterAccount(cachedInvest.getAmount(),cachedInvest.getRealAmount(),cachedInvest.getUserId(),cachedInvest.getInvestOrderNo());
		}
		
		double investAmount = Double.valueOf(model.getAmount());
		//3、归项目还剩余可投金额 (redis)
		double remainAmount = ProjectCache.incrRemainAccount( model.getProjectId(), investAmount);
		if(remainAmount == investAmount){//退回之前无剩余可投，项目的投资阶段改为可投
			LOGGER.info("updateStage >>:投资支付失败");
			projectService.updateStage(LoanEnum.STAGE_SALE.getInt(), model.getProjectId());
		}
		
	}
	/**
	 * 正常投资，成功业务处理
	 * 
	 * @param model
	 * @param invest
	 */
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_PROJECT_LIST,interval=ExpireTime.ONE_SEC)
	public void ufxInvestSuccess(UfxInvestModel model) {
		LOGGER.info("投资回调业务处理 （成功）: {} ", model.getOrderNo());

		ProjectInvest invest = projectInvestService.getInvestByOrderNo(model.getOrderNo());
		LOGGER.info("投资回调业务处理 （成功）,投资记录ID：{}, DB中状态：{} ", invest.getUuid(), invest.getStatus());

		TransactionDefinition definition = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(definition);
		try {
			projectInvestService.updateStatus(invest.getUuid(), InvestEnum.STATUS_SUCCESS.getValue(),
					InvestEnum.STATUS_NEW.getValue());
			// 1更新投资记录
			invest.setInvestNo(model.getInvestNo());
			invest.setInvestDate(model.getInvestDate());
			invest.setFreezeNo(model.getFreezeNo());
			invest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
			projectInvestService.update(invest);

			// 判断是否执行发放首投奖励
			if (!ConfigUtils.firstInvestRewardInVerify()) {
				// 用户首次投资逻辑处理
				UserCache userCache = userCacheService.findByUserId(invest.getUserId());
				if (!CommonEnum.YES.eq(StringUtils.isNull(userCache.getFirstAwardFlag()))
						&& userCacheService.userFirstInvest(invest.getUserId()) > 0) {
					invest.setUserFirstInvest(CommonEnum.YES.getValue());
					LOGGER.info("进入首投,userId = {}", invest.getUserId());
					// 首投活动入队列
					MqActPlanModel actModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_INVEST_FIRST,
							userService.get(invest.getUserId()), invest, null);
					RabbitUtils.actPlan(actModel);
				}
				// 标记用户为投资人
				statisticUserService.updateUserType(invest.getUserId(), UserEnum.USER_TYPE_INVESTOR.getValue());
			}

			// 投资成功- 处理项目已投、投资进度
			this.handleProjectAmount(invest);

			transactionManager.commit(status);
		} catch (Exception e) {
			transactionManager.rollback(status);
			throw e;
		}
		// 用户未支付投资订单数 -1
		if (!InvestCache.decrUserInvestUnpay(model.getUserCustId())) {
			LOGGER.error("投资成功回调-用户(UserCustId：{})未支付投资订单数-处理错误!", model.getUserCustId());
		}

		Project project = projectService.get(invest.getProjectId());
		// 发送通知
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", userService.get(invest.getUserId()));
			params.put("investMoney", invest.getRealAmount().doubleValue());
			params.put("investTime", DateUtils.dateStr4(invest.getCreateTime()));
			params.put("projectName", project.getProjectName().length() > 10
					? project.getProjectName().substring(0, 10) + "..." : project.getProjectName());
			BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST, params);
			baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("给投资人发送投资消息处理失败，investId:{}", invest.getUuid(), e);
		}

		if (ProjectCache.getRemainAccount(project.getUuid()) == 0) {
			LOGGER.info("updateStage >>:投资回调处理");
			projectService.updateStage(LoanEnum.STAGE_FULL.getInt(), project.getProjectNo());
		}
		// 即息理财-开始放款
		if (project.isInterestFinancing() && ConfigUtils.supportInterestFinancing()) {
			projectService.handleInvestLoan(invest);
		}

		LOGGER.info("投资回调业务处理 （ufxInvestSuccess）  {} ----处理结束", model.getOrderNo());
	}
	
	@Override
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_PROJECT_LIST,interval=ExpireTime.ONE_SEC)
	public void cbhbInvestSuccess(CbhbBackInvestModel model,ProjectInvest invest) {
		LOGGER.info("渤海银行---投资回调业务处理 （成功）: {} " , model.getMerBillNo());
	    final User user = userService.get(invest.getUserId());
    	LOGGER.info("渤海银行---投资回调业务处理 （成功）,投资记录ID：{}, DB中状态：{} " , invest.getUuid(),invest.getStatus());
    	
		// 1更新投资记录
        invest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		invest.setInvestNo(model.getTransId());
		invest.setInvestDate(DateUtils.dateStr7(DateUtils.getNow()));
		invest.setFreezeNo(model.getFreezeId());
		invest.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		projectInvestService.insert(invest);
		
		// 判断是否执行发放首投奖励
		if(!ConfigUtils.firstInvestRewardInVerify()){
			// 用户首次投资逻辑处理
			UserCache userCache = userCacheService.findByUserId(invest.getUserId());
			if (!CommonEnum.YES.eq(StringUtils.isNull(userCache.getFirstAwardFlag()))
					&&userCacheService.userFirstInvest(invest.getUserId()) >0){
				invest.setUserFirstInvest(CommonEnum.YES.getValue());
				LOGGER.info("渤海银行---进入首投,userId = {}" , invest.getUserId());
				//首投活动入队列
				MqActPlanModel actModel = new MqActPlanModel(MqConstant.OPERATE_ACTPLAN_INVEST_FIRST,userService.get(invest.getUserId()),invest,null);
				RabbitUtils.actPlan(actModel);
			}
		}
		
		//投资成功- 处理项目已投、投资进度
		this.handleProjectAmount(invest);
		 
		// 用户未支付投资订单数 -1
		LOGGER.info("投资成功,扣除未支付数：{}",user.getTppUserCustId());
		if (!InvestCache.decrUserInvestUnpay(user.getTppUserCustId())) {
		 	LOGGER.error("渤海银行---投资成功回调-用户(UserCustId：{})未支付投资订单数-处理错误!",user.getTppUserCustId() );
		}
		
		Project project = projectService.get(invest.getProjectId());
		// 发送通知
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", userService.get(invest.getUserId()));
			params.put("investMoney", invest.getRealAmount().doubleValue());
			params.put("investTime", DateUtils.dateStr4(invest.getCreateTime()));
			params.put("projectName",project.getProjectName().length()>10?project.getProjectName().substring(0, 10)+"...":project.getProjectName());
	        BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST, params);
	        baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("渤海银行---给投资人发送投资消息处理失败，investId:{}", invest.getUuid(), e);
		}
	
		//即息理财-开始放款    计息理财渤海不支持
	/*	if( project.isInterestFinancing() &&  ConfigUtils.supportInterestFinancing() ){
			projectService.handleInvestLoan(invest);
		}*/
			
		LOGGER.info("渤海银行---投资回调业务处理 （ufxInvestSuccess）  {} ----处理结束", model.getMerBillNo() );
	}
	
	/**
	 * 投资成功- 处理项目已投、投资进度
	 * @author  FangJun
	 * @date 2016年8月19日
	 * @param model
	 */
	private void handleProjectAmount(final ProjectInvest invest) {
		// 1.2 修改项目的已投金额，投资进度
		 //递增修改项目已投
		 projectService.updateAccountYes(invest.getAmount().doubleValue(), invest.getProjectId()); 
		 Project project = projectService.getProjectByUuid(invest.getProjectId());
		 
		 //清理项目详情缓存
		 CacheUtils.del(CacheConstant.KEY_PREFIX_PROJECT_DETAIL+project.getUuid());
		 
		// 判断是否满标，修改项目状态
		if (project.getAccount().compareTo( project.getAccountYes()) == Constant.INT_ZERO) {
			//更新募集结束时间
			final  Project projectTemp=new Project();
			projectTemp.setUuid(project.getUuid());
			//即息理财-投满 显示-直接还款中
			if( project.isInterestFinancing() &&  ConfigUtils.supportInterestFinancing() ){
				projectTemp.setStatus(LoanEnum.STATUS_REPAY_START.getValue());
			}else{
				projectTemp.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
			}
			projectTemp.setRaiseEndTime(DateUtils.getNow());
			projectTemp.setStage(LoanEnum.STAGE_FULL.getInt());//满标
			projectService.update(projectTemp);
			
		   //满标，清理所有项目列表缓存
		  	CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
		}
		// 变现借款删除详情缓存
		if (LoanEnum.REALIZE_FLAG_REALIZE.eq(project.getRealizeFlag())) {
			RealizeCache.delRealizeAndList(project.getUuid());
		}
	}

	/**
	 * 投资超时、取消-支付成功-回调处理 
	 * 
	 * @param model 投资回调信息
	 * @param investId 投资记录ID
	 * @param preStatus 修改前状态
	 */
	@Transactional
	private void ufxInvalidInvestHandle(UfxInvestModel model,String investId,String preStatus) {
		 LOGGER.info("投资回调处理 -超时/取消-orderNo={} ",  model.getOrderNo() );
 
		// 1更新投资记录
		final ProjectInvest investTemp = new ProjectInvest();
		investTemp.setUuid(investId);
		investTemp.setInvestNo(model.getInvestNo());
		investTemp.setInvestDate(model.getInvestDate());
		investTemp.setFreezeNo(model.getFreezeNo());
		projectInvestService.update(investTemp);
		
		// 用户未支付投资订单数 -1
		if (InvestEnum.STATUS_TIMEOUT.eq(preStatus) && !InvestCache.decrUserInvestUnpay(model.getUserCustId())) {
			 LOGGER.error("投资成功回调-用户(userCustId:{} 未支付投资订单数-处理错误!",model.getUserCustId());
		}

		// 2 调用接口： 投资失败资金退回（investFail），UFX撤销用户投资（解冻资金）
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderNo", model.getTppOrderNo());
		map.put("projectId", model.getProjectId());
		map.put("projectAmount", model.getProjectAmount());
		map.put("userCustId", model.getUserCustId());
		map.put("amount", model.getAmount());
		map.put("investNo", model.getInvestNo());
		map.put("investDate", model.getInvestDate());
		map.put("freezeNo", model.getFreezeNo());
		map.put("sponsorer", model.getSponsorer());
		TppService tppService = (TppService)TppUtil.tppService();
		tppService.tppInvestFail(map);

		LOGGER.info("投资回调业务处理 （超时、取消）-orderNo={} ----处理结束", model.getOrderNo());
	}

	@Override
	public void ufxInvestFailHandle(UfxInvestFailModel model) {
	 	ProjectInvest invest = projectInvestService.getInvestByFreezeNo(model.getFreezeNo());
		if(invest == null){
			throw new BussinessException("数据库无该投资记录, FreezeNo=" + model.getFreezeNo());
		}
		
		 LOGGER.info("投资失败退回资金，回调处理 -investNo : {}---- status : {} ", model.getInvestNo(),invest.getStatus());
		 
		if(InvestEnum.STATUS_REFUND.eq(invest.getStatus())){
	    	//1 状态更新入库
		    projectInvestService.updateStatus(invest.getUuid(), InvestEnum.STATUS_FAIL.getValue(), InvestEnum.STATUS_REFUND.getValue());
		 // 1.2 在投记录-1
		    userCacheService.subUserInvestNum(invest.getUserId());
		   // 2 退还红包、加息券
			projectInvestService.backVoucher(invest.getUuid());
			//3 处理本地资金，退还用户投资实际支付金额 (dubbo)
			if(BigDecimalUtils.validAmount(invest.getRealAmount())){
				projectInvestService.handleFailInvesterAccount(invest.getAmount(),invest.getRealAmount(),invest.getUserId(),invest.getInvestOrderNo());
				try {
					Project project = projectService.get(invest.getProjectId());
					HashMap<String, Object> params = new HashMap<String, Object>();
					params.put("user", userService.get(invest.getUserId()));
					params.put("investTime", DateUtils.dateStr4(invest.getCreateTime()));
					params.put("amount", invest.getAmount().doubleValue());
					params.put("realAmount", invest.getRealAmount().doubleValue());
					params.put("projectName", project.getProjectName().length()>10?project.getProjectName().substring(0, 10)+"...":project.getProjectName());
					BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST_FAIL, params);
					baseMsg.doEvent();
				} catch (Exception e) {
					LOGGER.error("给投资人发送投资失败退回资金通知处理失败", e);
				}
			}
		}else{
			projectInvestService.updateStatus(invest.getUuid(), InvestEnum.STATUS_FAIL.getValue(), invest.getStatus());
		}
		 LOGGER.info("投资失败退回资金，回调处理 -investNo: {}----END", model.getInvestNo());
	}

	@Override
	@Transactional(readOnly = false)
	public boolean doAutoInvest(final MqAutoInvestModel model) {
		final Project project = projectService.getProjectByUuid(model.getProject().getUuid());
		// 执行自动投资
		ProjectInvest invest = doAutoInvest(model, project);
		//即息理财-开始放款
		if(invest != null && project.isInterestFinancing() &&  ConfigUtils.supportInterestFinancing() ){
			projectService.handleInvestLoan(invest);
		}
		return true;
	}

	@Transactional(readOnly = false)
	private ProjectInvest doAutoInvest(final MqAutoInvestModel model,final Project base) {
		 LOGGER.info("进入自动投资.................");
		final User user = model.getUser();
		final Project project = projectService.get(base.getUuid());
		
		BigDecimal tempAmount = model.getAmount();
		final String userId = user.getUuid();
		// 获取自动投资
		final UserAutoInvest autoInvest = userAutoInvestMapper.getAutoInvestByUserId(userId);
		autoInvest.setSortTime(DateUtils.getNow().getTime());
		final UserInvestAutoLog investLog = new UserInvestAutoLog(base.getUuid(), userId);
		// 剩余可投 小于 产品最低投资金额，跳出循环
		BigDecimal lowestAccount = base.getLowestAccount();
		// 自动投资时可投资额度小于两倍最底投资额度，直接排序到队尾
		if(tempAmount.compareTo(lowestAccount.multiply(BigDecimal.valueOf(Constant.DOUBLE_TWO))) < Constant.INT_ZERO){
			// 更新auto时间
			userAutoInvestMapper.update(autoInvest);
			// 生成自动投资记录
			investLog.setInvestMoney(tempAmount);
			investLog.setStatus(InvestEnum.STATUS_FAIL.getValue());
			investLog.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_REMAINACCOUNT_LTDOUBLELOWESTACCOUNT));
			userInvestAutoLogService.save(investLog);
			LOGGER.info("自动投资所剩额度已不足两倍最底投资额度");
			return null;
		}
		// 自动投标第三方调用
		UfxAutoInvestModel autoInvestModel = new UfxAutoInvestModel();
		// 查看自动投资调用是否正常，异常直接返回
		try {
			autoInvestModel = doAutoUfxInvest(project, user, tempAmount);
		} catch (Exception e) {
			LOGGER.error("自动投资第三方调用错误",e);
			 
			if (AccountLogback.LOGGER.isErrorEnabled()) {
				AccountLogback.LOGGER.error("auto Invest failed [investOrderNo=" + autoInvestModel==null?null:autoInvestModel.getOrderNo() + ", money=" + tempAmount + "]");
			}
			return null;
		}
		// 处理投资人资金及更新产品信息
		doProjectAccount(tempAmount, project, user,autoInvestModel.getOrderNo());
		// 生成投资记录
		ProjectInvest invest = createInvest(autoInvestModel, tempAmount, user, project,model.getUserCache());
		
		// 更新auto时间
		userAutoInvestMapper.update(autoInvest);

		// 生成自动投资记录
		investLog.setInvestMoney(tempAmount);
		investLog.setStatus(InvestEnum.STATUS_SUCCESS.getValue());
		investLog.setRemark(ResourceUtils.get(ResourceConstant.AUTO_INVEST_SUCCESS));
		userInvestAutoLogService.save(investLog);
		
	 	LOGGER.info("自动投资成功.................");
		return invest;
	}
	
	/**
	 * 
	 * 自动投资生成投资记录
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月6日
	 * @param autoInvestModel
	 * @param amount
	 * @param user
	 * @param project
	 */
	private ProjectInvest createInvest(final UfxAutoInvestModel autoInvestModel,final BigDecimal amount,
			final User user,final Project project,final UserCache userCache) {
		final ProjectInvest invest = new ProjectInvest(autoInvestModel, amount);
		invest.setUserId(user.getUuid());
		invest.setProjectId(project.getUuid());
		invest.setBorrowFlag(project.getBorrowFlag());
		projectInvestService.insert(invest);
		
		// 保存第三方投资记录start
	     AccountLogback.LOGGER.info("auto Invest successed [investOrderNo={}, money={}]",autoInvestModel.getOrderNo(),amount);	
		// 如果是T+n计息保存计息
		if (LoanEnum.INTEREST_STYLE_TN.eq(project.getInterestStyle())) {
			final ProjectWorker worker = new ProjectWorker(project);
			final InterestCalculator ic = worker.interestCalculator(amount, null, null, invest.getCreateTime());
			final BigDecimal repayTotal = ic.repayTotal().add(ic.repayInterest());
			invest.setPayment(repayTotal);
			invest.setInterest(ic.repayInterest());
			invest.setRaiseInterest(ic.raiseInterest());
			invest.setWaitAmount(repayTotal);
			invest.setWaitInterest(ic.collectInterest());
			invest.setWaitRaiseInterest(ic.raiseInterest());
			projectInvestService.update(invest);
		}
		//扣除可投金额
		ProjectCache.decrRemainAccount(project.getUuid(), amount.doubleValue());
		// 扣除自动投资可投
		ProjectCache.decrRemainAccount(project.getUuid()+CacheConstant.KEY_AUTOINVEST_REMAINACCOUNT, amount.doubleValue());
		// 判断是否满标，修改项目状态
		Project proj = projectService.getProjectByUuid(project.getUuid());
		if (proj.getAccount().compareTo(proj.getAccountYes()) == Constant.INT_ZERO) {
			//更新募集结束时间
			final  Project projectTemp=new Project();
			projectTemp.setUuid(project.getUuid());
			projectTemp.setStage(LoanEnum.STAGE_FULL.getInt());
			projectTemp.setStatus(LoanEnum.STATUS_RAISE_FINISHED.getValue());
			projectTemp.setRaiseEndTime(DateUtils.getNow());
			projectService.update(projectTemp);
			
			//满标，清理所有项目列表缓存
		  	CacheUtils.batchDel(CacheConstant.KEY_PREFIX_PROJECT_LIST);
		}
		return invest;
	}

	/**
	 * 
	 * 处理投资人资金及产品信息
	 * 
	 * @author ZhangBiao
	 * @date 2016年8月6日
	 * @param amount
	 * @param project
	 * @param accountYes
	 * @param user
	 */
	private void doProjectAccount(final BigDecimal amount,final Project project,final User user,final String orderNo) {
		// 冻结投资人本金
		final Map<String, Object> remarkData = new HashMap<String, Object>(2);
		remarkData.put("amount", amount.doubleValue());
		remarkData.put("realAmount", amount.doubleValue());
		remarkData.put("info", projectService.getProjectInfo(project.getUuid(), project.getProjectName(), project.getRealizeFlag()));
		remarkData.put("redEnvelope", Constant.INT_ZERO);
		String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_FREEZE, remarkData);
		final AccountLog log = new AccountLog(ConfigUtils.getValue("account_code"), Constant.INVEST_FREEZE, user.getUuid(), amount,
				remark);
		log.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
		log.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
		log.setOrderNo(orderNo);
		accountService.saveAccountChange(
				new AccountModel(user.getUuid(), ConfigUtils.getValue("account_code"), amount.negate(), amount), log);

		projectService.updateAccountYes(amount.doubleValue(), project.getUuid());
	}

	/**
	 * 
	 * 自动投资参数封装及接口调用
	 * @author ZhangBiao
	 * @date 2016年8月6日
	 * @param project
	 * @param user
	 * @param amount
	 * @return
	 */
	private UfxAutoInvestModel doAutoUfxInvest(final Project project, final User user, final BigDecimal amount) {
		final User projectUser = userService.get(project.getUserId());
		final Map<String, Object> map = new HashMap<String, Object>();
		map.put("sponsorer", projectUser.getTppUserCustId());
		map.put("accountId", user.getTppUserCustId());
		map.put("orderId", OrderNoUtils.getSerialNumber());
		map.put("txAmount", BigDecimalUtils.round(amount));
		map.put("productId", project.getProjectNo());
		map.put("frzFlag", JxbankConstant.JXBANK_FRZFLAG_FREEZE);
		map.put("contOrderId", userAuthSignLogService.getOrderNo("1", user.getUuid()));
		TppService tppService = (TppService)TppUtil.tppService();
		return (UfxAutoInvestModel)tppService.tppAutoTender(map);//TODO
	}
}
