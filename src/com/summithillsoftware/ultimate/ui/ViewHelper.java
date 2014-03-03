package com.summithillsoftware.ultimate.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
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
	
	public static List<View> allChildViews(View view) {
		if (view instanceof ViewGroup) {
			List<View> views = new ArrayList<View>();
			for(int i=0; i<((ViewGroup)view).getChildCount(); ++i) {
				views.add(((ViewGroup)view).getChildAt(i));
			}
			return views;
		} else {
			return Collections.emptyList();
		}
	}
	
	public static List<View> allDescendentViews(View view, @SuppressWarnings("rawtypes") Class viewType) {
		List<View> views = new ArrayList<View>();
		addDescendentViews(view, viewType, views);
		return views;
	}
	
	public static void addDescendentViews(View view, @SuppressWarnings("rawtypes") Class viewType, List<View> viewList) {
		if (view.getClass() == viewType) {
			viewList.add(view);
		}
		if (view instanceof ViewGroup) {
			for(int i=0; i<((ViewGroup)view).getChildCount(); ++i) {
				addDescendentViews(((ViewGroup)view).getChildAt(i), viewType, viewList);
			}
		} 
	}
	
	public static int dpAsPixels(int dpValue, Context context) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, r.getDisplayMetrics());
		return (int)Math.round(px);
	}

}
