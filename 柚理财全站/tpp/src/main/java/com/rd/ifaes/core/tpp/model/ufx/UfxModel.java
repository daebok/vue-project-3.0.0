package com.rd.ifaes.core.tpp.model.ufx;

import org.springframework.beans.BeanUtils;

import com.rd.ifaes.core.tpp.domain.Ufx;

/**
 * 接口参数基类
 * 
 * @author wj
 * @version 2.0
 * @date 2015年6月2日 上午9:31:57
 */
public class UfxModel extends Ufx {
	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1L;
	
	/************ 操作状态常量 start *************/

	/**
	 * 未处理
	 */
	public final static int STATUS_UNTREATED = 0;
	
	/**
	 * 处理成功
	 */
	public final static int STATUS_SUCEESS = 1;
	
	/**
	 * 处理失败
	 */
	public final static int STATUS_FAIL = 2;
	
	/************ 操作状态常量 end *************/
	

	/************ 操作类型常量 start *************/
	
	/**
	 * 发布借款标
	 */
	public final static String ADDBORROW = "addBorrow";
	
	/**
	 * 投标
	 */
	public final static String ADDTENDER = "addTender";
	
	/**
	 * 申请VIP
	 */
	public final static String APPLYVIP = "applyVip";
	
	/**
	 *投资失败 
	 */
	public final static String INVEST_FAIL = "invest_fail";
	
	/**
	 * 债权投资失败
	 */
	public final static String BOND_INVEST_FAIL = "bond_invest_fail";
	
	/**
	 *流转理财投资失败 
	 */
	public final static String FLOW_FAIL = "flow_fail";
	
	/**
	 * 还款
	 */
	public final static String DOREPAY = "doRepay";
	
	/**
	 * 自动还款
	 */
	public final static String AUTOREPAY = "autoRepay";
	
	/**
	 * 网站垫付
	 */
	public final static String ADVANCEREPAY = "advanceRepay";
	
	/**
	 * 借款标复审成功
	 */
	public final static String BORROWVERIFYFULLSUCCESS = "borrowVerifyFullSuccess";
	
	/**
	 * 发布借款标失败
	 */
	public final static String ADDBORROWFAIL = "addBorrowFail";
	
	/**
	 * 借款标取消
	 */
	public final static String BORROWCANCEL = "borrowCancle";
	
	/**
	 * 借款标复审失败
	 */
	public final static String BORROWVERIFYFULLFAIL = "borrowVerifyFullFail";
	
	/**
	 * 发布秒还标冻结
	 */
	public final static String SECONDFREEZE = "secondFreeze";
	
	/**
	 * 发布秒还标解冻
	 */
	public final static String SECONDUNFREEZE = "secondUnFreeze";
	
	/**
	 * 后台充值
	 */
	public final static String WEBRECHARGE = "webRecharge";
	
	/**
	 * 流转标还款
	 */
	public final static String FLOW_REPAY = "flow_repay";
	
	/**
	 * 项目状态更新
	 */
	public final static String PROJECT_UPDATE = "projectUpdate";
	
	/**
	 * 债权放款
	 */
	public final static String BOND_LOANS = "bondLoans";

	/**
	 * 红包兑换
	 */
	public final static String RED_ENVELOPE_EXCHANGE = "red_envelope_exchange";

	/**
	 * 积分抽中现金
	 */
	public final static String SCORE_LOTTERY_MONEY = "score_lottery_money";

	/************ 操作类型常量 end *************/
	
	
	/***********  接口服务类型常量 start ***********/
	/**
	 * 放款
	 */
	public final static String TPP_LOAN = "loan";

	/**
	 * 债权放款
	 */
	public final static String TPP_BOND_LOANS = "bondLoans";

	/**
	 * 冻结
	 */
	public final static String TPP_FREEZE = "freeze";

	/**
	 * 解冻
	 */
	public final static String TPP_UNFREEZE = "unFreeze";

	/**
	 * 转账
	 */
	public final static String TPP_TRANSFER = "transfer";

	/**
	 * 还款
	 */
	public final static String TPP_REPAY = "repay";

	/**
	 * 投资失败
	 */
	public final static String TPP_INVESTFAIL = "investFail";

	/**
	 * 债权投资失败
	 */
	public final static String TPP_BONDINVESTFAIL = "bondInvestFail";

	/**
	 * 更新状态
	 */
	public final static String TPP_PROJECT_UPDATE = "projectUpdate";
	
