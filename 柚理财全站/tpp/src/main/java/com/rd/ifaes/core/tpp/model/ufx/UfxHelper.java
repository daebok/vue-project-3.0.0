package com.rd.ifaes.core.tpp.model.ufx;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.rd.ifaes.common.constant.UfxConstant;
import com.rd.ifaes.common.dict.CommonEnum;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.Base64Utils;
import com.rd.ifaes.common.util.BigDecimalUtils;
import com.rd.ifaes.common.util.NumberUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.core.core.constant.ConfigConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.ConfigUtils;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.tpp.exception.UfxException;

/**
 * 接口调用
 * @author xhf
 * @version 3.0
 * @date 2015年12月31日 上午11:15:02
 */
public class UfxHelper {
	
	/**
	 * 日志记录器
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(UfxHelper.class);
	
	/**
	 * 提交，状态码必须是0000（成功）
	 */
	public static UfxBaseModel doSubmit(final UfxBaseModel mod) {
		try {
			mod.submit(mod);
		} catch (Exception e) {
			mod.setRespDesc(e.getMessage());
			LOGGER.error("报文发送异常，单号：{}，错误信息：{}",mod.getOrderNo(),e.getMessage());
			LOGGER.error("doSubmit请求异常", e);
		}
		if (!mod.checkReturn()) {
			throw new UfxException(mod.getRespDesc(),BussinessException.TYPE_JSON);
		}
		return mod;
	}
	
	/**
	 * 提交，不判断状态码是否是0000（成功）
	 */
	public static UfxBaseModel getSubmitResult(UfxBaseModel mod) {
		try {
			mod.submit(mod);
		} catch (Exception e) {
			mod=null;
			LOGGER.error("doSubmit请求异常", e);
		}
		return mod;
	}
	
	/**
	 * 授权
	 * @param tppModel
	 * @param serviceTypes
	 * @param authOption
	 * @return
	 */
	@Deprecated
	public static UfxAuthSignModel authSign(final Map<String,Object> map) {
		final UfxAuthSignModel authSign = new UfxAuthSignModel();
		authSign.setOrderNo(OrderNoUtils.getSerialNumber());
		authSign.setUserCustId(StringUtils.isNull(map.get("tppUserCustId")));
		authSign.setUserAccId(StringUtils.isNull(map.get("tppUserId")));
		authSign.setServiceTypes(StringUtils.isNull(map.get("serviceType")));
		authSign.setAuthOption(StringUtils.isNull(map.get("authOption")));
//		authSign.setMerPriv(StringUtils.isNull(map.get("autoTenderId")));
		authSign.signReq(authSign);
		return authSign;
	}
	
	/**
	 * 取现
	 * @param cash
	 * @return
	 */
	@Deprecated
	public static UfxCashModel cash(final Map<String,Object> map) {
		final UfxCashModel cash = new UfxCashModel();
		cash.setOrderNo(OrderNoUtils.getSerialNumber());
		cash.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		cash.setAmount(NumberUtils.format2Str(map.get("amount")));
		cash.setCardId(StringUtils.isNull(map.get("cardId")));
		cash.setIsAdvance(StringUtils.isNull(map.get("isAdvance")));
		cash.setServFee(NumberUtils.format2Str(map.get("servFee")));
		cash.signReq(cash);
		return cash;
	}
	
	/**
	 * 取现复核
	 * @param tppModel
	 * @return
	 */
	@Deprecated
	public static UfxCashAuditModel cashAudit(final Map<String,Object> map) {
		final UfxCashAuditModel cashAudit = new UfxCashAuditModel();
		cashAudit.setOrderNo(OrderNoUtils.getSerialNumber());
		cashAudit.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		cashAudit.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		cashAudit.setAuditFlag(StringUtils.isNull(map.get("auditFlag")));
		cashAudit.setCashNo(StringUtils.isNull(map.get("cashNo")));
		cashAudit.signReq(cashAudit);
		return (UfxCashAuditModel) doSubmit(cashAudit);
	}
	
