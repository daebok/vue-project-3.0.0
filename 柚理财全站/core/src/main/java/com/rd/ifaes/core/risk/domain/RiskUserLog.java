package com.rd.ifaes.core.risk.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:UserLog
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-14
 */
public class RiskUserLog extends BaseEntity<RiskUserLog> {

	private static final long serialVersionUID = 1L;
	/** 关联用户UUID（风险评估人） */
	private String userId;
	/** 关联问卷UUID（问卷号） */
	private String papersId;
	/** 评估所得分数 */
	private Integer score;
	/** 评估分数对应风险等级 */
	private Integer riskLevel;
	/** 创建时间 */
	private Date createTime;
	/** 评估状态值（0:初始状态值；1：有效状态值） */
	private Integer status;
	/** 用户答题的内容（数组题号和用户选择答案） */
	private String riskAnswers;

	// 其他自定义属性
	/** 关联的评测用户 */
	private String userName;
	/** 关联的评测试卷 */
	private String papersName;//
	/** 关联的风险类型 */
	private String riskLevelName;//
	/** 关联的真实姓名 */
	private String realName;//
	/**
	 * Constructor
	 */
	public RiskUserLog() {
		super();
	}

	/**
	 * @param userId
	 * @param papersId
	 */
	public RiskUserLog(final String userId, final String papersId) {
		super();
		this.userId = userId;
		this.papersId = papersId;
	}

	/**
	 * full Constructor
	 */
	public RiskUserLog(final String uuid, final String userId,
			final String papersId, final Integer score,
			final Integer riskLevel, final Date createTime,
			final Integer status, final String riskAnswers) {
		super();
		setUuid(uuid);
		this.userId = userId;
		this.papersId = papersId;
		this.score = score;
		this.riskLevel = riskLevel;
		this.createTime = createTime;
		this.status = status;
		this.riskAnswers = riskAnswers;
	}

	/**
	 * 重写preInsert
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		super.preInsert();
	}

	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * @return
	 */
	public String getPapersId() {
		return papersId;
	}

	/**
	 * @param papersId
	 */
	public void setPapersId(final String papersId) {
		this.papersId = papersId;
	}

	/**
	 * @return
	 */
	public Integer getScore() {
		return score;
	}

	/**
	 * @param score
	 */
	public void setScore(final Integer score) {
		this.score = score;
	}

	/**
	 * @return
	 */
	public Integer getRiskLevel() {
		return riskLevel;
	}

	/**
	 * @param riskLevel
	 */
	public void setRiskLevel(final Integer riskLevel) {
		this.riskLevel = riskLevel;
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
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 */
	public void setStatus(final Integer status) {
		this.status = status;
	}

	/**
	 * @return
	 */
	public String getRiskAnswers() {
		return riskAnswers;
	}

	/**
	 * @param riskAnswers
	 */
	public void setRiskAnswers(final String riskAnswers) {
		this.riskAnswers = riskAnswers;
	}

	/**
	 * @Title: getUserName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @Title: setUserName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * @Title: getPapersName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public String getPapersName() {
		return papersName;
	}

	/**
	 * @Title: setPapersName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public void setPapersName(final String papersName) {
		this.papersName = papersName;
	}

	/**
	 * @Title: getRiskLevelName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public String getRiskLevelName() {
		return riskLevelName;
	}

	/**
	 * @Title: setRiskLevelName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public void setRiskLevelName(final String riskLevelName) {
		this.riskLevelName = riskLevelName;
	}

	
	/**
	 * 获取属性realName的值
	 * @return realName属性值
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * 设置属性realName的值
	 * @param  realName属性值
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "UserLog [" + "uuid=" + uuid + ", userId=" + userId
				+ ", papersId=" + papersId + ", score=" + score
				+ ", riskLevel=" + riskLevel + ", createTime=" + createTime
				+ ", status=" + status + ", riskAnswers=" + riskAnswers + "]";
	}
}