	/** ===========汇付转换双乾修改变动 start============== **/
	/**
	 * 手续费收取
	 */
	public final static String TPP_FEE = "fee";
	
	/**
	 * 代金券红包放款
	 */
	public final static String TPP_VOUCHER_LOAN = "voucherLoan";
	/** ===========汇付转换双乾修改变动 end============== **/
	
	/***********  接口服务类型常量 end ***********/
	
	/**
	 * 自动投标Id
	 */
	private long autoTenderId; 
	
	/**
	 * 是否是流转标
	 */
	private int isFlow;
	
	/**
	 * 搜索条件：收款方用户名 
	 */
	private String searchName;
	
	/**
	 * 开始时间
	 */
	private String startTime;
	
	/**
	 * 结束时间
	 */
	private String endTime;
	
	/**
	 * 付款方用户名
	 */
	private String userName;
	
	/**
	 * 收款方用户名
	 */
	private String toUserName;
	
	/**
	 * 接口操作类型文字描述
	 */
	private String tppTypeStr;
	
	/**
	 * 平台操作类型文字描述
	 */
	private String serviceTypeStr;
	
	/**
	 * 交易状态文字描述
	 */
	private String statusStr;

	/**
	 * 项目总额
	 */
	private double borrowAccount;
	
	/**
	 * 投资使用红包金额
	 */
	private double redMoney;
	
	/**
	 * 投资使用体验券金额
	 */
	private double experMoney;

	/**
	 * 投标标识
	 */
	private String tppBorrowId;
	
	/**
	 * 银行编号
	 */
	private String bankCode;
	
	/**
	 * 充值方式
	 */
	private int rechargeType;
	
	/**
	 * 企业名称
	 */
	private String companyName;
	
	/**
	 * 营业执照编号
	 */
	private String bussinessCode;
	
	/**
	 * 是否是担保类型
	 */
	private String guarType;
	
	/**
	 * 是否需要审核 1需要，2不需要
	 */
	private String needAudit;
	
	/** 提现ufx变更处理 2016-05-27 start **/
	/**
	 * 冻结、解冻类型
	 */
	private String freezeType;
	/** 提现ufx变更处理 2016-05-27 end **/

	/**
	 * 每页总数
	 */
	private int rows;
	
	/**
	 * 获取实体
	 * @return
	 */
	public Ufx prototype(){
		Ufx tpp = new Ufx();
		BeanUtils.copyProperties(this, tpp);
		return tpp;
	}
	

	/**
	 * 获取model对象
	 * @param tpp
	 * @return
	 */
	public static UfxModel instance(Ufx tpp) {
		UfxModel model = new UfxModel();
		BeanUtils.copyProperties(tpp, model);
		return model;
	}


	public String getTppTypeStr() {
		// 冻结
		if (UfxModel.TPP_FREEZE.equals(this.getTppType())) {
			this.tppTypeStr = "冻结";
		}
		// 放款
		if (UfxModel.TPP_LOAN.equals(this.getTppType())) {
			this.tppTypeStr = "放款";
		}
		// 还款
		if (UfxModel.TPP_REPAY.equals(this.getTppType())) {
			this.tppTypeStr = "还款";
		}
		// 转账
		if (UfxModel.TPP_TRANSFER.equals(this.getTppType())) {
			this.tppTypeStr = "转账";
		}
		// 解冻
		if (UfxModel.TPP_UNFREEZE.equals(this.getTppType())) {
			this.tppTypeStr = "解冻";
		}
		/** ===========汇付转换双乾修改变动 start============== **/
		// 收取手续费
		if (UfxModel.TPP_FEE.equals(this.getTppType())) {
			this.tppTypeStr = "手续费收取";
		}
		// 代金券红包放款
		if (UfxModel.TPP_VOUCHER_LOAN.equals(this.getTppType())) {
			this.tppTypeStr = "代金券红包放款";
		}
		/** ===========汇付转换双乾修改变动 end============== **/
		// 债权投资失败
		if (UfxModel.TPP_BONDINVESTFAIL.equals(this.getTppType())) {
			this.tppTypeStr = "债权投资失败资金退回";
		}
		return tppTypeStr;
	}

	public void setTppTypeStr(String tppTypeStr) {
		this.tppTypeStr = tppTypeStr;
	}

