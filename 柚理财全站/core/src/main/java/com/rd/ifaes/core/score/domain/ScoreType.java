package com.rd.ifaes.core.score.domain;

import java.math.BigDecimal;

import com.rd.ifaes.common.entity.BaseEntity;

/**
 * entity:ScoreType
 * 
 * @author 
 * @version 3.0
 * @date 2016-8-4
 */
public class ScoreType extends BaseEntity<ScoreType> {
	/** 序列号*/
	private static final long serialVersionUID = 3110042094552667045L;
	/** 积分类型名*/
	private String	typeName;
	/** 积分类型标识*/
	private String	typeCode;		
	/** 发放类型（1：固定值，2：固定比例，3：金额满返 4：比例满返，5：固定递增）*/
	private String	grantType;		
	/** 发放积分（发放类型为1的时候填写）*/
	private Integer	grantValue;		
	/** 发放的比例（发放类型为2,4有效）*/
	private BigDecimal	grantRate;		
	/** 发放的最小值 */
	private Integer	grantMin;		
	/** 发放的最大值 */
	private Integer	grantMax;		
	/** 发放类型为5 固定递增值 */
	private Integer	grantUp;		
	/** 发放类型为5 规则备注*/
	private String	remark;		
	/** 状态： 0禁用  1启用*/
	private String	status;		

	//其他自定义属性
	
	/** Constructor*/
	public ScoreType() {
		super();
	}

	/**
	 * full Constructor
	 */
	public ScoreType(final String uuid, final String typeName, final String typeCode, final String grantType,
			final Integer grantValue,
			final BigDecimal grantRate,final  Integer grantMin,final Integer grantMax, 
			final Integer grantUp, final String remark, final String status) {
		super();
		setUuid(uuid);
		this.typeName = typeName;
		this.typeCode = typeCode;
		this.grantType = grantType;
		this.grantValue = grantValue;
		this.grantRate = grantRate;
		this.grantMin = grantMin;
		this.grantMax = grantMax;
		this.grantUp = grantUp;
		this.remark = remark;
		this.status = status;
	}
	/** 获取积分类别名称 */
	public String getTypeName() {
		return typeName;
	}
	/** 设置 积分类别名称 */
	public void setTypeName(final String typeName) {
		this.typeName = typeName;
	}
	/** 获取积分类别标识 */
	public String getTypeCode() {
		return typeCode;
	}
	/** 设置积分类别标识 */
	public void setTypeCode(final String typeCode) {
		this.typeCode = typeCode;
	}
	/** 获取积分发放方式 */
	public String getGrantType() {
		return grantType;
	}
	/** 设置 积分发放方式 */
	public void setGrantType(final String grantType) {
		this.grantType = grantType;
	}
	/** 获取积分发放的值*/
	public Integer getGrantValue() {
		return grantValue;
	}
	/** 设置积分发放的值*/
	public void setGrantValue(final Integer grantValue) {
		this.grantValue = grantValue;
	}

	/** 获取积分发放的比例*/
	public BigDecimal getGrantRate() {
		return grantRate;
	}
	/** 设置积分发放的比例*/
	public void setGrantRate(final BigDecimal grantRate) {
		this.grantRate = grantRate;
	}
	/** 获取积分发放的比例的区间小值*/
	public Integer getGrantMin() {
		return grantMin;
	}
	/** 设置 积分发放的比例的区间小值*/
	public void setGrantMin(final Integer grantMin) {
		this.grantMin = grantMin;
	}
	/** 获取积分发放的比例的区间大值*/
	public Integer getGrantMax() {
		return grantMax;
	}
	/** 设置 积分发放的比例的区间大值*/
	public void setGrantMax(final Integer grantMax) {
		this.grantMax = grantMax;
	}
	/** 获取积分发放的增长值*/
	public Integer getGrantUp() {
		return grantUp;
	}
	/** 设置积分发放的增长值*/
	public void setGrantUp(final Integer grantUp) {
		this.grantUp = grantUp;
	}
	/** 获取积分发放的备注*/
	public String getRemark() {
		return remark;
	}
	/** 设置积分发放的备注*/
	public void setRemark(final String remark) {
		this.remark = remark;
	}
	/** 获取积分发放的状态*/
	public String getStatus() {
		return status;
	}
	/** 设置积分发放的状态*/
	public void setStatus(final String status) {
		this.status = status;
	}
	/** toString*/
	@Override
	public String toString() {
		return "ScoreType [" + "uuid=" + uuid + ", typeName=" + typeName + ", typeCode=" + typeCode + ", grantType=" + grantType + ", grantValue=" + grantValue + ", grantRate=" + grantRate + ", grantMin=" + grantMin + ", grantMax=" + grantMax + ", grantUp=" + grantUp + ", remark=" + remark + ", status=" + status +  "]";
	}
}
