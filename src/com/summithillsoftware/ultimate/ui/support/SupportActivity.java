package com.summithillsoftware.ultimate.ui.support;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class SupportActivity extends UltimateActivity {
	
	// widgets
	private CheckBox includeLogFilesCheckbox;
	private CheckBox includeTeamsCheckbox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_support);
		connectWidgets();
	}

	private void connectWidgets() {
		includeLogFilesCheckbox = (CheckBox)findViewById(R.id.settingsFragment).findViewById(R.id.includeLogFilesCheckbox);
		includeTeamsCheckbox = (CheckBox)findViewById(R.id.settingsFragment).findViewById(R.id.includeTeamsCheckbox);		
	}
	
	public void sendEmailClicked(View v) {
		showEmailCompose();
	}
	
	private void showEmailCompose() {
		boolean includeLogs = includeLogFilesCheckbox.isChecked();
		boolean includeTeams = includeTeamsCheckbox.isChecked();
		
		File attachment = null;
		if (includeLogs || includeTeams) {
			attachment = UltimateApplication.current().createZipForSupport(includeTeams);
		}
		
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);  
		emailIntent.setType("plain/text"); 
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "support@ultimate-numbers.com" });
		Uri uri = Uri.parse("file://" + attachment);
		emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
		
		startActivity(emailIntent);  
	}

}
