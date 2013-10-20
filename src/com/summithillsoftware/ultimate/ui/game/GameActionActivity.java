package com.summithillsoftware.ultimate.ui.game;

import android.os.Bundle;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class GameActionActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_action);
		setupActionBar();  // Show the Up button in the action bar.		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_action, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle(getString(R.string.common_versus_short) + " " + Game.current().getOpponentName());
	}

}
