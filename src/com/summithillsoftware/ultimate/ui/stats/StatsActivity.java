package com.summithillsoftware.ultimate.ui.stats;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class StatsActivity extends UltimateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stats);
		setupActionBar();  // Show the Up button in the action bar.
		registerListeners();
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
	
	private void registerListeners() {
		getButton(R.id.button_stattype_plus_minus).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_points_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_opoints_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_dpoints_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_goals).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_assists).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		getButton(R.id.button_stattype_callahans).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_throws).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_drops).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});	
		getButton(R.id.button_stattype_throwaways).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		getButton(R.id.button_stattype_stalled).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_penalties).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		getButton(R.id.button_stattype_callahaned).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});	
		getButton(R.id.button_stattype_ds).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});	
		getButton(R.id.button_stattype_pulls).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});	
		getButton(R.id.button_stattype_pullobs).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});			
		
	}
	
	private Button getButton(int viewId) {
		return (Button)findViewById(R.id.statsFragment).findViewById(viewId);
	}

}
