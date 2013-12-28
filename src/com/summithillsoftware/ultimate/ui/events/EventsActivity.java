package com.summithillsoftware.ultimate.ui.events;

import android.os.Bundle;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class EventsActivity extends UltimateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
		// Show the Up button in the action bar.
		setupActionBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}

}
