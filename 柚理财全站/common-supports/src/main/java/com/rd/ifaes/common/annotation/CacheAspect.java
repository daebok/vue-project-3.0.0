package com.rd.ifaes.common.annotation;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.dict.ExpireTime;
import com.rd.ifaes.core.core.constant.CacheConstant;
import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;
/**
 * 缓存操作切面
 * 注意：一个支持缓存的方法，在对象内部被调用是不会触发缓存功能的。
 * @author lh
 *
 */
@Aspect
@Component
public class CacheAspect {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheAspect.class);
	
	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 启用新的get方法，防止缓存被“击穿”
	 * <p>
	 * 击穿 ：缓存在某个时间点过期的时候，恰好在这个时间点对这个Key有大量的并发请求过来，
	 * 		这些请求发现缓存过期一般都会从后端DB加载数据并回设到缓存，这个时候大并发的请求可能会瞬间把后端DB压垮。
	 * 如何解决：业界比较常用的做法，是使用mutex。
	 * 		简单地来说，就是在缓存失效的时候（判断拿出来的值为空），不是立即去load db，而是先使用缓存工具的某些带成
	 * 		功操作返回值的操作（比如Redis的SETNX或者Memcache的ADD）去set一个mutex key，当操作返回成功时，再进行
	 * 		load db的操作并回设缓存；否则，就重试整个get缓存的方法。
	 * </p>
	 * @param key
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	private Object get(final String key, final ProceedingJoinPoint pjp, final Cacheable cache) throws Throwable {
		@SuppressWarnings("unchecked")
		ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
		Object value = valueOper.get(key); // 从缓存获取数据
		if (value == null) { // 代表缓存值过期
			// 设置2min的超时，防止del操作失败的时候，下次缓存过期一直不能load db
			String keynx = key.concat(":nx");
			if (CacheUtils.setnx(keynx, "1", ExpireTime.FIVE_SEC)) { // 代表设置成功
				try {
					value = pjp.proceed();
					if (cache.expire().getTime() <= 0) { // 如果没有设置过期时间,则无限期缓存
						valueOper.set(key, value);
					} else { // 否则设置缓存时间
						valueOper.set(key, value, cache.expire().getTime(), TimeUnit.SECONDS);
					}
				} catch (Exception e) {
					throw e;
				} finally {
					CacheUtils.del(keynx);
				}
				return value;
			} else { // 这个时候代表同时候的其他线程已经load db并回设到缓存了，这时候重试获取缓存值即可
				Thread.sleep(10);
				return get(key, pjp, cache); // 重试
			}
		} else {
			return value;
		}
	}
	/**
	 * 添加缓存
	 * @param pjp
	 * @param cache
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(cache)")
	public Object cacheable(final ProceedingJoinPoint pjp, Cacheable cache)throws Throwable {

		String key = CacheUtils.appendKeyPrefix(AopUtils.getCacheKey(pjp, cache.key()));

//		//使用redisTemplate操作缓存
//		@SuppressWarnings("unchecked")
//		ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
//		Object value =  valueOper.get(key); // 从缓存获取数据
//		if (value != null) {
//			return value; // 如果有数据,则直接返回
//		}
//		
//		value = pjp.proceed();
//		LOGGER.info("cachePut, key={}", key);			
//		// 缓存,到后端查询数据
//		if (cache.expire().getTime() <= 0) { // 如果没有设置过期时间,则无限期缓存
//			valueOper.set(key, value);
//		} else { // 否则设置缓存时间
//			valueOper.set(key, value, cache.expire().getTime(), TimeUnit.SECONDS);
//		}
//		return value;
		return get(key, pjp, cache);
	}
	
	
	@Around("@annotation(evict)")
	public Object cacheEvict(final ProceedingJoinPoint pjp, CacheEvict evict)throws Throwable {
		Object value = pjp.proceed();
		//清除keys对应的缓存数据
		if (evict.keys() != null && evict.keys().length > 0) {
			for (String keyname : evict.keys()) {
				evictByKeyname(pjp, keyname,evict.interval());
			}
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	private void evictByKeyname(final ProceedingJoinPoint pjp, final String keyname, ExpireTime interval) {
		final String key = CacheUtils.appendKeyPrefix(AopUtils.getCacheKey(pjp, keyname));
		//操作间隔判断
		if (!ExpireTime.NONE.equals(interval)) {
			final String intervalKey = CacheConstant.KEY_PREFIX_CACHE_EVICT + key;
			if (CacheUtils.incr(intervalKey, Constant.DOUBLE_ONE) > Constant.DOUBLE_ONE) {
				return;
			}
			CacheUtils.expire(intervalKey, interval);
		}
		
		LOGGER.info("cacheEvict, key={}", key);			
		
		//使用redisTemplate操作缓存
		if (keyname.equals(key)) {// 支持批量删除
			Set<String> keys = redisTemplate.keys(key.concat("*"));
			if (!CollectionUtils.isEmpty(keys)) {
				redisTemplate.delete(keys);
			}
		} else {
			redisTemplate.delete(key);
		}
	}

	
}