package com.summithillsoftware.ultimate.ui.stats;

import java.util.Collections;
import java.util.List;

import android.os.AsyncTask;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.stats.PlayerStat;
import com.summithillsoftware.ultimate.stats.PlayerStatistics;

public class StatsCalculationAsyncTask extends AsyncTask<Void, Void, List<PlayerStat>> {
	private int statsTypeId;
	private boolean isTournamentIncluded;
	private StatsActivity statsActivity;

	public StatsCalculationAsyncTask(int statsTypeId,
			boolean isTournamentIncluded, StatsActivity statsActivity) {
		super();
		this.statsTypeId = statsTypeId;
		this.isTournamentIncluded = isTournamentIncluded;
		this.statsActivity = statsActivity;
	}

	@Override
	protected List<PlayerStat> doInBackground(Void... paramArrayOfParams) {
		List<PlayerStat> stats = Collections.emptyList();

		if (statsTypeId == R.id.button_stattype_plus_minus) {
			stats = PlayerStatistics.plusMinusCountPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_points_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), true, true, isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_opoints_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), true, false, isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_dpoints_played) {
			stats = PlayerStatistics.pointsPerPlayer(game(), false, true, isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_goals) {
			stats = PlayerStatistics.goalsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_assists) {
			stats = PlayerStatistics.assistsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_callahans) {
			stats = PlayerStatistics.callahansPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_throws) {
			stats = PlayerStatistics.throwsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_drops) {
			stats = PlayerStatistics.dropsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_throwaways) {
			stats = PlayerStatistics.throwawaysPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_stalled) {
			stats = PlayerStatistics.stallsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_penalties) {
			stats = PlayerStatistics.miscPenaltiesPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_callahaned) {
			stats = PlayerStatistics.callahanedPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_ds) {
			stats = PlayerStatistics.dsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_pulls) {
			stats = PlayerStatistics.pullsPerPlayer(game(), isTournamentIncluded);
		} else if (statsTypeId == R.id.button_stattype_pullobs) {
			stats = PlayerStatistics.pullsObPerPlayer(game(), isTournamentIncluded);
		} 
		if (isTournamentIncluded) { // pause long enough for animation on UI finish
			try { Thread.sleep(500); } catch (InterruptedException e) { /* no-op */ }
		}
		return stats;
	}

	@Override
	protected void onPostExecute(List<PlayerStat> result) {
		statsActivity.updatePlayerStats(result);
	}

	private Game game() {
		return Game.current();
	}


}
