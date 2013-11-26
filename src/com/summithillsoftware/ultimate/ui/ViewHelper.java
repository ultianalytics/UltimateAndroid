package com.summithillsoftware.ultimate.ui;

import android.view.View;
import android.view.ViewGroup;

public class ViewHelper {
	
	public static boolean removeViewFromParent(View view) {
		if (view != null && view.getParent() != null) {
			((ViewGroup)view.getParent()).removeView(view);
			return true;
		}
		return false;
	}

}
