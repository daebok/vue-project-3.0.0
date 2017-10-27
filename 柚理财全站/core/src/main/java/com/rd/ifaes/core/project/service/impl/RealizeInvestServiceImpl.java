package com.rd.ifaes.core.project.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.Account;
import com.rd.account.model.AccountQueryModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.annotation.CacheEvict;
import com.rd.ifaes.common.constant.CommonConstant;
import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.orm.Page;
import com.rd.ifaes.common.security.shiro.IdGen;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.AccountResource;
import com.rd.ifaes.common.util.resource.LoanResource;
import com.rd.ifaes.core.cache.InvestCache;
import com.rd.ifaes.core.cache.ProjectCache;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.project.domain.Realize;
import com.rd.ifaes.core.project.domain.RealizeRule;
import com.rd.ifaes.core.project.exception.ProjectException;
import com.rd.ifaes.core.project.model.ProjectInvestModel;
import com.rd.ifaes.core.project.service.ProjectCollectionService;
import com.rd.ifaes.core.project.service.ProjectRepaymentService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeInvestService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.model.UserFreezeModel;
import com.rd.ifaes.core.user.service.UserCacheService;
import com.rd.ifaes.core.user.service.UserFreezeService;
import com.rd.ifaes.core.user.service.UserIdentifyService;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 变现投资实现类
 * @version 3.0
 * @author fxl
 * @date 2016年8月1日
 */
