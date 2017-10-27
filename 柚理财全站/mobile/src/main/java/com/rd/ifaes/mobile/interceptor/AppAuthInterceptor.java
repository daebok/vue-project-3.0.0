package com.rd.ifaes.mobile.interceptor;

import java.net.URLDecoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.SessionUtils;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.web.interceptor.BaseInterceptor;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.user.domain.User;
import com.rd.ifaes.mobile.common.AppCommons;
import com.rd.ifaes.mobile.common.AppDataUtil;
import com.rd.ifaes.mobile.common.AppException;
import com.rd.ifaes.mobile.common.AppRequestParam;
import com.rd.ifaes.mobile.common.AppResultCode;
import com.rd.ifaes.mobile.common.AppValidateUtil;
import com.rd.ifaes.mobile.controller.user.UserController;
import com.rd.ifaes.mobile.model.AppResponse;
import com.rd.ifaes.mobile.model.token.OauthAccessToken;
import com.rd.ifaes.mobile.model.token.OauthTokenManager;
import com.rd.ifaes.mobile.util.JsonPrinter;
/**
 * 前台登录认证拦截器
 * 
 * @version 3.0
 * @author LGX
 * @date 2016年8月2日
 */
public class AppAuthInterceptor extends BaseInterceptor {
	/**
	 * 日志记录器
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	private JsonPrinter jsonPrinter = new JsonPrinter(this.LOGGER );
	private void logRequest( HttpServletRequest request,Map<String, String> treeMap) {
		Map<String, String[]> map = request.getParameterMap();  
        Set<String> keySet = map.keySet();  
        for (String key : keySet) {  
        	String[] values = (String[]) map.get(key);  
        	for (String value : values) {  
        		//LOGGER.info("移动端请求日志>>map>>>> "+key+"="+value);  
        		treeMap.put(key, value);
        	}  
        }
	}
	/**
	 * 请求拦截处理
	 * @throws Exception 
	 */
	@Override
	public boolean preHandle(
			final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception{
		LOGGER.info( "uri: " + request.getRequestURI() );
		if(!StringUtils.isBlank(request.getParameter("testMobile"))) {
			return true;
		}
		String appSignaMd5="";
		Map<String, String> treeMap=new TreeMap<String, String>();
		logRequest( request ,treeMap); 
        Iterator<String> it = treeMap.keySet().iterator();   
               while (it.hasNext()) {   
            	     Object key = it.next();   
            	     String value = URLDecoder.decode(treeMap.get(key), "UTF-8");
                   LOGGER.info("移动端请求日志>>treemap>> "+key+"="+value); 
                   if(!"signa".equals(key))
                 appSignaMd5=appSignaMd5+key+"="+value;
                }   
               LOGGER.info("移动端请求日志>>appSignaMd5>> "+appSignaMd5);
		String appkey = request.getParameter("appkey");
		String userAgent = request.getHeader("User-Agent").toLowerCase();//判断访问来源 是否是微信浏览器
		int source=0;//来源  
		if("android".equals(userAgent)){//安卓
			source=1;
		}else if("iphone".equals(userAgent)){//ios
			source=2;
		}else if(userAgent.contains("android")||userAgent.contains("iphone")||userAgent.contains("micromessenger")){// 手机浏览器
			source=3;
		}
		if("micromessenger".equals(request.getParameter("User-Agent"))){
			source=3;
		}
		if(source!=3){//微信或者浏览器访问  不用校验签名
		if (!AppCommons.APPKEY_MAP.containsKey(appkey)) {
			String res_msg = "appkey不正确，请确认!";
			jsonPrinter.printBeanJson( new AppResponse(AppResultCode.ERROR,res_msg) , response);
			LOGGER.error( res_msg );
			return false;
		}
		String signa = request.getParameter("signa");
		String serverSigna = AppDataUtil.getMD5Str(appSignaMd5);
		String serverSignas = AppDataUtil.getMD5Str(serverSigna + AppCommons.APPSECRET).toUpperCase();
		LOGGER.info("签名验证日志>>appSignaMd5》》》"+appSignaMd5+"第一次签名日志》》》"+serverSigna+"第二次加密前字符串》》》"+serverSigna + AppCommons.APPSECRET+"第二次签名日志"+serverSignas);
		if (!serverSignas.equals(signa)) {
			String res_msg = "签名验证失败!";
			jsonPrinter.printBeanJson( new AppResponse(AppResultCode.ERROR,res_msg) , response);
			LOGGER.error( res_msg );
			return false;
		}
		//验证接口版本号
		String mobileType = request.getParameter("mobileType");
		String versionNumber = request.getParameter("versionNumber");
		if(!verifyInterfaceVersion(mobileType, versionNumber,response) ){
			String res_msg = "版本号验证不通过，请下载最新版!";
			jsonPrinter.printBeanJson( new AppResponse(AppResultCode.UPDATE_NORMAL,res_msg) , response);
			LOGGER.error( res_msg );
			return false;
		}
		}
		final String uri = request.getRequestURI();
		/*//如果是"/"，则直接跳转到首页
		if(uri.equals(InterceptorEnum.LOGIN_URL_WEB_HOME.getValue())){
			return true;
		}*/
		/**
		 * 跳过无需过滤的uri
		 */
			if (uri.startsWith("/app/member")) {
				if(source==3){//微信或者浏览器访问
				if (!isAuthenticated()) {// 未登录
					if(request.getHeader("Origin")!=null){
						response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
					}else{
						response.addHeader("Access-Control-Allow-Origin", "*");
					}
					response.addHeader("Access-Control-Allow-Credentials", "true");
					jsonPrinter.printBeanJson( new AppResponse(AppResultCode.TOKEN_NOT_EXIT,"请先登录后再尝试") , response);
					return false;
				}
				}else{//安卓 或者 iPhone
					String userId = request.getParameter(AppRequestParam.USER_ID);
					if (!AppValidateUtil.checkStrNull(userId)) {
						jsonPrinter.printBeanJson( new AppResponse(AppResultCode.TOKEN_NOT_EXIT,"无效的用户id") , response);
						return false;
					}
					String oauthToken = request.getParameter(AppRequestParam.OAUTH_TOKEN);
					if ((StringUtils.isEmpty(oauthToken))) {
						jsonPrinter.printBeanJson( new AppResponse(AppResultCode.ERROR,"参数有误") , response);
						return false;
					}
					OauthTokenManager oauthTokenManager = new OauthTokenManager();
					OauthAccessToken token = null;
					try {
						token = oauthTokenManager.getOauthTokenStore(userId,"mobile",oauthToken);
					} catch (AppException e) {
						jsonPrinter.printBeanJson( new AppResponse(e) , response);
						return false;
					}
					if (!token.getBindingId().equals(String.valueOf(userId))) {
						jsonPrinter.printBeanJson( new AppResponse(AppResultCode.TOKEN_REFRESH_TIMEOUT,"长时间不在线，请重新登录") , response);
						return false;
					}
					final User user = (User) SessionUtils.getSessionAttr(Constant.SESSION_USER);
					if (user==null) {
						jsonPrinter.printBeanJson( new AppResponse(AppResultCode.TOKEN_TIMEOUT,"Token过期") , response);
						return false;
					}
					if(user != null) {
						if(User.USER_STATUS_LOCKED.equals(user.getStatus())){ //锁定
							jsonPrinter.printBeanJson( new AppResponse(AppResultCode.USER_LOCK,"账户已被锁定,请联系客服") , response);
							return false;
						}
						}
				}
				}else{
					return true;
				}
				

		return super.preHandle(request, response, handler);
	}

	/**
	 * @Description: 验证接口版本号
	 * @author yusj
	 * 2016年2月24日 下午1:51:13
	 */
  public boolean verifyInterfaceVersion(String mobileType, String versionNumber,HttpServletResponse response){
	  if(versionNumber.contains("V")){
		  versionNumber=versionNumber.replace("V", "");
	  }
	  if(versionNumber.contains("-")){
		  String[] sourceStrArray = versionNumber.split("-"); 
	   if(sourceStrArray.length>1)
		  versionNumber=versionNumber.replace("-"+sourceStrArray[1], "");
	  }
	  
		//判空
		if(mobileType == null || mobileType.equals("") ||
				versionNumber == null || versionNumber.equals("")) {
			jsonPrinter.printBeanJson( new AppResponse(AppResultCode.UPDATE_NORMAL,"请下载最新版本APP") , response);
			return false;
		}
		String iosVer = AppCommons.APP_IOS_VERSION;
		String andVer = AppCommons.APP_ANDROID_VERSION;
		//校验版本号
		if (Integer.valueOf(mobileType) == 1) {// ios:1
			if(!isNewVersion(versionNumber, iosVer)){
				jsonPrinter.printBeanJson( new AppResponse(AppResultCode.UPDATE_NORMAL,"请下载最新版本APP") , response);
				return false;
			}
		} else if (Integer.valueOf(mobileType) == 2) {// android:2
			if(!isNewVersion(versionNumber, andVer)){
				jsonPrinter.printBeanJson( new AppResponse(AppResultCode.UPDATE_NORMAL,"请下载最新版本APP") , response);
				return false;
			}
		}
		return true;
	}
 
	//校验app版本
		public static boolean isNewVersion(String online,String local){		
			String[] versionLocal=local.split("\\.");
			String[] versionOnline=online.split("\\.");
			boolean flag=false;
			//判断app版本号长度大于服务器端版本号的情况
			StringBuffer localstr = new StringBuffer();
			StringBuffer  onlinestr=new StringBuffer();
			//分别获得两版本号前两字节的值
			Integer localNumber=  Integer.valueOf(((localstr.append(versionLocal[0])).append(versionLocal[1])).toString());		
		    Integer onlineNumber = Integer.valueOf(((onlinestr.append(versionOnline[0])).append(versionOnline[1])).toString());		
			System.out.println(localNumber+":"+onlineNumber);
			if(versionOnline.length>versionLocal.length){
				if(onlineNumber>=localNumber){
				return true;
				}else{
					return false;
				}
			}

			//app版本号长度和服务器端版本号长度一致时
			if(Integer.valueOf(online.replace(".","")).equals(Integer.valueOf(local.replace(".","")))){
				flag=true;	
			}else{
				//app版本号长度小于服务器端版本号长度(且前两位字段一致，第三位字段为0)
				if((onlineNumber).equals(localNumber)&&(Integer.valueOf(versionLocal[2]))<=0){
					flag=true;
				}else{
				int length=versionOnline.length<versionLocal.length?versionOnline.length:versionLocal.length;
			    for (int i = 0; i < length; i++)
			{
				if (Integer.parseInt(versionOnline[i]) > Integer.parseInt(versionLocal[i]))
				{
					flag= true;
				}
				else if (Integer.parseInt(versionOnline[i]) < Integer.parseInt(versionLocal[i]))
				{
					flag= false;
				}
				// 相等 比较下一组值
			}
			}
			}
	        return flag;
			
		}
		/**
		 * 是否通过登录认证  微信专用
		 * 
		 */
		private boolean isAuthenticated() {
			return SessionUtils.getSessionAttr(Constant.SESSION_USER)!=null;
		}

}
