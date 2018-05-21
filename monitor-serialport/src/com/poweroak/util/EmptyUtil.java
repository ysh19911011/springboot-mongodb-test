package com.poweroak.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 空值判断的辅助类
 * @author 
 */
public class EmptyUtil {
	/**
	 * 表示Sring类型的数据的空白值	*/
	public static final String Empty = "Empty_Value";
	
	/**
	 * 判断数组是否为空
	 * @param array 数组
	 * @return true，空; false，非空
	 */
	public static boolean isArrayEmpty(Object[] array) {
		if (array == null) {
			return true;
		}
		if (array.length == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断集合是否为空
	 * @param collection 集合
	 * @return
	 */
	public static boolean isCollectionEmpty(Collection<?> collection) {
		if (collection == null) {
			return true;
		}
		
		if (collection.size() == 0) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断List是否为空
	 * @since 2014-1-2 23:23:05
	 * @param list
	 * @return true，空; false，非空
	 */
	public static boolean isListEmpty(List<?> list) {
		return isCollectionEmpty(list);
	}
	
	/**
	 * 判断Map是否为空
	 * @since 2014-1-8 15:55:42
	 * @param map
	 * @return true，空; false，非空
	 */
	public static boolean isMapEmpty(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符是否为""或null
	 * @param string
	 * @return false: 非空，true: ""或null
	 */
	public static boolean isNullOrEmpty(String string) {
		if (string == null) {
			return true;
		}
		
		string = string.trim();
		if (string.equals("")) {
			return true;
		}
		if (string.equals(Empty)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 判断Set是否为空
	 * @param set Set集合
	 * @return
	 */
	public static boolean isSetEmpty(Set<?> set) {
		if (set == null) {
			return true;
		}
		
		return set.isEmpty();
	}
}
