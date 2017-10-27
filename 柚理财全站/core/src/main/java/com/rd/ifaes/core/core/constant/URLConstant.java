/**
* 互联网金融交易系统ifaes (Internet Finance  Assets  Exchange  System)
 * Copyright (c) 2016 杭州融都科技股份有限公司 版权所有
 */
package com.rd.ifaes.core.core.constant;

/**
 *  系统使用到的URL地址
 * @version 3.0
 * @author FangJun
 * @date 2016年10月19日
 */
public final class URLConstant {
	
	/**
	 *  投资详情页URL地址-MVC使用
	 */
	public static final String  INVEST_DETAIL_PAGE_MVC= "/invest/detail";
	/**
	 *  变现详情页URL地址-MVC使用
	 */
	public static final String  REALIZE_DETAIL_PAGE_MVC= "/realize/detail";
	/**
	 *  投资详情页URL地址-前缀，需拼接项目ID
	 */
	public static final String  INVEST_DETAIL_PAGE_PREFIX=INVEST_DETAIL_PAGE_MVC+".html?id=";
	/**
	 *  变现详情页URL地址-前缀，需拼接项目ID
	 */
	public static final String  REALIZE_DETAIL_PAGE_PREFIX=REALIZE_DETAIL_PAGE_MVC+".html?id=";
    /**
     * 理财频道首页
     */
	public static final String  INVEST_HOME_PAGE="/invest/index.html";
    /**
     * 变现通频道首页
     */
	public static final String  REALIZE_HOME_PAGE="/realize/index.html";
	/**
     * 我的投资页面
     */
	public static final String  MEMBER_MYINVEST_PAGE = "/member/myInvest/list.html?tab=2";
	/**
	 * 债权详情页面URL地址 -MVC使用
	 */
	public static final String BOND_DETAIL_PAGE_MVC = "/bond/detail";
	/**
	 *  债权详情页URL地址-前缀，需拼接项目ID
	 */
	public static final String  BOND_DETAIL_PAGE_PREFIX = BOND_DETAIL_PAGE_MVC+".html?id=";
	/**
     * 转让专区首页
     */
	public static final String  BOND_HOME_PAGE = "/bond/index.html";
	/**
	 * 进入转让设置页面
	 */
	public static final String BOND_SET_PAGE = "/member/myBond/bondSetList.html?id=";
	
	/**
	 * 可转让列表页面
	 */
	public static final String BOND_LIST_ABLE = "/member/myBond/list.html?tab=1";

	/**
	 * 转让中列表页面
	 */
	public static final String BOND_LIST_SELLING = "/member/myBond/list.html?tab=2";
	
	/**
	 * 已受让列表页面
	 */
	public static final String BOND_LIST_BOUGHT = "/member/myBond/list.html?tab=4";
	
	/**
	 * return  到登录页面
	 */
	public static final String LOGIN_HTML = "/user/login";
	
	/**
	 * 
	 */
	public static final String RECHARGE_DETAIL= "/member/recharge/detail.html";
	public static final String SECURITY_REALNAMEIDENTIFY = "/member/security/realNameIdentify.html";
	public static final String SECURITY_AUTHSIGN = "/member/security/bindAuthorization.html";
	public static final String RISK_USERRISKPAPERS = "/member/risk/userRiskPapers.html";
	/**
	 * 借款预约
	 */
	public static final String BORROW_BESPEAK="/borrow/bespeak.html";
}
