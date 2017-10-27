package com.rd.ifaes.common.util.constants;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.util.StringUtils;

/**
 * 异常错误代码常量类
 * @author lh
 *
 */
public class ExceptionCodeConstant {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCodeConstant.class);
	
	/************************* java 高频异常 *************************/
	/**
	 * 空指针异常
	 */
	public static final String NULLPOINTEREXCEPTION="000001";

	/**
	 * 指定的类不存在
	 */
	public static final String CLASSNOTFOUNDEXCEPTION="000002";
	
	/**
	 * 数学运算异常
	 */
	public static final String ARITHMETICEXCEPTION="000003";
	
	/**
	 * 数组下标越界
	 */
	public static final String ARRAYINDEXOUTOFBOUNDSEXCEPTION="000004";
	
	/**
	 * 方法的参数错误
	 */
	public static final String ILLEGALARGUMENTEXCEPTION="000005";
	
	/**
	 * 没有访问权限
	 */
	public static final String ILLEGALACCESSEXCEPTION="000006";
	
	/**
	 * 不兼容的类变化错误
	 */
	public static final String INCOMPATIBLECLASSCHANGEERROR="000007";
	/**
	 * 实例化错误
	 */
	public static final String INSTANTIATIONERROR="000008";
	/**
	 * 链接错误
	 */
	public static final String LINKAGEERROR="000009";
	/**
	 * 堆栈溢出错误
	 */
	public static final String STACKOVERFLOWERROR="000010";
	
	/**
	 * 安全异常
	 */
	public static final String SECURITYEXCEPTION="000011";
	
	/**
	 * 未知异常
	 */
	public static final String UNKNOWEXCEPTION="000999";
	
	
	/************************* 认证、权限 异常 *************************/	
	/**
	 * 账户授权异常
	 */
	public static final String AUTHORIZATIONEXCEPTION="001000";
	/**
	 * 没有访问权限
	 */
	public static final String UNAUTHORIZEDEXCEPTION="001001";	
	/**
	 * 账户未认证
	 */
	public static final String UNAUTHENTICATEDEXCEPTION="001002";
	
	/************************* spring 异常 *************************/	
	/**
	 * 参数绑定失败
	 */
	public static final String BINDEXCEPTION="002001";
	/**
	 * 参数校验错误
	 */
	public static final String VALIDATIONEXCEPTION="002002";
	/**
	 * UFX异常
	 */
	public static final String UFXEXCEPTION="002003";
	/**
	 * mysql异常(唯一键冲突)
	 */
	public static final String MYSQLINTEGRITYCONSTRAINTVIOLATIONEXCEPTION="002004";
	/**
	 * mysql语法错误
	 */
	public static final String MYSQLSYNTAXERROREXCEPTION = "002005";
	
	/**
	 * 取得错误代码
	 * @param exceptionName
	 * @return
	 */
	public static String getCodeByExceptionName(String exceptionName){		
		Class<?> clazz = ExceptionCodeConstant.class;
		Field field;
		try {
			field = clazz.getField(StringUtils.isNull(exceptionName).toUpperCase());
		} catch (NoSuchFieldException | SecurityException e) {
			return null;
		}
		try {
			return field.get(clazz).toString();
		} catch (IllegalArgumentException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IllegalAccessException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return null;
	}

}
