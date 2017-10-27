package com.rd.ifaes.common.util;

import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.constant.ResourceConstant;
import com.rd.ifaes.core.core.util.ResourceUtils;
import com.rd.ifaes.core.user.domain.User;
/**
 * session操作工具类
 * @author lh
 * @version 3.0
 *
 */
public class SessionUtils {
	
	private SessionUtils() {
	}
	
	private static Subject getSubject(){
		Subject subject = PrincipalUtils.getSubject();
		if(subject == null){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN),BussinessException.TYPE_JSON);
		}
		return subject;
	}

	/**
	 * 获得session
	 * @param key
	 * @param value
	 */
	public static Session getSession(){
		return getSubject().getSession(true);
	}
	
	/**
	 * 获得session
	 * @param create true:不存在时创建session
	 */
	public static Session getSession(boolean create){
		return getSubject().getSession(create);
	}
	/**
	 * 保存session相关属性
	 * @param key
	 * @param value
	 */
	public static void setSessionAttr(String key, Object value){
		Session session = getSubject().getSession(true);
		session.setAttribute(key, value);
	}
	
	/**
	 * 取得session中对应 key的值
	 * @param key
	 * @return
	 */
	public static Object getSessionAttr(String key){
		org.apache.shiro.session.Session session = getSubject().getSession(false);
		return session==null? null:session.getAttribute(key);
	}
	
	/**
	 * 移除session中对应 key的值
	 * @param key
	 * @return
	 */
	public static Object removeAttribute(String key){
		org.apache.shiro.session.Session session = getSubject().getSession();
		return session.removeAttribute(key);
	}
	
	public static User getSessionUser(){
		return getSessionUser(BussinessException.TYPE_JSON);
	}
	
	public static User getSessionUser(int exceptionType){
		User user = (User) getSessionAttr(Constant.SESSION_USER);
		if(user==null && BussinessException.TYPE_JSON == exceptionType){
			throw new BussinessException(ResourceUtils.get(ResourceConstant.USER_NOT_LOGIN), exceptionType);
		}
		return user;
	}
     /**
      *  用户退出登录
      * @author  FangJun
      * @date 2016年7月26日
      */
	public static void  logout(){		
		setSessionAttr(Constant.SESSION_USER, null);
		getSubject().logout();
	}
	
}
