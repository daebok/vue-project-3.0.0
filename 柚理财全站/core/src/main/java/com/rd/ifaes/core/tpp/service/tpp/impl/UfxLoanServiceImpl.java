/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.tpp.service.tpp.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rd.account.domain.AccountLog;
import com.rd.account.model.AccountBatchModel;
import com.rd.account.model.AccountModel;
import com.rd.account.service.AccountService;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.LoanEnum;
import com.rd.ifaes.common.dict.TppEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.core.account.model.TppMerchantLogModel;
import com.rd.ifaes.core.account.service.CashFeeMarkLogService;
import com.rd.ifaes.core.account.service.TppMerchantLogService;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.MessageConstant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.sms.msg.BaseMsg;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.LogTemplateUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.operate.service.ActivityPlanService;
import com.rd.ifaes.core.project.domain.Project;
import com.rd.ifaes.core.project.domain.ProjectInvest;
import com.rd.ifaes.core.project.service.ProjectInvestService;
import com.rd.ifaes.core.project.service.ProjectService;
import com.rd.ifaes.core.project.service.RealizeRuleService;
import com.rd.ifaes.core.project.service.RealizeService;
import com.rd.ifaes.core.score.service.UserScoreService;
import com.rd.ifaes.core.tpp.domain.TppTrade;
import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;
import com.rd.ifaes.core.tpp.service.tpp.TppTradeService;
import com.rd.ifaes.core.tpp.service.tpp.UfxLoanService;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.core.user.service.UserService;

/**
 * 放款回调处理
 * 
 * @version 3.0
 * @author FangJun
 * @date 2016年10月14日
 */
