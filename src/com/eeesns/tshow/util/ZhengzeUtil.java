package com.eeesns.tshow.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ZhengzeUtil {
	public static boolean isNumber(String key) {
		String s = "^[0-9]+$";
		Pattern pattern = Pattern.compile(s);
		Matcher matcher = pattern.matcher(key);
		return matcher.matches();
	}

	public static String filterNoSecurity(String content) {
		if (content == null)
			return null;
		return content.replaceAll("<", "&lt");
	}
}
