 package com.rd.ifaes.common.validator;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.rd.ifaes.common.exception.BussinessException;
import com.rd.ifaes.common.util.SpringContextHolder;

/**
 * JSR303 Validator(Hibernate Validator)工具类.
 * 
 * ConstraintViolation中包含propertyPath, message 和invalidValue等信息.
 * 提供了各种convert方法，适合不同的i18n需求:
 * 1. List<String>, String内容为message
 * 2. List<String>, String内容为propertyPath + separator + message
 * 3. Map<propertyPath, message>
 * 
 * 详情见wiki: https://github.com/springside/springside4/wiki/HibernateValidator
 * @author calvin
 * @version 2013-01-15
 */
public class BeanValidators {
     /**
      * JSR303校验器
      */
	private static final Validator VALIDATOR= SpringContextHolder.getBean("validator");

	public static <T> void validate(final T t) {
		Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(t);
		if (constraintViolations.size() > 0) {
			final StringBuffer buffer = new StringBuffer();
			for (final ConstraintViolation<T> violation : constraintViolations) {
				buffer.append(violation.getMessage()).append(";");
			}
			throw new BussinessException(buffer.toString(), BussinessException.TYPE_JSON);
		}
	}
	/**
	 * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void validateWithException(final Validator validator, final Object object, final Class<?>... groups)
			throws ConstraintViolationException {
		final  Set constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(constraintViolations);
		}
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>中为List<message>.
	 */
	public static List<String> extractMessage(ConstraintViolationException e) {
		return extractMessage(e.getConstraintViolations());
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为List<message>
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
		final List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为Map<property, message>.
	 */
	public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
		return extractPropertyAndMessage(e.getConstraintViolations());
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为Map<property, message>.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, String> extractPropertyAndMessage(Set<? extends ConstraintViolation> constraintViolations) {
		Map<String, String> errorMessages = Maps.newHashMap();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errorMessages;
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath message>.
	 */
	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), " ");
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolations>为List<propertyPath message>.
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations) {
		return extractPropertyAndMessageAsList(constraintViolations, " ");
	}

	/**
	 * 辅助方法, 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath +separator+ message>.
	 */
	public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
		return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
	}

	/**
	 * 辅助方法, 转换Set<ConstraintViolation>为List<propertyPath +separator+ message>.
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
			String separator) {
		final List<String> errorMessages = Lists.newArrayList();
		for (ConstraintViolation violation : constraintViolations) {
			errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return errorMessages;
	}
}