package com.rd.ifaes.common.jedis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import net.sf.ehcache.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
/**
 * RdRedisCache
 * @author lh
 * @version 3.0
 *
 */
public class RdRedisCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RdRedisCache.class);

	private RedisTemplate<String, Object> redisTemplate;
	private String name;

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		return this.redisTemplate;
	}

	@Override
	public ValueWrapper get(Object key) {
		
		final String keyf = obj2Str(key);
		Object object = redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {

				byte[] key = keyf.getBytes();
				byte[] value = connection.get(key);
				if (value == null) {
					return null;
				}
				return toObject(value);

			}
		});
		return object != null ? new SimpleValueWrapper(object) : null;
	}

	@Override
	public void put(Object key, Object value) {
		
		final String keyf = obj2Str(key);
		final Object valuef = value;
		final long liveTime = 86400;

		redisTemplate.execute(new RedisCallback<Long>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				byte[] keyb = keyf.getBytes();
				byte[] valueb = toByteArray(valuef);
				connection.set(keyb, valueb);
				if (liveTime > 0) {
					connection.expire(keyb, liveTime);
				}
				return 1L;
			}
		});
	}
	
	/**
	 * object转string
	 * @param key
	 * @return
	 */
	public String obj2Str(Object key){
		String keyStr;
		if(key instanceof Integer){
			keyStr = ((Integer)key).toString();
		}else if(key instanceof Long){
			keyStr = ((Long)key).toString();
		}else {
			keyStr = (String)key;
		}
		return keyStr;
	}

	/**
	 * 描述 : <Object转byte[]>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @param obj
	 * @return
	 */
	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return bytes;
	}

	/**
	 * 描述 : <byte[]转Object>. <br>
	 * <p>
	 * <使用方法说明>
	 * </p>
	 * 
	 * @param bytes
	 * @return
	 */
	private Object toObject(byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.error(e.getMessage(), e);
		} 
		return obj;
	}

	@Override
	public void evict(Object key) {
		final String keyf = obj2Str(key);
		redisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.del(keyf.getBytes());
			}
		});
	}

	@Override
	public void clear() {
		redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return "ok";
			}
		});
	}

	@Override
	public <T> T get(Object key, Class<T> type) {
		ValueWrapper wrapper = get(key);
		return wrapper == null ? null : (T) wrapper.get();
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		synchronized (key) {
			ValueWrapper wrapper = get(key);
			if (wrapper != null) {
				return wrapper;
			}
			put(key, value);
			return toWrapper(new Element(key, value));
		}
	}
	
	private ValueWrapper toWrapper(Element element) {
		return element != null ? new SimpleValueWrapper(element.getObjectValue()) : null;
	}

}
