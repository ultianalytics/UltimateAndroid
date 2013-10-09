package com.summithillsoftware.ultimate.ui.game;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Team;

public class GamesActivity extends Activity {

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
	}


}
