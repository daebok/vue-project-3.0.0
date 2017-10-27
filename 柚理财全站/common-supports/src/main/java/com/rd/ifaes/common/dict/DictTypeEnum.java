package com.rd.ifaes.common.dict;

/**
 * 字典类型常量
 * @version 3.0
 * @author xhf
 * @date 2016年7月27日
 */
public enum DictTypeEnum implements BaseEnum<String, String> {
	/**
	 *  学历
	 */
	EDUCATION_LEVEL("学历", "educationLevel"),
	
	/**
	 *  婚姻状况
	 */
	MARITAL_STATUS("婚姻状况", "maritalStatus"),
	
	/**
	 * 工作年限
	 */
	WORK_EXPERIENCE("工作年限", "workExperience"),
	
	/**
	 * 薪资范围
	 */
	SALARY_RANGE("薪资范围", "salaryRange"),
	
	/**
	 * 车产
	 */
	CAR_STATUS("车产", "carStatus"),
	
	/**
	 * 房产
	 */
	HOUSES_STATUS("房产", "houseStatus"),
	
	/**
	 * 用户密保问题
	 */
	SECURITY_QUESTION("用户密保问题", "user_security_question"),
	
	/**
	 * 用户充值状态
	 */
	RECHARGE_STATUS("用户充值状态", "rechargeStatus"),
	
	/**
	 * 用户提现状态
	 */
	CASH_STATUS("用户提现状态", "cashStatus"),
	
	/**
	 * 银行列表
	 */
	ACCOUNT_BANK("银行列表", "accountBank"),
	
	/**
	 * 账户类别
	 */
	ACCOUNT_TYPE("账户类别", "accountType"),
	
	/**
	 * 账户类别
	 */
	USER_FREEZE("用户冻结功能", "userFreeze"),
	
	/**
	 * 担保机构
	 */
	VOUCH_FREEZE("担保机构冻结功能", "vouchFreeze"),
	
	/**
	 * 性别
	 */
	SEX("性别", "sex"),
	
	/**
	 * 实名状态
	 */
	REAL_NAME_STATUS("实名状态", "realNameStatus"),
	
	/**
	 * 用户类型
	 */
	USER_NATURE("用户类型", "userNature"),
	
	/**
	 * 借贷状态
	 */
	PROJECT_STATUS("借贷状态", "projectStatus"),
	/**
	 * 我的借款-项目状态
	 */
     MY_LOAN_STATUS("我的借款-项目状态", "myLoanStatus"),
	/**
	 * 还款状态
	 */
	REPAY_STATUS("还款状态", "repayStatus"),
	
	/**
	 * 个人证明资料类型
	 */
	QUALIFICATION_TYPE("个人证明资料类型","qualificationType"),
	
	/**
	 * 企业证明资料类型
	 */
	COMPANY_QUALIFICATION_TYPE("企业证明资料类型","companyQualificationType"),
	
	/**
	 * 资质证明上传状态
	 */
	QUALIFICATION_APPLY_STATUS("资质证明上传状态","qualificationApplyStatus"),
	
	/**
	 * 还款方式
	 */
	REPAY_STYLE("还款方式","repayStyle"),
	
	/**
	 * 计息方式
	 */
	INTEREST_STYLE("计息方式","interestStyle"),
	
	/**
	 * 借款用途
	 */
	BORROW_USE("借款用途","borrowUse"),
	
	/**
	 * 预约借款期限
	 */
	BESPEAK_LIMIT_TIME("预约借款期限","bespeakLimitTime"),
	
	/**
	 * 统计金额范围
	 */
	ACCOUNT_RANGE("统计金额范围","accountRange"),
	
	/**
	 * 投资金额范围
	 */
	INVEST_AMOUNT_RANGE("投资金额范围","investAmountRange"),
	
	/**
	 * 统计年龄范围
	 */
	AGE_RANGE("统计金额范围","ageRange"),
	
	/**
	 * 统计期限范围
	 */
	TIME_LIMIT_RANGE("统计期限范围","timeLimitRange"),
	
	/**
	 * 统计期限范围
	 */
	BOND_TIME_LIMIT_RANGE("债权期限范围","bondTimeLimitRange"),
	
	/**
	 * 统计借款类型
	 */
	BORROW_TYPE_RANGE("统计借款类型","borrowTypeRange"),
	
	/**
	 * 渠道
	 */
	SALE_CHANNEL("上架渠道","saleChannel"),
	
	GOODS_STATUS("积分商品状态","goodsStatus"),
	
	/**
	 * 活动日志状态
	 */
	ACTIVITY_LOG_STATUS("活动日志状态","activityLogStatus"),
	PROJECT_TYPE_MANAGE("标的类别管理", "project_type_manage"),
	/**
	 * 账户金额浮动类型
	 */
	ACCOUNT_CHANGE_TYPE("账户金额浮动类型" , "accountChangeType"),
	/**
	 * 账户金额浮动类型
	 */
	USER_ACCOUNT_TYPE("用户账号类型" , "userAccountType"),
	/**
	 * 定向销售
	 */
	SPECIFIC_SALE("定向销售" , "specificSale"),
	/**
	 * 
	 */
	LOTTERY_TYPE("抽奖类型", "lotteryType");
	
	
	
	/**
	 *  字典项的中文名称
	 */
	private String name;
	
	/**
	 *  字典项的值
	 */
	private String value;

	private DictTypeEnum(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 获取label
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	@Override
	public String getValue() {
		return this.value;
	}

}
