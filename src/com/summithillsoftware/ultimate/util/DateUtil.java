package com.summithillsoftware.ultimate.util;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


@SuppressLint("SimpleDateFormat")
public class DateUtil {
	
	public static final boolean isToday(Date date) {
		Calendar today = Calendar.getInstance(); 
		Calendar other = Calendar.getInstance();
		other.setTime(date); 
		return (today.get(Calendar.YEAR) == other.get(Calendar.YEAR)
		  && today.get(Calendar.DAY_OF_YEAR) == other.get(Calendar.DAY_OF_YEAR));
	}
	
	public static String toUtcString(Date date) {
		return getUtcDateFormat().format(date);
	}
	
	public static Date fromUtcString(String utcFormattedDate) {
		try {
			return getUtcDateFormat().parse(utcFormattedDate);
		} catch (ParseException e) {
			UltimateLogger.logError("Unable to parse date", e);
			return null;
		}
	}
	
	private static DateFormat getUtcDateFormat() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
		df.setTimeZone(tz);
		return df;
	}


}
