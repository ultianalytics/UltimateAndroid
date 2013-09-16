package com.summithillsoftware.ultimate.ui.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Team;

public class TeamActivity extends AbstractActivity {
	public static final String NEW_TEAM = "NewTeam";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		populateView();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    menu.findItem(R.id.action_delete).setVisible(!isNewTeam());
	    menu.findItem(R.id.action_teams).setVisible(!isNewTeam());
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_delete) {
			if (Team.numberOfTeams() > 1) {
				Team.current().delete();
				finish();
			} else {
				displayErrorMessage("Delete Not Allowed", "You cannot delete this team because it is the only team on this device.  Create another team and then retry.");
			}

			return true;
		} else if (item.getItemId() == R.id.action_teams) {
			goToTeamsActivity();
			return true;
		} else {
			return super.onMenuItemSelected(featureId, item);
		}
	}
	
	public void saveClicked(View v) {
		if (isTeamValid()) {
			populateModel();
			finish();
		} 
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	
	private void populateView() {
		if (!isNewTeam()) {
			if (!Team.current().isDefaultTeamName()) {
				getNameTextView().setText(Team.current().getName());
			}
			getTeamTypeRadioGroup().check(Team.current().isMixed() ? R.id.radio_team_type_mixed : R.id.radio_team_type_uni);
			getPlayerDisplayRadioGroup().check(Team.current().isDisplayingPlayerNumber() ? R.id.radio_team_playerdisplay_number : R.id.radio_team_playerdisplay_name);
		}
	}
	
	private void populateModel() {
		Team team = isNewTeam() ? new Team() : Team.current();
		team.setName(getTeamName());
		team.setMixed(getTeamTypeRadioGroup().getCheckedRadioButtonId() == R.id.radio_team_type_mixed);
		team.setDisplayingPlayerNumber(getPlayerDisplayRadioGroup().getCheckedRadioButtonId() == R.id.radio_team_playerdisplay_number);
		team.save();
		Team.setCurrentTeamId(team.getTeamId());
	}
	
	private String getTeamName() {
		return getNameTextView().getText().toString().trim();
	}
	private TextView getNameTextView() {
		return (TextView)findViewById(R.id.teamFragment).findViewById(R.id.text_team_name);
	}
	
	private RadioGroup getTeamTypeRadioGroup() {
		return (RadioGroup)findViewById(R.id.teamFragment).findViewById(R.id.radiogroup_team_type);
	}
	
	private RadioGroup getPlayerDisplayRadioGroup() {
		return (RadioGroup)findViewById(R.id.teamFragment).findViewById(R.id.radiogroup_team_playerdisplay);
	}
	
	private boolean isNewTeam() {
		return getIntent().getBooleanExtra(NEW_TEAM, false);
	}
	
	private boolean isTeamValid() {
		if (getTeamName().isEmpty()) {
			displayErrorMessage("Team Name Required", "Please enter a name for this team.");
			return false;
		} else if (Team.isDuplicateTeamName(getTeamName(), isNewTeam() ? null : Team.current())) {
			displayErrorMessage("Duplicate Name", "Each team must have a unique name.  Please enter a different name for this team.");
			return false;
		}
		return true;
	}
	
	private void goToTeamsActivity() {
		startActivity(new Intent(this, TeamsActivity.class));
	}

}
