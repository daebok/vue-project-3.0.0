package com.rd.ifaes.core.risk.domain;

import java.util.Date;

import com.rd.ifaes.common.dict.RiskEnum;
import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;

/**
 * entity:Answer
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public class Answer extends BaseEntity<Answer> {

	private static final long serialVersionUID = 1L;
	/** 关联问题表ID */
	private String questionId;
	/** 答案编号 */
	private String answerNo;
	/** 答案内容 */
	private String content;
	/** 答案分值 */
	private Integer score;
	/** 排序 ABCD */
	private String sort;
	/** 创建时间 */
	private Date createTime;
	/** 逻辑删除 */
	private Integer deleteFlag;

	// 其他自定义属性
	/** 试卷名称 */
	private String papersName;
	/** 问题名称 */
	private String questionName;
	/**是否被选择*/
	private int isSelected;
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
	 * @Title: getQuestionName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public String getQuestionName() {
		return questionName;
	}

	/**
	 * @Title: setQuestionName <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public void setQuestionName(final String questionName) {
		this.questionName = questionName;
	}

	/**
	 * Constructor
	 */
	public Answer() {
		super();
	}

	/**
	 * full Constructor
	 */
	public Answer(final String uuid, final String questionId,
			final String answerNo, final String content, final Integer score,
			final String sort, final Date createTime, final int deleteFlag) {
		super();
		setUuid(uuid);
		this.questionId = questionId;
		this.answerNo = answerNo;
		this.content = content;
		this.score = score;
		this.sort = sort;
		this.createTime = createTime;
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * @param questionId
	 */
	public void setQuestionId(final String questionId) {
		this.questionId = questionId;
	}

	/**
	 * @return
	 */
	public String getAnswerNo() {
		return answerNo;
	}

	/**
	 * @param answerNo
	 */
	public void setAnswerNo(final String answerNo) {
		this.answerNo = answerNo;
	}

	/**
	 * @return
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 */
	public void setContent(final String content) {
		this.content = content;
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
	 * @Title: getSort <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public String getSort() {
		return sort;
	}

	/**
	 * @Title: setSort <BR>
	 * @Description: please write your description <BR>
	 * @return: String <BR>
	 */
	public void setSort(final String sort) {
		this.sort = sort;
	}

	/**
	 * 获取属性isSelected的值
	 * @return isSelected属性值
	 */
	public int getIsSelected() {
		return isSelected;
	}

	/**
	 * 设置属性isSelected的值
	 * @param  isSelected属性值
	 */
	public void setIsSelected(final int isSelected) {
		this.isSelected = isSelected;
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
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "Answer [" + "uuid=" + uuid + ", questionId=" + questionId
				+ ", answerNo=" + answerNo + ", content=" + content
				+ ", score=" + score + ", sort=" + sort + ", createTime="
				+ createTime + "]";
	}

	/**
	 * <p>
	 * Title: preInsert
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @see com.rd.ifaes.common.entity.BaseEntity#preInsert()
	 */
	@Override
	public void preInsert() {
		this.deleteFlag = Integer.parseInt(RiskEnum.CONFIG_IS_NOT_DELETE_FLAG
				.getValue());
		this.createTime = DateUtils.getNow();
		super.preInsert();
	}

}
