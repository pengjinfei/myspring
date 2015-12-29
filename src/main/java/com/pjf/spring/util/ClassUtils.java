package com.pjf.spring.util;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public class ClassUtils {
	
	/** 包名的分隔符 */
	private static final String PACKAGE_SEPARATOR = ".";
	
	/** 文件名的分隔符 */
	private static final CharSequence PATH_SEPARATOR = "/";

	/** 数组类的后最 */
	private static final String ARRAY_SUFFIX = "[]";
	
	private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new HashMap<Class<?>, Class<?>>(8);

	private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new HashMap<Class<?>, Class<?>>(8);

	private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap<String, Class<?>>(32);
	
	private static final Map<String, Class<?>> commonClassCache = new HashMap<String, Class<?>>(32);
	
	static {
		primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
		primitiveWrapperTypeMap.put(Byte.class, byte.class);
		primitiveWrapperTypeMap.put(Character.class, char.class);
		primitiveWrapperTypeMap.put(Double.class, double.class);
		primitiveWrapperTypeMap.put(Float.class, float.class);
		primitiveWrapperTypeMap.put(Integer.class, int.class);
		primitiveWrapperTypeMap.put(Long.class, long.class);
		primitiveWrapperTypeMap.put(Short.class, short.class);

		for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
			primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
			registerCommonClasses(entry.getKey());
		}

		Set<Class<?>> primitiveTypes = new HashSet<Class<?>>(32);
		primitiveTypes.addAll(primitiveWrapperTypeMap.values());
		primitiveTypes.addAll(Arrays.asList(new Class<?>[] {
				boolean[].class, byte[].class, char[].class, double[].class,
				float[].class, int[].class, long[].class, short[].class}));
		primitiveTypes.add(void.class);
		for (Class<?> primitiveType : primitiveTypes) {
			primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
		}

		registerCommonClasses(Boolean[].class, Byte[].class, Character[].class, Double[].class,
				Float[].class, Integer[].class, Long[].class, Short[].class);
		registerCommonClasses(Number.class, Number[].class, String.class, String[].class,
				Object.class, Object[].class, Class.class, Class[].class);
		registerCommonClasses(Throwable.class, Exception.class, RuntimeException.class,
				Error.class, StackTraceElement.class, StackTraceElement[].class);
	}
	
	/**
	 * 获得默认的类加载器
	 * @return
	 */
	public static ClassLoader getDefaultClassLoader(){
		ClassLoader classLoader=null;
		try {
			classLoader=Thread.currentThread().getContextClassLoader();
		} catch (Exception e) {
		}
		if(classLoader==null){
			classLoader=ClassUtils.class.getClassLoader();
			if(classLoader==null){
				try {
					classLoader=ClassLoader.getSystemClassLoader();
				} catch (Exception e) {
				}
			}
		}
		return classLoader;
	}

	private static void registerCommonClasses(Class<?>...commonClasses) {
		for(Class<?> clazz:commonClasses){
			commonClassCache.put(clazz.getName(), clazz);
		}
	}

	/**
	 * 获得类的路径，以加载资源
	 * @param clazz
	 * @return
	 */
	public static String classPackageAsResourcePath(Class<?> clazz) {
		if(clazz==null){
			return "";
		}
		String className=clazz.getName();
		int packageEndIndex=className.lastIndexOf(PACKAGE_SEPARATOR);
		if(packageEndIndex==-1){
			return "";
		}
		String packageName=className.substring(0,packageEndIndex);
		return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}

	/**
	 * 获得一个类的描述
	 * @param value
	 * @return
	 */
	public static String getDescriptiveType(Object value) {
		if(value==null){
			return null;
		}
		Class<?> clazz=value.getClass();
		if(Proxy.isProxyClass(clazz)){
			StringBuilder result=new StringBuilder(clazz.getName());
			result.append(" implementing ");
			Class<?>[] interfaces = clazz.getInterfaces();
			for(int i=0;i<interfaces.length;i++){
				result.append(interfaces[i].getName());
				if(i<interfaces.length-1){
					result.append(",");
				}
			}
			return result.toString();
		}
		else if (clazz.isArray()) {
			return getQualifiedNameForArray(clazz);
		}
		else {
			return clazz.getName();
		}
	}

	private static String getQualifiedNameForArray(Class<?> clazz) {
		StringBuilder result=new StringBuilder();
		while(clazz.isArray()){
			clazz=clazz.getComponentType();
			result.append(ClassUtils.ARRAY_SUFFIX);
		}
		result.insert(0,clazz.getName());
		return result.toString();
	}

	/**
	 * 获得一个类的qualified name
	 * @param clazz
	 * @return
	 */
	public static String getQualifiedName(Class<?> clazz) {
		Assert.notNull(clazz, "Class must not be null");
		if(clazz.isArray()){
			return getQualifiedNameForArray(clazz);
		}else {
			return clazz.getName();
		}
	}

	/**
	 * 检测一个类是否匹配一个类名
	 * @param clazz
	 * @param typeName
	 * @return
	 */
	public static boolean matchesTypeName(Class<?> clazz, String typeName) {
		return (typeName != null &&
				(typeName.equals(clazz.getName()) || typeName.equals(clazz.getSimpleName()) ||
				(clazz.isArray() && typeName.equals(getQualifiedNameForArray(clazz)))));
	}

	/**
	 * 检查是否可以将左边的类对象赋值给右边的对象
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean isAssignableValue(Class<?> type, Object value) {
		Assert.notNull(type, "Type must not be null");
		return (value != null ? isAssignable(type, value.getClass()) : !type.isPrimitive());
	}

	/**
	 * 检查是否可以将左边的类对象赋值给右边的类
	 * @param lType
	 * @param rType
	 * @return
	 */
	public static boolean isAssignable(Class<?> lType, Class<?> rType) {
		Assert.notNull(lType, "Left-hand side type must not be null");
		Assert.notNull(rType, "Right-hand side type must not be null");
		if(lType.isAssignableFrom(rType)){
			return true;
		}
		if (lType.isPrimitive()) {
			Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rType);
			if (resolvedPrimitive != null && lType.equals(resolvedPrimitive)) {
				return true;
			}
		}
		else {
			Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rType);
			if (resolvedWrapper != null && lType.isAssignableFrom(resolvedWrapper)) {
				return true;
			}
		}
		return false;
	}
}
