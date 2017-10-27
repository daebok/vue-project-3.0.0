//package com.rd.ifaes.core.tpp.service.impl;
//
//
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//import com.rd.ifaes.common.constant.CbhbConstant;
//import com.rd.ifaes.common.util.FileUtil;
//import com.rd.ifaes.common.util.StringUtils;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbBackInvestModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbBatInvestCancleModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindCardWebModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindMobileNoModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbBindPassModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbCreateBidModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbDrawingsModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbExistUserRegisterModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbExperBonusModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbExperCapModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileReleaseModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbFileRepaymentModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbHelper;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbHoldAmtModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbInvestCancleModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbMercRechargeModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbMercWithdrawModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryBalanceModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryMerchantAcctsModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryTransStatModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbQueryUserInfModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbRealNameWebModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbSendUapMsgModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbUnHoldAmtModel;
//import com.rd.ifaes.core.tpp.model.cbhb.CbhbWebRechargeModel;
//import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanBindCardMessageModel;
//import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRechargeModel;
//import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanRegisterModel;
//import com.rd.ifaes.core.tpp.model.cbhb.app.CbhbNetLoanWithDrawModel;
//import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbExpChkModel;
//import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbInvestChkModel;
//import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbPpdChkModel;
//import com.rd.ifaes.core.tpp.model.cbhb.chk.CbhbWdcChkModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileBatInvestCancleDetailModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileBatInvestCancleModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileExistUserRegisterDetailModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileExistUserRegisterModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileExperCapDetailModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileExperCapModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileReleaseDetailModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileReleaseModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileRepaymentDetailModel;
//import com.rd.ifaes.core.tpp.model.cbhb.file.FileRepaymentModel;
//import com.rd.ifaes.core.tpp.model.cbhb.xml.XmlQueryMerchantAccts;
//
//@SuppressWarnings("deprecation")
//@Service("tppCbhbServiceImpl")
//public class TppCbhbServiceImpl extends TppServiceImpl {
//
//	/**
//	 * LOGGER
//	 */
//	private static final Logger LOGGER = LoggerFactory.getLogger(TppCbhbServiceImpl.class);
//
//
//	/*不同的第三方共同的接口   个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询  start*/
//
//	/**
//	 * 渤海银行--APP提现
//	 */
//	@Override
//	public Object tppAppCash(Map<String, Object> params) {
//		final CbhbNetLoanWithDrawModel withDrawModel = new CbhbNetLoanWithDrawModel();
//		withDrawModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		withDrawModel.setFeeType(StringUtils.isNull(params.get("feeType")));
//		withDrawModel.setMerFeeAmt(StringUtils.isNull(params.get("merFeeAmt")));
//		withDrawModel.setTransAmt(StringUtils.isNull(params.get("transAmt")));
//		String netLoanInfo = withDrawModel.handleNetLoanInfo(withDrawModel.getPlainTxtJson());
//		return withDrawModel.getUrl(netLoanInfo);
//	}
//
//
//	/**
//	 * 渤海银行--APP充值
//	 */
//	@Override
//	public Object tppAppRecharge(Map<String, Object> params) {
//		final CbhbNetLoanRechargeModel rechargeModel  = new CbhbNetLoanRechargeModel();
//		rechargeModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		rechargeModel.setFeeType(StringUtils.isNull(params.get("feeType")));
//		rechargeModel.setMerFeeAmt(StringUtils.isNull(params.get("merFeeAmt")));
//		rechargeModel.setMarketAmt(StringUtils.isNull(params.get("marketAmt")));
//		rechargeModel.setTransAmt(StringUtils.isNull(params.get("transAmt")));
//		String netLoanInfo = rechargeModel.handleNetLoanInfo(rechargeModel.getPlainTxtJson());
//		return rechargeModel.getUrl(netLoanInfo);
//	}
//
//	/**
//	 * 渤海银行-- APP修改银行卡
//	 */
//	@Override
//	public Object tppAppUpdateBankCard(Map<String, Object> params) {
//		final CbhbNetLoanBindCardMessageModel bindCardModel = new CbhbNetLoanBindCardMessageModel();
//		bindCardModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		try {
//			bindCardModel.setMerPriv(URLEncoder.encode(StringUtils.isNull(params.get("merPriv")),"GBK"));
//		} catch (UnsupportedEncodingException e) {
//			LOGGER.info("解码错误!");
//		}
//		String netLoanInfo = bindCardModel.handleNetLoanInfo(bindCardModel.getPlainTxtJson());
//		return bindCardModel.getUrl(netLoanInfo);
//	}
//
//	/**
//	 * 渤海银行--APP个人开户
//	 */
//	@Override
//	public Object tppAppRegister(Map<String, Object> params) {
//		final CbhbNetLoanRegisterModel appRegisterModel = new CbhbNetLoanRegisterModel();
//		String usrFlag = StringUtils.isNull(params.get("usrFlag"));
//		appRegisterModel.setMobileNo(StringUtils.isNull(params.get("mobielNo")));
//		appRegisterModel.setUsrFlag(usrFlag);
//		try {
//			appRegisterModel.setMerPriv(URLEncoder.encode(StringUtils.isNull(params.get("merPriv")),"GBK"));
//		} catch (UnsupportedEncodingException e) {
//			LOGGER.info("解码错误!");
//		}
//		if(usrFlag.equals("1")) {
//			appRegisterModel.setIdentType(StringUtils.isNull(params.get("identType")));
//			appRegisterModel.setIdentNo(StringUtils.isNull(params.get("identNo")));
//			appRegisterModel.setUsrName(StringUtils.isNull(params.get("usrName")));
//			appRegisterModel.setOpenBankId(StringUtils.isNull(params.get("openBankId")));
//			appRegisterModel.setOpenAcctId(StringUtils.isNull(params.get("openAcctId")));
//		}
//		String netLoanInfo = appRegisterModel.handleNetLoanInfo(appRegisterModel.getPlainTxtJson());
//		return appRegisterModel.getUrl(netLoanInfo);
//	}
//
//	/**
//	 * 渤海银行  --  个人开户
//	 */
//	@Override
//	public Object tppRegister(Map<String, Object> params) {
//		final CbhbRealNameWebModel realNameWeb = new CbhbRealNameWebModel();
//		String openType = StringUtils.isNull(params.get("openType"));
//		String identType = StringUtils.isNull(params.get("identType"));
//		String identNo = StringUtils.isNull(params.get("identNo"));
//		String usrName = StringUtils.isNull(params.get("usrName"));
//		if(openType.equals(CbhbConstant.STRING_ONE)){//新用户注册
//			realNameWeb.setOpenType(openType);
//			realNameWeb.setIdentType(StringUtils.EMPTY);
//			realNameWeb.setIdentNo(StringUtils.EMPTY);
//			realNameWeb.setUsrName(StringUtils.EMPTY);
//		}else{//2：老用户平台绑定3：老用户 （只有证件信息）绑定 不
//			realNameWeb.setOpenType(openType);
//			realNameWeb.setIdentType(identType);
//			realNameWeb.setIdentNo(identNo);
//			realNameWeb.setUsrName(usrName);
//		}
//		realNameWeb.setMobileNo(StringUtils.isNull(params.get("mobileNo")));
//		realNameWeb.requestColumnValid();
//		realNameWeb.sign();
//		return realNameWeb;
//	}
//
//	/**
//	 * 渤海银行-- 充值
//	 */
//	@Override
//	public Object tppRecharge(Map<String, Object> params) {
//		final CbhbWebRechargeModel cbhbWebRechargeModel = new CbhbWebRechargeModel();
//		cbhbWebRechargeModel.setPlaCustId(params.get("userCustId").toString());
//		cbhbWebRechargeModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(StringUtils.isNull(params.get("amount"))));
//		cbhbWebRechargeModel.setFeeType(StringUtils.isNull(params.get("feeType")));
//		cbhbWebRechargeModel.setMerFeeAmt(CbhbHelper.getBigDecimalMoneyUpper(StringUtils.isNull(params.get("merFeeAmt"))));
//		cbhbWebRechargeModel.setMarketAmt(CbhbHelper.getBigDecimalMoneyUpper(StringUtils.isNull(params.get("marketAmt"))));
//		cbhbWebRechargeModel.requestColumnValid();
//		cbhbWebRechargeModel.sign();
//		return cbhbWebRechargeModel;
//	}
//
//	/**
//	 * 渤海银行-- 取现
//	 */
//	@Override
//	public Object tppCash(Map<String, Object> params) {
//		final CbhbDrawingsModel drawingsModel = new CbhbDrawingsModel();
//		drawingsModel.setPlaCustId(params.get("userCustId").toString());
//		String transAmt = CbhbHelper.getBigDecimalMoneyUpper(StringUtils.isNull(params.get("amount")));
//		String servFee = StringUtils.isNull(params.get("servFee"));//商户收取用户手续费
//		String isAdvance = StringUtils.isNull(params.get("isAdvance"));//是否垫付
//		drawingsModel.setTransAmt(transAmt);
//		drawingsModel.setFeeType("Y".equals(isAdvance) ? "0":"1");//垫付就是不收取用户手续费  为0 不垫付就是收取 为 1
//		drawingsModel.setMerFeeAmt("Y".equals(isAdvance) ? "0":servFee);
//		drawingsModel.requestColumnValid();
//		drawingsModel.sign();
//		return drawingsModel;
//	}
//
//	/**
//	 * 渤海银行--商户充值
//	 */
//	@Override
//	public Object tppMerchantRecharge(Map<String, Object> params) {
//		final CbhbMercRechargeModel cbhbMercRechargeModel = new CbhbMercRechargeModel();
//		cbhbMercRechargeModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("transAmt").toString()));
//		cbhbMercRechargeModel.setMerAccTyp(params.get("merAccTyp").toString());
//		cbhbMercRechargeModel.sign();
//		CbhbMercRechargeModel responseModel = (CbhbMercRechargeModel) CbhbHelper.doSubmitWithValidCode(cbhbMercRechargeModel);
//		LOGGER.info("渤海银行--商户充值返回结果：{}",responseModel.getRespCode());
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行 -- 商户取现
//	 */
//	@Override
//	public Object tppMerchantCash(Map<String, Object> params) {
//		final CbhbMercWithdrawModel cbhbMercWithdrawModel = new CbhbMercWithdrawModel();
//		cbhbMercWithdrawModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("transAmt").toString()));
//		cbhbMercWithdrawModel.sign();
//		CbhbMercWithdrawModel responseModel = (CbhbMercWithdrawModel) CbhbHelper.doSubmitWithValidCode(cbhbMercWithdrawModel);
//		LOGGER.info("渤海银行--商户取现返回结果：{}",responseModel.getRespCode());
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行-- 建标
//	 */
//	@Override
//	public Object tppAddProject(Map<String, Object> params) {
//		final CbhbCreateBidModel cbhbCreateBidModel = new CbhbCreateBidModel();
//		cbhbCreateBidModel.setBorrowId(params.get("projectId").toString());
//
//		cbhbCreateBidModel.setBorrowTyp(params.get("borrowNature").toString());
//		if(CbhbConstant.STRING_TWO.equals(cbhbCreateBidModel.getBorrowTyp())){ // 对公需要对公账户名
//			cbhbCreateBidModel.setAccountName(params.get("accountName").toString());
//		}
//		cbhbCreateBidModel.setBorrowerAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("projectAmount").toString()));
//		cbhbCreateBidModel.setBorrowerInterestAmt(CbhbHelper.checkNumber(params.get("apr").toString()));
//		cbhbCreateBidModel.setBorrCustId(params.get("sponsorer").toString());
//		if(params.get("guarantorCustId") != null){
//			cbhbCreateBidModel.setGuaranteeNo(params.get("guarantorCustId").toString());
//		}else{
//			cbhbCreateBidModel.setGuaranteeNo("2000000388002750");
//		}
//		cbhbCreateBidModel.setBorrowerStartDate(params.get("cbhbStartTime").toString());
//		cbhbCreateBidModel.setBorrowerEndDate(params.get("cbhbRaiseEndTime").toString());
//
//		cbhbCreateBidModel.setBorrowerRepayDate(params.get("cbhbEndTime").toString());
//		cbhbCreateBidModel.setReleaseType(CbhbConstant.STRING_ZERO);	// 文件方式
//		String timeType = params.get("timeType").toString();
//		String investDateType = CbhbConstant.STRING_ONE; // 投资期限类型：日
//		if(CbhbConstant.STRING_ZERO.equals(timeType)){
//			investDateType = CbhbConstant.STRING_TWO;// 投资期限类型：月
//		}else if(CbhbConstant.STRING_ONE.equals(timeType)){
//			investDateType = CbhbConstant.STRING_ONE;
//		}
//		cbhbCreateBidModel.setInvestDateType(investDateType);
//		cbhbCreateBidModel.setInvestPeriod(params.get("timeLimit").toString());
//		cbhbCreateBidModel.requestColumnValid();
//		cbhbCreateBidModel.sign();
//		CbhbCreateBidModel responseModel = (CbhbCreateBidModel) CbhbHelper.doSubmitWithValidCode(cbhbCreateBidModel);
//		LOGGER.info("渤海银行--建标接口返回结果：{}",responseModel.getRespCode());
//		return cbhbCreateBidModel;
//	}
//
//	/**
//	 * 渤海银行-- 投资
//	 */
//	@Override
//	public Object tppAddTender(Map<String, Object> params) {
//		final CbhbBackInvestModel cbhbBackInvestModel = new CbhbBackInvestModel();
//		cbhbBackInvestModel.setBusiType(params.get("busiType").toString());
//		cbhbBackInvestModel.setPlaCustId(params.get("plaCustId").toString());
//		cbhbBackInvestModel.setSmsCode(params.get("smsCode").toString());
//		cbhbBackInvestModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("transAmt").toString()));
//		cbhbBackInvestModel.setMarketAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("marketAmt").toString()));
//		cbhbBackInvestModel.setBorrowId(params.get("borrowId").toString());
//		cbhbBackInvestModel.sign();
//		CbhbBackInvestModel responseModel = (CbhbBackInvestModel) CbhbHelper.doSubmitWithValidCode(cbhbBackInvestModel);
//		LOGGER.info("渤海银行--投资接口返回结果：{}",responseModel.getRespCode());
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行 --  放款  商户同时提交放款文件和放款请求，账户存管平台接收商户发来的请求，实时处理文件明细并返回结果。
//	 */
//	@Override
//	public Object tppLoan(Map<String, Object> params) {
//		LOGGER.info("params:{}",params.toString());
//		//1-获取参数
//		CbhbFileReleaseModel releaseModel = new CbhbFileReleaseModel();
//		String borrowId = StringUtils.isNull(params.get("borrowId"));//标ID
//		LOGGER.info("borrowId1：{}",borrowId);
//		String[][] detailList = (String[][])params.get("detailList");//投资明细数组  四个字段：投资uuid ,投资人存管账户，投资金额，冻结编号
//		String transAmt = StringUtils.isNull(params.get("transAmt"));//实际满标金额
//		String feeAmt = StringUtils.isNull(params.get("feeAmt"));//借款人手续费（借款管理费）
//		String borrowerAmt = StringUtils.isNull(params.get("borrowerAmt"));//标的金额
//		String borrCustId = StringUtils.isNull(params.get("borrCustId"));//借款人存管账号
//		String totalNum = detailList.length+StringUtils.EMPTY;
//
//		//2-通过参数拼接好文件的内容   商户流水号要保持统一 都用CbhbFileReleaseModel 的实例化的商户流水号
//		String txtContent = this.getContent(transAmt, feeAmt, borrowId, borrowerAmt, borrCustId, totalNum, detailList,releaseModel.getMerBillNo());
//		LOGGER.info("txtContent={}",txtContent);
//
//		//3-本地生成临时文件然后FTP上传到对方服务器上，成功再删除临时文件
//		boolean uploadFlag = false;
//		//获取文件路径和OK文件路径 以及对应的文件名称
//		String path = releaseModel.getFileNamePath();
//		String okPath =releaseModel.getOKFileNamePath();
//		String fileName = releaseModel.getFileName();
//		String okFileName= releaseModel.getOKFileName();
//		uploadFlag = FileUtil.createFileFTPToClient(path, okPath, txtContent, fileName, okFileName);
//
//		//4-放款实时回调请求
//		CbhbFileReleaseModel repsonseModel = null;
//		if(uploadFlag){
//			releaseModel.setBorrowId(borrowId);
//			LOGGER.info("borrowId2：{}",releaseModel.getBorrowId());
//			releaseModel.requestColumnValid();
//			releaseModel.sign();
//			repsonseModel = (CbhbFileReleaseModel)CbhbHelper.doSubmit(releaseModel);
//		}
//		return repsonseModel;
//	}
//
//	/**
//	 * 渤海银行-- 还款
//	 */
//	@Override
//	public Object tppRepayment(Map<String, Object> params) {
//		CbhbFileRepaymentModel repayModel = new CbhbFileRepaymentModel();
//		//1-获取参数
//		String borrowId = StringUtils.isNull(params.get("borrowId"));
//		String[][] detailList = (String[][])params.get("detailList");
//		String transAmt = StringUtils.isNull(params.get("transAmt"));
//		String feeAmt = StringUtils.isNull(params.get("feeAmt"));
//		String borrowerAmt = StringUtils.isNull(params.get("borrowerAmt"));
//		String borrCustId = StringUtils.isNull(params.get("borrCustId"));
//		String totalNum = detailList.length+StringUtils.EMPTY;
//
//		//2-通过参数拼接好文件的内容
//		String txtContent = this.getRepayContent(transAmt, feeAmt, borrowId, borrowerAmt, borrCustId, totalNum, detailList,repayModel.getMerBillNo());
//		LOGGER.info("txtContent={}",txtContent);
//
//		//3-本地生成临时文件然后FTP上传到对方服务器上，成功再删除临时文件
//		boolean uploadFlag = false;
//		//获取文件路径和OK文件路径 以及对应的文件名称
//		String path = repayModel.getFileNamePath();
//		String okPath =repayModel.getOKFileNamePath();
//		String fileName = repayModel.getFileName();
//		String okFileName= repayModel.getOKFileName();
//		uploadFlag = FileUtil.createFileFTPToClient(path, okPath, txtContent, fileName, okFileName);
//
//		//4-还款实时回调请求
//		CbhbFileRepaymentModel responseModel = null;
//		if(uploadFlag){
//			repayModel.setBorrowId(borrowId);
//			repayModel.setRepayTyp(params.get("repayTyp").toString());
//			repayModel.setRepayInstTot(params.get("repayInstTot").toString());
//			repayModel.setRepayInstCur(params.get("repayInstCur").toString());
//			repayModel.sign();
//			responseModel = (CbhbFileRepaymentModel) CbhbHelper.doSubmitWithValidCode(repayModel);
//			LOGGER.info("渤海银行--还款接口返回结果：{}",responseModel.getRespCode());
//		}
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行 -- 查询用户余额的接口
//	 */
//	@Override
//	public Object tppQueryBalance(Map<String, Object> params) {
//		final CbhbQueryBalanceModel cbhbQueryBalanceModel = new CbhbQueryBalanceModel();
//		cbhbQueryBalanceModel.setPlaCustId(params.get("plaCustId").toString());
//		cbhbQueryBalanceModel.sign();
//		CbhbQueryBalanceModel responseModel = (CbhbQueryBalanceModel)CbhbHelper.doSubmitWithValidCode(cbhbQueryBalanceModel);
//		LOGGER.info("查询用户余额的接口:可用：{},总额：{}，冻结：{}",responseModel.getAcctBal(),responseModel.getAvlBal(),responseModel.getFrzBal());
//		final Map<String,Object> map = new HashMap<String, Object>();
//		map.put("avlBal", CbhbHelper.getBigDecimalMoneyLow(responseModel.getAvlBal()));
//		map.put("acctBal", CbhbHelper.getBigDecimalMoneyLow(responseModel.getAcctBal()));
//		map.put("frzBal", CbhbHelper.getBigDecimalMoneyLow(responseModel.getFrzBal()));
//		return map;
//	}
//
//
//	/*不同的第三方共同的接口   个人开户、个人充值、个人提现、商户充值、商户提现、建标、投标、放款、还款、用户余额查询  end*/
//
//
//	/* ufx汇付接口                 start */
//
//	@Override
//	public Object tppCropRegister(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppVerifyCash(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppAddBankCard(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppDeleteBankBank(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppAutoTenderPlan(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppCloseAutoTenderPlan(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppAutoTender(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppQueryUserBankCard(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppUserLogin(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppFssTrans(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppTransfer(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppCreditAssign(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppAuthSign(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppProjectRevoke(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppUpdateProject(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppProjectAttachInfo(Map<String, Object> params) {
//		return null;
//	}
//
//	@Override
//	public Object tppInvestFail(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppAdditionOrder(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppQueryTransfer(Map<String, Object> params) {
//		return null;
//	}
//
//
//	@Override
//	public Object tppMerSubAccountTransfer(Map<String, Object> params) {
//		return null;
//	}
//
//	/* ufx汇付接口                 end */
//
//
//	/* 渤海银行存管接口                 start */
//	/**
//	 * 渤海银行 -- 修改三方支付密码
//	 */
//	@Override
//	public Object tppUpdatePayPass(Map<String, Object> params) {
//		final CbhbBindPassModel passModel = new CbhbBindPassModel();
//		passModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		passModel.requestColumnValid();
//		passModel.sign();
//		return passModel;
//	}
//
//	/**
//	 * 渤海银行-- 修改手机号
//	 */
//	@Override
//	public Object tppUpdateMobileNo(Map<String, Object> params) {
//		final CbhbBindMobileNoModel bindMobileNoModel = new CbhbBindMobileNoModel();
//		bindMobileNoModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		bindMobileNoModel.setMobileNo(StringUtils.isNull(params.get("oldMobileNo")));
//		LOGGER.info("渤海银行--修改手机号的接口：用户为：{}，旧的手机号为：{}",bindMobileNoModel.getPlaCustId(),
//				bindMobileNoModel.getMobileNo());
//		bindMobileNoModel.requestColumnValid();
//		bindMobileNoModel.sign();
//		return bindMobileNoModel;
//	}
//
//	/**
//	 * 渤海银行 -- 修改银行卡的接口
//	 */
//	@Override
//	public Object tppUpdateBankCard(Map<String, Object> params) {
//		final CbhbBindCardWebModel bindCardWebModel = new CbhbBindCardWebModel();
//		bindCardWebModel.setPlaCustId(StringUtils.isNull(params.get("userCustId")));
//		LOGGER.info("渤海银行--修改银行卡的接口：用户为：{}",bindCardWebModel.getPlaCustId());
//		bindCardWebModel.requestColumnValid();
//		bindCardWebModel.sign();
//		return bindCardWebModel;
//	}
//
//	/**
//	 * 渤海银行--获取动态口令
//	 */
//	@Override
//	public Object tppSendUapMsg(Map<String, Object> params) {
//		final CbhbSendUapMsgModel sendUapMsg = new CbhbSendUapMsgModel();
//		sendUapMsg.setMobileNo(StringUtils.isNull(params.get("mobileNo")));
//		LOGGER.info("渤海银行--获取投标动态口令的接口：手机号为：{}",sendUapMsg.getMobileNo());
//		sendUapMsg.requestColumnValid();
//		sendUapMsg.sign();
//		CbhbSendUapMsgModel responseModel = (CbhbSendUapMsgModel)CbhbHelper.doSubmitWithValidCode(sendUapMsg);
//		LOGGER.info("渤海银行--获取到投标动态口令的：{}",responseModel.getRtnCod());
//		return responseModel.getRtnCod();
//	}
//
//	/**
//	 * 渤海银行  --- 商户账户查询
//	 */
//	@Override
//	public Object queryMerchantAccts(Map<String, Object> params) {
//		List<XmlQueryMerchantAccts> list = new ArrayList<XmlQueryMerchantAccts>();
//		final CbhbQueryMerchantAcctsModel model = new CbhbQueryMerchantAcctsModel();
//		model.requestColumnValid();
//		model.sign();
//		CbhbQueryMerchantAcctsModel responseModel = (CbhbQueryMerchantAcctsModel)CbhbHelper.doSubmitWithValidCode(model);
//		responseModel.setRespData(CbhbHelper.base64Decode(responseModel.getRespData()));
//		LOGGER.error("渤海银行--商户账户查询回调RespData--XML:"+responseModel.getRespData());
//		list = CbhbHelper.xmlToQueryMerchantAccts(responseModel.getRespData());
//		return list;
//	}
//
//	/**
//	 * 渤海银行-- 实时红包
//	 */
//	@Override
//	public Object experBonus(Map<String, Object> params) {
//		final CbhbExperBonusModel cbhbExperBonusModel = new CbhbExperBonusModel();
//		cbhbExperBonusModel.setPlaCustId(params.get("plaCustId").toString());
//		cbhbExperBonusModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("transAmt").toString()));
//		cbhbExperBonusModel.setMerFeeAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("merFeeAmt").toString()));
//		cbhbExperBonusModel.requestColumnValid();
//		cbhbExperBonusModel.sign();
//		CbhbExperBonusModel responseModel = (CbhbExperBonusModel)CbhbHelper.doSubmitWithValidCode(cbhbExperBonusModel);
//		LOGGER.error("渤海银行--实时红包回调:"+responseModel.getRespCode());
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行 -- 资金冻结
//	 */
//	@Override
//	public Object tppFreeze(Map<String, Object> params) {
//		final CbhbHoldAmtModel cbhbHoldAmtModel = new CbhbHoldAmtModel();
//		cbhbHoldAmtModel.setCustomerId(params.get("customerId").toString());
//		cbhbHoldAmtModel.setHoldAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("holdAmt").toString()));
//		cbhbHoldAmtModel.sign();
//		CbhbHoldAmtModel responseModel = (CbhbHoldAmtModel) CbhbHelper.doSubmitWithValidCode(cbhbHoldAmtModel);
//		LOGGER.info("渤海银行--资金冻结接口返回结果：{}",responseModel.getRespCode());
//		return cbhbHoldAmtModel;
//	}
//
//	/**
//	 * 渤海银行 -- 解冻
//	 */
//	@Override
//	public Object tppUnFreeze(Map<String, Object> params) {
//		final CbhbUnHoldAmtModel cbhbUnHoldAmtModel = new CbhbUnHoldAmtModel();
//		cbhbUnHoldAmtModel.setUnHoldAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("unHoldAmt").toString()));
//		cbhbUnHoldAmtModel.setBorrowId(params.get("borrowId").toString());
//		cbhbUnHoldAmtModel.setMercAmt(CbhbHelper.getBigDecimalMoneyUpper(params.get("mercAmt").toString()));
//		cbhbUnHoldAmtModel.setTotalNum(Integer.valueOf(params.get("totalNum").toString()));
//		cbhbUnHoldAmtModel.setBidDetailList(params.get("bidDetailList").toString());
//		cbhbUnHoldAmtModel.sign();
//		return cbhbUnHoldAmtModel;
//	}
//
//	/**
//	 * 获取传递的文件的内容
//	 * @author QianPengZhan
//	 * @date 2017年3月8日
//	 * @param transAmt
//	 * @param feeAmt
//	 * @param borrowId
//	 * @param borrowerAmt
//	 * @param borrCustId
//	 * @param totalNum
//	 * @param detailList
//	 * @return
//	 */
//	private String getContent(String transAmt,String feeAmt,String borrowId,String borrowerAmt,String borrCustId,String totalNum,String[][] detailList,String merBillNo) {
//		//2-获取第一行的内容
//		FileReleaseModel fileReleaseModel = new FileReleaseModel();
//		String fileContent = fileReleaseModel.getFirstFileContent(transAmt, feeAmt, borrowId, borrowerAmt, borrCustId, totalNum,merBillNo);
//		LOGGER.info("fileContent={},totalNum={}",fileContent,fileReleaseModel.getTotalNum());
//
//		//3-获取第二行之后的内容然后合并
//		StringBuilder detailContent = new StringBuilder();
//		for (int i = 0; i < detailList.length; i++) {
//			String[] detail = detailList[i];
//			FileReleaseDetailModel detailModel = new FileReleaseDetailModel();
//			detailModel.setId(detail[0]);
//			detailModel.setPlaCustId(detail[1]);
//			detailModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(detail[2]));
//			detailModel.setFreezeId(detail[3]);
//			detailContent.append(detailModel.getFileContent());
//		}
//		return  fileContent+detailContent.toString();
//	}
//
//
//	/**
//	 * 还款文件内容处理
//	 * @author QianPengZhan
//	 * @date 2017年4月11日
//	 * @param transAmt
//	 * @param feeAmt
//	 * @param borrowId
//	 * @param borrowerAmt
//	 * @param borrCustId
//	 * @param totalNum
//	 * @param detailList
//	 * @param merBillNo
//	 * @return
//	 */
//	private String getRepayContent(String transAmt, String feeAmt,
//			String borrowId, String borrowerAmt, String borrCustId,
//			String totalNum, String[][] detailList,String merBillNo) {
//		//2-获取第一行的内容
//		FileRepaymentModel fileRepaymentModel = new FileRepaymentModel();
//		String fileContent = fileRepaymentModel.getFirstFileContent(transAmt, feeAmt, borrowId, borrowerAmt, borrCustId, totalNum,merBillNo);
//		LOGGER.info("fileContent={},totalNum={}",fileContent,fileRepaymentModel.getTotalNum());
//
//		//3-获取第二行之后的内容然后合并
//		StringBuilder detailContent = new StringBuilder();
//		for (int i = 0; i < detailList.length; i++) {
//			String[] detail = detailList[i];
//			FileRepaymentDetailModel detailModel = new FileRepaymentDetailModel();
//			detailModel.setId(detail[0]);
//			detailModel.setPlaCustId(detail[1]);
//			detailModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(detail[2]));
//			detailModel.setInterest(CbhbHelper.getBigDecimalMoneyUpper(detail[3]));
//			detailModel.setInvesFee(CbhbHelper.getBigDecimalMoneyUpper(detail[4]));
//			detailContent.append(detailModel.getFileContent());
//		}
//		return  fileContent+detailContent.toString();
//	}
//
//	/**
//	 * 渤海银行--用户信息查询
//	 */
//	@Override
//	public Object queryUserInf(Map<String, Object> params) {
//		final CbhbQueryUserInfModel cbhbQueryUserInfModel = new CbhbQueryUserInfModel();
//		cbhbQueryUserInfModel.setMobileNo(StringUtils.isNull(params.get("mobileNo")));
//		cbhbQueryUserInfModel.setPlaCustId(StringUtils.isNull(params.get("plaCustId")));
//		cbhbQueryUserInfModel.requestColumnValid();
//		cbhbQueryUserInfModel.sign();
//		CbhbQueryUserInfModel responseModel = (CbhbQueryUserInfModel)CbhbHelper.doSubmit(cbhbQueryUserInfModel);
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行-- 交易状态查询
//	 */
//	@Override
//	public Object tppQueryTransStat(Map<String, Object> params) {
//		final CbhbQueryTransStatModel  cbhbQueryTransStatModel = new CbhbQueryTransStatModel();
//		cbhbQueryTransStatModel.setMerBillNo(StringUtils.isNull(params.get("merBillNo")));
//		cbhbQueryTransStatModel.setQueryTransType(StringUtils.isNull(params.get("queryTransType")));
//		cbhbQueryTransStatModel.sign();
//		CbhbQueryTransStatModel responseModel = (CbhbQueryTransStatModel)CbhbHelper.doSubmit(cbhbQueryTransStatModel);
//		return responseModel.getTransStat()==null?StringUtils.EMPTY:responseModel.getTransStat();
//	}
//
//	/**
//	 * 渤海银行-- 投标撤销
//	 */
//	@Override
//	public Object tppInvestCancle(Map<String, Object> params) {
//		final CbhbInvestCancleModel investCancleModel = new CbhbInvestCancleModel();
//		investCancleModel.setOldTransId(StringUtils.isNull(params.get("OldTransId")));
//		investCancleModel.setSmsCode(StringUtils.isNull(params.get("SmsCode")));
//		investCancleModel.setTransAmt(StringUtils.isNull(params.get("TransAmt")));
//		investCancleModel.setFreezeId(StringUtils.isNull(params.get("FreezeId")));
//		investCancleModel.setPlaCustId(StringUtils.isNull(params.get("PlaCustId")));
//		investCancleModel.requestColumnValid();
//		investCancleModel.sign();
//		CbhbInvestCancleModel responseModel = (CbhbInvestCancleModel)CbhbHelper.doSubmitWithValidCode(investCancleModel);
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行-- 红包体验金
//	 */
//	@Override
//	public Object tppExperCap(Map<String, Object> params) {
//
//		final CbhbExperCapModel cbhbExperCapModel = new CbhbExperCapModel();
//		//1-获取参数
//		String[][] detailList = (String[][])params.get("detailList");
//		String borrowId = StringUtils.isNull(params.get("borrowId")); // 标的ID
//		String transAmt = StringUtils.isNull(params.get("transAmt")); // 收益金额
//		String merFeeAmt = StringUtils.isNull(params.get("merFeeAmt")); // 商户手续费收入
//		String tasteTolAmt = StringUtils.isNull(params.get("tasteTolAmt")); // 红包总金额
//		String capTyp = StringUtils.isNull(params.get("capTyp")); // 总笔数
//		String totalNum = StringUtils.isNull(params.get("totalNum"));
//
//		//2-通过参数拼接好文件的内容
//		String txtContent = this.getExperCapContent(transAmt, merFeeAmt, borrowId, tasteTolAmt, capTyp, totalNum, detailList,cbhbExperCapModel.getMerBillNo());
//		LOGGER.info("txtContent={}",txtContent);
//
//		//3-本地生成临时文件然后FTP上传到对方服务器上，成功再删除临时文件
//		boolean uploadFlag = false;
//		//获取文件路径和OK文件路径 以及对应的文件名称
//		String path =  cbhbExperCapModel.getFileNamePath();
//		String okPath = cbhbExperCapModel.getOKFileNamePath();
//		String fileName = cbhbExperCapModel.getFileName();
//		String okFileName= cbhbExperCapModel.getOKFileName();
//		uploadFlag = FileUtil.createFileFTPToClient(path, okPath, txtContent, fileName, okFileName);
//
//		//4-红包体验金实时回调请求
//		CbhbExperCapModel responseModel = null;
//		if(uploadFlag){
//			cbhbExperCapModel.sign();
//			responseModel = (CbhbExperCapModel) CbhbHelper.doSubmitWithValidCode(cbhbExperCapModel);
//			LOGGER.info("渤海银行--红包体验金接口返回结果：{}",responseModel.getRespCode());
//		}
//		return responseModel;
//	}
//
//	/**
//	 * 渤海银行 -- 批量投标撤销申请
//	 */
//	@Override
//	public Object tppBatInvestCancle(Map<String, Object> params) {
//		CbhbBatInvestCancleModel cancleModel = new CbhbBatInvestCancleModel();
//		//1-获取参数
//		String[][] detailList = (String[][])params.get("detailList");
//		String totalNum = detailList.length+StringUtils.EMPTY;
//
//		//2-通过参数拼接好文件的内容
//		String txtContent = this.getInvestCancleContent(cancleModel.getBatchNo(),totalNum,detailList);
//		LOGGER.info("txtContent={}",txtContent);
//
//		//3-本地生成临时文件然后FTP上传到对方服务器上，成功再删除临时文件
//		boolean uploadFlag = false;
//		//获取文件路径和OK文件路径 以及对应的文件名称
//		String path = cancleModel.getFileNamePath();
//		String okPath =cancleModel.getOKFileNamePath();
//		String fileName = cancleModel.getFileName();
//		String okFileName= cancleModel.getOKFileName();
//		uploadFlag = FileUtil.createFileFTPToClient(path, okPath, txtContent, fileName, okFileName);
//
//		//4-还款实时回调请求
//		CbhbBatInvestCancleModel responseModel = null;
//		if(uploadFlag){
//			cancleModel.sign();
//			responseModel = (CbhbBatInvestCancleModel) CbhbHelper.doSubmitWithValidCode(cancleModel);
//			LOGGER.info("渤海银行--批量撤销接口返回结果：{}",responseModel.getRespCode());
//		}
//		return responseModel;
//	}
//
//	/**
//	 *  批量撤销文件的内容
//	 * @author QianPengZhan
//	 * @date 2017年3月13日
//	 * @return
//	 */
//	private String getInvestCancleContent(String batchNo,String totalNum,String[][] detailList){
//		//2-获取第一行的内容
//		FileBatInvestCancleModel fileBatInvestCancleModel = new FileBatInvestCancleModel();
//		String fileContent = fileBatInvestCancleModel.getFirstFileContent(batchNo,totalNum);
//		LOGGER.info("fileContent={},totalNum={}",fileContent,fileBatInvestCancleModel.getTotalNum());
//
//		//3-获取第二行之后的内容然后合并
//		StringBuilder detailContent = new StringBuilder();
//		for (int i = 0; i < detailList.length; i++) {
//			String[] detail = detailList[i];
//			FileBatInvestCancleDetailModel detailModel = new FileBatInvestCancleDetailModel();
//			detailModel.setId(detail[0]);
//			detailModel.setOldTransId(detail[1]);
//			detailModel.setPlaCustId(detail[2]);
//			detailModel.setTransAmt(CbhbHelper.getBigDecimalMoneyUpper(detail[3]));
//			detailModel.setFreezeId(detail[4]);
//			detailContent.append(detailModel.getFileContent());
//		}
//		return  fileContent+detailContent.toString();
//	}
//
//	private String getExperCapContent(String transAmt, String merFeeAmt,
//			String borrowId, String tasteTolAmt, String capTyp,
//			String totalNum, String[][] detailList, String merBillNo) {
//		//2-获取第一行的内容
//		FileExperCapModel experCapModel = new FileExperCapModel();
//		String fileContent = experCapModel.getFirstFileContent(transAmt, merFeeAmt, borrowId, tasteTolAmt, capTyp, totalNum,merBillNo);
//		LOGGER.info("渤海银行--红包体验金 fileContent={},totalNum={}",fileContent,experCapModel.getTotalNum());
//
//		//3-获取第二行之后的内容然后合并
//		StringBuilder detailContent = new StringBuilder();
//		for (int i = 0; i < Integer.valueOf(totalNum); i++) {
//			String[] detail = detailList[i];
//			if(detail != null){
//				FileExperCapDetailModel detailModel = new FileExperCapDetailModel();
//				detailModel.setId(detail[0]);
//				detailModel.setPlaCustId(detail[1]);
//				detailModel.setTasteAmt(CbhbHelper.getBigDecimalMoneyUpper(detail[2]));
//				detailModel.setInterest(CbhbHelper.getBigDecimalMoneyUpper(detail[3]));
//				detailModel.setInvesFee(CbhbHelper.getBigDecimalMoneyUpper(detail[4]));
//				detailContent.append(detailModel.getFileContent());
//			}
//		}
//		return  fileContent+detailContent.toString();
//	}
//
//	@SuppressWarnings("rawtypes")
//	@Override
//	public Object tppExperRealeName(Map<String, Object> params) {
//		final CbhbExistUserRegisterModel registerModel = new CbhbExistUserRegisterModel();
//		registerModel.setBatchNo(registerModel.getMerBillNo().substring(registerModel.getMerBillNo().length()-10,registerModel.getMerBillNo().length()));
//		//1-获取参数
//		List[] userList = (List[])params.get("userInfo");
//		String totalNum = userList.length+StringUtils.EMPTY;
//
//		//2-通过参数拼接好文件的内容
//		String txtContent = this.getExperRealeNameContent(totalNum, userList,registerModel.getBatchNo());
//		LOGGER.info("txtContent={}",txtContent);
//
//		//3-本地生成临时文件然后FTP上传到对方服务器上，成功再删除临时文件
//		boolean uploadFlag = false;
//		//获取文件路径和OK文件路径 以及对应的文件名称
//		String path =  registerModel.getFileNamePath();
//		String okPath = registerModel.getOKFileNamePath();
//		String fileName = registerModel.getFileName();
//		String okFileName= registerModel.getOKFileName();
//		uploadFlag = FileUtil.createFileFTPToClient(path, okPath, txtContent, fileName, okFileName);
//
//		//4-批量注册实时回调请求
//		CbhbExistUserRegisterModel responseModel = null;
//		if(uploadFlag){
//			registerModel.sign();
//			responseModel = (CbhbExistUserRegisterModel) CbhbHelper.doSubmitWithValidCode(registerModel);
//			LOGGER.info("渤海银行--批量注册接口返回结果：{}",responseModel.getRespCode());
//		}
//		return responseModel;
//	}
//
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	private String getExperRealeNameContent(String totalNum, List[] userList,
//			String batchNo) {
//		//2-获取第一行的内容
//		FileExistUserRegisterModel registerModel = new FileExistUserRegisterModel();
//		String fileContent = registerModel.getFirstFileContent(batchNo,totalNum);
//		LOGGER.info("fileContent={},totalNum={}",fileContent,registerModel.getTotalNum());
//
//		//3-获取第二行之后的内容然后合并
//		StringBuilder detailContent = new StringBuilder();
//		for (int i = 0; i < userList.length; i++) {
//			List<Object> list = userList[i];
//			FileExistUserRegisterDetailModel detailModel = new FileExistUserRegisterDetailModel();
//			detailModel.setIdentType(list.get(0).toString());
//			detailModel.setIdentNo(list.get(1).toString());
//			detailModel.setUsrName(list.get(2).toString());
//			detailModel.setMobileNo(list.get(3).toString());
//			detailModel.setOpenBankId(list.get(4).toString());
//			detailModel.setOpenAcctId(list.get(5).toString());
//			detailContent.append(detailModel.getFileContent());
//		}
//		return  fileContent+detailContent.toString();
//
//	}
//
//	/**
//	 * 渤海银行   投标对账返回对象
//	 */
//	@Override
//	public Object tppInvestChk(Map<String, Object> params) {
//		Date searchTime = (Date)params.get("searchTime");//对账查询日期时间
//		List<CbhbInvestChkModel> chkList = new ArrayList<CbhbInvestChkModel>();
//		CbhbInvestChkModel investChkModel = new CbhbInvestChkModel();
//		String fileName =investChkModel.getResultFileName(searchTime);
//		boolean downFlag = FileUtil.downloadResultFile(investChkModel.getReSultFilePath(searchTime,fileName), investChkModel.getRemotePath(searchTime),fileName);
//		LOGGER.info("下载是否成功：{}",downFlag);
//		if(downFlag){
//			String result = FileUtil.readTxtFile(investChkModel.getReSultFilePath(searchTime,fileName));
//			LOGGER.info("读取的内容：{}",result);
//			if(StringUtils.isNotBlank(result.toString())){
//				String[] line = result.split(",");
//				for (int i = 0; i < line.length; i++) {
//					String str =  line[i];
//					String[] column = str.split(StringUtils.VERTICAL_LINE);
//					CbhbInvestChkModel newInvestChkModel = new CbhbInvestChkModel();
//					for (int j = 0; j < column.length; j++) {
//						newInvestChkModel.setOrdNo(column[0]);
//						newInvestChkModel.setMercId(column[1]);
//						newInvestChkModel.setPlaCustId(column[2]);
//						newInvestChkModel.setTransAmt(column[3]);
//						newInvestChkModel.setBorrowId(column[4]);
//						newInvestChkModel.setCreDt(column[5]);
//						newInvestChkModel.setCreTm(column[6]);
//						newInvestChkModel.setOrdSts(column[7]);
//						newInvestChkModel.setMerBillNo(column[8]);
//					}
//					chkList.add(newInvestChkModel);
//				}
//			}
//		}
//		return chkList;
//	}
//
//	/**
//	 * 渤海银行  --  实时红包对账
//	 */
//	@Override
//	public Object tppExpChk(Map<String, Object> params) {
//		Date searchTime = (Date)params.get("searchTime");//对账查询日期时间
//		List<CbhbExpChkModel> chkList = new ArrayList<CbhbExpChkModel>();
//		CbhbExpChkModel expChkModel = new CbhbExpChkModel();
//		String fileName =expChkModel.getResultFileName(searchTime);
//		boolean downFlag = FileUtil.downloadResultFile(expChkModel.getReSultFilePath(searchTime,fileName), expChkModel.getRemotePath(searchTime),fileName);
//		LOGGER.info("下载是否成功：{}",downFlag);
//		if(downFlag){
//			String result = FileUtil.readTxtFile(expChkModel.getReSultFilePath(searchTime,fileName));
//			LOGGER.info("读取的内容：{}",result);
//			if(StringUtils.isNotBlank(result.toString())){
//				String[] line = result.split(",");
//				for (int i = 0; i < line.length; i++) {
//					String str =  line[i];
//					String[] column = str.split(StringUtils.VERTICAL_LINE);
//					CbhbExpChkModel newExpChkModel = new CbhbExpChkModel();
//					for (int j = 0; j < column.length; j++) {
//						newExpChkModel.setTransId(column[0]);
//						newExpChkModel.setMerBillNo(column[1]);
//						newExpChkModel.setCreDt(column[2]);
//						newExpChkModel.setTransAmt(column[3]);
//						newExpChkModel.setPlaCustId(column[4]);
//					}
//					chkList.add(newExpChkModel);
//				}
//			}
//		}
//		return chkList;
//	}
//
//	/**
//	 * 渤海银行 --  提现对账
//	 */
//	@Override
//	public Object tppWdcChk(Map<String, Object> params) {
//		Date searchTime = (Date)params.get("searchTime");//对账查询日期时间
//		List<CbhbWdcChkModel> chkList = new ArrayList<CbhbWdcChkModel>();
//		CbhbWdcChkModel wdcChkModel = new CbhbWdcChkModel();
//		String fileName =wdcChkModel.getResultFileName(searchTime);
//		boolean downFlag = FileUtil.downloadResultFile(wdcChkModel.getReSultFilePath(searchTime,fileName), wdcChkModel.getRemotePath(searchTime),fileName);
//		LOGGER.info("下载是否成功：{}",downFlag);
//		if(downFlag){
//			String result = FileUtil.readTxtFile(wdcChkModel.getReSultFilePath(searchTime,fileName));
//			LOGGER.info("读取的内容：{}",result);
//			if(StringUtils.isNotBlank(result.toString())){
//				String[] line = result.split(",");
//				for (int i = 0; i < line.length; i++) {
//					String str =  line[i];
//					String[] column = str.split(StringUtils.VERTICAL_LINE);
//					CbhbWdcChkModel newWdcChkModel = new CbhbWdcChkModel();
//					for (int j = 0; j < column.length; j++) {
//						newWdcChkModel.setOrdNo(column[0]);
//						newWdcChkModel.setMerBillNo(column[1]);
//						newWdcChkModel.setCreDt(column[2]);
//						newWdcChkModel.setTransAmt(column[3]);
//						newWdcChkModel.setPlaCustId(column[4]);
//						newWdcChkModel.setWdcSts(column[5]);
//						newWdcChkModel.setFalRsn(column[6]);
//					}
//					chkList.add(newWdcChkModel);
//				}
//			}
//		}
//		return chkList;
//	}
//
//	/**
//	 * 充值对账
//	 */
//	@Override
//	public Object tppPpdChk(Map<String, Object> params) {
//		Date searchTime = (Date)params.get("searchTime");//对账查询日期时间
//		List<CbhbPpdChkModel> chkList = new ArrayList<CbhbPpdChkModel>();
//		CbhbPpdChkModel ppdChkModel = new CbhbPpdChkModel();
//		String fileName =ppdChkModel.getResultFileName(searchTime);
//		boolean downFlag = FileUtil.downloadResultFile(ppdChkModel.getReSultFilePath(searchTime,fileName), ppdChkModel.getRemotePath(searchTime),fileName);
//		LOGGER.info("下载是否成功：{}",downFlag);
//		if(downFlag){
//			String result = FileUtil.readTxtFile(ppdChkModel.getReSultFilePath(searchTime,fileName));
//			LOGGER.info("读取的内容：{}",result);
//			if(StringUtils.isNotBlank(result.toString())){
//				String[] line = result.split(",");
//				for (int i = 0; i < line.length; i++) {
//					String str =  line[i];
//					String[] column = str.split(StringUtils.VERTICAL_LINE);
//					CbhbPpdChkModel newPpdChkModel = new CbhbPpdChkModel();
//					for (int j = 0; j < column.length; j++) {
//						newPpdChkModel.setOrdNo(column[0]);
//						newPpdChkModel.setCreDt(column[1]);
//						newPpdChkModel.setTransAmt(column[2]);
//						newPpdChkModel.setFeeAmt(column[3]);
//						newPpdChkModel.setPlaCustId(column[4]);
//						newPpdChkModel.setMerBillNo(column[5]);
//					}
//					chkList.add(newPpdChkModel);
//				}
//			}
//		}
//		return chkList;
//	}
//	/* 渤海银行存管接口                 end */
//
//
//	@Override
//	public Object tppSmsCodeApply(Map<String, Object> params) {
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//
//
//	@Override
//	public Object tppAccountIdQuery(Map<String, Object> params) {
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//
//
//	@Override
//	public Object voucherPayCancel(Map<String, Object> params) {
//
//		LOGGER.info("江西银行的接口，此处无无需处理");
//		return null;
//	}
//}
