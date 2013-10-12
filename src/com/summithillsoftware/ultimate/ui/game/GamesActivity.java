package com.summithillsoftware.ultimate.ui.game;

import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class GamesActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.games, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		setTitle(getString(R.string.action_games) + " - " + Team.current().getName());
		if (Game.numberOfGamesForTeam(Team.current().getTeamId()) == 0) {
		     Toast.makeText(getApplicationContext(), getString(R.string.toast_games_no_games_yet), Toast.LENGTH_LONG).show();
		}
	}
	
	


}
