package com.summithillsoftware.ultimate.ui.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class PlayersActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_players);
		registerPlayerSelectedListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.players, menu);
		return true;
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		getPlayersListViewAdapter().resetPlayers();
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(this, PlayerActivity.class);
			intent.putExtra(PlayerActivity.NEW_PLAYER, true);
			startActivity(intent);
		}
		return true;
	}
	
	private ListView getPlayersListView() {
		return (ListView)findViewById(R.id.playersFragment).findViewById(R.id.listview_players);
	}
	
	private PlayersListAdapter getPlayersListViewAdapter() {
		return (PlayersListAdapter)getPlayersListView().getAdapter();
	}
	
	private void registerPlayerSelectedListener() {
		getPlayersListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Player player = (Player)getPlayersListViewAdapter().getItem(position);
				Intent intent = new Intent(PlayersActivity.this, PlayerActivity.class);
				intent.putExtra(PlayerActivity.PLAYER_NAME, player.getName());
				startActivity(intent);
			}

		});
	}
}
