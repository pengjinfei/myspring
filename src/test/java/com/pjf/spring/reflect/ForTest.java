package com.pjf.spring.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ForTest {

	public static void main(String[] args) {
		ForReflect reflect=new ForReflect();
		Class<?> clazz=reflect.getClass();
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			for (Annotation[] annotations : parameterAnnotations) {
				System.out.println(annotations.length+"++++++++++++");
				for (Annotation annotation : annotations) {
					if(annotation instanceof AnnotA){
						AnnotA a=(AnnotA)annotation;
						System.out.println(a.value());
					}else {
						AnootB b=(AnootB)annotation;
						System.out.println(b.value());
					}
				}
			}
		}
	}
}