@Service("ufxLoanService")
public class UfxLoanServiceImpl implements UfxLoanService {
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxLoanServiceImpl.class);
	@Resource
	private ProjectService projectService;
	@Resource
	private ProjectInvestService projectInvestService;
	@Resource
	private TppTradeService tppTradeService;
	@Resource
	private UserScoreService userScoreService;
	@Resource
	private UserService userService;
	@Resource
	private ActivityPlanService activityPlanService;
	@Resource
	private AccountService accountService;
	@Resource
	private RealizeService realizeService;
	@Resource
	private RealizeRuleService realizeRuleService;
	@Resource
	private TppMerchantLogService tppMerchantLogService;
	@Resource
	private CashFeeMarkLogService cashFeeMarkLogService;
	/**
	 * 放款回调处理
	 * 
	 * @param model
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void ufxLoanHandle(UfxLoansModel model) {
		LOGGER.info("放款回调业务处理 orderNo: {},investNo: {},freezeNo: {} ,projectNo:{},respDesc:{}", model.getOrderNo(), model.getInvestNo(),
				model.getFreezeNo(),model.getProjectId(),model.getRespDesc());
		final TppTrade trade = tppTradeService.findByOrderNo(model.getOrderNo());
		if(trade == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.GLOBAL_RECORD_NO_EXISTS));
		}
		int updateNum = 0;
		if (UfxConstant.UFX_CODE_SUCCESS.equals(model.getRespCode())) {
			updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_SUCEESS.getValue(), model.getRespDesc());
			if (updateNum == Constant.INT_ONE) {
				this.successLoanHandle(trade);
			}
		} else {
			// 失败处理
			updateNum = tppTradeService.callbackHandle(trade.getUuid(), TppEnum.STATUS_FAIL.getValue(), model.getRespDesc());
		}
		if (updateNum != Constant.INT_ONE) {
			LOGGER.warn("放款异步回调处理--更新状态失败,orderNo：{}", model.getOrderNo());
		}
		LOGGER.debug("放款回调业务处理结束 orderNo: {},investNo: {},freezeNo: {} ,projectNo:{}", model.getOrderNo(), model.getInvestNo(),
				model.getFreezeNo(),model.getProjectId());
	}
	
	/**
	 * 放款成功 
	 * @author FangJun
	 * @date 2016年11月3日
	 * @param trade 放款报文记录
	 */
	@Override
	public void successLoanHandle(TppTrade trade){
		// 放款项目
		final Project project = projectService.get(trade.getInvestProjectId());
		// 需要调用Account的dubbo服务执行： 借款人、投资人的账户金额处理
		final List<AccountModel> accountList = new ArrayList<AccountModel>();
		// 借款人、投资人的资金日志
		final List<AccountLog> accountLogList = new ArrayList<AccountLog>();
		// 项目详情页链接
		final String projectInfo = projectService.getProjectInfo(project.getUuid(), projectService.getProjectName(project), project.getRealizeFlag());
		// 处理投资人资金
		handleInvesterAccount(trade, project, projectInfo, accountList, accountLogList);
		// 处理借款人资金
		if (LoanEnum.REALIZE_FLAG_NORMAL.eq(project.getRealizeFlag())) {// 普通标放款
			loanHandleForProject(trade, project, projectInfo, accountList, accountLogList);
		} else {
			loanHandleForRealize(trade, project, projectInfo, accountList, accountLogList);
		}
		// 6 Dubbo服务处理账户信息
		accountService.saveBatch(new AccountBatchModel(accountList, accountLogList));
	}
	/**
	 * 处理投资人资金
	 * @author FangJun
	 * @date 2016年8月9日
	 * @param trade 报文记录
	 */
	private void handleInvesterAccount(TppTrade trade,Project project, String projectInfo, List<AccountModel> accountList,
			List<AccountLog> accountLogList) {
		//投资人、投资记录
		 final User investUser = userService.get(trade.getUserId());
		 ProjectInvest invest = projectInvestService.get(trade.getInvestId());
		 
		// 投资人资金修改
		accountList.add(new AccountModel(trade.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), BigDecimal.ZERO,
				invest.getRealAmount().negate(), invest.getRealAmount().negate()));
		final Map<String, Object> remarkData = new HashMap<String, Object>(1);
		remarkData.put("amount", invest.getAmount().doubleValue());
		remarkData.put("realAmount", invest.getRealAmount().doubleValue());
		remarkData.put("projectInfo", projectInfo);
		final String remark = LogTemplateUtils.getAccountTemplate(Constant.INVEST_SUCCESS, remarkData);
		AccountLog accountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE), Constant.INVEST_SUCCESS,
				investUser.getUuid(), invest.getRealAmount(), remark);
		accountLog.setToUser(project.getUserId());
		accountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
		accountLogList.add(accountLog);
		
		//平台资金日志- 红包抵扣
		BigDecimal voucher=BigDecimalUtils.round(invest.getAmount().subtract(invest.getRealAmount()));
		if(BigDecimalUtils.validAmount(voucher)){
		    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_REDENVELOPE, trade.getUserId(),voucher, trade.getOrderNo());
		}
		try {
			cashFeeMarkLogService.preSave(trade.getUserId(), Constant.INVEST_SUCCESS, 
					invest.getRealAmount().negate(), null, trade.getInvestId());
		} catch (Exception e) {
			LOGGER.error("放款成功，更新提现需要手续费的金额值失败:{}", e.getLocalizedMessage());
		}
		// 给投资人发送项目成立通知  invest_success_msg
		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("user", userService.get(invest.getUserId()));
			params.put("amount", invest.getAmount().doubleValue());
			params.put("realAmount", invest.getRealAmount().doubleValue());
			params.put("projectName", project.getProjectName().length()>10?project.getProjectName().substring(0, 10)+"...":project.getProjectName());
	        BaseMsg baseMsg = new BaseMsg(MessageConstant.INVEST_SUCC, params);
	        baseMsg.doEvent();
		} catch (Exception e) {
			LOGGER.error("给投资人发送项目成立通知处理失败，orderNo:{}", trade.getOrderNo(), e);
		}
	}
	
	/**
	 * 普通标放款处理借款人
	 * @author fxl
	 * @date 2016年10月27日
	 */
	private void loanHandleForProject(TppTrade trade,Project project, String projectInfo, List<AccountModel> accountList,
			List<AccountLog> accountLogList) {
		accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),  trade.getMoney(),
				BigDecimal.ZERO));
		Map<String, Object> borrowerRemarkData = new HashMap<String, Object>(1);
		borrowerRemarkData.put("amount",  trade.getMoney().doubleValue());
		borrowerRemarkData.put("projectInfo", projectInfo);
		String borrowerRemark = LogTemplateUtils.getAccountTemplate(Constant.BORROW_SUCCESS, borrowerRemarkData);
		final AccountLog borrowerAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
				Constant.BORROW_SUCCESS, project.getUserId(),  trade.getMoney(), borrowerRemark);
		borrowerAccountLog.setToUser(Constant.TO_USER_INVEST);
		borrowerAccountLog.setOrderNo(trade.getOrderNo());
		borrowerAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
		accountLogList.add(borrowerAccountLog);

		// 借贷产品收取管理费
