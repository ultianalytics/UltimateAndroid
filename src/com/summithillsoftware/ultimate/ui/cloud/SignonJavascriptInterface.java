package com.summithillsoftware.ultimate.ui.cloud;

import com.summithillsoftware.ultimate.model.Preferences;

import android.webkit.JavascriptInterface;

public class SignonJavascriptInterface {
	
	@JavascriptInterface
	public void setUserEmail(String email) {
	   Preferences.current().setCloudEMail(email);
	   Preferences.current().save();
	}

}
