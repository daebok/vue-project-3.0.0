package com.rd.ifaes.common.dict;
/**
 * 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有<br>
 * 
 * 借贷相关状态值（产品、借款）  债权相关状态值
 * 
 * @author  FangJun
 * @date 2016年7月1日
 */
public enum LoanEnum implements BaseEnum<String, String> {
	//--------------借贷状态------------------
	/**
	 * 借贷状态：新增,0
	 */
	STATUS_NEW("新增", "0"), 
	/**
	 * 借贷状态：担保待审,11
	 */
	STATUS_WAIT_VOUCH_VERIFY("担保待审", "11"),
	/**
	 * 借贷状态：担保被拒,12
	 */
	STATUS_VOUCH_VERIFY_FAIL("担保被拒", "12"),
	/**
	 * 借贷状态：发布待审,1
	 */
	STATUS_WAIT_PUBLISH("发布待审", "1"),
	/**
	 * 借贷状态：发布审核成功,2
	 */
	STATUS_PUBLISH_SUCCESS("发布审核成功", "2"), 
	/**
	 * 借贷状态：发布审核被拒,3
	 */
	STATUS_PUBLISH_FAIL("发布审核被拒", "3"), 
	/**
	 * 借贷状态：已作废,30
	 */
	STATUS_DELETE("已作废", "30"),
	/**
	 * 借贷状态：待上架,40 (查询使用)
	 */
	STATUS_WAIT_SHOW("待上架","40"), 
	/**
	 * 借贷状态：待售,41 (查询使用)
	 */
	STATUS_WAIT_SALE("待售","41"), 
	/**
	 * 借贷状态：募集中,4
	 */
	STATUS_RAISING("募集中","4"), 
	/**
	 * 借贷状态：成立待审,5 已下架的借款、募集金额已满的借款、募集时间结束的借款
	 */
	STATUS_RAISE_FINISHED("成立待审", "5"), 
	/**
	 * 借贷状态：成立审核成功,6
	 */
	STATUS_ESTABLISH("成立审核成功", "6"), 
	/**
	 * 借贷状态：未成立,7
	 */
	STATUS_NOT_ESTABLISH("未成立","7"), 
	/**
	 * 借贷状态：撤销处理中,71
	 */
	STATUS_CANCELLING("撤销处理中","71"), 
	/**
	 * 借贷状态：还款中,8
	 */
	STATUS_REPAY_START("还款中", "8"), 
	/**
	 * 借贷状态：逾期中,87 到期未还款
	 */
	STATUS_REPAY_OVERDUE("逾期中", "87"),
	/**
	 * 借贷状态：坏账,88
	 */
	STATUS_BAD_DEBT("坏账", "88"),
	/**
	 * 借贷状态：还款处理中,90 
	 */
	STATUS_REPAYING("还款处理中", "90"), 
	/**
	 * 借贷状态：还款成功,9
	 */
	STATUS_REPAYED_SUCCESS("还款成功", "9"),
	/**
	 * 借贷状态：逾期还款,91  逾期后已还款
	 */
	STATUS_REPAYED_LATE("逾期还款", "91"),

	//---------------变现标识--------------------------------------------------------
	/**
	 * 产品变现后发布借款,1
	 */
	REALIZE_FLAG_REALIZE ("产品变现后发布借款", "1"),
	/**
	 * 普通借款,0
	 */
	REALIZE_FLAG_NORMAL ("普通借款", "0"),
	//---------------是否可变现标识--------------------------------------------------------
	/**
	 * 可变现,1
	 */
	REALIZE_USEFUL ("可变现", "1"),
	/**
	 * 不可变现,0
	 */
	REALIZE_USEFULNESS ("不可变现", "0"),
	
	//---------------借款标识--------------------------------------------------------
	/**
	 * 借款标识,1
	 */
	BORROW_FLAG_BORROW ("借款标识", "1"),
	/**
	 * 产品标识,0
	 */
	BORROW_FLAG_PRODUCT ("产品标识", "0"),
	
	//----------------销售方式---------------------------------------------------------
	/**
	 * 销售方式 : 按金额,1
	 */
	SALE_STYLE_MONEY("按金额", "1"),
	/**
	 * 销售方式 : 按份数,2
	 */
	SALE_STYLE_COPY("按份数", "2"),

	//----------------是否担保---------------------------------------------------------
	/**
	 * 是否担保 : 担保,1
	 */
	VOUCH_FLAG_YES("担保", "1"),
	/**
	 * 是否担保 : 不担保,0
	 */
	VOUCH_FLAG_NO("不担保", "0"),
	
	//----------------还款方式---------------------------------------------------------
	/**
	 * 等额本息 ，1
	 */
	STYLE_INSTALLMENT_REPAYMENT("等额本息", "1"),
	/**
	 * 一次性还款,2
	 */
	STYLE_ONETIME_REPAYMENT("一次性还款", "2"),
	
