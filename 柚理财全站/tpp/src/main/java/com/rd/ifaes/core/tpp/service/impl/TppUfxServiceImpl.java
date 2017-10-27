//package com.rd.ifaes.core.tpp.service.impl;
//
//import java.math.BigDecimal;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.alibaba.fastjson.JSON;
//import com.rd.ifaes.common.constant.UfxConstant;
//import com.rd.ifaes.common.dict.CommonEnum;
//import com.rd.ifaes.common.util.Base64Utils;
//import com.rd.ifaes.common.util.BigDecimalUtils;
//import com.rd.ifaes.common.util.NumberUtils;
//import com.rd.ifaes.common.util.StringUtils;
//import com.rd.ifaes.core.core.constant.ConfigConstant;
//import com.rd.ifaes.core.core.constant.Constant;
//import com.rd.ifaes.core.core.util.ConfigUtils;
//import com.rd.ifaes.core.core.util.OrderNoUtils;
//import com.rd.ifaes.core.tpp.model.ufx.UfxAdditionalOrderModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxAuthSignModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxAutoInvestModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxBindBankCardModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxCashAuditModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxCashModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxCompanyRegisterModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxCreditTransferModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxFreezeModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxHelper;
//import com.rd.ifaes.core.tpp.model.ufx.UfxInvestFailModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxInvestModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxLoansModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxLoginModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxMerTransferModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxProjectApplyModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxProjectAttachInfoModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxProjectRevokeModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxProjectUpdateModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBalanceModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxQueryBankCardModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxQueryTransferModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxRechargeModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxRegisterModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxRepaymentModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxUnBindBankCardModel;
//import com.rd.ifaes.core.tpp.model.ufx.UfxUnFreezeModel;
//import com.rd.ifaes.core.tpp.service.TppService;
//
//@Service("tppUfxServiceImpl")
//public class TppUfxServiceImpl extends TppServiceImpl {
//	/**
//	 * 日志记录器
//	 */
//	private static final Logger LOGGER = LoggerFactory.getLogger(TppUfxServiceImpl.class);
//
//	/*不同的第三方共同的接口   个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询  start*/
//	/**
//	 * ufx汇付个人汇付开户
//	 */
//	@Override
//	public Object tppRegister(Map<String, Object> params) {
//		final UfxRegisterModel register = new UfxRegisterModel();
//		register.setOrderNo(OrderNoUtils.getSerialNumber());
//		register.setUserId(StringUtils.isNull(params.get("userId")));
//		register.setRealName(StringUtils.isNull(params.get("realName")));
//		register.setPhone(StringUtils.isNull(params.get("mobilePhone")));
//		register.setEmail(StringUtils.isNull(params.get("email")));
//		register.setIdNo(StringUtils.isNull(params.get("idNo")));
//		register.setSex(StringUtils.isNull(params.get("sex")));
//		register.signReq(register);
//		return register;
//	}
//
//	/**
//	 * ufx汇付个人充值
//	 */
//	@Override
//	public Object tppRecharge(Map<String, Object> map) {
//		final UfxRechargeModel recharge = new UfxRechargeModel();
//		recharge.setOrderNo(OrderNoUtils.getSerialNumber());
//		recharge.setUserAccId(StringUtils.isNull(map.get("userId")));
//		recharge.setUserCustId(StringUtils.isNull(map.get("userCustId")));
//		recharge.setAmount(NumberUtils.format2Str(NumberUtils.getBigDecimal(map.get("amount"))));
//		recharge.setIdNo(StringUtils.isNull(map.get("idNo")));
//		recharge.setRealName(StringUtils.isNull(map.get("realName")));
//		recharge.signReq(recharge);
//		return recharge;
//	}
//
//	/**
//	 * ufx汇付个人提现接口
//	 */
//	@Override
//	public Object tppCash(Map<String, Object> params) {
//		final UfxCashModel cash = new UfxCashModel();
//		cash.setOrderNo(OrderNoUtils.getSerialNumber());
//		cash.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		cash.setAmount(NumberUtils.format2Str(params.get("amount")));
//		cash.setCardId(StringUtils.isNull(params.get("cardId")));
//		cash.setIsAdvance(StringUtils.isNull(params.get("isAdvance")));
//		cash.setServFee(NumberUtils.format2Str(params.get("servFee")));
//		cash.signReq(cash);
//		return cash;
//	}
//
//	/**
//	 * ufx汇付商户充值
//	 */
//	@Override
//	public Object tppMerchantRecharge(Map<String, Object> params) {
//		final UfxRechargeModel recharge = new UfxRechargeModel(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
//		recharge.setOrderNo(OrderNoUtils.getSerialNumber());
//		recharge.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		recharge.setIdNo(UfxConstant.UFX_MERCHANT_IDNO);
//		recharge.signReq(recharge);
//		return recharge;
//	}
//
//	/**
//	 * ufx汇付商户提现
//	 */
//	@Override
//	public Object tppMerchantCash(Map<String, Object> params) {
//		final UfxCashModel cash = new UfxCashModel(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
//		cash.setOrderNo(OrderNoUtils.getSerialNumber());
//		cash.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		cash.setCardId(String.valueOf(params.get("cardId")));
//		cash.setIsAdvance(CommonEnum.YES.eq(ConfigUtils.getValue(ConfigConstant.CASH_FEE_PLATFORM_ADVANCE)) ? UfxCashModel.IS_ADVANCE_Y : UfxCashModel.IS_ADVANCE_N);
//		cash.setServFee(NumberUtils.format2Str(BigDecimalUtils.decimal(new BigDecimal(String.valueOf(params.get("servFee"))), 2)));
//		cash.signReq(cash);
//		return cash;
//	}
//
//
//	/**
//	 * ufx汇付建标
//	 */
//	@Override
//	public Object tppAddProject(Map<String, Object> map) {
//		final UfxProjectApplyModel projectApply = new UfxProjectApplyModel();
//		final String order = OrderNoUtils.getSerialNumber();
//		projectApply.setOrderNo(order);
//		projectApply.setProjectId(StringUtils.isNull(map.get("projectId")));
//		//由于第三方项目名称不可空,当产品名为空时取项目编号
//		if(StringUtils.isBlank(map.get("projectName"))){
//			projectApply.setProjectName(StringUtils.isNull(map.get("projectId")));
//		}else{
//			projectApply.setProjectName(StringUtils.isNull(map.get("projectName")));
//		}
//		projectApply.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
//		projectApply.setRealName(StringUtils.isNull(map.get("realName")));
//		projectApply.setPhone(StringUtils.isNull(map.get("mobile")));
//		projectApply.setProjectUse(StringUtils.isNull(map.get("projectUse")));
//		projectApply.setProjectArea("3301");
//		projectApply.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("projectAmount"))));
//		projectApply.setApr(NumberUtils.format2Str(NumberUtils.getDouble(map.get("apr"))));
//		projectApply.setRepaymentType(StringUtils.isNull(map.get("repaymentType")));
//		projectApply.setRepaymentAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("repaymentAmount"))));
//		projectApply.setRepaymentTime(StringUtils.isNull(map.get("repaymentTime")));
//		projectApply.setStartTime(StringUtils.isNull(map.get("startTime")));
//		projectApply.setEndTime(StringUtils.isNull(map.get("endTime")));
//		projectApply.setUserType(StringUtils.isNull(map.get("userType")));
//		projectApply.setUserId(StringUtils.isNull(map.get("userId")));
//		projectApply.setPayPwd(StringUtils.isNull(map.get("payPwd")));
//		projectApply.setCtrlOverInvest(UfxProjectApplyModel.CTRL_OVER_INVEST_YES);
//		projectApply.setGuarantorCustId(StringUtils.isNull(map.get("guarantorCustId")));
//		projectApply.setNominalCustId(StringUtils.isNull(map.get("nominalCustId")));
//		projectApply.setReqExt(StringUtils.isNull(map.get("reqExt")));
//		UfxHelper.doSubmit(projectApply);
//		if (StringUtils.isNotBlank(projectApply.getRespCode()) && UfxConstant.UFX_CODE_VERIFY.equals(projectApply.getRespCode())) {
//			LOGGER.warn("项目待审,请补录项目信息");
//		}
//
//		return projectApply;
//	}
//
//
//	/**
//	 * UFX汇付 投资
//	 */
//	@Override
//	public Object tppAddTender(Map<String, Object> params) {
//		UfxInvestModel invest = null;
//		if("realize".equals(StringUtils.isNull(params.get("flag")))){
//			invest = new UfxInvestModel("realize");
//		}else{
//			invest = new UfxInvestModel();
//		}
//		invest.setOrderNo(OrderNoUtils.getSerialNumber());
//		invest.setProjectId(StringUtils.isNull(params.get("projectId")));
//		invest.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		invest.setProjectAmount(StringUtils.isNull(params.get("projectAmount")));
//		invest.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		invest.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		invest.setVoucher(StringUtils.isNull(params.get("voucher")));
//		invest.setUserId(StringUtils.isNull(params.get("userNo")));
//		invest.setMerPriv(StringUtils.isNull(params.get("merPriv")));
//		invest.signReq(invest);
//		return invest;
//	}
//
//	/**
//	 * UFX汇付放款
//	 */
//	@Override
//	public Object tppLoan(Map<String, Object> params) {
//		final UfxLoansModel loans = new UfxLoansModel();
//		loans.setOrderNo(StringUtils.isBlank(params.get("orderNo")) ? OrderNoUtils.getSerialNumber()
//				: StringUtils.isNull(params.get("orderNo")));
//		loans.setProjectId(StringUtils.isNull(params.get("projectId")));
//		loans.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		loans.setProjectAmount(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(params.get("projectAmount")))));
//		loans.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		loans.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		loans.setProjectFee(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(params.get("projectFee")))));
//		loans.setVoucher(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(params.get("voucher")))));
//		loans.setInvestNo(StringUtils.isNull(params.get("investNo")));
//		loans.setInvestDate(StringUtils.isNull(params.get("investDate")));
//		loans.setFreezeNo(StringUtils.isNull(params.get("freezeNo")));
//		loans.setUserId(StringUtils.isNull(params.get("userId")));
//		loans.setRealName(StringUtils.isNull(params.get("realName")));
//		loans.setMerPriv(StringUtils.EMPTY);
//		UfxHelper.doSubmit(loans);
//
//		return loans;
//	}
//
//	/**
//	 * UFX汇付还款
//	 */
//	@Override
//	public Object tppRepayment(Map<String, Object> params) {
//		final UfxRepaymentModel repayModel = new UfxRepaymentModel();
//		repayModel.setOrderNo(StringUtils.isBlank(params.get("orderNo")) ? OrderNoUtils.getSerialNumber()
//				: StringUtils.isNull(params.get("orderNo")));
//		repayModel.setProjectId(StringUtils.isNull(params.get("projectId")));
//		repayModel.setOutCustId(StringUtils.isNull(params.get("outCustId")));
//		repayModel.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		repayModel.setInCustId(StringUtils.isNull(params.get("inCustId")));
//		repayModel.setInvestNo(StringUtils.isNull(params.get("investNo")));
//		repayModel.setInvestDate(StringUtils.isNull(params.get("investDate")));
//		repayModel.setInvestCustId(StringUtils.isNull(params.get("investCustId")));
//		repayModel.setAmount(StringUtils.isNull(params.get("amount")));
//		repayModel.setSumIncome(StringUtils.isNull(params.get("sumIncome")));
//		repayModel.setFee(NumberUtils.format2Str(BigDecimalUtils.decimal(new BigDecimal(String.valueOf(params.get("fee"))), 2)));
//		repayModel.setRepayType(StringUtils.isNull(params.get("repayType")));
//		repayModel.setFundFlow(StringUtils.isNull(params.get("fundFlow")));
//		repayModel.setUserId(StringUtils.isNull(params.get("userId")));
//		repayModel.setRealName(StringUtils.isNull(params.get("realName")));
//		UfxHelper.doSubmit(repayModel);
//
//		return repayModel;
//	}
//
//	/**
//	 * 汇付的账户余额查询接口
//	 */
//	@Override
//	public Object tppQueryBalance(Map<String, Object> params) {
//		final UfxQueryBalanceModel model = new UfxQueryBalanceModel();
//		model.setOrderNo(OrderNoUtils.getSerialNumber());
//		model.setUserCustId(StringUtils.checkObj(params.get("userCustId"), params.get("plaCustId")));
//		model.setAccountType(UfxQueryBalanceModel.ACCOUNT_TYPE_CASH);
//		model.setUserType(StringUtils.isNull(params.get("userType")));
//		model.setIdNo(StringUtils.isNull(params.get("idNo")));
//		return (UfxQueryBalanceModel)UfxHelper.doSubmit(model);
//	}
//
//	/*不同的第三方共同的接口   个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询  end*/
//
//	/* ufx汇付接口                 start */
//	/**
//	 * 子账户之间的互转
//	 */
//	@Override
//	public Object tppMerSubAccountTransfer(Map<String, Object> params) {
//		final UfxMerTransferModel merTransfer = new UfxMerTransferModel();
//		merTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
//		merTransfer.setUserCustId(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
//		merTransfer.setReqExt(StringUtils.isNull(params.get("reqExt")));
//		merTransfer.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		merTransfer.setTransAction(UfxMerTransferModel.TRANS_ACTION_USER);
//		merTransfer.setParticAccType(UfxMerTransferModel.ACCOUNT_PRIVATE);
//		UfxHelper.doSubmit(merTransfer);
//		return merTransfer;
//	}
//
//	/**
//	 * UFX汇付的补录项目信息
//	 */
//	@Override
//	public Object tppProjectAttachInfo(Map<String, Object> params) {
//		UfxProjectAttachInfoModel model = new UfxProjectAttachInfoModel();
//		model.setProjectId(StringUtils.isNull(params.get("projectId")));
//		model.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		model.setProjectName(StringUtils.isNull(params.get("projectName")));// 浙商必传
//		model.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("projectAmount"))));
//		model.setStartTime(StringUtils.isNull(params.get("startTime")));
//		model.setProjectPeriod(StringUtils.isNull(params.get("projectPeriod")));// 浙商必传
//		model.setReqExt(StringUtils.isNull(params.get("reqExt")));
//		model.setMerPriv(StringUtils.isNull(params.get("merPriv")));
//		UfxHelper.doSubmit(model);
//		return model;
//	}
//
//	/**
//	 * UFX汇付的撤销项目信息
//	 */
//	@Override
//	public Object tppProjectRevoke(Map<String, Object> params) {
//		UfxProjectRevokeModel revokeModel = new UfxProjectRevokeModel();
//		String order = OrderNoUtils.getSerialNumber();
//		revokeModel.setOrderNo(order);
//		revokeModel.setProjectId(StringUtils.isNull(params.get("projectId")));
//		revokeModel.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		revokeModel.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("projectAmount"))));
//		revokeModel.setUserId(StringUtils.isNull(params.get("userId")));
//		revokeModel.setPayPwd(StringUtils.isNull(params.get("payPwd")));
//		revokeModel.setSuperviseNo(StringUtils.isNull(params.get("superviseNo")));
//		revokeModel.setReqExt(StringUtils.isNull(params.get("reqExt")));
//		revokeModel.setMerPriv(StringUtils.isNull(params.get("merPriv")));
//
//		UfxHelper.doSubmit(revokeModel);
//		return null;
//	}
//
//	/**
//	 * UFX汇付的更新项目信息
//	 */
//	@Override
//	public Object tppUpdateProject(Map<String, Object> params) {
//		UfxProjectUpdateModel updateModel = new UfxProjectUpdateModel();
//		String orderNo = OrderNoUtils.getSerialNumber();
//		updateModel.setOrderNo(orderNo);
//		updateModel.setProjectId(StringUtils.isNull(params.get("projectId")));//
//		updateModel.setProjectName(StringUtils.isNull(params.get("projectName")));// 浙商必传
//		updateModel.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));// 浙商必传
//		updateModel.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		updateModel.setChangeType("01");
//		updateModel.setProjectState(StringUtils.isNull(params.get("projectState")));
//		updateModel.setUserType("01");
//		updateModel.setProjectPeriod(StringUtils.isNull(params.get("projectPeriod")));// 浙商必传
//		UfxHelper.doSubmit(updateModel);
//
//		return updateModel;
//	}
//
//	/**
//	 * UFX汇付企业开户
//	 */
//	@Override
//	public Object tppCropRegister(Map<String, Object> params) {
//		final UfxCompanyRegisterModel register = new UfxCompanyRegisterModel();
//		register.setOrderNo(OrderNoUtils.getSerialNumber());
//		register.setUserId(StringUtils.isNull(params.get("userId")));
//		register.setMobile(StringUtils.isNull(params.get("mobilePhone")));
//		register.setEmail(StringUtils.isNull(params.get("email")));
//		register.setCompanyName(StringUtils.isNull(params.get("companyName")));
//		register.setBussinessCode(StringUtils.isNull(params.get("bussinessCode")));
//		register.setOrgCode(StringUtils.isNull(params.get("orgCode")));
//		register.setGuarType(StringUtils.isNull(params.get("guarType")));
//		register.setMerPriv(Base64Utils.base64Encode(JSON.toJSONString(params)));
//		register.signReq(register);
//		return register;
//	}
//
//	/**
//	 * UFX汇付的三方授权接口
//	 */
//	@Override
//	public Object tppAuthSign(Map<String, Object> params) {
//		final UfxAuthSignModel authSign = new UfxAuthSignModel();
//		authSign.setOrderNo(OrderNoUtils.getSerialNumber());
//		authSign.setUserCustId(StringUtils.isNull(params.get("tppUserCustId")));
//		authSign.setUserAccId(StringUtils.isNull(params.get("tppUserId")));
//		authSign.setServiceTypes(StringUtils.isNull(params.get("serviceType")));
//		authSign.setAuthOption(StringUtils.isNull(params.get("authOption")));
////		authSign.setMerPriv(StringUtils.isNull(params.get("autoTenderId")));
//		authSign.signReq(authSign);
//		return authSign;
//	}
//
//	/**
//	 * UFX汇付的取现复核接口
//	 */
//	@Override
//	public Object tppVerifyCash(Map<String, Object> params) {
//		final UfxCashAuditModel cashAudit = new UfxCashAuditModel();
//		cashAudit.setOrderNo(OrderNoUtils.getSerialNumber());
//		cashAudit.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		cashAudit.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		cashAudit.setAuditFlag(StringUtils.isNull(params.get("auditFlag")));
//		cashAudit.setCashNo(StringUtils.isNull(params.get("cashNo")));
//		cashAudit.signReq(cashAudit);
//		return (UfxCashAuditModel) UfxHelper.doSubmit(cashAudit);
//	}
//
//	/**
//	 * UFX汇付的绑卡接口
//	 */
//	@Override
//	public Object tppAddBankCard(Map<String, Object> params) {
//		final UfxBindBankCardModel bindBankCard = new UfxBindBankCardModel();
//		bindBankCard.setOrderNo(OrderNoUtils.getSerialNumber());
//		bindBankCard.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		bindBankCard.setUserId(StringUtils.isNull(params.get("userId")));
//		bindBankCard.setIdNo(StringUtils.isNull(params.get("idNo")));
//		bindBankCard.setRealName(StringUtils.isNull(params.get("realName")));
//		bindBankCard.signReq(bindBankCard);
//		return bindBankCard;
//	}
//
//	/**
//	 * UFX汇付删除银行卡（解绑银行卡）
//	 */
//	@Override
//	public Object tppDeleteBankBank(Map<String, Object> params) {
//		final UfxUnBindBankCardModel unBindCard = new UfxUnBindBankCardModel();
//		unBindCard.setOrderNo(OrderNoUtils.getSerialNumber());
//		unBindCard.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		unBindCard.setCardId(StringUtils.isNull(params.get("cardId")));
//		unBindCard.setUserId(StringUtils.isNull(params.get("userId")));
//		unBindCard.setBankCode(StringUtils.isNull(params.get("bankCode")));
//		unBindCard.setIdNo(StringUtils.isNull(params.get("idNo")));
//		unBindCard.setRealName(StringUtils.isNull(params.get("realName")));
//		unBindCard.signReq(unBindCard);
//		return unBindCard;
//	}
//
//	/**
//	 * UFX汇付资金冻结
//	 */
//	@Override
//	public Object tppFreeze(Map<String, Object> params) {
//		String orderNo = StringUtils.isBlank(params.get("orderNo"))?OrderNoUtils.getSerialNumber():params.get("orderNo").toString();
//		LOGGER.info("用户资金冻结, orderNo={}", orderNo);
//		final UfxFreezeModel freeze = new UfxFreezeModel();
//		freeze.setOrderNo(orderNo);
//		freeze.setUserId(StringUtils.isNull(params.get("userId")));
//		freeze.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		freeze.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		freeze.setProjectId(StringUtils.isNull(params.get("projectId")));
//		freeze.setFreezeType(StringUtils.isNull(params.get("freezeType")));
//		UfxHelper.doSubmit(freeze);
//		return freeze;
//	}
//
//	/**
//	 * UFX汇付资金解冻
//	 */
//	@Override
//	public Object tppUnFreeze(Map<String, Object> params) {
//		String orderNo = StringUtils.isBlank(params.get("orderNo"))?OrderNoUtils.getSerialNumber():params.get("orderNo").toString();
//		LOGGER.info("调用解冻接口, orderNo={}", orderNo);
//		final UfxUnFreezeModel unFreeze = new UfxUnFreezeModel();
//		unFreeze.setOrderNo(orderNo);
//		unFreeze.setOrderNo(OrderNoUtils.getSerialNumber());
//		unFreeze.setFreezeNo(StringUtils.isNull(params.get("freezeNo")));
//		unFreeze.setUserId(StringUtils.isNull(params.get("userId")));
//		unFreeze.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		unFreeze.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		unFreeze.setProjectId(StringUtils.isNull(params.get("projectId")));
//		unFreeze.setUnFreezeType(StringUtils.isNull(params.get("unFreezeType")));
//		UfxHelper.doSubmit(unFreeze);
//		return unFreeze;
//	}
//
//	/**
//	 * UFX汇付自动投标计划或授权
//	 */
//	@Override
//	public Object tppAutoTenderPlan(Map<String, Object> params) {
//		return null;
//	}
//
//	/**
//	 * UFX汇付关闭自动投标计划
//	 */
//	@Override
//	public Object tppCloseAutoTenderPlan(Map<String, Object> params) {
//		return null;
//	}
//
//	/**
//	 * UFX汇付自动投标操作
//	 */
//	@Override
//	public Object tppAutoTender(Map<String, Object> params) {
//		UfxAutoInvestModel autoInvest = new UfxAutoInvestModel();
//		autoInvest.setOrderNo(OrderNoUtils.getSerialNumber());
//		autoInvest.setProjectId(StringUtils.isNull(params.get("projectId")));
//		autoInvest.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		autoInvest.setProjectAmount(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(params.get("projectAmount")))));
//		autoInvest.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		autoInvest.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(params.get("amount"))));
//		UfxHelper.doSubmit(autoInvest);
//		return autoInvest;
//	}
//
//	/**
//	 * UFX汇付查询用户银行卡
//	 */
//	@Override
//	public Object tppQueryUserBankCard(Map<String, Object> params) {
//		final UfxQueryBankCardModel queryBankCard = new UfxQueryBankCardModel();
//		queryBankCard.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		queryBankCard.setUserId(StringUtils.isNull(params.get("userId")));
//		queryBankCard.setOrderNo(OrderNoUtils.getSerialNumber());
//		queryBankCard.signReq(queryBankCard);
//		return (UfxQueryBankCardModel) UfxHelper.doSubmit(queryBankCard);
//	}
//
//
//	/**
//	 * UFX汇付登录资金托管方
//	 */
//	@Override
//	public Object tppUserLogin(Map<String, Object> params) {
//		final UfxLoginModel login = new  UfxLoginModel();
//		final String orderNo = OrderNoUtils.getSerialNumber();
//		login.setOrderNo(orderNo);
//		login.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		login.signReq(login);
//		return login;
//	}
//
//	@Override
//	public Object tppFssTrans(Map<String, Object> params) {
//		return null;
//	}
//
//	/**
//	 * 第三方商户给用户转账接口
//	 */
//	@Override
//	public Object tppTransfer(Map<String, Object> params) {
//		final UfxMerTransferModel merTransfer = new UfxMerTransferModel();
//		merTransfer.setUserId(StringUtils.isNull(params.get("userNo")));
//		merTransfer.setRealName(StringUtils.isNull(params.get("realName")));
//		merTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
//		merTransfer.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		merTransfer.setAmount(NumberUtils.format2Str(StringUtils.isNull(params.get("money"))));
//		merTransfer.setTransAction(UfxMerTransferModel.TRANS_ACTION_USER);
//		merTransfer.setParticAccType(UfxMerTransferModel.ACCOUNT_PRIVATE);
//		UfxHelper.doSubmit(merTransfer);
//		return merTransfer;
//	}
//
//	/**
//	 * UFX汇付债权转让
//	 */
//	@Override
//	public Object tppCreditAssign(Map<String, Object> map) {
//		LOGGER.info("调用债权投资接口");
//		final UfxCreditTransferModel creditTransfer=  new UfxCreditTransferModel();
//		creditTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
//		creditTransfer.setProjectId(StringUtils.isNull(map.get("projectId")));//被转让项目的项目ID  原始标uuid
//		creditTransfer.setSponsorer(StringUtils.isNull(map.get("sponesorer")));//被转让项目的发起人    原始标借款人商户号
//		creditTransfer.setProjectAmount(StringUtils.isNull(map.get("projectAmount")));//项目的金额   被转让的项目的发布金额#.00
//		creditTransfer.setAssignorCustId(StringUtils.isNull(map.get("assignorCustId")));//转让人   债权转让转让人在第三方的商户号
//		creditTransfer.setAssignAmount(StringUtils.isNull(map.get("assignAmount")));//债权转让金额  #.00
//		creditTransfer.setInvestNo(StringUtils.isNull(map.get("investNo")));//投资流水号
//		creditTransfer.setInvestDate(StringUtils.isNull(map.get("investDate")));//投资日期
//		creditTransfer.setUserCustId(StringUtils.isNull(map.get("userCustId")));//受让人     即 投资人
//		creditTransfer.setAmount(StringUtils.isNull(map.get("amount")));//操作金额  投资金额
//		creditTransfer.setCreditFee(StringUtils.isNull(map.get("creditFee")));//债权转让手续费
//		creditTransfer.setPortion(StringUtils.isNull(map.get("portion")));//债权转让份额
//		creditTransfer.setUserId(StringUtils.isNull(map.get("userId")));//商户平台用户唯一标识
//		creditTransfer.setIdNo(StringUtils.isNull(map.get("idNo")));//身份证号码(不必)
//		creditTransfer.setEndAmt(StringUtils.isNull(map.get("endAmt")));//到期后总金额
//		creditTransfer.setApr(StringUtils.isNull(map.get("apr")));//年化率
//		creditTransfer.setFeeWay(StringUtils.isNull(map.get("FeeWay")));//收费方式
//		creditTransfer.setSuperviseNo(StringUtils.isNull(map.get("superviseNo")));//监管协议编号
//		creditTransfer.setAuthCode(StringUtils.isNull(map.get("authCode")));//授权码
//		creditTransfer.setSmsCode(StringUtils.isNull(map.get("smsCode")));//短信验证码
//		creditTransfer.setRepaymentYesAccount(StringUtils.isNull(map.get("repaymentYesAccount")));//项目已还金额
//		creditTransfer.setPhone(StringUtils.isNull(map.get("phone")));//受让人手机号
//		creditTransfer.setMerPriv(StringUtils.isNull(map.get("merPriv")));
//		creditTransfer.signReq(creditTransfer);
//		return creditTransfer;
//	}
//	/**
//	 * 单笔交易查询
//	 */
//	@Override
//	public Object tppQueryTransfer(Map<String, Object> params) {
//		final UfxQueryTransferModel model = new UfxQueryTransferModel();
//		model.setOrderNo(OrderNoUtils.getSerialNumber());
//		model.setLoanNo(StringUtils.isNull(params.get("loanNo")));
//		model.setLoanDate(StringUtils.isNull(params.get("loanDate")));
//		model.setTransType(StringUtils.isNull(params.get("transType")));
//		model.setReqExt("");
//		return (UfxQueryTransferModel)UfxHelper.doSubmit(model);
//	}
//
//	/**
//	 * 补单
//	 * 		如果obj为空，表示ufx未收到该订单号对应的交易记录，可重新发送该交易信息进行补单；
//	 * 		 如果obj不为空，表示ufx收到该订单号对应的交易记录，可根据响应码做相关业务处理；
//	 * 		loanNo	本系统生成的订单号
//	 * 		loanDate 格式为 yyyyMMdd 请求 ufx 时的日期
//	 * 		isRepeatSend 是否重新发送异步回调：01：发送，02：不发送
//	 */
//	@Override
//	public Object tppAdditionOrder(Map<String, Object> params) {
//		final UfxAdditionalOrderModel model = new UfxAdditionalOrderModel();
//		model.setOrderNo(OrderNoUtils.getSerialNumber());
//		model.setLoanNo(StringUtils.isNull(params.get("loanNo")));
//		model.setLoanDate(StringUtils.isNull(params.get("loanDate")));
//		model.setIsRepeatSend(StringUtils.isNull(params.get("isRepeatSend")));
//		Object obj = UfxHelper.getSubmitResult(model);
//		return obj==null? null:(UfxAdditionalOrderModel)obj;
//	}
//
//	/**
//	 * 投资失败
//	 */
//	@Override
//	public Object tppInvestFail(Map<String, Object> params) {
//		UfxInvestFailModel investFail = new UfxInvestFailModel();
//		String ordno = StringUtils.isNotBlank(StringUtils.isNull(params.get("orderNo")))? StringUtils.isNull(params.get("orderNo")): OrderNoUtils.getSerialNumber();
//		investFail.setOrderNo(ordno);
//		investFail.setProjectId(StringUtils.isNull(params.get("projectId")));
//		investFail.setSponsorer(StringUtils.isNull(params.get("sponsorer")));
//		investFail.setProjectAmount(StringUtils.isNull(params.get("projectAmount")));
//		investFail.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		investFail.setAmount(StringUtils.isNull(params.get("amount")));
//		investFail.setInvestNo(StringUtils.isNull(params.get("investNo")));
//		investFail.setInvestDate(StringUtils.isNull(params.get("investDate")));
//		investFail.setFreezeNo(StringUtils.isNull(params.get("freezeNo")));
//		UfxHelper.doSubmit(investFail);
//		return investFail;
//	}
//
//	/* ufx汇付接口                 end */
//
//	/* 渤海银行存管接口                 start */
//	@Override
//	public Object tppSendUapMsg(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	/**
//	 * 商户账户查询接口
//	 */
//	@Override
//	public Object queryMerchantAccts(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppUpdateBankCard(Map<String, Object> params) {
//		final UfxBindBankCardModel bindBankCard = new UfxBindBankCardModel();
//		bindBankCard.setOrderNo(OrderNoUtils.getSerialNumber());
//		bindBankCard.setUserCustId(StringUtils.isNull(params.get("userCustId")));
//		bindBankCard.setUserId(StringUtils.isNull(params.get("userId")));
//		bindBankCard.setIdNo(StringUtils.isNull(params.get("idNo")));
//		bindBankCard.setRealName(StringUtils.isNull(params.get("realName")));
//		bindBankCard.signReq(bindBankCard);
//		return bindBankCard;
//	}
//
//	@Override
//	public Object tppUpdateMobileNo(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object experBonus(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object queryUserInf(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppQueryTransStat(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppUpdatePayPass(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppInvestCancle(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppExperCap(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppBatInvestCancle(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppExperRealeName(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppInvestChk(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppExpChk(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppWdcChk(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppPpdChk(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppAppRegister(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppAppUpdateBankCard(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppAppRecharge(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppAppCash(Map<String, Object> params) {
//		LOGGER.info("渤海银行的接口，此处无无需处理");
//		return null;
//	}
//	/* 渤海银行存管接口                 end */
//
//	@Override
//	public Object tppSmsCodeApply(Map<String, Object> params) {
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object tppAccountIdQuery(Map<String, Object> params) {
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//
//	@Override
//	public Object voucherPayCancel(Map<String, Object> params) {
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//}
