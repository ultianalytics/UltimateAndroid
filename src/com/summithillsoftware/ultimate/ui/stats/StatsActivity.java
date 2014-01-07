package com.summithillsoftware.ultimate.ui.stats;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class StatsActivity extends UltimateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		setupActionBar();  // Show the Up button in the action bar.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stats, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return navigateUp();
		}
		return super.onOptionsItemSelected(item);
	}

}
