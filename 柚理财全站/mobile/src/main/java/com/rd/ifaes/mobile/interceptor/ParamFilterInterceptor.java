package com.rd.ifaes.mobile.interceptor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.blogspot.radialmind.html.HTMLParser;
import com.rd.ifaes.blogspot.radialmind.html.HandlingException;
import com.rd.ifaes.blogspot.radialmind.xss.XSSFilter;
import com.rd.ifaes.common.util.EncodeUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
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
	private static String[] denied_texts={"'",";","insert","declare","drop","create",
		"alter","rename","join","where","like","cast","script","iframe","%27",
		"%3b","%22","</","<",">","grant","%3c","%3e","unicode","alert","confirm","\"","“","”","‘","’"};
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
		String illegalParam = "";
		String pathUrl = request.getServletPath();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String[] values = request.getParameterValues(name);
			for (int i = 0; i < values.length; i++) {
				illegalParam = values[i];
				// 先对请求进行转码，然后转成大写
				String needCheckParams = StringUtils.isNull(EncodeUtils.urlDecode(values[i])).toUpperCase();
				// 编辑器特殊处理
				if (isEdit(StringUtils.isNull(name), StringUtils.isNull(pathUrl))) {
					hasDeniedText = isAttack(needCheckParams);
					fliterXSSForContent(StringUtils.isNull(values[i]));
				} else {
					hasDeniedText = isDeniedText(needCheckParams);
					hasDeniedXss = fliterXSS(StringUtils.isNull(values[i]));
				}
				// 文本校验
				if (hasDeniedText) {
					break;
				}
				// XSS校验
				if (hasDeniedXss) {
					break;
				}
			}
			// 文本校验
			if (hasDeniedText) {
				break;
			}
			// XSS校验
			if (hasDeniedXss) {
				break;
			}
		}
		// 文本过滤
		if (hasDeniedText) {
			LOGGER.info("存在非法字符:" + illegalParam);
			super.message("存在非法字符:" + illegalParam);
			return false;
		}
		// xss过滤
		if (hasDeniedXss) {
			if(LOGGER.isInfoEnabled()){
				LOGGER.info("存在非法字符:" + illegalParam);
			}
			super.message("存在非法字符:" + illegalParam);
			return false;
		}
		return true;
	}
	/**
	 * 校验是否为编辑器
	 * @param name
	 * @param path
	 * @return
	 */
	public boolean isEdit(String name,String path){
		if(name.equals("content") || name.equals("safeguard")||name.equals("templet") || name.equals("RespExt")){
			return true;
		}
		if(name.equals("description") || name.equals("attribute") || name.equals("expression")){
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
	 * 检查是否已经存在xss的字符
	 * 
	 * @param text
	 * @return 不存在返回true
	 */
	public String fliterXSSForContent(String text) {
		StringBuffer sb = new StringBuffer("<html>").append(text).append("</html>");
		StringReader reader = new StringReader(sb.toString());
		StringWriter writer = new StringWriter();
		try {
			HTMLParser.process(reader, writer, XSSFILTER, true);
		} catch (HandlingException e) {
		}
		return writer.toString();
	}
	
	/**
	 * 返回true就是包含非法字符，返回false就是不包含非法字符 
	 * 系统内容过滤规则
	 * 1、包含  『 and 1 特殊字符 』， 特殊字符指>,<,=, in , like 字符 
	 * 2、『 /特殊字符/ 』，特殊字符指 *字符
	 * 3、『<特殊字符 script 』特殊字符指空字符
	 * 4、『 EXEC 』
	 * 5、『 UNION SELECT』
	 * 5、『 UPDATE SET』
	 * 5、『 INSERT INTO VALUES』
	 * 5、『 SELECT或DELETE FROM』
	 * 5、『CREATE或ALTER或DROP或TRUNCATE TABLE或DATABASE』
	 */
	public static boolean isAttack(String input) {
		String getfilter = "\\b(AND|OR)\\b.+?(>|<|=|\\bIN\\b|\\bLIKE\\b)|\\/\\*.+?\\*\\/|<\\s*SCRIPT\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)|\\bON.+\\b\\s*=|\\bJAVASCRIPT\\s*:\\b";
		Pattern pat = Pattern.compile(getfilter);
		Matcher mat = pat.matcher(input);
		boolean rs = mat.find();
		return rs;
	}
	
	/**
	 * 通过数组 过滤
	 * @param text
	 * @return
	 */
	public boolean isDeniedText(String text){
		for(int j=0;j<denied_texts.length;j++){
			String denied_Text=StringUtils.isNull(denied_texts[j]);
			if(StringUtils.isNull(text).indexOf(denied_Text)>-1){
				return true;
			}
		}
		return false;
	}
	
}
