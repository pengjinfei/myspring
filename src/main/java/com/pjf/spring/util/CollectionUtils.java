package com.pjf.spring.util;

import java.util.Collection;

/**
 * 集合工具类
 * @author pengjinfei
 * @date 2015年12月26日
 */
public abstract class CollectionUtils {

	/**
	 * 判断一个集合是否为空
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		return (collection == null || collection.isEmpty());
	}
}
