package com.summithillsoftware.ultimate.ui;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;

import com.summithillsoftware.ultimate.UltimateApplication;

public class UltimateActivity extends ActionBarActivity {
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public static List<View> findViewsWithTag(ViewGroup viewGroup, Object tag){
	    List<View> answerList = new ArrayList<View>();
	    
	    if (viewGroup.getTag().equals(tag)) {
	    	answerList.add(viewGroup);
	    }
	    final int numberOfChildren = viewGroup.getChildCount();
	    for (int i=0; i < numberOfChildren; i++){
	        final View childView = viewGroup.getChildAt(i);
	        if (childView.getTag().equals(tag)) {
	        	answerList.add(childView);
	        }
	        if(childView instanceof ViewGroup){
	        	answerList.addAll(findViewsWithTag((ViewGroup)childView, tag));
	        }
	    }

	    return answerList;
	}
	
	public static int convertToPixelValue(int dpValue) {
		final float scale = UltimateApplication.current().getResources().getDisplayMetrics().density;
		int pixels = (int) (dpValue * scale + 0.5f);
		return pixels;
	}
	
	public static View findFirstViewWithTag(ViewGroup viewGroup, Object tag){
	    final int numberOfChildren = viewGroup.getChildCount();
	    
	    for (int i = 0; i < numberOfChildren; i++) {
	    	final View childView = viewGroup.getChildAt(i);
	        if (tag.equals(childView.getTag())) {
	        	return childView;
	        }
	        if (childView instanceof ViewGroup) {
	        	View hit = findFirstViewWithTag((ViewGroup)childView, tag);
	        	if (hit != null) {
	        		return hit;
	        	}
	        }
	    }

	    return null;
	}
	
	protected boolean navigateUp() {
			// This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For more details, see the Navigation pattern on Android Design:
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			NavUtils.navigateUpFromSameTask(this);
			return true;
	}
	
	public void displayErrorMessage(String title, String message) {
		AlertDialog errorDialog = new AlertDialog.Builder(this).create();
 		errorDialog.setTitle(title);
 		errorDialog.setMessage(message);
 		errorDialog.setCancelable(false);
 		errorDialog.setButton(BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
			}
 		});

 		errorDialog.show();
	}
	
	public void displayConfirmDialog(String title, String message, String yesButtonText, String noButtonText, DialogInterface.OnClickListener yesHandler) {
		displayConfirmDialog(title, message, yesButtonText, noButtonText, yesHandler, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				// no-op
			}
 		});
	}
	
	public void displayConfirmDialog(String title, String message, String yesButtonText, String noButtonText, DialogInterface.OnClickListener yesHandler, DialogInterface.OnClickListener noHandler) {
		AlertDialog errorDialog = new AlertDialog.Builder(this).create();
 		errorDialog.setTitle(title);
 		errorDialog.setMessage(message);
 		errorDialog.setCancelable(false);
 		errorDialog.setButton(BUTTON_POSITIVE, yesButtonText, yesHandler);
 		errorDialog.setButton(BUTTON_NEGATIVE, noButtonText, noHandler);
 		errorDialog.show();
	}
	
	public void displayThreeButtonDialog(String title, String message, String button1Text, String button2Text, String cancelButtonText, DialogInterface.OnClickListener button1Handler, DialogInterface.OnClickListener button2Handler, DialogInterface.OnClickListener cancelHandler) {
		AlertDialog errorDialog = new AlertDialog.Builder(this).create();
 		errorDialog.setTitle(title);
 		errorDialog.setMessage(message);
 		errorDialog.setCancelable(false);
 		errorDialog.setButton(BUTTON_POSITIVE, button1Text, button1Handler);
 		errorDialog.setButton(BUTTON_POSITIVE, button2Text, button2Handler);
 		errorDialog.setButton(BUTTON_NEGATIVE, cancelButtonText, cancelHandler);
 		errorDialog.show();
	}

	public Size getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		@SuppressWarnings("deprecation")
		Size size = new Size(display.getWidth(), display.getHeight());
//		display.getSize(point);
		return size;
	}
	
	public ViewGroup getRootView() {
		return (ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content);
	}
	
	public ViewGroup getRootContentView() {
		return (ViewGroup)getRootView().getChildAt(0);
	}
	
	public void lockOrientation() {
		int orientation = getResources().getConfiguration().orientation;
		int rotation = getWindowManager().getDefaultDisplay().getRotation();
		if (orientation == Configuration.ORIENTATION_LANDSCAPE
				&& (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT
				&& (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (orientation == Configuration.ORIENTATION_LANDSCAPE
				&& (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270)) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
		} else {
			if (orientation == Configuration.ORIENTATION_PORTRAIT
					&& (rotation == Surface.ROTATION_180 || rotation == Surface.ROTATION_270)) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
			}
		}
	}
	
	public void unLockOrientation() {
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
