package com.rd.account.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DomainExecUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(DomainExecUtils.class);

	/**
	 * 
	 * 调用自己的方法 ownerClass.getDeclaredMethods()
	 * @author jxx
	 * @date 2016年10月18日
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object setObj(Object obj) {
		Class<Object> ownerClass = (Class<Object>) obj.getClass();

		// Field[] fields = ownerClass.getDeclaredFields();
		// for(Field fieldsss : fields){
		// LOGGER.info("field "+fieldsss.getName());
		// }
		//
		Method[] methods = ownerClass.getDeclaredMethods();

		doMethod(methods, obj);

		return obj;
	}

	/**
	 * 
	 * 调用父类的方法 ownerClass.getMethods()
	 * @author jxx
	 * @date 2016年10月18日
	 * @param obj
	 * @return
	 */
	public static Object setParentObj(Object obj) {
		Class<Object> ownerClass = (Class<Object>) obj.getClass();

		Method[] methods = ownerClass.getMethods();

		doMethod(methods, obj);

		return obj;
	}

	/**
	 * 
	 * 执行set，get，is,preInsert,preUpdate，instance，prototype等方法
	 * @author jxx
	 * @date 2016年10月18日
	 * @param methods
	 * @param obj
	 * @return
	 */
	public static Object doMethod(Method[] methods, Object obj) {
		String methodName = null;
		String filedName = null;
		try {
			for (Method method : methods) {
				methodName = method.getName();
				// LOGGER.info("method " + methodName);
				if (methodName.startsWith("set")) {
					filedName = methodName.substring(3, 4).toLowerCase() + methodName.substring(4);

					Class[] classes = method.getParameterTypes();
					// LOGGER.info("classes[0].getName()" +
					// classes[0].getName());
					// LOGGER.info("filedName " + filedName);
					if (classes[0].getName().equals("java.lang.String")) {
						if (filedName.indexOf("Time") != -1) {
							method.invoke(obj, "2016-09-27");
							continue;
						} else {
							method.invoke(obj, filedName);
							continue;
						}
					}

					if (classes[0].getName().equals("java.lang.Integer") || classes[0].getName().equals("int")) {
						method.invoke(obj, 0);
						continue;
					}
					if (classes[0].getName().equals("java.lang.Long") || classes[0].getName().equals("long")) {
						method.invoke(obj, 1L);
						continue;
					}
					if (classes[0].getName().equals("java.util.Date")) {
						method.invoke(obj, new Date());
						continue;
					}

					if (classes[0].getName().equals("java.math.BigDecimal")) {
						method.invoke(obj, new BigDecimal(0));
						continue;
					}

					if (classes[0].getName().equals("double")) {
						method.invoke(obj, 0.0);
						continue;
					}
					if (classes[0].getName().equals("byte")) {
						method.invoke(obj, (byte) 0);
						continue;
					}

					if (classes[0].getName().equals("java.util.List")) {
						method.invoke(obj, new ArrayList<>());
						continue;
					}
					if (classes[0].getName().equals("[Ljava.lang.String;")) {
						String[] names = new String[] { "A", "B", "C" };
						method.invoke(obj, new Object[] { names });
						continue;
					}
					if (classes[0].getName().equals("boolean") || classes[0].getName().equals("java.lang.Boolean")) {
						method.invoke(obj, true);
						continue;
					}

					/*if (classes[0].getName().equals("com.rd.ifaes.core.user.domain.User")) {
						method.invoke(obj, new User());
						continue;
					}

					if (classes[0].getName().equals("com.rd.ifaes.common.orm.Page")) {
						method.invoke(obj, new Page());
						continue;
					}*/

					// method.invoke(obj, filedName);
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error("doMethod {}", e.getMessage());
		}

		try {
			for (Method method : methods) {
				methodName = method.getName();
				// LOGGER.info("method " + methodName);
				try {
					if (methodName.startsWith("get")) {
						// LOGGER.info();
						method.invoke(obj);
						// LOGGER.info("method.invoke(obj); " +
						// method.invoke(obj));
						continue;
					}

					if (methodName.startsWith("is")) {
						if (0x00000002 == method.getModifiers()) {

						} else {
							Class[] cla = method.getParameterTypes();
							if (cla == null || cla.length == 0) {
								// LOGGER.info();
								method.invoke(obj);
								// LOGGER.info("method.invoke(obj); " +
								// method.invoke(obj));
							}
						}
						continue;
					}

					if (methodName.startsWith("preInsert") || methodName.startsWith("preUpdate")) {
						// LOGGER.info();
						method.invoke(obj);
						// LOGGER.info("method.invoke(obj); " +
						// method.invoke(obj));
						continue;
					}
					if (methodName.equals("instance")) {
						Class[] cla = method.getParameterTypes();
						if (cla == null || cla.length == 0) {
							// LOGGER.info();
							method.invoke(obj);
							// LOGGER.info("method.invoke(obj); " +
							// method.invoke(obj));
							continue;
						}
					}
					if (methodName.equals("prototype")) {
						// LOGGER.info();
						method.invoke(obj);
						// LOGGER.info("method.invoke(obj); " +
						// method.invoke(obj));
						continue;
					}
				} catch (Exception e) {
					// e.printStackTrace();
					LOGGER.error("doMethod,{},{}", methodName, e.getMessage());
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error("doMethod,{}", e.getMessage());
		}
		return obj;
	}

	public static void main(String[] args) {
		//Answer answer = new Answer();
		// LOGGER.info(DomainExecUtils.genObj(answer));
	}

	/**
	 * 
	 * 执行覆盖构造函数，返回值无意义
	 * @author jxx
	 * @date 2016年10月18日
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object genObj(Object obj) {
		Class<Object> ownerClass = (Class<Object>) obj.getClass();
		Constructor[] constructors = ownerClass.getDeclaredConstructors();
		List list = new ArrayList();
		try {
			for (Constructor constructor : constructors) {
				list = new ArrayList();
				Class[] classs = constructor.getParameterTypes();
				int i = 0;
				for (Class classt : classs) {
					i++;
					if (classt.getName().equals("java.lang.String")) {
						list.add("stringmock");
						continue;
					}

					if (classt.getName().equals("java.lang.Integer") || classt.getName().equals("int")) {
						list.add(0);
						continue;
					}
					if (classt.getName().equals("java.util.Date")) {
						list.add(new Date());
						continue;
					}

					if (classt.getName().equals("java.math.BigDecimal")) {
						list.add(new BigDecimal(0));
						continue;
					}

					if (classt.getName().equals("java.lang.Double") || classt.getName().equals("double")) {
						list.add(0.0);
						continue;
					}
					if (classt.getName().equals("java.lang.Long") || classt.getName().equals("long")) {
						list.add(0L);
						continue;
					}
					if (classt.getName().equals("byte")) {
						list.add((byte) 0);
						continue;
					}
				}
				try {
					if (i != 0) {
						obj = constructor.newInstance(list.toArray());
					} else {
						obj = constructor.newInstance();
					}
				} catch (Exception e) {
					// e.printStackTrace();
					LOGGER.error("genObj constructor newInstance,{}", e.getMessage());
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			LOGGER.error("genObj {}", e.getMessage());
		}

		return obj;
	}

	/**
	 * 
	 * 执行toString()方法
	 * @author jxx
	 * @date 2016年10月18日
	 * @param obj
	 * @return
	 */
	public static Object execOther(Object obj) {
		obj.toString();
		return obj;
	}

	/**
	 * 
	 * domain类覆盖使用
	 * @author jxx
	 * @date 2016年10月18日
	 * @param obj
	 * @return
	 */
	public static Object exec(Object obj) {
		obj = setObj(obj);
		obj = genObj(obj);
		obj = execOther(obj);
		return obj;
	}

	/**
	 * 
	 * 根据无参构造函数生成对象
	 * @author jxx
	 * @date 2016年10月18日
	 * @param cla
	 * @return
	 */
	public static <T> T genObject(Class<T> cla) {
		T obj = null;

		Constructor c0 = null;
		try {
			c0 = cla.getDeclaredConstructor();
			c0.setAccessible(true);

		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.error("getDeclaredConstructor {}", e.getMessage());
		}
		try {
			obj =(T) c0.newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error("newInstance {}", e.getMessage());
		}

		return obj;
	}
}
