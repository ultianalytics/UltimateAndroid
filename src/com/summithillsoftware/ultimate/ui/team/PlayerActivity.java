package com.summithillsoftware.ultimate.ui.team;

import android.os.Bundle;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class PlayerActivity extends AbstractActivity {
	public static final String NEW_PLAYER = "NewPlayer";
	public static final String PLAYER_NAME = "PlayerName";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

}
