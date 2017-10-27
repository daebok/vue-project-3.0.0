package com.rd.ifaes.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rd.ifaes.common.entity.BaseEntity;
import com.rd.ifaes.common.exception.BussinessException;

@SuppressWarnings("rawtypes")
public class ReflectionUtils {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String CGLIB_CLASS_SEPARATOR = "$$";
	
	private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

	public static Object invokeGetMethod(Class<?> claszz, Object o, String name) {
		Object ret = null;
		try {
			Method method = claszz.getMethod("get" +  com.rd.ifaes.common.util.StringUtils.firstCharUpperCase(name));
			ret = method.invoke(o);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return ret;
	}
	
	
	/**
	 * 调用Getter方法.
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static Object invokeGetter(Object obj, String propertyName) {
		Object object = obj;
		for (String name : StringUtils.split(propertyName, ".")){
			String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
			object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
		}
		return object;
	}

	/**
	 * 调用Setter方法, 仅匹配方法名。
	 * 支持多级，如：对象名.对象名.方法
	 */
	public static void invokeSetter(Object obj, String propertyName, Object value) {
		Object object = obj;
		String[] names = StringUtils.split(propertyName, ".");
		for (int i=0; i<names.length; i++){
			if(i<names.length-1){
				String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i]);
				object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
			}else{
				String setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i]);
				invokeMethodByName(object, setterMethodName, new Object[] { value });
			}
		}
	}
	
	/**
	 * 调用Setter方法(赋值)
	 * @param claszz
	 * @param o
	 * @param name
	 * @param argType
	 * @param args
	 * @return
	 */
	public static Object invokeSetter(Class<?> claszz, Object o, String name, Class<?> argType, Object args) {
		Object ret = null;
		try {
			// 非 常量 进行反射
			if (!checkModifiers(claszz, name)) {
				Method method = claszz.getMethod("set" + StringUtils.firstCharUpperCase(name), new Class[] { argType });
				ret = method.invoke(o, new Object[] { args });
			}
		} catch (Exception e) {
			logger.error("claszz:{},name:{},argType:{},args:{}",claszz,name,argType, args);
		}
		return ret;
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过getter函数.
	 */
	public static Object getFieldValue(final Object obj, final String fieldName) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		Object result = null;
		try {
			result = field.get(obj);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常{}", e.getMessage());
		}
		return result;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 */
	public static void setFieldValue(final Object obj, final String fieldName, final Object value) {
		Field field = getAccessibleField(obj, fieldName);

		if (field == null) {
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + obj + "]");
		}

		try {
			field.set(obj, value);
		} catch (IllegalAccessException e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用.
	 * 同时匹配方法名+参数类型，
	 */
	public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
			final Object[] args) {
		Method method = getAccessibleMethod(obj, methodName, parameterTypes);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 直接调用对象方法, 无视private/protected修饰符，
	 * 用于一次性调用的情况，否则应使用getAccessibleMethodByName()函数获得Method后反复调用.
	 * 只匹配函数名，如果有多个同名函数调用第一个。
	 */
	public static Object invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
		Method method = getAccessibleMethodByName(obj, methodName);
		if (method == null) {
			throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + obj + "]");
		}

		try {
			return method.invoke(obj, args);
		} catch (Exception e) {
			throw convertReflectionExceptionToUnchecked(e);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
	 * 
	 * 如向上转型到Object仍无法找到, 返回null.
	 */
	public static Field getAccessibleField(final Object obj, final String fieldName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(fieldName, "fieldName can't be blank");
		for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
			try {
				Field field = superClass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {//NOSONAR
				// Field不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethod(final Object obj, final String methodName,
			final Class<?>... parameterTypes) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			try {
				Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
				// Method不在当前类定义,继续向上转型
				continue;// new add
			}
		}
		return null;
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
	 * 如向上转型到Object仍无法找到, 返回null.
	 * 只匹配函数名。
	 * 
	 * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
	 */
	public static Method getAccessibleMethodByName(final Object obj, final String methodName) {
		Validate.notNull(obj, "object can't be null");
		Validate.notBlank(methodName, "methodName can't be blank");

		for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
			Method[] methods = searchType.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}
		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	/**
	 * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 */
	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier
				.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	/**
	 * 通过反射, 获得Class定义中声明的泛型参数的类型, 注意泛型必须定义在父类处
	 * 如无法找到, 返回Object.class.
	 * eg.
	 * public UserDao extends HibernateDao<User>
	 *
	 * @param clazz The class to introspect
	 * @return the first generic declaration, or Object.class if cannot be determined
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getClassGenricType(final Class clazz) {
		return getClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
	 * 如无法找到, 返回Object.class.
	 * 
	 * 如public UserDao extends HibernateDao<User,Long>
	 *
	 * @param clazz clazz The class to introspect
	 * @param index the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or Object.class if cannot be determined
	 */
	public static Class getClassGenricType(final Class clazz, final int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			logger.warn("{}'s superclass not ParameterizedType",clazz.getSimpleName());
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			logger.warn("Index: {}, Size of {}'s Parameterized Type: {}",index,clazz.getSimpleName(), params.length);
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			logger.warn(" {} not set the actual class on superclass generic parameter",clazz.getSimpleName());
			return Object.class;
		}

		return (Class) params[index];
	}
	
	public static Class<?> getUserClass(Object instance) {
		if(instance == null){
			throw new BussinessException("Instance must not be null");
		}
		Class clazz = instance.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			Class<?> superClass = clazz.getSuperclass();
			if (superClass != null && !Object.class.equals(superClass)) {
				return superClass;
			}
		}
		return clazz;

	}
	
	/**
	 * 取得类中某个Field的类型名称
	 * @param clazz
	 * @param fieldName
	 * @return
	 */
	public static String getFieldTypeName(final Class clazz, String fieldName) {
		Field field = null;
		Class tclazz = clazz;
		do {
			try {
				field = tclazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				tclazz = clazz.getSuperclass();
			} catch (SecurityException e) {
			}

		} while (tclazz!= BaseEntity.class && tclazz != Object.class && field == null);
		
		return (field == null)?null:field.getType().getSimpleName();
	}
	
	/**
	 * 将反射时的checked exception转换为unchecked exception.
	 */
	public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
		if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
				|| e instanceof NoSuchMethodException) {
			return new IllegalArgumentException(e);
		} else if (e instanceof InvocationTargetException) {
			return new RuntimeException(((InvocationTargetException) e).getTargetException());
		} else if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		}
		return new RuntimeException("Unexpected Checked Exception.", e);
	}
	
	/**
	 * object 属性名称及属性值组装为String字符串。
	 * 组装规则：
	 * field.name1=field.value1&field.name2=field.value2 ...
	 * @param object
	 * @return
	 */
	public static String objToString(Object object) {
		Class<?> clazz = object.getClass();
		Field[] fss = new Field[0];
		for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
			try {
				Field[] fs = clazz.getDeclaredFields();
				fss = ArrayUtils.addAll(fss, fs);
			} catch (Exception e) {
				// 这里异常不能抛出去。
				// 如果这里的异常打印或者往外抛，就不会执行clazz = clazz.getSuperclass(),
				// 最后就不会进入到父类中了
			}
		}
		StringBuffer sb = new StringBuffer(50);
		for (Field f : fss) {
			// 反射对象中String类型，且不为常量的字段
			if (String.class.equals(f.getType()) && !isConstant(f.getModifiers())) {
				String fieldName = f.getName();
				Object o = invokeGetMethod(f.getDeclaringClass(), object, fieldName);
				String value = StringUtils.isNull(null==o?"":o.toString());
				if (value == "") {
					continue;
				}
				sb.append(fieldName + "=" + value + "&");
			}
		}
		logger.info("请求参数："+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 是否为常量
	 * @param modifiers
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean isConstant(int modifiers) {
		// static 和 final修饰
		if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
			return true;
		} 
		return false;
	}
	
	/**
	 * 校验参数类型 
	 * 目前只校验是否为 常量
	 * @param claszz
	 * @param name
	 * @return 常量返回true，非常量返回false
	 */
	private static boolean checkModifiers(Class<?> claszz, String name) {
		try {
			Field field = claszz.getField(name);
			if (isConstant(field.getModifiers())) {
				return true;
			}
		} catch (NoSuchFieldException | SecurityException e) {
			return false;
		} 
		return false;
	}
	
	/**
	 * 取得属性
	 * @param clazz
	 * @return
	 */
	public static Map<String, Field> getClassField(Class<?> clazz) {
		Field[] declaredFields = clazz.getDeclaredFields();
		Map<String, Field> fieldMap = new HashMap<String, Field>();
		Map<String, Field> superFieldMap = new HashMap<String, Field>();
		for (Field field : declaredFields) {
			fieldMap.put(field.getName(), field);
		}
		if (clazz.getSuperclass() != null) {
			superFieldMap = getClassField(clazz.getSuperclass());
		}
		fieldMap.putAll(superFieldMap);
		return fieldMap;
	}
	
}
