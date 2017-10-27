package com.rd.ifaes.core.user.domain;

import java.util.Date;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.util.DateUtils;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * entity:UserSecurityAnswer
 * 
 * @author xhf
 * @version 3.0
 * @date 2016-7-28
 */
public class UserSecurityAnswer extends BaseEntity<UserSecurityAnswer> {
	
	private static final long serialVersionUID = 1L;
	/** 用户ID */ 
	private String	userId;		
	/** 问题标识（关联字典数据user_security_question） */ 
	private String	questionFlag;	
	/** 答案 */ 
	private String	answer;		
	/** 排序 */ 
	private Integer	sort;		 
	/** 添加时间 */ 
	private Date	createTime;	
	/** 添加IP */ 
	private String	addIp;		
	/** 删除标识 */ 
	private String	deleteFlag;		 

	//其他自定义属性
	
	/**
	 * 构造函数
	 */
	public UserSecurityAnswer() {
		super();
	}
	
	/**
	 * 构造函数
	 */
	public UserSecurityAnswer(final String userId, final String questionFlag, final String deleteFlag) {
		super();
		this.userId = userId;
		this.questionFlag = questionFlag;
		this.deleteFlag = deleteFlag;
	}
	
	/**
	 * 构造函数
	 */
	public UserSecurityAnswer(final String userId, final String addIp) {
		super();
		this.userId = userId;
		this.createTime = DateUtils.getNow();
		this.addIp = addIp;
		this.deleteFlag=Constant.FLAG_NO;
	}

	/**
	 * 获取用户ID
	 * @return userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置用户ID
	 * @param  userId
	 */
	public void setUserId(final String userId) {
		this.userId = userId;
	}

	/**
	 * 获取问题标识（关联字典数据user_security_question）
	 * @return questionFlag
	 */
	public String getQuestionFlag() {
		return questionFlag;
	}

	/**
	 * 设置问题标识（关联字典数据user_security_question）
	 * @param  questionFlag
	 */
	public void setQuestionFlag(final String questionFlag) {
		this.questionFlag = questionFlag;
	}

	/**
	 * 获取答案
	 * @return answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * 设置答案
	 * @param  answer
	 */
	public void setAnswer(final String answer) {
		this.answer = answer;
	}

	/**
	 * 获取排序
	 * @return sort
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * @param  sort
	 */
	public void setSort(final Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取添加时间
	 * @return createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置添加时间
	 * @param  createTime
	 */
	public void setCreateTime(final Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取添加IP
	 * @return addIp
	 */
	public String getAddIp() {
		return addIp;
	}

	/**
	 * 设置添加IP
	 * @param  addIp
	 */
	public void setAddIp(final String addIp) {
		this.addIp = addIp;
	}

	/**
	 * 获取删除标识
	 * @return deleteFlag
	 */
	public String getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * 设置删除标识
	 * @param  deleteFlag
	 */
	public void setDeleteFlag(final String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * 重写toString()方法
	 */
	@Override
	public String toString() {
		return "UserSecurityAnswer [" + "uuid=" + uuid + ", userId=" + userId + ", questionFlag=" + questionFlag + ", answer=" + answer + ", sort=" + sort + ", createTime=" + createTime + ", addIp=" + addIp + ", deleteFlag=" + deleteFlag +  "]";
	}
	
}
