package com.rd.ifaes.core.core.web.interceptor;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.dict.InterceptorEnum;
import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
import com.rd.ifaes.core.core.constant.Constant;

/**
 * 前台登录认证拦截器
 * 
 * @version 3.0
 * @author lh
 * @date 2016年8月2日
 */
public class WebAuthInterceptor extends BaseInterceptor {
	/**
	 * 登录跳转地址，例如：访问请求是/member/myLoan/xxx.html,session过期，则重新登录后默认跳转到/member/myLoan/list.html
	 */
	private static Map<String,String> urlMap=null;
	static {
		urlMap = new HashMap<String, String>();
		urlMap.put("/member/account/", "/member/account/main.html");
		urlMap.put("/member/fund/", "/member/fund/list.html");
		urlMap.put("/member/recharge/", "/member/recharge/index.html");
		urlMap.put("/member/myInvest/", "/member/myInvest/list.html");
		urlMap.put("/member/myBond/", "/member/myBond/list.html");
		urlMap.put("/member/myRealize/", "/member/myRealize/list.html");
		urlMap.put("/member/coupon/", "/member/coupon/myCoupon.html");
		urlMap.put("/member/myLoan/", "/member/myLoan/list.html");
		urlMap.put("/member/myRepayment/", "/member/myRepayment/list.html");
		urlMap.put("/member/baseInfo/", "/member/baseInfo/index.html");
		urlMap.put("/member/bankCard/", "/member/bankCard/list.html");
		urlMap.put("/member/invite/", "/member/invite/index.html");
		urlMap.put("/member/project/", "/user/login.html");
		urlMap.put("/borrow/", "/borrow/qualification.html");
		urlMap.put("/member/myAssistant/", "/member/myAssistant/opinionAdd.html");
		urlMap.put("/member/risk/", "/member/risk/userRiskPapers.html");
		urlMap.put("/member/vouch/", "/member/vouch/verify.html");
		urlMap.put("/scoreshop/", "/scoreshop/index.html");
	}

	/**
	 * 
	 * 无需过滤的uri <br>
	 * 	1、登录;
	 * 
	 */
	private static final String[] NO_FILTER_URI = { 
		InterceptorEnum.LOGIN_URL_WEB_UPLOAD.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_VALIDIMG.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_VALIDCODE.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_USER.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_REALIZE.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_PUBLIC.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_PROJECT.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_BORROW.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_INVEST.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_INDEX.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_COLUMN.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_BOND.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_SHOW_CAPTCHA.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_UFX.getValue(),
		InterceptorEnum.BOND_PROTOCOL_URL.getValue(),
		InterceptorEnum.RETRIEVE_PASSWORD.getValue(),
		InterceptorEnum.INCOME_CALCULATION.getValue(),
		InterceptorEnum.CALCULATION_INCOME.getValue(),
		InterceptorEnum.PROTOCOL_URL.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_CBHB.getValue(),
		InterceptorEnum.LOGIN_URL_WEB_CBHB_APP.getValue(),
		InterceptorEnum.ERRORPAGE.getValue(),
		InterceptorEnum.JXBANK_URL.getValue()
	};

	/**
	 * 请求拦截处理
	 * @throws Exception 
	 */
	@Override
	public boolean preHandle(
			final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{

		final String uri = request.getRequestURI();
		//如果是"/"，则直接跳转到首页
		if(uri.equals(InterceptorEnum.LOGIN_URL_WEB_HOME.getValue())){
			return true;
		}
		
		/**
		 * 跳过无需过滤的uri
		 */
		for (final String nfu : NO_FILTER_URI) {
			if (uri.startsWith(nfu)) {
				if(nfu.equals(InterceptorEnum.LOGIN_URL_WEB_BORROW.getValue())){
					if(uri.equals(InterceptorEnum.LOGIN_URL_WEB_BORROW_BESPEAK.getValue())){
						return true;
					}
					if(uri.equals(InterceptorEnum.LOGIN_URL_WEB_BORROW_BESPEAK_ADD.getValue())){
						return true;
					}
				}else if(uri.equals(InterceptorEnum.LOGIN_URL_WEB_BOND_ORDER.getValue())){//需要
					break;
				}else{
					return true;
				}
				
			}
		}

		if (!isAuthenticated()) {// 未登录
			String redirectUrl = InterceptorEnum.LOGIN_URL_WEB.getValue();
			if(!urlMap.isEmpty()){
				for (final Map.Entry<String, String> entry : urlMap.entrySet()) {
					if(uri.startsWith(entry.getKey())){
						redirectUrl = redirectUrl+"?redirectURL="+entry.getValue();
						break;
					}
				}
			}
			// AJAX request
			if ((request.getHeader("X-Requested-With") != null
					&& request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
				response.setContentType("application/json;charset=UTF-8");
				PrintWriter out = response.getWriter();
				final Map<String, Object> map = Maps.newHashMap();
				map.put("result", false);
				map.put("msg", "请您先登录网站");
				map.put("redirect", true);
				map.put("url", redirectUrl);
				out.print(JSON.toJSON(map));
			}else{
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html;charset=UTF-8");
				response.sendRedirect(redirectUrl);
			}
			return false;
		}/*else {
    		User user = (User)SessionUtils.getSessionAttr(Constant.SESSION_USER);
    		if (user != null) {
    			UserCacheService userCacheService = SpringContextHolder.getBean("userCacheService");
                UserCache userCache = userCacheService.findByUserId(user.getUuid());
                if (Constant.FLAG_YES.equals(userCache.getResetPwdFlag())) {
                    //如果是"/"，则直接跳转到首页
    				if (uri.equals(InterceptorEnum.LOGIN_URL_PWD_RESET.getValue())
    						|| uri.equals(InterceptorEnum.LOGIN_URL_DO_MODIFY_PWD.getValue())
    						|| uri.equals(InterceptorEnum.LOGIN_URL_RETRIEVEPWD.getValue())) {
    					return true;
    				}
        	        String redirectUrl = InterceptorEnum.LOGIN_URL_PWD_RESET.getValue();
        	        response.sendRedirect(redirectUrl);
        	        return false;
                }
    		}

        }*/
		
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
		return SessionUtils.getSessionAttr(Constant.SESSION_USER)!=null;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
	
}
