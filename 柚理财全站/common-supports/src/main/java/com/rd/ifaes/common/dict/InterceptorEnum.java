package com.rd.ifaes.common.dict;

/**
 * 拦截器枚举类
 * @version 3.0
 * @author lh
 * @date 2016年8月2日
 */
public enum InterceptorEnum implements BaseEnum<String, String> {

	/**
	 * 后台登录地址
	 */
	LOGIN_URL_MANAGE("后台登录地址", "/admin/login.html"),
	
	/**
	 * 后台登出地址
	 */
	LOGOUT_URL_MANAGE("后台登出地址", "/admin/logout.html"),
	/**
	 * 后台登录验证码地址
	 */
	LOGIN_VALID_IMG_URL_MANAGE("后台登出地址", "/admin/validImg.html"),
	/**
	 * 后台登录验证码校验地址
	 */
	LOGIN_VALID_CODE_URL_MANAGE("后台登出地址", "/admin/validLogonCode.html"),
	
	/**
	 * 前台-用户登录和注册
	 */
	LOGIN_URL_WEB("前台用户登录和注册", "/user/login.html"),
	
	/**
	 * 前台-用户登录和注册
	 */
	LOGIN_URL_PWD_RESET("前台用户修改密码", "/user/pwdReset.html"),
	LOGIN_URL_DO_MODIFY_PWD("前台用户确认修改密码", "/member/security/doModifyPwd.html"),
	LOGIN_URL_RETRIEVEPWD("前台用户确认密码", "/user/retrievepwd/confirm.html"),
	
	/**
	 * 前台-用户登录和注册
	 */
	LOGIN_URL_WEB_USER("前台用户登录和注册", "/user/"),
	
	/**
	 * 前台-投资类
	 */
	LOGIN_URL_WEB_INVEST("前台-投资类", "/invest/"),
	
	/**
	 * 前台-首页
	 */
	LOGIN_URL_WEB_INDEX("前台-首页", "/index"),
	
	/**
	 * 前台-公共类
	 */
	LOGIN_URL_WEB_PUBLIC("前台-公共类", "/public/"),
	
	/**
	 * 前台-产品类
	 */
	LOGIN_URL_WEB_PROJECT("前台-产品类", "/project/"),
	
	/**
	 * 前台-文章栏目
	 */
	LOGIN_URL_WEB_COLUMN("前台-文章栏目", "/column/"),
	
	/**
	 * 前台-债权
	 */
	LOGIN_URL_WEB_BOND("前台-债权", "/bond/"),
	
	/**
	 * 前台-变现
	 */
	LOGIN_URL_WEB_REALIZE("前台-变现", "/realize/"),
	
	/**
	 * 前台-登录页面图形验证码显示
	 */
	LOGIN_URL_WEB_SHOW_CAPTCHA("前台-登录页面图形验证码显示", "/user/showCaptcha"),
	
	/**
	 * 前台-图形验证码
	 */
	LOGIN_URL_WEB_VALIDIMG("前台-图形验证码", "/validimg"),
	
	/**
	 * 前台-校验图形验证码
	 */
	LOGIN_URL_WEB_VALIDCODE("前台-校验图形验证码", "/valicode"),
	
	/**
	 * UFX第三方回调
	 */
	LOGIN_URL_WEB_UFX("前台-UFX第三方回调", "/ufx/"),
	
	/**
	 * CBHB第三方回调
	 */
	LOGIN_URL_WEB_CBHB("前台-渤海第三方回调", "/cbhb/"),
	/**
	 *  CBHB --APP 第三方回调
	 */
	LOGIN_URL_WEB_CBHB_APP("前台-渤海APP第三方回调", "/cbhbApp/"),
	
	/**
	 * 首页
	 */
	LOGIN_URL_WEB_HOME("前台-首页", "/"),
	
	/**
	 * 前台-借款
	 */
	LOGIN_URL_WEB_BORROW("前台-借款类", "/borrow/"),
	
	/**
	 * 前台-上传
	 */
	LOGIN_URL_WEB_UPLOAD("前台-图片上传类", "/upload/"),
	
	/**
	 * 报表导出
	 */
	SYS_EXPORT_PROGRESS("报表导出", "/sys/export/progress.html"),
	
	//****************************特例**************************************
	/**
	 * 前台债权支付
	 */
	LOGIN_URL_WEB_BOND_ORDER("前台债权支付", "/bond/order.html"),
	/**
	 * 前台预约借款
	 */
	LOGIN_URL_WEB_BORROW_BESPEAK("前台预约借款", "/borrow/bespeak.html"),
	/**
	 * 前台预约借款添加
	 */
	LOGIN_URL_WEB_BORROW_BESPEAK_ADD("前台预约借款添加", "/borrow/bespeakAdd.html"),
	/**
	 * 债权协议预览无需登录即可查看
	 */
	BOND_PROTOCOL_URL("债权协议预览无需登录即可查看","/member/myBond/getBondProtocolContent"),
	/**
	 * 借款协议预览无需登录即可查看
	 */
	PROTOCOL_URL("借款协议预览无需登录即可查看","/member/myInvest/protocolSearch"),
	/**
	 * 找回密码：获取动态验证码
	 */
	RETRIEVE_PASSWORD("找回密码获取动态验证码","/user/security/sendValidCode"),
	/**
	 * 计息计算器
	 */
	INCOME_CALCULATION("收益计算","/member/myAssistant/incomeCalculation"),
	/**
	 * 计息计算器
	 */
	CALCULATION_INCOME("计算收益","/member/myAssistant/calculateIncome"),
	/**
	 * 错误页面
	 */
	ERRORPAGE("错误页面","/WEB-INF/views/errorPage"),

	JXBANK_URL("江西银行接口","/jxbank/")
	
	;

	/**
	 *  字典项的中文名称
	 */
	private String name;
	/**
	 *  字典项的值
	 */
	private String value;

	private InterceptorEnum(final String name, final String value) {
		this.name = name;
		this.value = value;
	}

	/**
	 * 获取label
	 * 
	 * @return
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	@Override
	public String getValue() {
		return this.value;
	}

}