	public String getServiceTypeStr() {
		if (UfxModel.ADDBORROW.equals(this.getServiceType())) {
			this.serviceTypeStr = "发布借款标";
		}
		if (UfxModel.ADDBORROWFAIL.equals(this.getServiceType())) {
			this.serviceTypeStr = "发标失败";
		}
		if (UfxModel.ADDTENDER.equals(this.getServiceType())) {
			this.serviceTypeStr = "投标";
		}
		if (UfxModel.APPLYVIP.equals(this.getServiceType())) {
			this.serviceTypeStr = "申请VIP";
		}
		if (UfxModel.AUTOREPAY.equals(this.getServiceType())) {
			this.serviceTypeStr = "自动还款";
		}
		if (UfxModel.BORROWCANCEL.equals(this.getServiceType())) {
			this.serviceTypeStr = "借款标取消";
		}
		if (UfxModel.BORROWVERIFYFULLFAIL.equals(this.getServiceType())) {
			this.serviceTypeStr = "借款标复审失败";
		}
		if (UfxModel.BORROWVERIFYFULLSUCCESS.equals(this.getServiceType())) {
			this.serviceTypeStr = "借款标复审成功";
		}
		if (UfxModel.DOREPAY.equals(this.getServiceType())) {
			this.serviceTypeStr = "还款";
		}
		if (UfxModel.FLOW_REPAY.equals(this.getServiceType())) {
			this.serviceTypeStr = "流转理财还款";
		}
		if (UfxModel.RED_ENVELOPE_EXCHANGE.equals(this.getServiceType())) {
			this.serviceTypeStr = "红包兑换";
		}
		if (UfxModel.ADVANCEREPAY.equals(this.getServiceType())) {
			this.serviceTypeStr = "网站垫付";
		}
		if (UfxModel.INVEST_FAIL.equals(this.getServiceType())) {
			this.serviceTypeStr = "投资失败";
		}
		if (UfxModel.FLOW_FAIL.equals(this.getServiceType())) {
			this.serviceTypeStr = "流转项目投资失败";
		}
		if (UfxModel.BOND_INVEST_FAIL.equals(this.getServiceType())) {
			this.serviceTypeStr = "债权投资失败";
		}
		return serviceTypeStr;
	}

	public void setServiceTypeStr(String serviceTypeStr) {
		this.serviceTypeStr = serviceTypeStr;
	}

	public String getStatusStr() {
		switch (getStatus()) {
		case UfxModel.STATUS_FAIL:
			this.statusStr = "失败";
			break;
		case UfxModel.STATUS_SUCEESS:
			this.statusStr = "成功";
			break;
		case UfxModel.STATUS_UNTREATED:
			this.statusStr = "未处理";
			break;
		default:
			break;
		}
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	
	public int getIsFlow() {
		return isFlow;
	}

	public void setIsFlow(int isFlow) {
		this.isFlow = isFlow;
	}

/*	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}*/

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public long getAutoTenderId() {
		return autoTenderId;
	}

	public void setAutoTenderId(long autoTenderId) {
		this.autoTenderId = autoTenderId;
	}

	public double getBorrowAccount() {
		return borrowAccount;
	}

	public void setBorrowAccount(double borrowAccount) {
		this.borrowAccount = borrowAccount;
	}

	/**
	 * 获取redMoney
	 * @return redMoney
	 */
	public double getRedMoney() {
		return redMoney;
	}


	/**
	 * 设置redMoney
	 * @param redMoney
	 */
	public void setRedMoney(double redMoney) {
		this.redMoney = redMoney;
	}


	/**
	 * 获取experMoney
	 * @return experMoney
	 */
	public double getExperMoney() {
		return experMoney;
	}


	/**
	 * 设置experMoney
	 * @param experMoney
	 */
	public void setExperMoney(double experMoney) {
		this.experMoney = experMoney;
	}

	public String getTppBorrowId() {
		return tppBorrowId;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public void setTppBorrowId(String tppBorrowId) {
		this.tppBorrowId = tppBorrowId;
	}

	public int getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(int rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getBussinessCode() {
		return bussinessCode;
	}

	public void setBussinessCode(String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	public String getGuarType() {
		return guarType;
	}

	public void setGuarType(String guarType) {
		this.guarType = guarType;
	}

	/**
	 * 获取是否需要审核1需要，2不需要
	 * @return needAudit
	 */
	public String getNeedAudit() {
		return needAudit;
	}


	/**
	 * 设置是否需要审核1需要，2不需要
	 * @param needAudit
	 */
	public void setNeedAudit(String needAudit) {
		this.needAudit = needAudit;
	}


	public String getFreezeType() {
		return freezeType;
	}


	public void setFreezeType(String freezeType) {
		this.freezeType = freezeType;
	}


	

}
