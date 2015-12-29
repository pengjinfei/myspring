package com.pjf.spring.beans;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月28日
 */
public interface Mergeable {

	boolean isMergeEnabled();

	Object merge(Object parent);

}
