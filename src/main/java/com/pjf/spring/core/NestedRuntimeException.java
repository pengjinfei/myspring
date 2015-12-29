package com.pjf.spring.core;

/**
 * @description 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public abstract class NestedRuntimeException extends RuntimeException{

	private static final long serialVersionUID = 8354758879309647952L;

	public NestedRuntimeException(String message) {
		super(message);
	}

	public NestedRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	@Override
	public String getMessage() {
		return NestedExceptionUtils.buildMessage(super.getMessage(), getCause());
	}

	/**
	 * 获得内置的异常
	 * @return 内置的异常，不存在返回null
	 */
	public Throwable getRootCause() {
		Throwable rootCause=null;
		Throwable cause = getCause();
		while (cause!=null&&cause!=rootCause) {
			rootCause=cause;
			cause=rootCause.getCause();
		}
		return rootCause;
	}
	
	/**
	 * 获得异常的原因，与{@link #getRootCause()}的区别是：如果有没有内置异常，返回异常本身。
	 * @return
	 */
	public Throwable getMostSpecificCause(){
		Throwable rootCause = getRootCause();
		return rootCause==null?this:rootCause;
	}
	
	/**
	 * 查找是否包含某指定类型的异常
	 * @param exType 查找的异常类型
	 * @return 
	 */
	public boolean contains(Class<?> exType) {
		if (exType==null) {
			return false;
		}
		if(exType.isInstance(this)){
			return true;
		}
		Throwable cause = getCause();
		if(cause==this){
			return false;
		}
		if(cause instanceof NestedRuntimeException){
			return ((NestedRuntimeException)cause).contains(exType);
		}
		else {
			while (cause!=null) {
				if(exType.isInstance(cause)){
					return true;
				}
				if(cause.getCause()==cause){
					return false;
				}
				cause=cause.getCause();
			}
			return false;
		}
	}
	
}
