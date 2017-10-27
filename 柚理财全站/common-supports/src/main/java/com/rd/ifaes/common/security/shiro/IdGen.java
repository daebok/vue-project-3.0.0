package com.rd.ifaes.common.security.shiro;

import java.io.Serializable;
import java.util.UUID;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionIdGenerator;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.rd.ifaes.core.core.constant.Constant;
import com.rd.ifaes.core.core.util.CacheUtils;


/**
 *   生成唯一性ID算法的工具类.
 * @author  FangJun
 *
 */
@Service
@Lazy(false)
public class IdGen implements SessionIdGenerator {
	
	public static final String ID_KEY_PREFIX = "id:";

	/**
	 * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
 
	@Override
	public Serializable generateId(Session session) {
		return IdGen.uuid();
	}
	
	/**
	 * 生成主键
	 * @param clazz POJO类
	 * @return
	 */
	public static long uuid(Class<?> clazz){
		return CacheUtils.incr(ID_KEY_PREFIX.concat(clazz.getSimpleName()), Constant.INT_ONE);
	}
	
}
