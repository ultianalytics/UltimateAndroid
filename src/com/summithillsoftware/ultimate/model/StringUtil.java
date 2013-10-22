package com.summithillsoftware.ultimate.model;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;

public class StringUtil {
	private static StringUtil Current;
	
	private String ourCapitalized;
	
	static {
		Current = new StringUtil();
	}
	
	private StringUtil() {
		super();
		ourCapitalized = capitalizeWord(getString(R.string.common_our));
	}
	
	public static StringUtil current() {
		return Current;
	}
	
	public final static String getString(int resId) {
		return UltimateApplication.current().getString(resId);
	}
	
	public final static String getString(int resId, Object...formatArgs) {
		return UltimateApplication.current().getString(resId, formatArgs);
	}
	
	public String ourCaptialized() {
		return ourCapitalized;
	}
	
	public String capitalizeWord(String s) {
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}


}
