package com.summithillsoftware.ultimate.ui.stats;

import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.stats.PlayerStat;
import com.summithillsoftware.ultimate.stats.PlayerStatistics;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.ViewHelper;

public class StatsActivity extends UltimateActivity {
	private int selectedStatId;

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
	
	@Override
	protected void onStart() {
		super.onStart();
		setStatButtonSelected(R.id.button_stattype_plus_minus);
	}
	
	private void registerListeners() {
		getIncludeRadioGroup().setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				setStatButtonSelected(getSelectedStatId());
			}
		});
		OnClickListener statTypeButtonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				setStatButtonSelected(v.getId());
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
	
	private void setStatButtonSelected(int buttonId) {
		selectedStatId = buttonId;
		ViewGroup buttonsContainer = (ViewGroup)findViewById(R.id.statsFragment).findViewById(R.id.statTypeButtons);
		List<View> buttons = ViewHelper.allChildViews(buttonsContainer);
		for (View button : buttons) {
			if (button instanceof Button) {
				button.setEnabled(button.getId() == buttonId ? false : true);
			}
		}
		updatePlayerStats();
	}
	
	private int getSelectedStatId() {
		if (selectedStatId == 0) {
			selectedStatId = R.id.button_stattype_plus_minus;
		}
		return selectedStatId;
	}
	
	private void updatePlayerStats() {
		ListView listView = (ListView)findViewById(R.id.statsFragment).findViewById(R.id.listview_player_stats);
		PlayerStatisticListAdapter playerStatsAdapter = (PlayerStatisticListAdapter)listView.getAdapter();
		playerStatsAdapter.setPlayerStats(statsForType(getSelectedStatId()));
	}
	
	private Button getButton(int viewId) {
		return (Button)findViewById(R.id.statsFragment).findViewById(viewId);
	}
	
	private Game game() {
		return Game.current();
	}
	
	private List<PlayerStat> statsForType(int buttonViewId) {
		List<PlayerStat> stats = Collections.emptyList();
		if (buttonViewId == R.id.button_stattype_plus_minus) {
			stats = PlayerStatistics.plusMinusCountPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_points_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), true, true, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_opoints_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), true, false, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_dpoints_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), false, true, isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_goals) {
			stats = PlayerStatistics.goalsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_assists) {
			stats = PlayerStatistics.assistsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_callahans) {
			stats = PlayerStatistics.callahansPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_throws) {
			stats = PlayerStatistics.throwsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_drops) {
			stats = PlayerStatistics.dropsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_throwaways) {
			stats = PlayerStatistics.throwawaysPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_stalled) {
			stats = PlayerStatistics.stallsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_penalties) {
			stats = PlayerStatistics.miscPenaltiesPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_callahaned) {
			stats = PlayerStatistics.callahanedPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_ds) {
			stats = PlayerStatistics.dsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_pulls) {
			stats = PlayerStatistics.pullsPerPlayer(game(), isTournamentIncluded());
		} else if (buttonViewId == R.id.button_stattype_pullobs) {
			stats = PlayerStatistics.pullsObPerPlayer(game(), isTournamentIncluded());
		} 
		return stats;
	}
	
	private boolean isTournamentIncluded() {
		return getIncludeRadioGroup().getCheckedRadioButtonId() == R.id.radioGroupGamesIncludedTournament;
	}
	
	private RadioGroup getIncludeRadioGroup() {
		return (RadioGroup)findViewById(R.id.statsFragment).findViewById(R.id.radioGroupGamesIncluded);
	}





}
 