//		if (LoanEnum.BORROW_FLAG_BORROW.eq(project.getBorrowFlag()) && trade.getServFee().doubleValue()>0) { 
//				accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
//						trade.getServFee().negate(), BigDecimal.ZERO));
//				Map<String, Object> feeRemarkData = new HashMap<String, Object>(1);
//				feeRemarkData.put("fee", trade.getServFee().doubleValue());
//				String feeRemark = LogTemplateUtils.getAccountTemplate(Constant.BORROW_FEE, feeRemarkData);
//				final AccountLog feeAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
//						Constant.BORROW_FEE, project.getUserId(), trade.getServFee(), feeRemark);
//				feeAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
//				feeAccountLog.setOrderNo(trade.getOrderNo());
//				feeAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
//				accountLogList.add(feeAccountLog);
//				
//				//平台资金日志- 借款管理费
//			    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_BORROW_FEE, project.getUserId(),trade.getServFee(), trade.getOrderNo());
//		}
	}
	
	
	/**
	 * 变现放款处理借款人资金
	 * @author fxl
	 * @date 2016年10月27日
	 */
	private void loanHandleForRealize(TppTrade trade, Project project, String projectInfo, List<AccountModel> accountList,
			List<AccountLog> accountLogList) {
		// 借款人变现入账
		accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),  trade.getMoney(),
				BigDecimal.ZERO));
		Map<String, Object> borrowerRemarkData = new HashMap<String, Object>(1);
		borrowerRemarkData.put("amount",  trade.getMoney().doubleValue());
		borrowerRemarkData.put("projectInfo", projectInfo);
		String borrowerRemark = LogTemplateUtils.getAccountTemplate(Constant.REALIZE_INCOME, borrowerRemarkData);
		final AccountLog borrowerAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
				Constant.REALIZE_INCOME, project.getUserId(),  trade.getMoney(), borrowerRemark);
		borrowerAccountLog.setToUser(Constant.TO_USER_INVEST);
		borrowerAccountLog.setOrderNo(trade.getOrderNo());
		borrowerAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_INCOME);
		accountLogList.add(borrowerAccountLog);
		// 变现管理费
		BigDecimal realizeManageFee = trade.getServFee();
		if (realizeManageFee.doubleValue()>0) {
			accountList.add(new AccountModel(project.getUserId(), ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
					realizeManageFee.negate(), BigDecimal.ZERO));
			Map<String, Object> feeRemarkData = new HashMap<String, Object>(1);
			feeRemarkData.put("amount", realizeManageFee.doubleValue());
			String feeRemark = LogTemplateUtils.getAccountTemplate(Constant.REALIZE_MANAGEFEE, feeRemarkData);
			final AccountLog feeAccountLog = new AccountLog(ConfigUtils.getValue(ConfigConstant.ACCOUNT_CODE),
					Constant.REALIZE_MANAGEFEE, project.getUserId(), realizeManageFee, feeRemark);
			feeAccountLog.setToUser(ConfigUtils.getValue(Constant.ADMIN_ID));
			feeAccountLog.setOrderNo(trade.getOrderNo());
			feeAccountLog.setPaymentsType(AccountLog.PAYMENTS_TYPE_EXPEND);
			accountLogList.add(feeAccountLog);
			
			//平台资金日志- 借款管理费(变现)
		    tppMerchantLogService.saveMerchantLog(TppMerchantLogModel.TYPE_REALIZE_FEE, project.getUserId(),realizeManageFee, trade.getOrderNo());
		}
	}
	

}
