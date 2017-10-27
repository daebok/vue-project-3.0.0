package com.rd.ifaes.common.web.freemarker.tag.directive;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.TokenUtils;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * 表单防重复提交令牌标签 使用说明，在freemarker配置XML中定义为：
 * 
 * <pre>
 *        <entry key="token" value-ref="tokenDirective" />
 * </pre>
 * 
 *  页面使用: name 是唯一标识，不可以为空 ,例子：
 * <pre>
 *      <form  action="">
 *           <@token name="xxToken"/>
 *      
 *       </form>
 * </pre>
 * 
 * @author FangJun
 * 
 * @see com.rd.ifaes.common.util.TokenUtils
 */
@Component
public class TokenDirective implements TemplateDirectiveModel {

	private static final Logger LOGGER = LoggerFactory.getLogger(TokenDirective.class);

	// 插入隐藏域模板
	private static final String TOKEN_INPUT = "<input type='hidden' name='%s' value='%s'/>";

	@Autowired(required = false)
	private HttpServletRequest request;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		HttpSession session = request.getSession();
		String tokenName = params.get("name")!=null ? params.get("name").toString() : "";
		if (StringUtils.isNotBlank(tokenName)) {
			TokenUtils.setToken(session, tokenName );
			String token = (String) session.getAttribute(tokenName);
			env.getOut().write(String.format(TOKEN_INPUT, tokenName, token));
			LOGGER.debug("TOKEN[{}]发放成功:",tokenName, token);
		}
	}

}
