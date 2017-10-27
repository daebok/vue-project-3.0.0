package com.rd.ifaes.common.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.*;
import org.springframework.util.CollectionUtils;

import com.rd.ifaes.common.util.ReflectionUtils;
import com.rd.ifaes.common.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 切面编程工具类
 * @author lh
 * @version 3.0
 * @since 2016-8-26
 */
public class AopUtils {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(AopUtils.class);
	private static final String DESC_DOUBLE = "D";
	private static final String DESC_SHORT = "J";
	
	private AopUtils() {	}
	
	/**
	 * <p>获取方法的参数名</p>
	 *
	 * @param m
	 * @return
	 */
	public static String[] getMethodParamNames(final Method m) {
		final String[] paramNames = new String[m.getParameterTypes().length];
		final String n = m.getDeclaringClass().getName();
		final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		String className = m.getDeclaringClass().getSimpleName();
		ClassReader cr = null;
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = Class.forName(n).getResourceAsStream(className + ".class");
			cr = new ClassReader(resourceAsStream);
		} catch (IOException | ClassNotFoundException e) {
			LOGGER.warn(e.getMessage(), e);
		} finally {
			if (resourceAsStream != null) {
				try {
					resourceAsStream.close();
				} catch (IOException e) {
					LOGGER.warn(e.getMessage(), e);
				}
			}
		}

		if (cr != null) {
			cr.accept(new ClassVisitor(Opcodes.ASM4, cw) {
				@Override
				public MethodVisitor visitMethod(final int access,
						final String name, final String desc,
						final String signature, final String[] exceptions) {
					final Type[] args = Type.getArgumentTypes(desc);
					// 方法名相同并且参数个数相同
					if (!name.equals(m.getName())
							|| !sameType(args, m.getParameterTypes())) {
						return super.visitMethod(access, name, desc, signature,
								exceptions);
					}
					MethodVisitor v = cv.visitMethod(access, name, desc, signature,
							exceptions);
					return new MethodVisitor(Opcodes.ASM4, v) {
						
						int fixCount = 0;//步长修正计数器
						
						@Override
						public void visitLocalVariable(String name, String desc,
								String signature, Label start, Label end, int index) {
							int i = index - 1;
							// 如果是静态方法，则第一就是参数
							// 如果不是静态方法，则第一个是"this"，然后才是方法的参数
							if (Modifier.isStatic(m.getModifiers())) {
								i = index;
							}
							if (i > fixCount) {
								i -= fixCount;
							}
							if(desc.equals(DESC_SHORT) || desc.equals(DESC_DOUBLE)){
								fixCount++;
							}
							if (i >= 0 && i < paramNames.length) {
								paramNames[i] = name;
							}
							super.visitLocalVariable(name, desc, signature, start,
									end, index);
						}
						
					};
				}
			}, 0);			
		}
		return paramNames;
	}

	/**
	 * <p>比较参数类型是否一致</p>
	 *
	 * @param types   asm的类型({@link Type})
	 * @param clazzes java 类型({@link Class})
	 * @return
	 */
	private static boolean sameType(Type[] types, Class<?>[] clazzes) {
		// 个数不同
		if (types.length != clazzes.length) {
			return false;
		}

		for (int i = 0; i < types.length; i++) {
			if (!Type.getType(clazzes[i]).equals(types[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 取得切面调用的方法
	 * @param pjp
	 * @return
	 */
	public static Method getMethod(ProceedingJoinPoint pjp){
		Signature sig = pjp.getSignature();
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        MethodSignature msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Method currentMethod = null;
		try {
			currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
		} catch (NoSuchMethodException | SecurityException e) {
			LOGGER.warn(e.getMessage(), e);
		}       
        return currentMethod;
	}
	
	public static List<String> getMatcher(String regex, String source) {  
        List<String> list = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        while (matcher.find()) {  
            list.add(matcher.group());
        }  
        return list;  
    }
	
	/**
	 * 取得注解参数
		(?=exp)	匹配exp前面的位置
		(?<=exp)	匹配exp后面的位置
		(?!exp)	匹配后面跟的不是exp的位置
		(?<!exp)	匹配前面不是exp的位置
	 * @param managers
	 * @return
	 */
	public static List<String> getAnnoParams(String source){
		String regex = "(?<=\\{)(.+?)(?=\\})";
        return getMatcher(regex, source);
    }
	
	/**
	 * 获取缓存的key值
	 * 
	 * @param pjp
	 * @param key
	 * @return
	 */
	public static String getCacheKey(final ProceedingJoinPoint pjp, final String key) {

		StringBuilder buf = new StringBuilder();
		final Object[] args = pjp.getArgs();
		
		if(StringUtils.isNotBlank(key)){
			buf.append(key);
			List<String> annoParamNames = AopUtils.getAnnoParams(key);
			String[] methodParamNames = AopUtils.getMethodParamNames(AopUtils.getMethod(pjp));
			if(!CollectionUtils.isEmpty(annoParamNames)){
				for (String ap : annoParamNames) {
					buf = replaceParam(buf, args, methodParamNames, ap);
				}				
			}
			
		}else{
			buf.append(pjp.getSignature().getDeclaringTypeName()).append(":").append(pjp.getSignature().getName());
			for (Object arg : args) {
				buf.append(":").append(arg.toString());
			}
		}	

		return buf.toString();
	}

	/**
	 * 替换占位参数
	 * @param buf
	 * @param args
	 * @param methodParamNames
	 * @param ap
	 * @return
	 */
	private static StringBuilder replaceParam(StringBuilder buf, final Object[] args, String[] methodParamNames, String ap) {
		StringBuilder builder = new StringBuilder(buf);
		String paramValue = "";
		for (int i = 0; i < methodParamNames.length; i++) {
			if(ap.startsWith(methodParamNames[i])){
				final Object arg = args[i];
				if (ap.contains(".")) {
					paramValue = String.valueOf(ReflectionUtils.invokeGetter(arg, ap.substring(ap.indexOf('.') + 1)));
				} else {
					paramValue = String.valueOf(arg);
				}
				break;
			}
		}
		int start = builder.indexOf("{" + ap);
		int end = start + ap.length() + 2;
		builder =builder.replace(start, end, paramValue);
		return builder;
	}
	

}