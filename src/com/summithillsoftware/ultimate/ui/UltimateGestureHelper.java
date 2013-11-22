package com.summithillsoftware.ultimate.ui;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.util.ArrayList;

import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.Prediction;
import android.util.Log;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;

public class UltimateGestureHelper {
	public static String SWIPE_RIGHT = "swiperight";
	public static String SWIPE_LEFT = "swipeleft";	
	
	private static UltimateGestureHelper Current;
	
	GestureLibrary gestureLibrary;

	static {
		Current = new UltimateGestureHelper();
		if (Current == null) {
			Current = new UltimateGestureHelper();
		}
	}
	
	public static UltimateGestureHelper current() {
		return Current;
	}
	
	public boolean isSwipeRight(Gesture gesture) {
		return isSwipe(gesture, true);
	}
	
	public boolean isSwipeLeft(Gesture gesture) {
		return isSwipe(gesture, false);
	}
	
	private boolean isSwipe(Gesture gesture, boolean isRight) {
		ArrayList<Prediction> predictions = getGestureLibrary().recognize(gesture);
		if (predictions.size() > 0) {
			Prediction topPrediction = predictions.get(0);
			return topPrediction.name.equals(isRight ? SWIPE_RIGHT : SWIPE_LEFT) && topPrediction.score > 1.0;
	    } 
		return false;
	}
	
	private GestureLibrary getGestureLibrary() {
		if (gestureLibrary == null) {
			gestureLibrary = GestureLibraries.fromRawResource(UltimateApplication.current(), R.raw.gestures);
	        if (!gestureLibrary.load()) {
	        	Log.e(ULTIMATE, "Failed to load gestures");
	         } 
		}
		return gestureLibrary;
	}

}
