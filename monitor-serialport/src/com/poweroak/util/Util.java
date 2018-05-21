package com.poweroak.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;



/**
 * 校验工具类
 */
public class Util {

	public static boolean checkNotNull(final Object value) {
		if (value == null) {
			return false;
		}
		return (value.toString().length() > 0);
	}

	public static boolean checkNull(final Object value) {
		if (value == null) {
			return true;
		}

		return (value.toString().length() == 0);

	}

	public static boolean checkNotNull(final Object[] values) {
		if (checkNull(values))
			return false;
		if (values.length <= 0)
			return false;
		for (Object value : values) {
			if (!checkNotNull(value)) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkNotNull(final String[] values) {
		if (values == null || values.length <= 0)
			return false;
		for (String value : values) {
			if (!checkNotNull(value)) {
				return false;
			}
		}
		return true;
	}

	public static String getNumber() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String a = formatter.format(new Date());
		Random rnd = new Random();
		int num = 100 + rnd.nextInt(900);
		return a + num;
	}
	
	public static boolean includeChinese(String str){
		boolean flag = false;
		for (int i = 0; i < str.length(); i++) {
			flag = str.substring(i,i+1).matches("[\\u4e00-\\u9fa5]+");
			if(flag){
				return flag;
			}
		}
		return flag;
	}
	/**
	 * 将16进制数据转换成16进制字节数组
	 * @param source 形如“58 0A 00 00 38 FF”的16进制数据
	 * @return
	 */
	public static byte[] prepareHexBytes(String source) {
		if (EmptyUtil.isNullOrEmpty(source)) {
			return null;
		}
		String[] hexArray = source.split(" ");
		byte[] bytes = new byte[hexArray.length];
		
		for (int i = 0; i < hexArray.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hexArray[i], 16);
		}
		
		return bytes;
	}
	/**
	 * 字节数组转16进制字符
	 * @param bytes 字节数字
	 * @param spaceFormatted 是否采用空格格式化，最终形如：01 03 00 00 00 0A C5 CD
	 */
	public static String ByteArrayToHexadecimal(byte[] bytes, boolean spaceFormatted) {
		String string;
		StringBuffer stringBuffer = new StringBuffer();   
	      
	    for (int i = 0; i < bytes.length; i++) {   
	    	string = Integer.toHexString(0xFF & bytes[i]);   
	    	if (string.length() < 2) {
	    		string = "0" + string;
	    	}
	    	if (spaceFormatted && i > 0) {
	    		string = " " + string;
			}
	    	
	    	stringBuffer.append(string.toUpperCase());   
	    }
	    
	    return stringBuffer.toString();   
	}
}