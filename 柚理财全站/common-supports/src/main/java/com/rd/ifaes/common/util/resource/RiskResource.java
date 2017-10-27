/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.common.util.resource;

/**
 * 风险评估资源类
 * @version 3.0
 * @author QianPengZhan
 * @date 2016年9月14日
 */
public class RiskResource {
	/**
	 * 已启用试题不允许删除
	 */
	public static final String RISK_QUESTION_ON_NOT_DELETE = "risk.message.question.on.not.delete";
	/**
	 * 已启用问卷不允许删除
	 */
	public static final String RISK_PAPERS_ON_NOT_DELETE = "risk.message.papers.on.not.delete";
	/**
	 * 已有对应风险等级的产品，不允许删除
	 */
	public static final String RISK_CONFIG_IN_PROJECT_IS_YES = "risk.message.config.in.project.is.yes";
	/**
	 * 已有对应级别的风险等级
	 */
	public static final String RISK_CONFIG_RISKVAL_ISEXISTS = "risk.message.config.riskVal.is.exists";
	/**
	 * 答案不存在
	 */
	public static final String RISK_ANSWER_IS_NULL = "risk.message.answer.is.null";
	/**
	 * 试题不存在
	 */
	public static final String RISK_QUESTION_IS_NULL = "risk.message.question.is.null";
	/**
	 * 试卷不存在
	 */
	public static final String RISK_PAPERS_IS_NULL = "risk.message.papers.is.null";
	/**
	 * "请添加试题!"
	 */
	public static final String RISK_QUESTION_IS_WAIT_ADD = "risk.messgae.qestion.is.wait.add";

	/**
	 * 当前已有启用的问卷
	 */
	public static final String RISK_PAPERS_PUBLISH_AFTER_CANCLE = "risk.messgae.papers.publish.after.cancle";

	/**
	 * "发布只能同时发布一个"
	 */
	public static final String RISK_PAPERS_PUBLISH_IS_ONLY = "risk.messgae.papers.publish.is.only";

	/**
	 * "请勾选需要发布的记录"
	 */
	public static final String RISK_PAPERS_PUBLISH_ID_NULL = "risk.messgae.papers.publish.id.null";
	/**
	 * "修改的分数参数异常"
	 */
	public static final String RISK_CONFIG_PAPERS_SCORE_ERROR = "risk.messgae.papers.score.error";
	/**
	 * 修改失败
	 */
	public static final String RISK_CONFIG_PAPERS_UPDATE_FAIL = "risk.messgae.papers.update.fail";
	/**
	 * "最低分大于最高分"
	 */
	public static final String RISK_CONFIG_MIN_UP_MAX = "risk.messgae.config.min.up.max";
	/**
	 * "最低分需高于上一级最高分"
	 */
	public static final String RISK_CONFIG_NEXT_MIN_UP_MAX = "risk.messgae.config.next.min.up.max";
	/**
	 * "请录入最低分值和最高分值,不能为空"
	 */
	public static final String RISK_CONFIG_MAX_MIN_SCORE_EMPTY = "risk.messgae.config.max.min.score.empty";
	/**
	 * "添加失败"
	 */
	public static final String RISK_PAPER_CONFIG_ADD_FAIL = "risk.messgae.papers.config.add.fail";
	/**
	 * "答案内容不能为空"
	 */
	public static final String RISK_PAPER_ANSWER_IS_EMPTY = "risk.messgae.papers.answer.empty";
	/**
	 * 试卷未发布
	 */
	public static final String RISK_PAPER_NOT_PUBLISH = "risk.messgae.papers.not.publish";
	/**
	 * "试卷已有等级,请点击编辑!"
	 */
	public static final  String  RISK_PAPERS_LEVEL_IS_EXISTS = "risk.messgae.papers.level.is.exists";
	/**
	 * 答案的序号已经存在,请重新选择!
	 */
	public static final  String  RISK_ANSWER_SORT_IS_EXISTS = "risk.messgae.answer.sort.is.exists";
	/**
	 * 请勾选需要删除的记录
	 */
	public static final String RISK_SELECTED_DELETE_ERROR  = "risk.messgae.common.selected.delete.error";
	/**
	 * 该试卷设定等级已经完成,请勿再次设定
	 */
	public static final String RISK_LEVEL_ALREADY_SET_ERROR  = "risk.messgae.level.already.set.error";
	/**
	 * 试卷标识不能为空
	 */
	public static final String RISK_PAPERS_ID_IS_NULL = "risk.message.papers.id.is.null";
	
	/**
	 * 试卷标识不能为空
	 */
	public static final String RISK_PAPERS_ID_IS_EMPTY = "risk.message.papers.id.is.empty";
	/**
	 * 问题内容不能为空，并且不能超过{0}个字符！
	 */
	public static final String RISK_QUESTION_NAME_VALID = "risk.question.name.valid";
	/**
	 * 问题序号必须大于等于零，并且小于{0}！
	 */
	public static final String RISK_QUESTION_SORT_VALID = "risk.question.sort.valid";
	/**
	 * 问题选项列必须大于等于{0}个！
	 */
	public static final String RISK_QUESTION_ANSWERLIST_VALID = "risk.question.answerList.valid";
}
