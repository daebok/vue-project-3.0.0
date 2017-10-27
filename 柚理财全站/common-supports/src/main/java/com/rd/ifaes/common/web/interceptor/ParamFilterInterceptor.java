package com.rd.ifaes.common.web.interceptor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.blogspot.radialmind.html.HTMLParser;
import com.rd.ifaes.blogspot.radialmind.xss.XSSFilter;
import com.rd.ifaes.common.util.HtmlUtils;
import com.rd.ifaes.common.util.StringUtils;
/**
 * 参数拦截Interceptor
 * @version 3.0
 * @since 2016-04-25
 */
public class ParamFilterInterceptor extends BaseInterceptor {

	/** 日志 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ParamFilterInterceptor.class);
	/** XSSFilter */
	private static final XSSFilter XSSFILTER = new XSSFilter();
	/**SQL正则表达式 */
	private static final String SQL_REG = "<\\s*SCRIPT\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)|\\bON.+\\b\\s*=|\\bJAVASCRIPT\\s*:\\b";

	/**SQL正则匹配 */
	private static final Pattern SQL_PATTERN = Pattern.compile(SQL_REG, Pattern.CASE_INSENSITIVE);   
	
	private static String[] pwd_white_list_name={"Pwd","loginPwd","newLoginPwd","confirmPassword","password","confirmPwd"};
	private static String[] pwd_white_list_path={ "/project/checkPwd.html","/user/registerFirst.html","/member/security/doModifyPwd.html","/user/retrievepwd/confirm.html","/user/doLogin.html"};
	

	/**
	 * 任何请求的参数 都要经过安全级别的过滤
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) throws Exception {
		@SuppressWarnings("rawtypes")
		Enumeration names = request.getParameterNames();
		String url = request.getRequestURI();
		if (url.contains("/jxbank/")) {
			return true;
		}
		// 默认不包含有
		boolean hasDeniedText = false;
		// 默认不包含有
		boolean hasDeniedXss = false;
		// 默认不包含有
		boolean hasDeniedHtml = false;
		String illegalParam = "";
		String pathUrl = request.getServletPath();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] paramValArr = request.getParameterValues(name);
			for (String paramVal : paramValArr) {
				illegalParam = paramVal;
				// 先对请求进行转码，然后转成大写
				String needCheckParams = StringUtils.isNull(HtmlUtils.htmlUnescape(paramVal));
				//密码白名单特殊处理
				if (isPwdWhiteList(StringUtils.isNull(name), StringUtils.isNull(pathUrl))) {
					break;
				}
				if (hasDeniedText =isAttack(needCheckParams) ) {
					break;
				}
				// 非编辑器额外处理
				if (!isEdit(StringUtils.isNull(name), StringUtils.isNull(pathUrl)) && ((hasDeniedXss=fliterXSS(StringUtils.isNull(paramVal))) || (hasDeniedHtml = filterHtml(paramVal)) )) {
						break;
			    }
			 }
			// 文本校验、XSS校验
			if (hasDeniedText ||  hasDeniedXss || hasDeniedHtml) {
				break;
			}
		}
		
		if(hasDeniedText ||  hasDeniedXss || hasDeniedHtml){
			LOGGER.info("存在非法字符:{}" , illegalParam);
			//避免脚本标签转义
			super.message("存在非法字符:" + illegalParam.replaceAll("<", "<&nbsp;").replaceAll(">", "&nbsp;>"));
			return false;

		}
		return true;
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}


	/**
	 * 校验是否为编辑器
	 * @param name
	 * @param path
	 * @return
	 */
	public boolean isEdit(String name,String path){
		if(name.equals("content") || name.equals("description")){
			return true;
		}
		//资金记录模板编辑
		if(name.equals("templateContent")&& (path.equals("/sys/logTemplate/logTemplateEdit.html") 
				|| path.equals("/sys/logTemplate/logTemplateAdd.html") )){
			return true;
		}
		//消息模板编辑
		if(name.equals("messageContent")&& (path.equals("/sys/messageType/messageTypeEdit.html") 
				|| path.equals("/sys/messageType/messageTypeAdd.html") )){
			return true;
		}
		//特殊处理：系统菜单新增、编辑时校验图标样式（iconCss）的特殊字符 add xxb20160927 begin
		if (StringUtils.equals("iconCss", name) && (StringUtils.equals(path, "/sys/menu/menuAdd.html")
				|| (StringUtils.equals(path, "/sys/menu/menuEdit.html")))) {
			return true;
		}
		//end
		return false;
	}
	
	/**
	 * 过滤html标签
	 * @param text
	 * @return
	 */
	public boolean filterHtml(String text){
		if(StringUtils.isBlank(text)){
			return false;
		}
		return !text.equals(HtmlUtils.filterHtmlTags(text));
	}
	
	/**
	 * 检查是否已经存在xss的字符
	 * 
	 * @param text 字符串
	 * @return 存在返回true
	 */
	public boolean fliterXSS(String text) {
		if(StringUtils.isBlank(text)){
			return false;
		}
		StringBuffer sb = new StringBuffer("<html>").append(text).append("</html>");
		StringReader reader = new StringReader(sb.toString());
		StringWriter writer = new StringWriter();

		StringReader noReader = new StringReader(sb.toString());
		StringWriter noWriter = new StringWriter();
		try {
			HTMLParser.process(reader, writer, XSSFILTER, true);
			HTMLParser.process(noReader, noWriter, null, true);
		} catch (Exception e) {
			//logger.error("检查是否已经存在xss的字符存在异常",e);
			return false;
		}

		String filterStr = writer.toString();
		String noFilterStr = noWriter.toString();
		return !filterStr.equals(noFilterStr);
	}

	/**
	 * 返回true就是包含非法字符，返回false就是不包含非法字符 
	 * 系统内容过滤规则
	 * **1、包含  『 and 1 特殊字符 』， 特殊字符指>,<,=, in , like 字符 
	 * **2、『 /特殊字符/ 』，特殊字符指 *字符
	 * 3、『<特殊字符 script 』特殊字符指空字符
	 * 4、『 EXEC 』
	 * 5、『 UNION SELECT』
	 * 5、『 UPDATE SET』
	 * 5、『 INSERT INTO VALUES』
	 * 5、『 SELECT或DELETE FROM』
	 * 5、『CREATE或ALTER或DROP或TRUNCATE TABLE或DATABASE』
	 */
	public static boolean isAttack(String input) {
		boolean result = false;
		if (SQL_PATTERN.matcher(input).find()) {
			result = true;
		}
		return result;
	}
	
	public boolean isPwdWhiteList(String name,String path){
		boolean nameResult=false;
		boolean pathResult=false;
		for(int j=0;j<pwd_white_list_name.length;j++){
			String pwdWhitelistName=StringUtils.isNull(pwd_white_list_name[j]);
			if(StringUtils.isNull(name).equals(pwdWhitelistName)){
				nameResult = true;
				break;
			}
		}
		for(int j=0;j<pwd_white_list_path.length;j++){
			String pwdWhitelistPath=StringUtils.isNull(pwd_white_list_path[j]);
			if(StringUtils.isNull(path).equals(pwdWhitelistPath)){
				pathResult = true;
				break;
			}
		}
		return nameResult && pathResult;
	}
	
}
