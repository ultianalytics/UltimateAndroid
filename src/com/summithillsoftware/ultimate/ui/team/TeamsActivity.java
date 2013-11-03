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
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.model.TeamDescription;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class TeamsActivity extends UltimateActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teams);
		registerTeamSelectedListener();
		if (UltimateApplication.current().isAppStartInProgress()) {
			UltimateApplication.current().setAppStartComplete();
			goToTeamActivity();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.teams, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(this, TeamActivity.class);
			intent.putExtra(TeamActivity.NEW_TEAM, true);
			startActivity(intent);
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		getTeamsListViewAdapter().resetTeams();
	}
	
	private ListView getTeamsListView() {
		return (ListView)findViewById(R.id.teamsFragment).findViewById(R.id.listview_teams);
	}
	
	private TeamsListAdaptor getTeamsListViewAdapter() {
		return (TeamsListAdaptor)getTeamsListView().getAdapter();
	}
	
	private void registerTeamSelectedListener() {
		getTeamsListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TeamDescription selectedTeam = (TeamDescription)getTeamsListViewAdapter().getItem(position);
				Team.setCurrentTeamId(selectedTeam.getTeamId());
				goToTeamActivity();
			}

		});
	}
	
	private void goToTeamActivity() {
		startActivity(new Intent(this, TeamActivity.class));
	}
}
