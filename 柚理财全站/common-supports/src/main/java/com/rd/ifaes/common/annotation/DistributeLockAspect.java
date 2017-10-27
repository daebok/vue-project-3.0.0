package com.rd.ifaes.common.annotation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.StringUtils;
import com.rd.ifaes.common.util.redisson.RedissonDistributeLock;

/**
 * Lock AOP切面
 * 
 * @author lh
 * @since 2016年7月25日
 * @version 3.0
 */
@Component // 注入依赖
@Aspect
public class DistributeLockAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(DistributeLockAspect.class);
	
	/**
	 * 分布式事务锁处理
	 * @param pjp
	 * @param lock
	 * @return
	 */
	@Around("@annotation(lock)")
	public Object lockAround(final ProceedingJoinPoint pjp, final DistributeLock lock) {

		String key = lock.value();
		Object retVal = null;
		try {
			if (StringUtils.isBlank(key)) {// 无key不上锁
				return pjp.proceed();
			} else {
				if (pjp.getSignature().toLongString().contains(".service.")) {//service层加锁
					key = getLockKeyFromService(pjp, key);
				} else {//controller层加锁
					key = getLockKeyFromController(key);
				}
			}
			RedissonDistributeLock.lock(key);
			retVal = pjp.proceed();
		} catch (Throwable e) {
			LOGGER.warn(e.getMessage(), e);
			throw new BussinessException(e.getMessage());			
		} finally {
			RedissonDistributeLock.unlock(key);
		}

		return retVal;
	}
	
	/**
	 * 取controller层参数
	 * @param key
	 * @return
	 */
	private String getLockKeyFromController(String key) {
		String ckey = key;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if (request != null && request.getParameter(ckey) != null) {
			List<String> annoParamNames = AopUtils.getAnnoParams(key);
			for (String ap : annoParamNames) {
				ckey = ckey.replaceAll(String.format("{%s}", ap), (String) request.getParameter(ckey));
			}
		}
		return ckey;
	}
	
	/**
	 * 取Service层参数
	 * @param pjp
	 * @param key
	 * @return
	 */
	private String getLockKeyFromService(final ProceedingJoinPoint pjp, String key) {
		return  AopUtils.getCacheKey(pjp, key);
	}

}
