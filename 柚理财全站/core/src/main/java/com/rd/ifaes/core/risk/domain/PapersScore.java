package com.rd.ifaes.core.risk.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:PapersScore
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public class PapersScore extends BaseEntity<PapersScore> {
	
	private static final long serialVersionUID = 1L;
	/** 关联试卷UUID */ 
	private String	papersId;		 
	/** 关联风险等级UUID */ 
	private String	riskId;		 
	 /** 起始分数0值 */ 
	private Integer	startScore;		
	/**截止分数值 */ 
	private Integer	endScore;		
	 /** 创建时间 */ 
	private Date	createTime;		
	//其他自定义属性
	/**关联的试卷名称*/
	private String papersName;
	/**关联的用户风险承受能力类型*/
	private String userRiskName;//
	/**
	 *  Constructor
	 */
	public PapersScore() {
		super();
	}

	/**
	 * full Constructor
	 */
	public PapersScore(final String uuid, final String papersId, final String riskId,final  Integer startScore, final Integer endScore, final Date createTime) {
		super();
		setUuid(uuid);
		this.papersId = papersId;
		this.riskId = riskId;
		this.startScore = startScore;
		this.endScore = endScore;
		this.createTime = createTime;
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
	public String getRiskId() {
		return riskId;
	}
	/**
	 * @param riskId
	 */
	public void setRiskId(final String riskId) {
		this.riskId = riskId;
	}
	/**
	 * @return
	 */
	public Integer getStartScore() {
		return startScore;
	}
	/**
	 * @param startScore
	 */
	public void setStartScore(final Integer startScore) {
		this.startScore = startScore;
	}
	/**
	 * @return
	 */
	public Integer getEndScore() {
		return endScore;
	}

	/**
	 * @param endScore
	 */
	public void setEndScore(final Integer endScore) {
		this.endScore = endScore;
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
	 * @Title:  getPapersName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getPapersName() {
		return papersName;
	}

	/**  
	 * @Title:  setPapersName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setPapersName(final String papersName) {
		this.papersName = papersName;
	}

	/**  
	 * @Title:  getUserRiskName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public String getUserRiskName() {
		return userRiskName;
	}

	/**  
	 * @Title:  setUserRiskName <BR>  
	 * @Description: please write your description <BR>  
	 * @return: String <BR>  
	 */
	public void setUserRiskName(final String userRiskName) {
		this.userRiskName = userRiskName;
	}
	
	/**
	 * 重写tostring
	 */
	@Override	
	public String toString() {
		return "PapersScore [" + "uuid=" + uuid + ", papersId=" + papersId + ", riskId=" + riskId + ", startScore=" + startScore + ", endScore=" + endScore + ", createTime=" + createTime +  "]";
	}

	/**   
	 * <p>Title: preInsert</p>   
	 * <p>Description: </p>      
	 * @see com.rd.ifaes.common.entity.BaseEntity#preInsert()   
	 */
	@Override
	public void preInsert() {
		this.createTime = DateUtils.getNow();
		super.preInsert();
	}
	
	
}
