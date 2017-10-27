package com.rd.ifaes.common.annotation;

import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Session AOP切面 
 * @author lh
 * @since 2016年5月17日 
 * @version 3.0
 */
@Component //注入依赖
@Aspect
public class SessionAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionAspect.class);
	
	/**
	 * 校验session
	 * @param pjp
	 * @param session
	 * @return
	 * @throws Throwable
	 */
	@Around("within(com.rd.ifaes..*) && @annotation(session)")
	public Object checkSession(ProceedingJoinPoint pjp, Session session )throws Throwable {
		boolean isAuthen = isAuthenticated();
		LOGGER.debug("isAuthenticated={}" , isAuthen);
		
		if(!isAuthen){
			return session.value();
		}
		// 继续执行接下来的代码
        return pjp.proceed();
	}

	private boolean isAuthenticated(){
		return SecurityUtils.getSubject().isAuthenticated();
	}
		
	

}
