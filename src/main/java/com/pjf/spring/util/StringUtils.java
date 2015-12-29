package com.pjf.spring.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * String工具类
 * 
 * @author pengjinfei
 * @date 2015年12月26日
 */
public abstract class StringUtils {

	private static final String FOLDER_SEPARATOR = "/";

	private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

	private static final String TOP_PATH = "..";

	private static final String CURRENT_PATH = ".";

	private static final char EXTENSION_SEPARATOR = '.';

	/**
	 * 判断一个string是否为null或者长度为0
	 * 
	 * @param string
	 * @return
	 */
	public static boolean hasLength(String string) {
		return hasLength((CharSequence) string);
	}

	public static boolean hasLength(CharSequence string) {
		return string != null && string.length() > 0;
	}

	/**
	 * 判断一个给定的CharSequence是否包含字符（不能为空或者全为空格）
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(CharSequence str) {
		if (!hasLength(str)) {
			return false;
		}
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 将inString中的oldPattern替换为newPattern
	 * 
	 * @param inString
	 * @param oldPattern
	 * @param newPattern
	 * @return
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (!hasLength(inString) || !hasLength(oldPattern) || hasLength(newPattern)) {
			return inString;
		}
		StringBuilder stringBuilder = new StringBuilder();
		int pos = 0;
		int index = inString.indexOf(oldPattern);
		int patLen = oldPattern.length();
		while (index >= 0) {
			stringBuilder.append(inString.substring(pos, index));
			stringBuilder.append(newPattern);
			pos += index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		stringBuilder.append(inString.substring(pos));
		return stringBuilder.toString();
	}

	/**
	 * Normalize the path by suppressing sequences like "path/.." and inner
	 * simple dots.
	 * <p>
	 * The result is convenient for path comparison. For other uses, notice that
	 * Windows separators ("\") are replaced by simple slashes.
	 * 
	 * @param path
	 *            the original path
	 * @return the normalized path
	 */
	public static String cleanPath(String path) {
		if (path == null) {
			return null;
		}
		String pathToUse = replace(path, WINDOWS_FOLDER_SEPARATOR, FOLDER_SEPARATOR);

		// Strip prefix from path to analyze, to not treat it as part of the
		// first path element. This is necessary to correctly parse paths like
		// "file:core/../core/io/Resource.class", where the ".." should just
		// strip the first "core" directory while keeping the "file:" prefix.
		int prefixIndex = pathToUse.indexOf(":");
		String prefix = "";
		if (prefixIndex != -1) {
			prefix = pathToUse.substring(0, prefixIndex + 1);
			if (prefix.contains("/")) {
				prefix = "";
			} else {
				pathToUse = pathToUse.substring(prefixIndex + 1);
			}
		}
		if (pathToUse.startsWith(FOLDER_SEPARATOR)) {
			prefix = prefix + FOLDER_SEPARATOR;
			pathToUse = pathToUse.substring(1);
		}

		String[] pathArray = delimitedListToStringArray(pathToUse, FOLDER_SEPARATOR);
		List<String> pathElements = new LinkedList<String>();
		int tops = 0;

		for (int i = pathArray.length - 1; i >= 0; i--) {
			String element = pathArray[i];
			if (CURRENT_PATH.equals(element)) {
				// Points to current directory - drop it.
			} else if (TOP_PATH.equals(element)) {
				// Registering top path found.
				tops++;
			} else {
				if (tops > 0) {
					// Merging path element with element corresponding to top
					// path.
					tops--;
				} else {
					// Normal path element found.
					pathElements.add(0, element);
				}
			}
		}

		// Remaining top paths need to be retained.
		for (int i = 0; i < tops; i++) {
			pathElements.add(0, TOP_PATH);
		}

		return prefix + collectionToDelimitedString(pathElements, FOLDER_SEPARATOR);
	}

