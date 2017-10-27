package com.rd.ifaes.core.risk.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.resource.RiskResource;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.OrderNoUtils;
import com.rd.ifaes.core.core.util.ResourceUtils;

/**
 * entity:Question
 * 
 * @author QianPengZhan
 * @version 3.0
 * @date 2016-7-12
 */
public class Question extends BaseEntity<Question> {

	private static final long serialVersionUID = 1L;
	/** 关联问卷表uuid */
	private String papersId;
	/** 问卷问题编号 */
	private String questionNo;
	/** 问卷问题内容 */
	private String questionName;
	/** 问题序号 */
	private Integer sort;
	/** 问题分值 */
	private Integer score;
	/** 是否单选（1，单选；2，多选，默认单选） */
	private Integer isSingle;
	/** 创建时间 */
	private Date createTime;
	/** 逻辑删除 */
	private Integer deleteFlag;
	// 其他自定义属性
	/** 试卷名称 */
	private String papersName;
	/** 单选集合 */
	private String[] isSingleSet;
	/** 答案集合 */
	private List<Answer> list;
	/**答案集合序号*/
	@SuppressWarnings("rawtypes")
	private Set set = new HashSet();
	private String[][] answerContent;
	private String[][] answerContentAdd;
	/**
	 * Constructor
	 */
	public Question() {
		super();
	}

	/**
	 * full Constructor
	 */
	public Question(final String uuid, final String papersId,
			final String questionNo, final String questionName,
			final Integer sort, final Integer score, final Integer isSingle,
			final Date createTime, final Integer deleteFlag) {
		super();
		setUuid(uuid);
		this.papersId = papersId;
		this.questionNo = questionNo;
		this.questionName = questionName;
		this.sort = sort;
		this.score = score;
		this.isSingle = isSingle;
		this.createTime = createTime;
		this.deleteFlag = deleteFlag;
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
	public String getQuestionNo() {
		return questionNo;
	}

	/**
	 * @param questionNo
	 */
	public void setQuestionNo(final String questionNo) {
		this.questionNo = questionNo;
	}

	/**
	 * @return
	 */
	public String getQuestionName() {
		return questionName;
	}

	/**
	 * @param questionName
	 */
	public void setQuestionName(final String questionName) {
		this.questionName = questionName;
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
	public Integer getIsSingle() {
		return isSingle;
	}

	/**
	 * @param isSingle
	 */
	public void setIsSingle(final Integer isSingle) {
		this.isSingle = isSingle;
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
	 * list.
	 * 
	 * @return the list
	 */
	public List<Answer> getList() {
		return list;
	}

	/**
	 * list.
	 * 
	 * @param list
	 *            the list to set
	 */
	public void setList(final List<Answer> list) {
		this.list = list;
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
	 * @Title: getIsSingleSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public String[] getIsSingleSet() {
		return isSingleSet;
	}

	/**
	 * @Title: setIsSingleSet <BR>
	 * @Description: please write your description <BR>
	 * @return: String[] <BR>
	 */
	public void setIsSingleSet(final String[] isSingleSet) {
		this.isSingleSet = isSingleSet;
	}

	/**
	 * 获取属性set的值
	 * @return set属性值
	 */
	@SuppressWarnings("rawtypes")
	public Set getSet() {
		return set;
	}

	/**
	 * 设置属性set的值
	 * @param  set属性值
	 */
	@SuppressWarnings("rawtypes")
	public void setSet(Set set) {
		this.set = set;
	}

	/**
	 * 获取属性answerContent的值
	 * @return answerContent属性值
	 */
	public String[][] getAnswerContent() {
		return answerContent;
	}

	/**
	 * 设置属性answerContent的值
	 * @param  answerContent属性值
	 */
	public void setAnswerContent(String[][] answerContent) {
		this.answerContent = answerContent;
	}

	/**
	 * 获取属性answerContentAdd的值
	 * @return answerContentAdd属性值
	 */
	public String[][] getAnswerContentAdd() {
		return answerContentAdd;
	}

	/**
	 * 设置属性answerContentAdd的值
	 * @param  answerContentAdd属性值
	 */
	public void setAnswerContentAdd(String[][] answerContentAdd) {
		this.answerContentAdd = answerContentAdd;
	}

	/**
	 * 重写toString
	 */
	@Override
	public String toString() {
		return "Question [" + "uuid=" + uuid + ", papersId=" + papersId
				+ ", questionNo=" + questionNo + ", questionName="
				+ questionName + ", sort=" + sort + ", score=" + score
				+ ", isSingle=" + isSingle + ", createTime=" + createTime + "]";
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
		this.createTime = DateUtils.getNow();
		this.deleteFlag = Constant.INT_ZERO;
		this.questionNo = OrderNoUtils.getSerialNumber();//问题编号
		super.preInsert();
	}

	/**
	 * 新增、修改试题基础校验
	 */
	public void validQuestion(){
		// 问卷问题内容
		if(StringUtils.isBlank(this.questionName) || this.questionName.length() > Constant.ENTITY_LENGTH_SIX){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(RiskResource.RISK_QUESTION_NAME_VALID), Constant.ENTITY_LENGTH_SIX));
		}
		// 问题序号检查
		if(this.sort == null || this.sort < Constant.INT_ZERO || this.sort > Constant.INT_ONE_THOUSAND){
			throw new BussinessException(StringUtils.format(ResourceUtils.get(RiskResource.RISK_QUESTION_SORT_VALID), Constant.INT_ONE_THOUSAND));
		}
	}
}
