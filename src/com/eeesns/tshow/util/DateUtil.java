package com.eeesns.tshow.util;

import java.text.SimpleDateFormat;

public class DateUtil {
	public static String formatDate(){
		SimpleDateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date da = new java.util.Date();
		String strdate=dateFormat.format(da);
		return strdate;
	}

}