	/**
	 * 发布项目
	 * @param model
	 * @return
	 */
	@Deprecated
	public static UfxProjectApplyModel projectApply(final Map<String,Object> map){
		final UfxProjectApplyModel projectApply = new UfxProjectApplyModel();
		final String order = OrderNoUtils.getSerialNumber();
		projectApply.setOrderNo(order);
		projectApply.setProjectId(StringUtils.isNull(map.get("projectId")));
		//由于第三方项目名称不可空,当产品名为空时取项目编号
		if(StringUtils.isBlank(map.get("projectName"))){
			projectApply.setProjectName(StringUtils.isNull(map.get("projectId")));
		}else{
			projectApply.setProjectName(StringUtils.isNull(map.get("projectName")));
		}
		projectApply.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		
		projectApply.setRealName(StringUtils.isNull(map.get("realName")));
		projectApply.setPhone(StringUtils.isNull(map.get("mobile")));
		projectApply.setProjectUse(StringUtils.isNull(map.get("projectUse")));
		
		projectApply.setProjectArea("3301");
		projectApply.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("projectAmount"))));
		projectApply.setApr(NumberUtils.format2Str(NumberUtils.getDouble(map.get("apr"))));
		projectApply.setRepaymentType(StringUtils.isNull(map.get("repaymentType")));
		
		projectApply.setRepaymentAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("repaymentAmount"))));
		projectApply.setRepaymentTime(StringUtils.isNull(map.get("repaymentTime")));
		projectApply.setStartTime(StringUtils.isNull(map.get("startTime")));
		projectApply.setEndTime(StringUtils.isNull(map.get("endTime")));
		projectApply.setUserType(StringUtils.isNull(map.get("userType")));
		
		projectApply.setUserId(StringUtils.isNull(map.get("userId")));
		projectApply.setPayPwd(StringUtils.isNull(map.get("payPwd")));
		projectApply.setCtrlOverInvest(UfxProjectApplyModel.CTRL_OVER_INVEST_YES);
		projectApply.setGuarantorCustId(StringUtils.isNull(map.get("guarantorCustId")));
		projectApply.setNominalCustId(StringUtils.isNull(map.get("nominalCustId")));
		
		projectApply.setReqExt(StringUtils.isNull(map.get("reqExt")));
		doSubmit(projectApply);
		if (StringUtils.isNotBlank(projectApply.getRespCode()) && UfxConstant.UFX_CODE_VERIFY.equals(projectApply.getRespCode())) {
			LOGGER.warn("项目待审,请补录项目信息");
		}
	 
		return projectApply;
				
	}
	
	/**
	 * 更新项目信息
	 * @param ufxModel
	 * @return 1:成功，2：失败
	 */
	@Deprecated
	public static UfxProjectUpdateModel projectUpdate(Map<String, Object> map){//UfxModel ufxModel
		UfxProjectUpdateModel updateModel = new UfxProjectUpdateModel();
		String orderNo = OrderNoUtils.getSerialNumber();
		updateModel.setOrderNo(orderNo);
		updateModel.setProjectId(StringUtils.isNull(map.get("projectId")));//
		updateModel.setProjectName(StringUtils.isNull(map.get("projectName")));// 浙商必传
		updateModel.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));// 浙商必传
		updateModel.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		updateModel.setChangeType("01");
		updateModel.setProjectState(StringUtils.isNull(map.get("projectState")));
		updateModel.setUserType("01");
		updateModel.setProjectPeriod(StringUtils.isNull(map.get("projectPeriod")));// 浙商必传
		
		doSubmit(updateModel);
 
		return updateModel;
	}
	
	/**
     * 撤销项目信息:仅用于尚未投标的项目<br>
     * 目前仅徽商、兴业支持此业务
     * @param map
     */
	@Deprecated
	public static void projectRevoke(Map<String, Object> map){ UfxProjectRevokeModel revokeModel = new UfxProjectRevokeModel();
		String order = OrderNoUtils.getSerialNumber();
		revokeModel.setOrderNo(order);
		revokeModel.setProjectId(StringUtils.isNull(map.get("projectId")));
		revokeModel.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		revokeModel.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("projectAmount"))));
		revokeModel.setUserId(StringUtils.isNull(map.get("userId")));
		revokeModel.setPayPwd(StringUtils.isNull(map.get("payPwd")));
		revokeModel.setSuperviseNo(StringUtils.isNull(map.get("superviseNo")));
		revokeModel.setReqExt(StringUtils.isNull(map.get("reqExt")));
		revokeModel.setMerPriv(StringUtils.isNull(map.get("merPriv")));
		
		doSubmit(revokeModel);
	 
	}
	
	/**
	 * 补录项目信息
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxProjectAttachInfoModel projectAttachInfo(Map<String, Object> map){
		UfxProjectAttachInfoModel model = new UfxProjectAttachInfoModel();
		model.setProjectId(StringUtils.isNull(map.get("projectId")));
		model.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		model.setProjectName(StringUtils.isNull(map.get("projectName")));// 浙商必传
		model.setProjectAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("projectAmount"))));
		model.setStartTime(StringUtils.isNull(map.get("startTime")));
		model.setProjectPeriod(StringUtils.isNull(map.get("projectPeriod")));// 浙商必传
		model.setReqExt(StringUtils.isNull(map.get("reqExt")));
		model.setMerPriv(StringUtils.isNull(map.get("merPriv")));
		doSubmit(model);
	 	
		return model;
		
	}
	

	/**
	 * 个人开户
	 * @param ufxModel
	 * @return
	 * @throws Exception 
	 */
	@Deprecated
	public static UfxRegisterModel register(final Map<String,Object> map){
		final UfxRegisterModel register = new UfxRegisterModel();
		register.setOrderNo(OrderNoUtils.getSerialNumber());
		register.setUserId(StringUtils.isNull(map.get("userId")));
		register.setRealName(StringUtils.isNull(map.get("realName")));
		register.setPhone(StringUtils.isNull(map.get("mobilePhone")));
		register.setEmail(StringUtils.isNull(map.get("email")));
		register.setIdNo(StringUtils.isNull(map.get("idNo")));
		register.setSex(StringUtils.isNull(map.get("sex")));
		register.signReq(register);
		return register;
	}
	
	/**
	 * 企业开户
	 * 
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxCompanyRegisterModel companyRegiseter(final Map<String, Object> map) {
		final UfxCompanyRegisterModel register = new UfxCompanyRegisterModel();
		register.setOrderNo(OrderNoUtils.getSerialNumber());
		register.setUserId(StringUtils.isNull(map.get("userId")));
		register.setMobile(StringUtils.isNull(map.get("mobilePhone")));
		register.setEmail(StringUtils.isNull(map.get("email")));
		register.setCompanyName(StringUtils.isNull(map.get("companyName")));
		register.setBussinessCode(StringUtils.isNull(map.get("bussinessCode")));
		register.setOrgCode(StringUtils.isNull(map.get("orgCode")));
		register.setGuarType(StringUtils.isNull(map.get("guarType")));
		register.setMerPriv(Base64Utils.base64Encode(JSON.toJSONString(map)));
		register.signReq(register);
		return register;
	}
	
	/**
	 * 企业账户查询(部分第三方支付接口不支持)2
	 * @param accountId
	 * @return
	 */
	@Deprecated
	public static UfxCompanyInfoModel queryComAccInfo(String accountId){
		final UfxCompanyInfoModel model = new UfxCompanyInfoModel();
		model.setOrderNo(OrderNoUtils.getSerialNumber());
		model.setAccountId(accountId);
		model.signReq(model);
		return (UfxCompanyInfoModel)doSubmit(model);
	}

    /**
	 * 投资
	 * @param ufxModel
	 * @return
	 */ 
	@Deprecated
	public static UfxInvestModel invest(final Map<String,Object> map){
		final UfxInvestModel invest = new UfxInvestModel();
		invest.setOrderNo(OrderNoUtils.getSerialNumber());
		invest.setProjectId(StringUtils.isNull(map.get("projectId")));
		invest.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		invest.setProjectAmount(StringUtils.isNull(map.get("projectAmount")));
		invest.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		invest.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		invest.setVoucher(StringUtils.isNull(map.get("voucher")));
		invest.setUserId(StringUtils.isNull(map.get("userNo")));
		invest.setMerPriv(StringUtils.isNull(map.get("merPriv")));
		invest.signReq(invest);
		return invest;
	}
	
	
	/**
	 * 变现投资
	 * @author fxl
	 * @date 2016年8月30日
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxInvestModel realizeInvest(final Map<String, Object> map) {
		final UfxInvestModel invest = new UfxInvestModel("realize");
		invest.setOrderNo(OrderNoUtils.getSerialNumber());
		invest.setProjectId(StringUtils.isNull(map.get("projectId")));
		invest.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		invest.setProjectAmount(StringUtils.isNull(map.get("projectAmount")));
		invest.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		invest.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		invest.setVoucher(StringUtils.isNull(map.get("voucher")));
		invest.setUserId(StringUtils.isNull(map.get("userNo")));
		invest.setMerPriv(StringUtils.isNull(map.get("merPriv")));
		invest.signReq(invest);
		return invest;
	}
    /**
	 * 自动投标
	 * @param tppModel
	 * @return
	 */
	@Deprecated
	public static UfxAutoInvestModel autoInvest(Map<String,Object> map){
		UfxAutoInvestModel autoInvest = new UfxAutoInvestModel();
		autoInvest.setOrderNo(OrderNoUtils.getSerialNumber());
		autoInvest.setProjectId(StringUtils.isNull(map.get("projectId")));
		autoInvest.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		autoInvest.setProjectAmount(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(map.get("projectAmount")))));
		autoInvest.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		autoInvest.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		doSubmit(autoInvest);
	 
		return autoInvest;
	}
	
	/**
	 * 投资失败，资金撤回
	 * @param ufxModel
	 * @return
	 */ 
	@Deprecated
	public static UfxInvestFailModel investFail(UfxInvestModel ufxModel){
		UfxInvestFailModel investFail = new UfxInvestFailModel();
		String ordno = StringUtils.isNotBlank(ufxModel.getTppOrderNo())? ufxModel.getTppOrderNo(): OrderNoUtils.getSerialNumber();
		investFail.setOrderNo(ordno);
		
		investFail.setProjectId(ufxModel.getProjectId());
		investFail.setSponsorer(ufxModel.getSponsorer());
		investFail.setProjectAmount(ufxModel.getProjectAmount());
		investFail.setUserCustId(ufxModel.getUserCustId());
		investFail.setAmount(ufxModel.getAmount());
		investFail.setInvestNo(ufxModel.getInvestNo());
		investFail.setInvestDate(ufxModel.getInvestDate());
		investFail.setFreezeNo(ufxModel.getFreezeNo());
		 
		doSubmit(investFail);
		
		return investFail;
	}
	
	
	/**
	 * 充值
	 * @param tppModel
	 * @return
	 */
	@Deprecated
	public static UfxRechargeModel recharge(final Map<String, Object> map){
		final UfxRechargeModel recharge = new UfxRechargeModel();
		recharge.setOrderNo(OrderNoUtils.getSerialNumber());
		recharge.setUserAccId(StringUtils.isNull(map.get("userId")));
		recharge.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		recharge.setAmount(NumberUtils.format2Str(NumberUtils.getBigDecimal(map.get("amount"))));
		recharge.setIdNo(StringUtils.isNull(map.get("idNo")));
		recharge.setRealName(StringUtils.isNull(map.get("realName")));
		recharge.signReq(recharge);
		return recharge;
	}
	
	/**
	 * 放款
	 * @param ufxModel
	 * @return
	 */ 
	@Deprecated
	public static UfxLoansModel loans(final Map<String,Object> map){
		final UfxLoansModel loans = new UfxLoansModel();
		loans.setOrderNo(StringUtils.isBlank(map.get("orderNo")) ? OrderNoUtils.getSerialNumber()
				: StringUtils.isNull(map.get("orderNo")));
		loans.setProjectId(StringUtils.isNull(map.get("projectId")));
		loans.setSponsorer(StringUtils.isNull(map.get("sponsorer")));
		loans.setProjectAmount(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(map.get("projectAmount")))));
		loans.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		loans.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		loans.setProjectFee(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(map.get("projectFee")))));
		loans.setVoucher(NumberUtils.format2Str(Double.parseDouble(StringUtils.isNull(map.get("voucher")))));
		loans.setInvestNo(StringUtils.isNull(map.get("investNo")));
		loans.setInvestDate(StringUtils.isNull(map.get("investDate")));
		loans.setFreezeNo(StringUtils.isNull(map.get("freezeNo")));
		loans.setUserId(StringUtils.isNull(map.get("userId")));
		loans.setRealName(StringUtils.isNull(map.get("realName")));
		loans.setMerPriv("");
		doSubmit(loans);
	
		return loans;
	}

	/**
	 * 查询用户银行卡
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxQueryBankCardModel queryBank(final Map<String,Object> map) {
		final UfxQueryBankCardModel queryBankCard = new UfxQueryBankCardModel();
		queryBankCard.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		queryBankCard.setUserId(StringUtils.isNull(map.get("userId")));
		queryBankCard.setOrderNo(OrderNoUtils.getSerialNumber());
		queryBankCard.signReq(queryBankCard);
		return (UfxQueryBankCardModel) doSubmit(queryBankCard);
	}
	
	/**
	 * 绑卡
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxBindBankCardModel bindBankCard(final Map<String,Object> map){
		final UfxBindBankCardModel bindBankCard = new UfxBindBankCardModel();
		bindBankCard.setOrderNo(OrderNoUtils.getSerialNumber());
		bindBankCard.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		bindBankCard.setUserId(StringUtils.isNull(map.get("userId")));
		bindBankCard.setIdNo(StringUtils.isNull(map.get("idNo")));
		bindBankCard.setRealName(StringUtils.isNull(map.get("realName")));
		bindBankCard.signReq(bindBankCard);
		return bindBankCard;
	}
	
	/**
	 * 解绑银行卡
	 * @param map
	 * @return
	 */
	@Deprecated
	public static UfxUnBindBankCardModel unBindCard(final Map<String,Object> map){
		final UfxUnBindBankCardModel unBindCard = new UfxUnBindBankCardModel();
		unBindCard.setOrderNo(OrderNoUtils.getSerialNumber());
		unBindCard.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		unBindCard.setCardId(StringUtils.isNull(map.get("cardId")));
		unBindCard.setUserId(StringUtils.isNull(map.get("userId")));
		unBindCard.setBankCode(StringUtils.isNull(map.get("bankCode")));
		unBindCard.setIdNo(StringUtils.isNull(map.get("idNo")));
		unBindCard.setRealName(StringUtils.isNull(map.get("realName")));
		unBindCard.signReq(unBindCard);
		return unBindCard;
	}
	
	/**
	 * 汇付-生利宝
	 * @param ufxModel
	 * @return
	 *//*
	public static UfxFssTransModel fssTrans(final Map<String,Object> map ){
		final UfxFssTransModel fssTrans = new UfxFssTransModel();
		fssTrans.setUserCustId(StringUtil.isNull(map.get("userCustId")));
		fssTrans.setOrderNo(OrderNoUtils.getSerialNumber());
		fssTrans.signReq(fssTrans);
		return fssTrans;
	}	
	*/
	
	/**
	 * 用户资金冻结
	 * @param ufxModel
	 * @return
	 */
	@Deprecated
	public static UfxFreezeModel freeze(final Map<String,Object> map) {
		String orderNo = StringUtils.isBlank(map.get("orderNo"))?OrderNoUtils.getSerialNumber():map.get("orderNo").toString();
		LOGGER.info("用户资金冻结, orderNo={}", orderNo);
		final UfxFreezeModel freeze = new UfxFreezeModel();
		freeze.setOrderNo(orderNo);
		freeze.setUserId(StringUtils.isNull(map.get("userId")));
		freeze.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		freeze.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		freeze.setProjectId(StringUtils.isNull(map.get("projectId")));
		freeze.setFreezeType(StringUtils.isNull(map.get("freezeType")));
		doSubmit(freeze);
 
		return freeze;
	}
	
	/**
	 * 用户资金解冻
	 * @param ufxModel
	 * @return
	 */
	public static UfxUnFreezeModel unFreeze(final Map<String,Object> map) {
		String orderNo = StringUtils.isBlank(map.get("orderNo"))?OrderNoUtils.getSerialNumber():map.get("orderNo").toString();
		LOGGER.info("调用解冻接口, orderNo={}", orderNo);
		final UfxUnFreezeModel unFreeze = new UfxUnFreezeModel();
		unFreeze.setOrderNo(orderNo);
		unFreeze.setOrderNo(OrderNoUtils.getSerialNumber());
		unFreeze.setFreezeNo(StringUtils.isNull(map.get("freezeNo")));
		unFreeze.setUserId(StringUtils.isNull(map.get("userId")));
		unFreeze.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		unFreeze.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		unFreeze.setProjectId(StringUtils.isNull(map.get("projectId")));
		unFreeze.setUnFreezeType(StringUtils.isNull(map.get("unFreezeType")));
		doSubmit(unFreeze);
	 
		return unFreeze;
	}
	
	/**
	 *  UFX 债权投资接口
	 * @author QianPengZhan
	 * @date 2016年7月28日
	 * @param map
	 * @return
	 */
	public static UfxCreditTransferModel creditTransfer(final Map<String,Object> map){
		LOGGER.info("调用债权投资接口");
		final UfxCreditTransferModel creditTransfer=  new UfxCreditTransferModel();
		creditTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
		creditTransfer.setProjectId(StringUtils.isNull(map.get("projectId")));//被转让项目的项目ID  原始标uuid
		creditTransfer.setSponsorer(StringUtils.isNull(map.get("sponesorer")));//被转让项目的发起人    原始标借款人商户号
		creditTransfer.setProjectAmount(StringUtils.isNull(map.get("projectAmount")));//项目的金额   被转让的项目的发布金额#.00
		creditTransfer.setAssignorCustId(StringUtils.isNull(map.get("assignorCustId")));//转让人   债权转让转让人在第三方的商户号
		creditTransfer.setAssignAmount(StringUtils.isNull(map.get("assignAmount")));//债权转让金额  #.00
		creditTransfer.setInvestNo(StringUtils.isNull(map.get("investNo")));//投资流水号
		creditTransfer.setInvestDate(StringUtils.isNull(map.get("investDate")));//投资日期
		creditTransfer.setUserCustId(StringUtils.isNull(map.get("userCustId")));//受让人     即 投资人
		creditTransfer.setAmount(StringUtils.isNull(map.get("amount")));//操作金额  投资金额
		creditTransfer.setCreditFee(StringUtils.isNull(map.get("creditFee")));//债权转让手续费
		creditTransfer.setPortion(StringUtils.isNull(map.get("portion")));//债权转让份额
		creditTransfer.setUserId(StringUtils.isNull(map.get("userId")));//商户平台用户唯一标识
		creditTransfer.setIdNo(StringUtils.isNull(map.get("idNo")));//身份证号码(不必)
		creditTransfer.setEndAmt(StringUtils.isNull(map.get("endAmt")));//到期后总金额
		creditTransfer.setApr(StringUtils.isNull(map.get("apr")));//年化率
		creditTransfer.setFeeWay(StringUtils.isNull(map.get("FeeWay")));//收费方式
		creditTransfer.setSuperviseNo(StringUtils.isNull(map.get("superviseNo")));//监管协议编号
		creditTransfer.setAuthCode(StringUtils.isNull(map.get("authCode")));//授权码
		creditTransfer.setSmsCode(StringUtils.isNull(map.get("smsCode")));//短信验证码
		creditTransfer.setRepaymentYesAccount(StringUtils.isNull(map.get("repaymentYesAccount")));//项目已还金额
		creditTransfer.setPhone(StringUtils.isNull(map.get("phone")));//受让人手机号
		creditTransfer.setMerPriv(StringUtils.isNull(map.get("merPriv")));
		creditTransfer.signReq(creditTransfer);
		return creditTransfer;
	}
	
	/**
	 * 登录资金托管方
	 * @param ufxModel
	 * @return
	 */
	@Deprecated
	public static UfxLoginModel login(final Map<String,Object> map){
		final UfxLoginModel login = new  UfxLoginModel();
		final String orderNo = OrderNoUtils.getSerialNumber();
		login.setOrderNo(orderNo);
		login.setUserCustId(StringUtils.isNull(map.get("userCustId")));
		login.signReq(login);
		return login;
		
	}
	
	
	/**
	 * 还款
	 * @param borrowModel
	 * @param repayment
	 * @return
	 */
	@Deprecated
	public static UfxRepaymentModel repayment(final Map<String,Object> map){
		final UfxRepaymentModel repayModel = new UfxRepaymentModel();
		repayModel.setOrderNo(StringUtils.isBlank(map.get("orderNo")) ? OrderNoUtils.getSerialNumber()
				: StringUtils.isNull(map.get("orderNo")));
		repayModel.setProjectId(StringUtils.isNull(map.get("projectId")));
		repayModel.setOutCustId(StringUtils.isNull(map.get("outCustId")));
		repayModel.setSponsorer(StringUtils.isNull(map.get("sponsorer"))); 
		repayModel.setInCustId(StringUtils.isNull(map.get("inCustId")));
		repayModel.setInvestNo(StringUtils.isNull(map.get("investNo")));
		repayModel.setInvestDate(StringUtils.isNull(map.get("investDate")));
		repayModel.setInvestCustId(StringUtils.isNull(map.get("investCustId")));
		repayModel.setAmount(StringUtils.isNull(map.get("amount")));
		repayModel.setSumIncome(StringUtils.isNull(map.get("sumIncome")));
		repayModel.setFee(NumberUtils.format2Str(BigDecimalUtils.decimal(new BigDecimal(String.valueOf(map.get("fee"))), 2)));
		repayModel.setRepayType(StringUtils.isNull(map.get("repayType")));
		repayModel.setFundFlow(StringUtils.isNull(map.get("fundFlow")));
		repayModel.setUserId(StringUtils.isNull(map.get("userId")));
		repayModel.setRealName(StringUtils.isNull(map.get("realName")));
		doSubmit(repayModel);
	 
		return repayModel;
	}
	
	/**
	 * 更新标的状态先更新成发标、再更新成投标中（联动使用，不属于接口调用）
	 * 联动的借款标和债权标都需要在第三方去更新标状态
	 * @param ufxProjectNo
	 * @param user
	 * @return
	 *//*
	public static List<UfxModel> updateProjectStatus(String ufxProjectNo,User user){
		//处理标状态更新信息:1、更新为开标状态，2、更新为投标中
    	List<UfxModel> ufxList = new ArrayList<UfxModel>();
    	UfxModel installModel = new UfxModel();
    	installModel.setServiceType(UfxModel.PROJECT_UPDATE);
    	installModel.setTppType(UfxModel.TPP_PROJECT_UPDATE);
    	installModel.setBidNo(ufxProjectNo);
    	installModel.setTppUserCustId(user.getTppUserCustId());
    	installModel.setUser(user);
    	installModel.setFlag("0");
    	installModel.setAddTime(DateUtil.getNow());
    	Map<String, Object> data =  UfxHelper.updateProject(installModel);
    	int flag =NumberUtil.getInt(StringUtils.isNull(data.get("flag")));
    	ufxList.add(installModel);
    	UfxModel startModel = new UfxModel();
    	startModel.setServiceType(UfxModel.PROJECT_UPDATE);
    	startModel.setTppType(UfxModel.TPP_PROJECT_UPDATE);
    	startModel.setBidNo(ufxProjectNo);
    	startModel.setTppUserCustId(user.getTppUserCustId());
    	startModel.setUser(user);
    	startModel.setFlag("1");
    	startModel.setAddTime(DateUtil.getNow());
    	if (flag ==1) { //状态更新为开标以后，才能将标的状态更新为投标中
    		UfxHelper.updateProject(startModel);
		}
    	ufxList.add(startModel);
    	return ufxList;
	}

	*//** ===========汇付转换双乾修改变动 start============== **//*
	*//**
	 * 收取手续费接口
	 * @param ufxModel
	 *//*
	public static UfxFeeModel fee(UfxModel ufxModel) {
		UfxFeeModel fee = new UfxFeeModel();
		fee.setOrderNo(ufxModel.getOrderId());
		fee.setProjectId(ufxModel.getBidNo());
		fee.setUserId(StringUtils.isNull(ufxModel.getUser().getUserId()));
		fee.setUserCustId(StringUtils.isNull(ufxModel.getUser().getTppUserCustId()));
		fee.setAmount(NumberUtil.format2Str(ufxModel.getMoney()));
		fee.setType(ufxModel.getExtended());
		doSubmit(fee);
		if (!fee.checkReturn()) {
			throw new UfxException("第三方处理收取手续费失败！失败原因：" + fee.getRespDesc(),BussinessException.TYPE_JSON);
		}
		return fee;
	}
	
	*//**
	 * 代金券红包放款
	 * @param ufxModel
	 *//*
	public static UfxVoucherLoanModel voucherLoan(UfxModel ufxModel) {
		UfxVoucherLoanModel voucherLoan = new UfxVoucherLoanModel();
		voucherLoan.setOrderNo(ufxModel.getOrderId());
		voucherLoan.setProjectId(ufxModel.getBidNo());
		voucherLoan.setUserId(StringUtils.isNull(ufxModel.getUser().getUserId()));
		voucherLoan.setUserCustId(StringUtils.isNull(ufxModel.getUser().getTppUserCustId()));
		voucherLoan.setRealName(ufxModel.getUser().getRealName());
		voucherLoan.setAmount(NumberUtil.format2Str(ufxModel.getMoney()));
		doSubmit(voucherLoan);
		if (!voucherLoan.checkReturn()) {
			throw new UfxException("第三方处理代金券红包放款失败！失败原因：" + voucherLoan.getRespDesc(),BussinessException.TYPE_JSON);
		}
		return voucherLoan;
	}*/
	/** ===========汇付转换双乾修改变动 end============== **/

	/**
	 * 账户余额查询
	 * @param ufxModel
	 * @throws Exception 
	 */
	@Deprecated
	public static UfxQueryBalanceModel queryBalance(final Map<String, Object> ufxMap){
		final UfxQueryBalanceModel model = new UfxQueryBalanceModel();
		model.setOrderNo(OrderNoUtils.getSerialNumber());
		model.setUserCustId(StringUtils.checkObj(ufxMap.get("userCustId"), ufxMap.get("plaCustId")));
		model.setAccountType(UfxQueryBalanceModel.ACCOUNT_TYPE_CASH);
		model.setUserType(StringUtils.isNull(ufxMap.get("userType")));
		model.setIdNo(StringUtils.isNull(ufxMap.get("idNo")));
		return (UfxQueryBalanceModel)doSubmit(model);
	}
	
	/**
	 * 商户充值
	 * @param tppModel
	 * @return
	 */
	@Deprecated
	public static UfxRechargeModel merchantRecharge(final Map<String, Object> map) {
		final UfxRechargeModel recharge = new UfxRechargeModel(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
		recharge.setOrderNo(OrderNoUtils.getSerialNumber());
		recharge.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		recharge.setIdNo(UfxConstant.UFX_MERCHANT_IDNO);
		recharge.signReq(recharge);
		return recharge;
	}
	
	/**
	 * 商户转账（子账户之间互转）
	 * @param ufxModel
	 * @return
	 */
	@Deprecated
	public static UfxMerTransferModel merSubAccountTransfer(final Map<String, Object> map){
		final UfxMerTransferModel merTransfer = new UfxMerTransferModel();
		merTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
		merTransfer.setUserCustId(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
		merTransfer.setReqExt(StringUtils.isNull(map.get("reqExt")));
		merTransfer.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		merTransfer.setTransAction(UfxMerTransferModel.TRANS_ACTION_USER);
		merTransfer.setParticAccType(UfxMerTransferModel.ACCOUNT_PRIVATE);
		doSubmit(merTransfer);	  
		return merTransfer;
	}
	
	/**
	 * 商户取现
	 * @param cash
	 * @return
	 */
	@Deprecated
	public static UfxCashModel merCash(final Map<String,Object> map) {
		final UfxCashModel cash = new UfxCashModel(ConfigUtils.getValue(Constant.UFX_CUSTOMERID));
		cash.setOrderNo(OrderNoUtils.getSerialNumber());
		cash.setAmount(NumberUtils.format2Str(NumberUtils.getDouble(map.get("amount"))));
		cash.setCardId(String.valueOf(map.get("cardId")));
		cash.setIsAdvance(CommonEnum.YES.eq(ConfigUtils.getValue(ConfigConstant.CASH_FEE_PLATFORM_ADVANCE)) ? UfxCashModel.IS_ADVANCE_Y : UfxCashModel.IS_ADVANCE_N);
		cash.setServFee(NumberUtils.format2Str(BigDecimalUtils.decimal(new BigDecimal(String.valueOf(map.get("servFee"))), 2)));
		cash.signReq(cash);
		return cash;
	}
	
	/**
	 * 商户转账给用户
	 * @param ufxModel
	 * @return
	 */
	public static UfxMerTransferModel merTransfer(final UfxModel ufxModel){
		final UfxMerTransferModel merTransfer = new UfxMerTransferModel();
		merTransfer.setUserId(ufxModel.getUserNo());
		merTransfer.setRealName(ufxModel.getRealName());
		merTransfer.setOrderNo(OrderNoUtils.getSerialNumber());
		merTransfer.setUserCustId(ufxModel.getToTppUserCustId());
		merTransfer.setAmount(NumberUtils.format2Str(ufxModel.getMoney()));
		merTransfer.setTransAction(UfxMerTransferModel.TRANS_ACTION_USER);
		merTransfer.setParticAccType(UfxMerTransferModel.ACCOUNT_PRIVATE);
		doSubmit(merTransfer);
	 
		return merTransfer;
	}

	/**
	 * 单笔交易查询
	 * @param ufxModel
	 * @throws Exception 
	 */
	public static UfxQueryTransferModel queryTransfer(final Map<String, Object> ufxMap){
		final UfxQueryTransferModel model = new UfxQueryTransferModel();
		model.setOrderNo(OrderNoUtils.getSerialNumber());
		model.setLoanNo(StringUtils.isNull(ufxMap.get("loanNo")));
		model.setLoanDate(StringUtils.isNull(ufxMap.get("loanDate")));
		model.setTransType(StringUtils.isNull(ufxMap.get("transType")));
		model.setReqExt("");
		return (UfxQueryTransferModel)doSubmit(model);
	}
	
	/**
	 * 
	 * 补单
	 * @desc 如果obj为空，表示ufx未收到该订单号对应的交易记录，可重新发送该交易信息进行补单；
	 * 		  如果obj不为空，表示ufx收到该订单号对应的交易记录，可根据响应码做相关业务处理；
	 * @author xhf
	 * @date 2016年9月28日
	 * @param ufxMap
	 * 		loanNo	本系统生成的订单号
	 * 		loanDate 格式为 yyyyMMdd 请求 ufx 时的日期
	 * 		isRepeatSend 是否重新发送异步回调：01：发送，02：不发送
	 * @return
	 */
	@Deprecated
	public static UfxAdditionalOrderModel additionOrder(final Map<String, Object> ufxMap){
		final UfxAdditionalOrderModel model = new UfxAdditionalOrderModel();
		model.setOrderNo(OrderNoUtils.getSerialNumber());
		model.setLoanNo(StringUtils.isNull(ufxMap.get("loanNo")));
		model.setLoanDate(StringUtils.isNull(ufxMap.get("loanDate")));
		model.setIsRepeatSend(StringUtils.isNull(ufxMap.get("isRepeatSend")));
		Object obj = getSubmitResult(model);
		return obj==null? null:(UfxAdditionalOrderModel)obj;
	}
}
