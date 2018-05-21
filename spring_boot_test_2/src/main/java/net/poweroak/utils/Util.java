package net.poweroak.utils;

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
}