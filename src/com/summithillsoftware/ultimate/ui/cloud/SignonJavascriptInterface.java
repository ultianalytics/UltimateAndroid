package com.summithillsoftware.ultimate.ui.cloud;

import android.webkit.JavascriptInterface;

import com.summithillsoftware.ultimate.UltimateLogger;
import com.summithillsoftware.ultimate.model.Preferences;

public class SignonJavascriptInterface {
	
	@JavascriptInterface
	public void setUserEmail(String email) {
		if (email != null && !email.isEmpty()) {
			Preferences.current().setCloudEMail(email);
			Preferences.current().save();
			UltimateLogger.logInfo( "remembering last user to authenticate is " + email);
			UltimateLogger.logInfo("remembering last user to authenticate is " + email);
		}
	}

}
