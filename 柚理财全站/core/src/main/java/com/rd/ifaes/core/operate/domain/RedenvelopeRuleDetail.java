package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 红包规则明细表pojo
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class RedenvelopeRuleDetail extends BaseEntity<RedenvelopeRuleDetail> {

	/**序列号*/
	private static final long serialVersionUID = 4260455350911310426L;
	/**红包规则id值*/
	private String	ruleId;		 /* 红包规则id值 */ 
	/**发放固定红包金额值*/
	private BigDecimal	amount;		 /* 发放固定红包金额值 */ 
	/**固定金额方式，最低投资金额限制*/
	private BigDecimal	useTenderMoney;		 /* 固定金额方式，最低投资金额限制 */
	/**投资金额开始区间值*/
	private BigDecimal	tenderMin;		 /* 投资金额开始区间值 */ 
	/**投资金额结束区间值*/
	private BigDecimal	tenderMax;		 /* 投资金额结束区间值 */ 
	/**比例发放的值（固定比例，比例满返）*/
	private BigDecimal	grantRate;		 /* 比例发放的值（固定比例，比例满返） */ 
	/**创建日期*/
	private Date	createTime;		 /* 创建日期 */ 

	//其他自定义属性

	// Constructor
	/**Constructor*/
	public RedenvelopeRuleDetail() {
		super();
	}

	/** full Constructor */
	public RedenvelopeRuleDetail(final String uuid, final String ruleId, final BigDecimal amount,
			final BigDecimal useTenderMoney, final BigDecimal tenderMin, final BigDecimal tenderMax, 
			final BigDecimal grantRate, final Date createTime) {
		super();
		setUuid(uuid);
		this.ruleId = ruleId;
		this.amount = amount;
		this.useTenderMoney = useTenderMoney;
		this.tenderMin = tenderMin;
		this.tenderMax = tenderMax;
		this.grantRate = grantRate;
		this.createTime = createTime;
	}
	
	@Override
	/**持久化数据前执行的操作*/
	public void preInsert() {
		super.preInsert();
		this.createTime = DateUtils.getNow();
	}
	/**获取规则id*/
	public String getRuleId() {
		return ruleId;
	}
	/**设置 规则id*/
	public void setRuleId(final String ruleId) {
		this.ruleId = ruleId;
	}
	/**获取 红包金额*/
	public BigDecimal getAmount() {
		return amount;
	}
	/**设置 红包金额*/
	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}
	/**获取 最低投标金额*/
	public BigDecimal getUseTenderMoney() {
		return useTenderMoney;
	}
	/**设置 最低投标金额*/
	public void setUseTenderMoney(final BigDecimal useTenderMoney) {
		this.useTenderMoney = useTenderMoney;
	}

	/**获取 投标金额区间小值*/
	public BigDecimal getTenderMin() {
		return tenderMin;
	}
	/**设置 投标金额区间小值*/
	public void setTenderMin(final BigDecimal tenderMin) {
		this.tenderMin = tenderMin;
	}

	/**获取 投标金额区间大值*/
	public BigDecimal getTenderMax() {
		return tenderMax;
	}
	/**设置 投标金额区间大值*/
	public void setTenderMax(final BigDecimal tenderMax) {
		this.tenderMax = tenderMax;
	}
	/**获取 发放比例*/
	public BigDecimal getGrantRate() {
		return grantRate;
	}
	/**设置 发放比例*/
	public void setGrantRate(final BigDecimal grantRate) {
		this.grantRate = grantRate;
	}
	/**获取 发放时间*/
	public Date getCreateTime() {
		return createTime;
	}
	/**设置 发放时间*/
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	@Override
	/**toString*/
	public String toString() {
		return "RedenvelopeRuleDetail [" + "uuid=" + uuid + ", ruleId=" + ruleId + ", amount=" + amount + ", useTenderMoney=" + useTenderMoney + ", tenderMin=" + tenderMin + ", tenderMax=" + tenderMax + ", grantRate=" + grantRate + ", createTime=" + createTime +  "]";
	}
}
