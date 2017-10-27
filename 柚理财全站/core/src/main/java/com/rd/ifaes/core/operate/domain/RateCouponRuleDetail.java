package com.rd.ifaes.core.operate.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * 
 * 红包规则明细设置pojo
 * @version 3.0
 * @author wyw
 * @date 2016-7-28
 */
public class RateCouponRuleDetail extends BaseEntity<RateCouponRuleDetail> {
	/** 序列号*/
	private static final long serialVersionUID = -5789523658192571923L;
	/** 加息券规则id值 */
	private String	ruleId;		 
	/** 发放加息券的固定值 */
	private BigDecimal	upApr;		 
	/** 最低投资金额限制 */
	private BigDecimal	useTenderMoney;		 
	/** 投资金额开始区间值 */
	private BigDecimal	tenderMin;		 
	/** 投资金额结束区间值 */
	private BigDecimal	tenderMax;		
	/** 创建日期*/
	private Date	createTime;	
	/**统计值  最小加息率 */
	private BigDecimal	minUpApr;	
	/**统计值  最大加息率 */
	private BigDecimal	maxUpApr;	
	

	public BigDecimal getMinUpApr() {
		return minUpApr;
	}

	public void setMinUpApr(BigDecimal minUpApr) {
		this.minUpApr = minUpApr;
	}

	public BigDecimal getMaxUpApr() {
		return maxUpApr;
	}

	public void setMaxUpApr(BigDecimal maxUpApr) {
		this.maxUpApr = maxUpApr;
	}

	//其他自定义属性
	/** Constructorr */
	public RateCouponRuleDetail() {
		super();
	}

	/** full Constructor */
	public RateCouponRuleDetail(final String uuid, final String ruleId,final  BigDecimal upApr,final BigDecimal useTenderMoney,
			final BigDecimal tenderMin, final BigDecimal tenderMax, final Date createTime) {
		super();
		setUuid(uuid);
		this.ruleId = ruleId;
		this.upApr = upApr;
		this.useTenderMoney = useTenderMoney;
		this.tenderMin = tenderMin;
		this.tenderMax = tenderMax;
		this.createTime = createTime;
	}
	/** 持久化数据库前进行的操作 */
	@Override
	public void preInsert() {
		super.preInsert();
		this.createTime = DateUtils.getNow();
	}
	/** 获取 规则id */
	public String getRuleId() {
		return ruleId;
	}
	/** 设置 规则id */
	public void setRuleId(final String ruleId) {
		this.ruleId = ruleId;
	}
	/** 获取 加息利率 */
	public BigDecimal getUpApr() {
		return upApr;
	}
	/** 设置 加息利率 */
	public void setUpApr(final BigDecimal upApr) {
		this.upApr = upApr;
	}
	/** 获取 最低投资金额 */
	public BigDecimal getUseTenderMoney() {
		return useTenderMoney;
	}
	/** 设置 最低投资金额 */
	public void setUseTenderMoney(final BigDecimal useTenderMoney) {
		this.useTenderMoney = useTenderMoney;
	}
	/** 获取 投资区间最小值 */
	public BigDecimal getTenderMin() {
		return tenderMin;
	}
	/** 设置 投资区间最小值 */
	public void setTenderMin(final BigDecimal tenderMin) {
		this.tenderMin = tenderMin;
	}
	/** 获取 投资区间最大值 */
	public BigDecimal getTenderMax() {
		return tenderMax;
	}
	/** 设置 投资区间最大值 */
	public void setTenderMax(final BigDecimal tenderMax) {
		this.tenderMax = tenderMax;
	}
	/** 获取 添加时间 */
	public Date getCreateTime() {
		return createTime;
	}
	/** 设置 添加时间 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/** toString */
	@Override
	public String toString() {
		return "RatecouponRuleDetail [" + "uuid=" + uuid + ", ruleId=" + ruleId + ", upApr=" + upApr + ", useTenderMoney=" + useTenderMoney + ", tenderMin=" + tenderMin + ", tenderMax=" + tenderMax + ", createTime=" + createTime +  "]";
	}
}
