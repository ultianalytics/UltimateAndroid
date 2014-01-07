package com.summithillsoftware.ultimate.ui.stats;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.stats.PlayerStat;
import com.summithillsoftware.ultimate.stats.PlayerStatistics;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class StatsActivity extends UltimateActivity {
	private Button selectedStatButton;
	private List<PlayerStat> playerStats;

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
		OnClickListener statTypeButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleStatTypeSelection(v, statsForType((v.getId())));
			}
		};
		getButton(R.id.button_stattype_plus_minus).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_points_played).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_opoints_played).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_dpoints_played).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_goals).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_assists).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_callahans).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_throws).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_drops).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_throwaways).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_stalled).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_penalties).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_callahaned).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_ds).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_pulls).setOnClickListener(statTypeButtonListener);
		getButton(R.id.button_stattype_pullobs).setOnClickListener(statTypeButtonListener);
	}
	
	private void handleStatTypeSelection(View button, List<PlayerStat> playerStats) {
		
	}
	
	private Button getButton(int viewId) {
		return (Button)findViewById(R.id.statsFragment).findViewById(viewId);
	}
	
	private Game game() {
		return Game.current();
	}
	
	private List<PlayerStat> statsForType(int buttonViewId) {
		if (buttonViewId == R.id.button_stattype_plus_minus) {
			return PlayerStatistics.plusMinusCountPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_points_played) {
			return PlayerStatistics.pointsPerPlayer(game(), true, true, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_opoints_played) {
			return PlayerStatistics.pointsPerPlayer(game(), true, false, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_dpoints_played) {
			return PlayerStatistics.pointsPerPlayer(game(), false, true, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_goals) {
			return PlayerStatistics.goalsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_assists) {
			return PlayerStatistics.assistsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_callahans) {
			return PlayerStatistics.callahansPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_throws) {
			return PlayerStatistics.throwsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_drops) {
			return PlayerStatistics.dropsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_throwaways) {
			return PlayerStatistics.throwawaysPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_stalled) {
			return PlayerStatistics.stallsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_penalties) {
			return PlayerStatistics.miscPenaltiesPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_callahaned) {
			return PlayerStatistics.callahanedPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_ds) {
			return PlayerStatistics.dsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_pulls) {
			return PlayerStatistics.pullsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_pullobs) {
			return PlayerStatistics.pullsObPerPlayer(game(), isTournamentIncluded());
		} else {
			return Collections.emptyList();
		}
	}
	
	private boolean isTournamentIncluded() {
		// TODO...read the state of the radio buttons
		return false;
	}

}
