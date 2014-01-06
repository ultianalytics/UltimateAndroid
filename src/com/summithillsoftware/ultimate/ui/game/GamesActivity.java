package com.summithillsoftware.ultimate.ui.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.GameDescription;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class GamesActivity extends UltimateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_games);
		setupActionBar();  // Show the Up button in the action bar.
		registerTeamSelectedListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.games, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return navigateUp();
		case R.id.action_add:
			goToGameActivity(true);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle(getString(R.string.action_games) + " - " + Team.current().getName());
		if (Game.numberOfGamesForTeam(Team.current().getTeamId()) == 0) {
			Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.toast_games_no_games_yet), Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getGamesListViewAdapter().resetGames();
	}
	
	private ListView getGamesListView() {
		return (ListView)findViewById(R.id.gamesFragment).findViewById(R.id.listview_games);
	}
	
	private GamesListAdapter getGamesListViewAdapter() {
		return (GamesListAdapter)getGamesListView().getAdapter();
	}
	
	private void registerTeamSelectedListener() {
		getGamesListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				GameDescription selectedGame = (GameDescription)getGamesListViewAdapter().getItem(position);
				Game.setCurrentGameId(selectedGame.getGameId());
				goToGameActivity(false);
			}

		});
	}
	
	private void goToGameActivity(boolean isNew) {
		Intent intent = new Intent(GamesActivity.this, GameActivity.class);
		if (isNew) {
			Game.setCurrentGame(Game.createGame());
		}
		startActivity(intent);
	}


}
