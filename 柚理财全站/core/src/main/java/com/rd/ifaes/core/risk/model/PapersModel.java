/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.risk.model;

/**
 * 答卷Model类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月10日
 */
public class PapersModel {
	/**试卷ID*/
	private String papersId;
	/** 答题内容*/
	private String questionContent;
	/**
	 * 获取属性papersId的值
	 * @return papersId属性值
	 */
	public String getPapersId() {
		return papersId;
	}
	/**
	 * 设置属性papersId的值
	 * @param  papersId属性值
	 */
	public void setPapersId(final String papersId) {
		this.papersId = papersId;
	}
	/**
	 * 获取属性questionContent的值
	 * @return questionContent属性值
	 */
	public String getQuestionContent() {
		return questionContent;
	}
	/**
	 * 设置属性questionContent的值
	 * @param  questionContent属性值
	 */
	public void setQuestionContent(final String questionContent) {
		this.questionContent = questionContent;
	}

	

	
}
