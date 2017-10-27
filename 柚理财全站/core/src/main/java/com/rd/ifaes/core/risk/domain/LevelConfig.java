package com.rd.ifaes.core.risk.domain;

import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * entity:LevelConfig
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public class LevelConfig extends BaseEntity<LevelConfig> {

	private static final long serialVersionUID = 1L;
	/** 风险等级编号 */
	private String riskLevelNo;
	
	/** 产品风险等级名称（默认分为：高风险、中高风险、中等风险、较低风险、低风险） */
	@Length(max=Constant.ENTITY_LENGTH_ONE)
	private String riskLevelName;
	
	/** 产品风险等级数值（高风险：5；中高风险：4；中等风险：3；较低风险：2；低风险：1；） */
	@Range(min=Constant.INT_ZERO,max=Constant.INT_TEN)
	private Integer riskLevelVal;
	
	/** 产品风险等级含义及介绍 */
	@Length(max=Constant.ENTITY_LENGTH_FIVE)
	private String riskLevelDesc;
	
	/** 用户风险承受能力评估名称 */
	@Length(max=Constant.ENTITY_LENGTH_ONE)
	private String userRiskLevelName;
	
	/** 用户风险承受能力值(0,1,2,3,4) */
	@Range(min=Constant.INT_ZERO,max=Constant.INT_TEN)
	private Integer userRiskLevelVal;
	
	/** 用户风险承受能力含义 */
	@Length(max=Constant.ENTITY_LENGTH_FIVE)
	private String userRiskLevelDesc;
	
	/** 排序 */
	private Integer sort;
	/** 逻辑删除标识 */
	private Integer deleteFlag;
	/** 创建时间 */
	private Date createTime;

	// 其他自定义属性

	/**
	 * Constructor
	 */
	public LevelConfig() {
		super();
	}

	/**
	 * full Constructor
	 */
	public LevelConfig(final String uuid, final String riskLevelNo,
			final String riskLevelName, final Integer riskLevelVal,
			final String riskLevelDesc, final String userRiskLevelName,
			final Integer userRiskLevelVal, final String userRiskLevelDesc,
			final Integer sort, final Integer deleteFlag, final Date createTime) {
		super();
		setUuid(uuid);
		this.riskLevelNo = riskLevelNo;
		this.riskLevelName = riskLevelName;
		this.riskLevelVal = riskLevelVal;
		this.riskLevelDesc = riskLevelDesc;
		this.userRiskLevelName = userRiskLevelName;
		this.userRiskLevelVal = userRiskLevelVal;
		this.userRiskLevelDesc = userRiskLevelDesc;
		this.sort = sort;
		this.deleteFlag = deleteFlag;
		this.createTime = createTime;
	}

	/**
	 * @return
	 */
	public String getRiskLevelNo() {
		return riskLevelNo;
	}

	/**
	 * @param riskLevelNo
	 */
	public void setRiskLevelNo(final String riskLevelNo) {
		this.riskLevelNo = riskLevelNo;
	}

	/**
	 * @return
	 */
	public String getRiskLevelName() {
		return riskLevelName;
	}

	/**
	 * @param riskLevelName
	 */
	public void setRiskLevelName(final String riskLevelName) {
		this.riskLevelName = riskLevelName;
	}

	/**
	 * @return
	 */
	public Integer getRiskLevelVal() {
		return riskLevelVal;
	}

	/**
	 * @param riskLevelVal
	 */
	public void setRiskLevelVal(final Integer riskLevelVal) {
		this.riskLevelVal = riskLevelVal;
	}

	/**
	 * @return
	 */
	public String getRiskLevelDesc() {
		return riskLevelDesc;
	}

	/**
	 * @param riskLevelDesc
	 */
	public void setRiskLevelDesc(final String riskLevelDesc) {
		this.riskLevelDesc = riskLevelDesc;
	}

	/**
	 * @return
	 */
	public String getUserRiskLevelName() {
		return userRiskLevelName;
	}

	/**
	 * @param userRiskLevelName
	 */
	public void setUserRiskLevelName(final String userRiskLevelName) {
		this.userRiskLevelName = userRiskLevelName;
	}

	/**
	 * @return
	 */
	public Integer getUserRiskLevelVal() {
		return userRiskLevelVal;
	}

	/**
	 * @param userRiskLevelVal
	 */
	public void setUserRiskLevelVal(final Integer userRiskLevelVal) {
		this.userRiskLevelVal = userRiskLevelVal;
	}

	/**
	 * @return
	 */
	public String getUserRiskLevelDesc() {
		return userRiskLevelDesc;
	}

	/**
	 * @param userRiskLevelDesc
	 */
	public void setUserRiskLevelDesc(final String userRiskLevelDesc) {
		this.userRiskLevelDesc = userRiskLevelDesc;
	}

	/**
	 * @return
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * @param sort
	 */
	public void setSort(final Integer sort) {
		this.sort = sort;
	}

	/**
	 * @return
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param deleteFlag
	 */
	public void setDeleteFlag(final Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "LevelConfig [" + "uuid=" + uuid + ", riskLevelNo="
				+ riskLevelNo + ", riskLevelName=" + riskLevelName
				+ ", riskLevelVal=" + riskLevelVal + ", riskLevelDesc="
				+ riskLevelDesc + ", userRiskLevelName=" + userRiskLevelName
				+ ", userRiskLevelVal=" + userRiskLevelVal
				+ ", userRiskLevelDesc=" + userRiskLevelDesc + ", sort=" + sort
				+ ", createTime=" + createTime + "]";
	}

	/**
	 * 重写preInsert
	 */
	@Override
	public void preInsert() {
		this.deleteFlag = Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG
				.getValue());
		this.setCreateTime(DateUtils.getNow());
		super.preInsert();
	}

}
