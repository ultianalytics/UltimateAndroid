package com.summithillsoftware.ultimate.ui.team;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class TeamActivity extends AbstractActivity {
	public static final String NEW_TEAM = "NewTeam";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isNewTeam()) {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
		}
		setContentView(R.layout.activity_team);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
		getMenuInflater().inflate(R.menu.team, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.action_delete) {
			deleteClicked();
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
			boolean wasNewTeam = isNewTeam();
			boolean wasDefaultTeamName = isNewTeam() ? false : Team.current().isDefaultTeamName();
			populateAndSaveTeam();
			getIntent().removeExtra(NEW_TEAM);
			if (wasNewTeam || (wasDefaultTeamName && !Team.current().isDefaultTeamName())) {
				goToPlayersActivity();
			} else {
				finish();
			}
		} 
	}
	
	public void playersClicked(View v) {
		goToPlayersActivity();
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	private void deleteClicked() {
		if (Team.numberOfTeams() > 1) {
			confirmDelete();
		} else {
			displayErrorMessage(getString(R.string.alert_team_delete_not_allowed_title), getString(R.string.alert_team_delete_not_allowed_message));
		}
	}
	
	private void confirmDelete() {
		displayConfirmDialog(getString(R.string.alert_team_confirm_delete_title), getString(R.string.alert_team_confirm_delete_message), getString(android.R.string.yes), getString(android.R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				Team.current().delete();
				finish();
			}
 		});
	}
	
	private void populateView() {
		if (!isNewTeam()) {
			if (!Team.current().isDefaultTeamName()) {
				getNameTextView().setText(Team.current().getName());
			}
			getTeamTypeRadioGroup().check(Team.current().isMixed() ? R.id.radio_team_type_mixed : R.id.radio_team_type_uni);
			getPlayerDisplayRadioGroup().check(Team.current().isDisplayingPlayerNumber() ? R.id.radio_team_playerdisplay_number : R.id.radio_team_playerdisplay_name);
		}
		
		if (isNewTeam() || Team.current().isDefaultTeamName()) { 
			getPlayersView().setVisibility(View.GONE);
			getNameTextView().requestFocus();
		} else {
			getPlayersView().setVisibility(View.VISIBLE);
			getCancelButton().requestFocus();
		}
	}
	
	private void populateAndSaveTeam() {
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
	
	private Button getCancelButton() {
		return (Button)findViewById(R.id.teamFragment).findViewById(R.id.button_cancel);
	}
	
	private View getPlayersView() {
		return (View)findViewById(R.id.teamFragment).findViewById(R.id.view_team_players_button);
	}
	
	private boolean isNewTeam() {
		return getIntent().getBooleanExtra(NEW_TEAM, false);
	}
	
	private boolean isTeamValid() {
		if (getTeamName().isEmpty()) {
			displayErrorMessage(getString(R.string.alert_team_name_required_title), getString(R.string.alert_team_name_required_message));
			return false;
		} else if (Team.isDuplicateTeamName(getTeamName(), isNewTeam() ? null : Team.current())) {
			displayErrorMessage(getString(R.string.alert_team_duplicate_name_title), getString(R.string.alert_team_duplicate_name_message));
			return false;
		}
		return true;
	}
	
	private void goToTeamsActivity() {
		startActivity(new Intent(this, TeamsActivity.class));
	}
	
	private void goToPlayersActivity() {
		startActivity(new Intent(this, PlayersActivity.class));
	}



}
