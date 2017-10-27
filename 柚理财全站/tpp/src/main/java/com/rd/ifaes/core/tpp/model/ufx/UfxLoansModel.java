package com.rd.ifaes.core.tpp.model.ufx;

import com.rd.ifaes.common.constant.UfxConstant;

/**
 *  UFX对接放款报文模型
 *  
 * @version 3.0
 * @author FangJun
 * @date 2016年7月26日
 */
@SuppressWarnings("serial")
public class UfxLoansModel extends UfxBaseModel {
	
	/**
	 * 项目手续费
	 */
	private String projectFee;
	
	/**
	 * 投资时使用的代金券金额
	 */
	private String voucher;
	
	/**
	 * 投资流水号
	 */
	private String investNo;
	
	/**
	 * 投资日期
	 */
	private String investDate;
	
	/**
	 * 投资冻结流水号
	 */
	private String freezeNo;
	
	/**
	 * 投标奖励 ,放款时会减掉，这样才能把钱给投资人
	 */
	private String awardMoney;	
	/**
	 * 用户类型,01：个人 02：企业 默认 01 联动必传
	 */
	private String userType;
	/**
	 * 01：普通项目放款 02：债权放款
		债权投资成功必须再调用债权放款
	 */
	private String loanType;
 
	/**
	 * 发起人姓名
	 */
	private String realName;
 
	/**
	 * 监管协议编号 ,发布项目时返回的监管协议编号 兴业必传
	 */
	private String superviseNo;
	/**
	 * 投资申请授权码,投资申请时返回的授权码 徽商必传
	 */
	private String authCode;
	/**
	 * ufx 处理流水号
	 */
	private String tradeNo;
	/**
	 * 批量放款,List 转化为 json 格式字符串，List 每一个对象都包含除参数列表中除 loansList 之外的其它业务参数  徽商必传
	 */
	private String loansList;
	
	/**
	 * 放款类型 01：普通项目放款  
	 */
	public   static final String   LOAN_TYPE_PROJECT="01";
	/**
	 * 放款类型 02：债权放款
	 */
	public   static final String   LOAN_TYPE_BOND="02";
	
	/**
	 * 请求参数数组
	 */
	private String[] requestParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "projectId", "sponsorer",
			"projectAmount", "userCustId", "amount", "projectFee", "voucher", 
			"awardMoney", "investNo", "investDate", "freezeNo", 
			"userType", "loanType", "userId", "realName", "superviseNo", "authCode", "loansList",
			"reqExt", "merPriv", "notifyUrl", "signInfo" };
	
	/**
	 * 响应参数数组
	 */
	private String[] responseParamNames = new String[] { "service", "orderNo",
			"ufxCustomerId", "businessWay", "respCode", "respDesc",
			"projectId", "sponsorer", "projectAmount", "userCustId", "amount",
			"projectFee", "voucher", "awardMoney", "investNo", "investDate", 
			"freezeNo",	"loanType","loansList","authCode", "tradeNo", "loansList",
			"reqExt", "merPriv", "signInfo" };
	
	public UfxLoansModel() {
		super();
		this.setService(UfxConstant.LOANS);
		this.setNotifyUrl("/ufx/loan/notify.html");
	}
	
	/**
	 * 获取项目手续费
	 * 
	 * @return
	 */
	public String getProjectFee() {
		return projectFee;
	}

	/**
	 * 设置项目手续费
	 * 
	 * @param projectFee
	 */
	public void setProjectFee(String projectFee) {
		this.projectFee = projectFee;
	}

	/**  
	 * 获取投资时使用的代金券金额  
	 * @return voucher  
	 */
	public String getVoucher() {
		return voucher;
	}

	/**  
	 * 设置投资时使用的代金券金额  
	 * @param voucher  
	 */
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

	/**  
	 * 获取投资流水号  
	 * @return investNo  
	 */
	public String getInvestNo() {
		return investNo;
	}

	/**  
	 * 设置投资流水号  
	 * @param investNo  
	 */
	public void setInvestNo(String investNo) {
		this.investNo = investNo;
	}

	/**  
	 * 获取投资日期  
	 * @return investDate  
	 */
	public String getInvestDate() {
		return investDate;
	}

	/**  
	 * 设置投资日期  
	 * @param investDate  
	 */
	public void setInvestDate(String investDate) {
		this.investDate = investDate;
	}

	/**  
	 * 获取投资冻结流水号  
	 * @return freezeNo  
	 */
	public String getFreezeNo() {
		return freezeNo;
	}

	/**  
	 * 设置投资冻结流水号  
	 * @param freezeNo  
	 */
	public void setFreezeNo(String freezeNo) {
		this.freezeNo = freezeNo;
	}

	/**
	 * 获取请求参数数组
	 * 
	 * @return
	 */
	public String[] getRequestParamNames() {
		return requestParamNames;
	}

	/**
	 * 设置请求参数数组
	 */
	public void setRequestParamNames(String[] requestParamNames) {
		this.requestParamNames = requestParamNames;
	}

	
	public String getAwardMoney() {
		return awardMoney;
	}

	public void setAwardMoney(String awardMoney) {
		this.awardMoney = awardMoney;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSuperviseNo() {
		return superviseNo;
	}

	public void setSuperviseNo(String superviseNo) {
		this.superviseNo = superviseNo;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public String getLoansList() {
		return loansList;
	}

	public void setLoansList(String loansList) {
		this.loansList = loansList;
	}

	/**
	 * 获取响应参数数组
	 * 
	 * @return
	 */
	public String[] getResponseParamNames() {
		return responseParamNames;
	}

	/**
	 * 设置响应参数数组
	 */
	public void setResponseParamNames(String[] responseParamNames) {
		this.responseParamNames = responseParamNames;
	}

	/**
	 * 获取ufx 处理流水号
	 * @return ufx 处理流水号
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置ufx 处理流水号
	 * @param  ufx 处理流水号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

}
