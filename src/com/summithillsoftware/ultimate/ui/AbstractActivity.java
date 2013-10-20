package com.summithillsoftware.ultimate.ui;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;

public class AbstractActivity extends ActionBarActivity {
	
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	protected boolean navigateUp() {
			// This ID represents the Home or Up button. In the case of this activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For more details, see the Navigation pattern on Android Design:
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			NavUtils.navigateUpFromSameTask(this);
			return true;
	}
	
	protected void displayErrorMessage(String title, String message) {
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
	
	protected void displayConfirmDialog(String title, String message, String yesButtonText, String noButtonText, DialogInterface.OnClickListener yesHandler) {
		AlertDialog errorDialog = new AlertDialog.Builder(this).create();
 		errorDialog.setTitle(title);
 		errorDialog.setMessage(message);
 		errorDialog.setCancelable(false);
 		errorDialog.setButton(BUTTON_POSITIVE, yesButtonText, yesHandler);
 		errorDialog.setButton(BUTTON_NEGATIVE, noButtonText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
			}
 		});
 		errorDialog.show();
	}

}
