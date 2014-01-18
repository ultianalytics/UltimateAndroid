package com.summithillsoftware.ultimate.ui.cloud;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.summithillsoftware.ultimate.Constants;
import com.summithillsoftware.ultimate.model.Preferences;

public class SignonJavascriptInterface {
	
	@JavascriptInterface
	public void setUserEmail(String email) {
		if (email != null && !email.isEmpty()) {
			Preferences.current().setCloudEMail(email);
			Preferences.current().save();
			Log.i(Constants.ULTIMATE, "remembering last user to authenticate is " + email);
		}
	}

}