	/**
	 * 将一个集合转化为String，集合的元素之间以delim分隔
	 * 
	 * @param coll
	 * @param delimiter
	 * @return
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delimiter) {
		return collectionToDelimitedString(coll, delimiter, "", "");
	}

	/**
	 * 将一个集合转化为String，集合的元素之间以delim分隔，并增加前缀prefix和后缀suffix
	 * 
	 * @param coll
	 * @param delim
	 * @param prefix
	 * @param suffix
	 * @return
	 */
	public static String collectionToDelimitedString(Collection<?> coll, String delim, String prefix, String suffix) {
		if (CollectionUtils.isEmpty(coll)) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			sb.append(prefix).append(it.next()).append(suffix);
			if (it.hasNext()) {
				sb.append(delim);
			}
		}
		return sb.toString();
	}

	/**
	 * 将一个集合转化为String，集合的元素之间以","分隔
	 * 
	 * @param coll
	 * @return
	 */
	public static String collectionToCommaDelimitedString(Collection<?> coll) {
		return collectionToDelimitedString(coll, ",");
	}

	/**
	 * 将一个String以delimiter分割，转化为String数组
	 * 
	 * @param string
	 * @param delimiter
	 * @return
	 */
	public static String[] delimitedListToStringArray(String string, String delimiter) {
		return delimitedListToStringArray(string, delimiter, null);
	}

	/**
	 * 将一个String以delimiter分割，转化为String数组,并删除指定的字符集charsToDelete
	 * 
	 * @param str
	 * @param delimiter
	 * @param charsToDelete
	 * @return
	 */
	public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
		if (str == null) {
			return new String[0];
		}
		if (delimiter == null) {
			return new String[] { str };
		}
		List<String> result = new ArrayList<String>();
		if ("".equals(delimiter)) {
			for (int i = 0; i < str.length(); i++) {
				result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
			}
		} else {
			int pos = 0;
			int delPos;
			while ((delPos = str.indexOf(delimiter, pos)) != -1) {
				result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
				pos = delPos + delimiter.length();
			}
			if (str.length() > 0 && pos <= str.length()) {
				// Add rest of String, but not in case of empty input.
				result.add(deleteAny(str.substring(pos), charsToDelete));
			}
		}
		return toStringArray(result);
	}

	/**
	 * 将一个字符list转化为字符数组
	 * 
	 * @param collection
	 * @return
	 */
	public static String[] toStringArray(List<String> collection) {
		if (collection == null) {
			return null;
		}
		return collection.toArray(new String[collection.size()]);
	}

	/**
	 * 删除一个String中的指定字符集
	 * 
	 * @param substring
	 * @param charsToDelete
	 * @return
	 */
	public static String deleteAny(String inString, String charsToDelete) {
		if (!hasLength(inString) || !hasLength(charsToDelete)) {
			return inString;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < inString.length(); i++) {
			char c = inString.charAt(i);
			if (charsToDelete.indexOf(c) == -1) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 根据一个指定的path和一个相对地址relativPath，获得一个绝对地址
	 * @param path
	 * @param relativePath
	 * @return
	 */
	public static String applyRelativePath(String path, String relativePath) {
		int separatorIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (separatorIndex != -1) {
			String newPath = path.substring(0, separatorIndex);
			if (!relativePath.startsWith(FOLDER_SEPARATOR)) {
				newPath += FOLDER_SEPARATOR;
			}
			return newPath + relativePath;
		} else {
			return relativePath;
		}
	}

	/**
	 * 获得一个指定路径中的文件名
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if(path==null){
			return null;
		}
		int separatorIndex=path.lastIndexOf(FOLDER_SEPARATOR);
		return (separatorIndex==-1?path:path.substring(separatorIndex+1));
	}

	/**
	 * 将一个对象数组转化为String，使用","作为分隔符
	 * @param arr
	 * @return
	 */
	public static Object arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	/**
	 * 将一个对象数组转化为String，使用delim作为分隔符
	 * @param arr
	 * @param delim
	 * @return
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		if(ObjectUtils.isEmpty(arr)){
			return "";
		}
		if(arr.length==1){
			return ObjectUtils.nullSafeToString(arr[0]);
		}
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<arr.length;i++){
			if(i>0){
				sb.append(delim);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}
}