@Service("realizeInvestService") 
public class RealizeInvestServiceImpl  extends ProjectInvestServiceImpl implements RealizeInvestService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RealizeInvestServiceImpl.class);
   
	@Resource
    private transient UserIdentifyService userIdentifyService;
    @Resource
    private transient UserService  userService;
	@Resource
	private transient UserCacheService userCacheService;
    @Resource
    private transient AccountService  accountService;
    @Resource
    private transient ProjectCollectionService projectCollectionService;
    @Resource
    private transient RealizeRuleService realizeRuleService;
    @Resource
    private transient RealizeService realizeService;
    @Resource
    private transient UserFreezeService userFreezeService;
    @Resource
    private transient ProjectService projectService;
    @Resource
    private transient ProjectRepaymentService projectRepaymentService;
   

	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeInvestService#doRealizeInvest(com.rd.ifaes.core.project.model.ProjectInvestModel)
	 */
	@Override
	@Transactional(readOnly = false)
	@CacheEvict(keys = CacheConstant.KEY_PREFIX_REALIZE_LIST,interval=ExpireTime.ONE_SEC)
	public UfxInvestModel doRealizeInvest(final ProjectInvestModel model) {
		//校验投资参数有效性
	    checkInvest(model);
	    final User user = userService.get(model.getUserId());
		//投资信息
		final Realize realize = realizeService.findRealize(model.getProjectId());
		//校验变现产品信息
		checkRealize(realize, user);
		model.setBorrowFlag(LoanEnum.BORROW_FLAG_PRODUCT.getValue());
		//未支付订单数校验
		if (!InvestCache.incrUserInvestUnpay(user.getTppUserCustId())) {
			throw new BussinessException(StringUtils.format(ResourceUtils.get(LoanResource.INVEST_UNPAY_TOO_MUCH), ConfigUtils.getValue(ConfigConstant.INVEST_UNPAY_MAX)), BussinessException.TYPE_JSON, "/member/myInvest/list.html?tab=2", "查看详情");
		}
		// 超投校验,同时扣除投资金额
		final double currentProjectAmount=InvestCache.checkOverInvest(realize.getUuid(), model.getAmount().doubleValue());
		LOGGER.info("变现投资进来,金额{}",model.getAmount());
		UfxInvestModel ufxModel = null;
		try {
			checkInvestAmount(realize.prototype(), model, model.getAmount().add(BigDecimal.valueOf(currentProjectAmount)));
			// 生成投资ID,给加加息劵、红包使用
			model.setUuid(IdGen.uuid());
			// 优惠劵信息校验
			checkVoucher(model, realize.prototype(), user);
			// 用户资金判断
			final Account account = accountService.getMoneyByUserId(new AccountQueryModel(user.getUuid(), ConfigUtils.getValue(Constant.ACCOUNT_CODE)));
			if (account.getUseMoney().compareTo(model.getAmount()) < 0) {
				throw new BussinessException(ResourceUtils.get(AccountResource.ACCOUNT_USEMONEY_NOTENOUGHERROR),
						BussinessException.TYPE_CLOSE);
			}
			// 封装UFX投资报文
			ufxModel = createUfxInvestModel(model, realize.prototype(), user);
			//投资记录+1
			userCacheService.addUserInvestNum(model.getUserId());
			// 新建投资记录
			createInvestRecord(model, user, ufxModel.getOrderNo());
			 //冻结投资金额
			handleInvesterAccount(model.getAmount(),model.getRealAmount(),model.getUpApr(),user,ufxModel.getOrderNo(),realize.prototype());

		} catch (Exception e) {
			// 投资失败，归还剩余可投金额
			ProjectCache.incrRemainAccount(realize.getUuid(), model.getAmount().doubleValue());

			//回退用户未支付次数
			InvestCache.decrUserInvestUnpay(user.getTppUserCustId());
			throw e;
		}
		return ufxModel;
	}
	
	 /**
	 * 校验投资参数有效性
	 */
	private void checkInvest(final ProjectInvestModel invest){
		if(StringUtils.isBlank(invest.getProjectId())){
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_ID_IS_NULL), BussinessException.TYPE_JSON);
		}
		if(StringUtils.isBlank(invest.getUserId())){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_EXIST), BussinessException.TYPE_JSON);
		}
		if (!BigDecimalUtils.validAmount(invest.getAmount())) {
			throw new BussinessException(ResourceUtils.get(LoanResource.INVEST_AMOUNT_IS_NOT_VAILD),
					BussinessException.TYPE_JSON);
		}
	}
	
	/**
	 * 校验：项目、用户可投情况 
	 * @param project
	 * @param user
	 */
	private void  checkRealize(final Realize realize,final User user){
		if (realize == null) {
			throw new BussinessException(ResourceUtils.get(LoanResource.PROJECT_NOT_EXISTS), BussinessException.TYPE_JSON);
		}
		//项目状态检查
		if (!LoanEnum.STATUS_RAISING.eq(realize.getStatus())) { // 初审通过
			throw new ProjectException(ResourceUtils.get(ResourceConstant.REALIZE_IS_NOT_RAISING), BussinessException.TYPE_CLOSE);
		}
		//自己不能投自己发的借款
		if (realize.getUserId().equals(user.getUuid())) {
			throw new ProjectException(ResourceUtils.get(ResourceConstant.CANOT_INVEST_SELF_REALIZE), BussinessException.TYPE_CLOSE);
		}
		//  用户投资功能是否被冻结
		if(userFreezeService.isFreezed(user.getUuid(), UserFreezeModel.STATUS_USER_FREEZE_INVEST)){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_FREEZE_INVEST_FREEZED), BussinessException.TYPE_CLOSE);
		}
		
	    //项目筹集额度判断
		if (realize.getAccountYes().compareTo(realize.getAccount())>=0) {
			throw new ProjectException(ResourceUtils.get(ResourceConstant.REALIZE_IS_FULL), BussinessException.TYPE_CLOSE);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rd.ifaes.core.project.service.RealizeInvestService#findRealizeAbleList(com.rd.ifaes.core.project.model.ProjectInvestModel)
	 */
	@Override
	public Page<ProjectInvestModel> findRealizeAbleList(final ProjectInvestModel model) {
		// 有逾期直接返回空列表
		if(projectRepaymentService.checkOverDueByUser(model.getUserId())){
			return model.getPage();
		}
		final RealizeRule rule = realizeRuleService.getRule();
		model.setRemainDays(rule.getRemainDays());
		model.setHoldDays(rule.getHoldDays());
		model.setPeriodRemainDays(rule.getPeriodRemainDays());
		model.setSellAmountMin(rule.getSellAmountMin());
		model.setSellStyle(rule.getSellStyle());
		model.dealFutureTime();
		final List<ProjectInvestModel> list = dao.findRealizeAbleList(model);// 获取可变现的投标
		if(CollectionUtils.isNotEmpty(list)){
			final Date nowTime = DateUtils.getToday();
			for(ProjectInvestModel pmodel :list){
				final Date lastRepayTime = pmodel.getLastRepayTime();
				double remainDay = DateUtils.getDistanceOfTwoDate(nowTime, lastRepayTime);
				if(!DateUtils.before(DateUtils.getNow(), rule.getIssueTime())){// 变现时间在发布时间之后
					remainDay = remainDay-1;
				}
				remainDay = remainDay < 0 ? 0 : remainDay;
				pmodel.setRemainDays((int)remainDay);//剩余期限（起息日到最后的还款日的期限）
				pmodel.setNextRepayTime(pmodel.getNextRepayTime());//本期还款日
			}
		}
		model.getPage().setRows(list);
		return model.getPage();
	}
	
	
}