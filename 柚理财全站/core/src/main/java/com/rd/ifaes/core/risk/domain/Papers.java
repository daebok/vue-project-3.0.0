package com.rd.ifaes.core.risk.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:Papers
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public class Papers extends BaseEntity<Papers> {

	private static final long serialVersionUID = 1L;
	/** 问卷编号 */
	private String papersNo;
	/** 问卷名称 */
	private String papersName;
	/** 试卷类型（2，答题，1：其他） */
	private Integer papersType;
	/** 问题数量 */
	private Integer questionCount;
	/** 创建时间 */
	private Date createTime;
	/** 问卷状态值（1:发布中；2：未发布） */
	private Integer status;
	/** 逻辑删除 */
	private Integer deleteFlag;
	// 其他自定义属性
	/** 答题类型集合 */
	private String[] papersTypeSet;
	/** 状态集合 */
	private String[] statusSet;//
	/** 试卷总分 */
	private int totalScore;//
	/** 问题集合 */
	private List<Question> questions = new ArrayList<Question>();
	/**是否可以被预览*/
	private String canSearch;
	/**
	 * Constructor
	 */
	public Papers() {
		super();
	}

	/**
	 * Constructor
	 */
	public Papers(final String uuid) {
		super();
		this.uuid = uuid;
	}

	/**
	 * full Constructor
	 */
	public Papers(final String uuid, final String papersNo,
			final String papersName, final Integer papersType,
			final Integer questionCount, final Date createTime,
			final Integer status, final Integer deleteFlag) {
		super();
		setUuid(uuid);
		this.papersNo = papersNo;
		this.papersName = papersName;
		this.papersType = papersType;
		this.questionCount = questionCount;
		this.createTime = createTime;
		this.status = status;
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return questions
	 */
	public List<Question> getQuestions() {
		return questions;
	}

	/**
	 * @param questions
	 */
	public void setQuestions(final List<Question> questions) {
		this.questions = questions;
	}

	/**
	 * totalScore.
	 * 
	 * @return the totalScore
	 */
	public int getTotalScore() {
		return totalScore;
	}

	/**
	 * totalScore.
	 * 
	 * @param totalScore
	 *            the totalScore to set
	 */
	public void setTotalScore(final int totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * @return
	 */
	public String getPapersNo() {
		return papersNo;
	}

	/**
	 * @param papersNo
	 */
	public void setPapersNo(final String papersNo) {
		this.papersNo = papersNo;
	}

	/**
	 * @return
	 */
	public String getPapersName() {
		return papersName;
	}

	/**
	 * @param papersName
	 */
	public void setPapersName(final String papersName) {
		this.papersName = papersName;
	}

	/**
	 * @return
	 */
	public Integer getPapersType() {
		return papersType;
	}

	/**
	 * @param papersType
	 */
	public void setPapersType(final Integer papersType) {
		this.papersType = papersType;
	}

	/**
	 * @return
	 */
	public Integer getQuestionCount() {
		return questionCount;
	}

	/**
	 * @param questionCount
	 */
	public void setQuestionCount(final Integer questionCount) {
		this.questionCount = questionCount;
	}

	/**
	 * @return
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param status
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
	 * @Title: getDeleteFlag <BR>
	 * @Description: please write your description <BR>
	 * @return: Integer <BR>
	 */
	public Integer getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @Title: setDeleteFlag <BR>
	 * @Description: please write your description <BR>
	 * @return: Integer <BR>
	 */
	public void setDeleteFlag(final Integer deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @Title: getPapersTypeSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public String[] getPapersTypeSet() {
		return papersTypeSet;
	}

	/**
	 * @Title: setPapersTypeSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public void setPapersTypeSet(final String[] papersTypeSet) {
		this.papersTypeSet = papersTypeSet;
	}

	/**
	 * @Title: getStatusSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public String[] getStatusSet() {
		return statusSet;
	}

	/**
	 * @Title: setStatusSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public void setStatusSet(final String[] statusSet) {
		this.statusSet = statusSet;
	}
	
	/**
	 * 获取属性canSearch的值
	 * @return canSearch属性值
	 */
	public String getCanSearch() {
		return canSearch;
	}

	/**
	 * 设置属性canSearch的值
	 * @param  canSearch属性值
	 */
	public void setCanSearch(String canSearch) {
		this.canSearch = canSearch;
	}

	/**
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "Papers [" + "uuid=" + uuid + ", papersNo=" + papersNo
				+ ", papersName=" + papersName + ", papersType=" + papersType
				+ ", questionCount=" + questionCount + ", createTime="
				+ createTime + ", status=" + status + "]";
	}

	/**
	 * 重写preInsert
	 */
	@Override
	public void preInsert() {
		this.deleteFlag = Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG
				.getValue());
		this.status = Integer.parseInt(RiskEnum.PAPERS_NO_PUBLISH.getValue());
		this.createTime = DateUtils.getNow();
		super.preInsert();
	}

}