	/**
	 * 每月还息到期还本,3
	 */
	STYLE_MONTHLY_INTEREST("每月还息到期还本","3"),
	
	/**
	 * 等额本金 ，4
	 */
	STYLE_AVERAGE_CAPITAL_REPAYMENT("等额本金", "4"),
	/**
	 * 每季还息到期还本,5
	 */
	STYLE_QUARTER_INTEREST("每季还息到期还本","5"),
	
	/**
	 * 即息理财 ，6
	 */
	STYLE_CURRENT_REPAYMENT("即息理财", "6"),
	
	//----------------还款方式---------------------------------------------------------
	/**
	 * 月标，0
	 */
	TIME_TYPE_MONTH("月标", "0"),
	/**
	 * 天标，1
	 */
	TIME_TYPE_DAY("天标", "1"),
	
	//----------------定向销售---------------------------------------------------------
	/**
	 * 定向销售:不定向，0
	 */
	SPECIFIC_SALE_NONE("不定向", "0"),
	/**
	 * 定向销售 : 定向密码，1
	 */
	SPECIFIC_SALE_PASSWORD("定向密码", "1"),
	/**
	 * 定向销售 : 定向等级，2
	 */
	SPECIFIC_SALE_VIP_LEVEL("定向等级", "2"),
	/**
	 *  定向销售 : 定向邮箱域名，3
	 */
	SPECIFIC_SALE_MAIL("定向邮箱域名", "3"),
	/**
	 *  定向销售 : 定向用户，4
	 */
	SPECIFIC_SALE_USER("定向用户", "4"),
	//----------------计息方式-----------------------------------------
	/**
	 * 变现还款方式-未还
	 */
	REALIZE_REPAY_WAY_UNPAY("未还款","0"),
	/**
	 * 变现还款方式-手动还款
	 */
	REALIZE_REPAY_WAY_HAND("手动还款","1"),
	/**
	 * 变现还款方式-手动还款
	 */
	REALIZE_REPAY_WAY_AUTO("自动还款","2"),
	
	//----------------计息方式-----------------------------------------
	/**
	 * 计息方式: 成立计息
	 */
	INTEREST_STYLE_EV("成立计息","1"),
	
	/**
	 * 计息方式: T+N计息
	 */
	INTEREST_STYLE_TN("T+N计息","2"),
	
	//----------------还款类型-----------------------------------------
	
	/**
	 * 正常还款
	 */
	REPAY_TYPE_NORMAL("正常还款","1"),
	
	/**
	 * 垫付
	 */
	REPAY_TYPE_ADVANCE("垫付","2"),
	
	/**
	 * 已还垫付
	 */
	REPAY_TYPE_ADVANCE_REPAYED("已还垫付","3"),	
	
	/**
	 * 逾期还款
	 */
	REPAY_TYPE_OVERDUE("逾期还款","4"),	
	
	//----------------产品类型-----------------------------------------
	/**
	 * 新手专享
	 */
	NOVICE_FLAG_YES("新手专享","1"),
	
	/**
	 * 普通
	 */
	NOVICE_FLAG_NO("普通","0"),
	/**
	 *  是否精选
	 */
	CHOICE_FLAG_YES("精选理财","1"),
	
	/**
	 * 非精选理财
	 */
	CHOICE_FLAG_NO("非精选理财","0"),
	/**
	 * 担保待审核
	 */
	VOUCH_STATUS_WAIT_VERIFY("担保待审核","0"),
	
     /**
	 * 审核通过
	 */
	VOUCH_STATUS_VERIFY("审核通过","1"),
	/**
	 * 审核拒绝
	 */
	VOUCH_STATUS_WAIT_FAIL("担保审核拒绝","2"),
	
	/**
	 * 投资阶段：在投
	 */
	STAGE_SALE("在投","1"),
	/**
	 * 投资阶段：待售
	 */
	STAGE_SHOW("待售","2"),
	/**
	 * 投资阶段：满额
	 */
	STAGE_FULL("满额","3"),
	/**
	 * 投资阶段：还款中或已还款
	 */
	STAGE_REPAY("还款中或已还款","4"),
	/*标的类别信用*/
	PROJECT_TYPE_CREDIT("信用", "4"),
	/*标的类别质押*/
	PROJECT_TYPE_PLEDGE("质押", "1")
	;
	
	// 字典项的中文名称
	private String name;
	// 字典项的值
	private String value;

	private LoanEnum(String name, String value) {
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
	
	/**
	 * 获取值(int)
	 * @return
	 */
	public Integer getInt(){
		return Integer.valueOf(this.value);
	}

	 /**
	  * 根据VALUE比较
	  * @param obj 目标值
	  * @return
	  */
	public boolean eq(Object obj) {
		return this.value.equals(obj);
	}
}
