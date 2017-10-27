package com.rd.ifaes.common.util.redisson;

import java.util.concurrent.TimeUnit;

import org.redisson.Redisson;
import org.redisson.core.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * redisson分布式事务锁
 * @author lh
 * @version 3.0
 * @since 2016-7-23
 *
 */
public class RedissonDistributeLock {

	private final static Logger LOGGER = LoggerFactory.getLogger(RedissonDistributeLock.class);
	
	private static final String LOCK_TITLE = "redisLock_";
	
	private static Redisson redisson = RedissonUtils.getInstance().getClient();
	
	/**
	 * 获得锁
	 * @param lockName
	 */
	public static void lock(String lockName) {
		String key = LOCK_TITLE + lockName;
		RLock mylock = redisson.getLock(key);
		mylock.lock(10, TimeUnit.MINUTES); // lock提供带timeout参数，timeout结束强制解锁，防止死锁
		LOGGER.debug("======lock====== lockName={}", lockName);
	}

	/**
	 * 释放锁
	 * @param lockName
	 */
	public static void unlock(String lockName) {
		String key = LOCK_TITLE + lockName;
		RLock mylock = redisson.getLock(key);
		mylock.unlock();
		LOGGER.debug("======unlock======lockName={}", lockName);
	}
	
	/**
	 * 判断是否被锁
	 * @param lockName
	 * @return
	 */
	public static boolean isLocked(String lockName) {
		String key = LOCK_TITLE + lockName;
		RLock mylock = redisson.getLock(key);
		return mylock.isLocked();
	}

}
