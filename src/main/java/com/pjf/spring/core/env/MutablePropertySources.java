package com.pjf.spring.core.env;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pjf.spring.util.StringUtils;

/**
 * 
 * @author pengjinfei
 * @date 2015年12月27日
 */
public class MutablePropertySources implements PropertySources {

	private final Logger logger;

	private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<PropertySource<?>>();

	public MutablePropertySources() {
		this.logger = LoggerFactory.getLogger(getClass());
	}

	public MutablePropertySources(PropertySources propertySources) {
		this();
		for (PropertySource<?> propertySource : propertySources) {
			addLast(propertySource);
		}
	}

	MutablePropertySources(Logger logger) {
		this.logger = logger;
	}

	public Iterator<PropertySource<?>> iterator() {
		return this.propertySourceList.iterator();
	}

	public boolean contains(String name) {
		return this.propertySourceList.contains(PropertySource.named(name));
	}

	public PropertySource<?> get(String name) {
		int index = this.propertySourceList.indexOf(PropertySource.named(name));
		return (index == -1 ? null : this.propertySourceList.get(index));
	}

	protected void removeIfPresent(PropertySource<?> propertySource) {
		this.propertySourceList.remove(propertySource);
	}

	protected void assertLegalRelativeAddition(String relativePropertySourceName, PropertySource<?> propertySource) {
		String newPropertySourceName = propertySource.getName();
		if (relativePropertySourceName.equals(newPropertySourceName)) {
			throw new IllegalArgumentException(String
					.format("PropertySource named [%s] cannot be added relative to itself", newPropertySourceName));
		}
	}

	protected int assertPresentAndGetIndex(String name) {
		int index = this.propertySourceList.indexOf(PropertySource.named(name));
		if (index == -1) {
			throw new IllegalArgumentException(String.format("PropertySource named [%s] does not exist", name));
		}
		return index;
	}

	public void addFirst(PropertySource<?> propertySource) {
		logger.debug(
				String.format("Adding [%s] PropertySource with highes search precedence", propertySource.getName()));
		removeIfPresent(propertySource);
		this.propertySourceList.add(propertySource);
	}

	public void addLast(PropertySource<?> propertySource) {
		logger.debug(
				String.format("Adding [%s] PropertySource with lowest search precedence", propertySource.getName()));
		removeIfPresent(propertySource);
		this.propertySourceList.add(propertySource);
	}

	public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately higher than [%s]",
				propertySource.getName(), relativePropertySourceName));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index, propertySource);
	}

	private void addAtIndex(int index, PropertySource<?> propertySource) {
		removeIfPresent(propertySource);
		this.propertySourceList.add(index, propertySource);
	}

	public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
		logger.debug(String.format("Adding [%s] PropertySource with search precedence immediately lower than [%s]",
				propertySource.getName(), relativePropertySourceName));
		assertLegalRelativeAddition(relativePropertySourceName, propertySource);
		removeIfPresent(propertySource);
		int index = assertPresentAndGetIndex(relativePropertySourceName);
		addAtIndex(index + 1, propertySource);
	}

	public int precedenceOf(PropertySource<?> propertySource) {
		return this.propertySourceList.indexOf(propertySource);
	}

	public PropertySource<?> remove(String name) {
		logger.debug(String.format("Removing [%s] PropertySource", name));
		int index = this.propertySourceList.indexOf(PropertySource.named(name));
		return (index != -1 ? this.propertySourceList.remove(index) : null);
	}

	public void replace(String name, PropertySource<?> propertySource) {
		logger.debug(String.format("Replacing [%s] PropertySource with [%s]", name, propertySource.getName()));
		int index = assertPresentAndGetIndex(name);
		this.propertySourceList.set(index, propertySource);
	}
	
	public int size() {
		return this.propertySourceList.size();
	}
	
	@Override
	public String toString() {
		String[] names = new String[this.size()];
		for (int i = 0; i < size(); i++) {
			names[i] = this.propertySourceList.get(i).getName();
		}
		return String.format("[%s]", StringUtils.arrayToCommaDelimitedString(names));
	}
}
