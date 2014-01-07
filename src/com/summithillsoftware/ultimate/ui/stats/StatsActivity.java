package com.summithillsoftware.ultimate.ui.stats;

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
				List<PlayerStat> playerStats = PlayerStatistics.plusMinusCountPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_points_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.pointsPerPlayer(game(), true, true, isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_opoints_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.pointsPerPlayer(game(), true, false, isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_dpoints_played).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.pointsPerPlayer(game(), false, true, isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_goals).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.goalsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_assists).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.assistsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});

		getButton(R.id.button_stattype_callahans).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.callahansPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_throws).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.throwsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_drops).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.dropsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});	
		getButton(R.id.button_stattype_throwaways).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.throwawaysPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});

		getButton(R.id.button_stattype_stalled).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.stallsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_penalties).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.miscPenaltiesPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});
		getButton(R.id.button_stattype_callahaned).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.callahanedPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});	
		getButton(R.id.button_stattype_ds).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.dsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});	
		getButton(R.id.button_stattype_pulls).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.pullsPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});	
		getButton(R.id.button_stattype_pullobs).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				List<PlayerStat> playerStats = PlayerStatistics.pullsObPerPlayer(game(), isTournamentIncluded());
				handleStatTypeSelection(v, playerStats);
			}
		});			
		
	}
	
	private void handleStatTypeSelection(View button, List<PlayerStat> playerStats) {
		
	}
	
	private Button getButton(int viewId) {
		return (Button)findViewById(R.id.statsFragment).findViewById(viewId);
	}
	
	private Game game() {
		return Game.current();
	}
	
	private boolean isTournamentIncluded() {
		// TODO...read the state of the radio buttons
		return false;
	}

}
