package com.xdelta3.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author	biao.tang
 * 2019年1月3日
 */
public class DateTimeUtil {
	
	public static String defaultFormat = "yyyy-MM-dd HH:mm:ss";
	
	public static String formatOne = "yyyyMMddHHmmssSSS";

	public static String formatDay = "yyyy-MM-dd";
	
	/**
     * Number of milliseconds in a standard second.
     */
	public static final long MILLIS_PER_SECOND = 1000;
    /**
     * Number of milliseconds in a standard minute.
     */
    public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
    /**
     * Number of milliseconds in a standard hour.
     */
    public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
    /**
     * Number of milliseconds in a standard day.
     */
    public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
    
    /**
     *	format date
     */
	public static String format(Date date) {
		return format(date, defaultFormat);
	}
	
	/**
	 *	format date
	 */
	public static String format(Date date, String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
}
