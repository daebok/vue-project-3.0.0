package com.rd.ifaes.core.core.web.interceptor;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;

import com.rd.ifaes.common.dict.InterceptorEnum;
import com.rd.ifaes.common.util.PrincipalUtils;
import com.rd.ifaes.common.util.SpringContextHolder;
import com.rd.ifaes.common.util.resource.MessageResource;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
import com.rd.ifaes.core.sys.domain.Operator;
import com.rd.ifaes.core.sys.service.OperatorService;

/**
 * 后台登录认证拦截器
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月2日
 */
public class ManageAuthInterceptor extends BaseInterceptor {

	/**
	 * 
	 * 无需过滤的uri <br>
	 * 	1、登录;
	 *  2、登出;
	 * 
	 */
	private static final String[] NO_FILTER_URI = { InterceptorEnum.LOGIN_URL_MANAGE.getValue(),
			InterceptorEnum.LOGOUT_URL_MANAGE.getValue(),
			InterceptorEnum.LOGIN_VALID_IMG_URL_MANAGE.getValue(),
			InterceptorEnum.LOGIN_VALID_CODE_URL_MANAGE.getValue()};

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String uri = request.getRequestURI();
		/**
		 * 跳过无需过滤的uri
		 */
		for (String nfu : NO_FILTER_URI) {
			if (nfu.equals(uri)) {
				return true;
			}
		}

		if (!isAuthenticated()) {// 未登录
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
			builder.append("alert(\"页面过期，请重新登录\");");
			builder.append("window.top.location.href=\"");
			builder.append(InterceptorEnum.LOGIN_URL_MANAGE.getValue());
			builder.append("\";</script>");
			out.print(builder.toString());
			out.close();
			return false;
		}
		//判断用户时候重置了密码
		Operator operator = (Operator) PrincipalUtils.getPrincipal();
		OperatorService operatorService = SpringContextHolder.getBean(OperatorService.class);
		Operator user = operatorService.get(operator.getUuid());
		if(Operator.PWD_FLAG_YES.equals(user.getPwdFlag()) && !("/sys/operator/operatorChangePasswordPage.html".equals(uri) || "/sys/operator/operatorChangePassword.html".equals(uri))) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">alert('登陆后请修改密码');window.top.location.href='/sys/operator/operatorChangePasswordPage.html'</script>");
			out.print(builder.toString());
			out.close();
			return false;
		}
		//判断用户是否首次登录
		int isFirstLogin=operatorService.findIsFirstLogin(user);
		if(isFirstLogin==0 && !("/sys/operator/operatorChangePasswordPage.html".equals(uri) || "/sys/operator/operatorChangePassword.html".equals(uri))) {//首次登陆 ,0代表没有登录记录
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			StringBuilder builder = new StringBuilder();
			builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">alert('首次登陆请修改密码');window.top.location.href='/sys/operator/operatorChangePasswordPage.html'</script>");
			out.print(builder.toString());
			out.close();
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	/**
	 * 是否通过登录认证
	 * 
	 * @author lh
	 * @date 2016年8月2日
	 * @return
	 */
	private boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}
}
