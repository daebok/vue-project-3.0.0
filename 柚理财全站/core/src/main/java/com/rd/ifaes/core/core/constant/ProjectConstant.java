package com.rd.ifaes.core.core.constant;

/**
 * 项目常量类
 * @version 3.0
 * @author lh
 * @date 2016年8月12日
 */
public class ProjectConstant {
	
	private ProjectConstant() {
	}
	  
	/**
	 * 项目期限临最小分组值
	 */
	public static final String PROJECT_TIME_LIMIT_MIN = "1";
 
	/**
	 * 项目剩余可投金额
	 */
	public static final String KEY_PROJECT_REMAIN_ACCOUNT = "project:projectNo:%s:remainAccount";
	/**
	 * 当日项目编号
	 */
	public static final String KEY_TODAY_PROJECT_NO = "project:projectNo:today:%s";
	
    /**
     * 项目的定向密码长度
     */
	public static final int PROJECT_PASSWORD_LENGTH = 6;
	
	/**
	 * 项目名称最大长度
	 */
	public static final int PROJECT_PROJECTNAME_MAX_LENGTH = 25;
	/**
	 * 借款详情最大长度
	 */
	public static final int PROJECT_CONTENT_MAX_LENGTH = 2000;
	
	/**
	 * 募集期最小天数
	 */
	public static final int RAISETIMELIMIT_MINDAYS = 1;
	/**
	 * 募集期最大天数
	 */
	public static final int RAISETIMELIMIT_MAXDAYS = 10;
	
	/**
	 * 项目状态
	 */
	public static final String DICT_TYPE_PROJECTSTATUS = "projectStatus";
	
	/**
	 * 项目环节
	 */
	public static final String DICT_TYPE_PROCESS_NODE = "projectProcessNode";
	
	/**
	 * 环节:新增
	 */
	public static final String PROCESS_NODE_ADD = "add";
	/**
	 * 环节:修改
	 */
	public static final String PROCESS_NODE_EDIT = "edit";
	/**
	 * 环节:审核
	 */
	public static final String PROCESS_NODE_VERIFY = "verify";
	/**
	 * 环节:担保审核
	 */
	public static final String PROCESS_NODE_VOUCH_VERIFY = "vouchVerify";
	/**
	 * 环节:上架
	 */
	public static final String PROCESS_NODE_SALE = "sale";
	/**
	 * 环节:下架
	 */
	public static final String PROCESS_NODE_STOP = "stop";
	/**
	 * 环节:成立审核
	 */
	public static final String PROCESS_NODE_ESTABLISHVERIFY = "establishVerify";
	/**
	 * 默认类别名称
	 */
	public static final String DEFAULT_PROJECT_TYPE_NAME ="借款产品";
	/**
	 * 用户定向密码校验正确标识
	 */
	public static final String PROJECT_PASS_RIGHT_PROJECT_ID = "right_pwd_%s";
}
