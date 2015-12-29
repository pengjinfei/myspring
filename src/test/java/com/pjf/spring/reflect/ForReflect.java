package com.pjf.spring.reflect;

public class ForReflect {

	@AnnotA("hello")
	@AnootB("howareyou")
	public String getName(@AnnotA("str1") String str1,@AnootB("str2") Integer integer) {
		return "test";
	}
}
