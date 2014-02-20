package com.summithillsoftware.ultimate.ui.team;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.cloud.CloudClient;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.cloud.CloudTeamUploadDialog;
import com.summithillsoftware.ultimate.ui.game.GamesActivity;
import com.summithillsoftware.ultimate.workflow.TeamUploadWorkflow;

public class TeamActivity extends UltimateActivity {
	public static final String NEW_TEAM = "NewTeam";
	
	// widgets
	private TextView text_team_name;
	private CheckBox mixedTeamCheckbox;
	private RadioGroup radiogroup_team_playerdisplay;
	private Button button_cancel;
	private View view_team_players_button;
	private View view_website;
	private Button button_website;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);
		connectWidgets();
		setupActionBar();  // Show the Up button in the action bar.
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
	    menu.findItem(R.id.action_upload).setVisible(!isNewTeam() && !getTeamName().isEmpty());	    
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.team, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return navigateUp();
		case R.id.action_delete:
			deleteClicked();
			return true;
		case R.id.action_upload:
			showTeamUploadDialog();
			return true;			
		case R.id.action_games:
			if (isNewTeam() || Team.current().getPlayers().size() == 0) {
				displayErrorMessage(getString(R.string.alert_team_no_games_without_players_title), getString(R.string.alert_team_no_games_without_players_message));
			} else {
				goToGamesActivity();
			}
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	private void connectWidgets() {
		text_team_name = (TextView)findViewById(R.id.teamFragment).findViewById(R.id.text_team_name);
		mixedTeamCheckbox = (CheckBox)findViewById(R.id.teamFragment).findViewById(R.id.mixedTeamCheckbox);
		radiogroup_team_playerdisplay = (RadioGroup)findViewById(R.id.teamFragment).findViewById(R.id.radiogroup_team_playerdisplay);
		button_cancel = (Button)findViewById(R.id.teamFragment).findViewById(R.id.button_cancel);
		view_team_players_button = (View)findViewById(R.id.teamFragment).findViewById(R.id.view_team_players_button);
		view_website = (View)findViewById(R.id.teamFragment).findViewById(R.id.view_website);
		button_website = (Button)findViewById(R.id.teamFragment).findViewById(R.id.button_website);
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
	
	public void websiteClicked(View v) {
		openWebsiteInBrower();
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	private void deleteClicked() {
		if (Team.numberOfTeams() > 1) {
			confirmAndDelete();
		} else {
			displayErrorMessage(getString(R.string.alert_team_delete_not_allowed_title), getString(R.string.alert_team_delete_not_allowed_message));
		}
	}
	
	private void confirmAndDelete() {
		displayConfirmDialog(getString(R.string.alert_team_confirm_delete_title), getString(R.string.alert_team_confirm_delete_message, Team.current().getName()), getString(android.R.string.yes), getString(android.R.string.no), new DialogInterface.OnClickListener() {
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
				text_team_name.setText(Team.current().getName());
			}
			mixedTeamCheckbox.setChecked(Team.current().isMixed());
			radiogroup_team_playerdisplay.check(Team.current().isDisplayingPlayerNumber() ? R.id.radio_team_playerdisplay_number : R.id.radio_team_playerdisplay_name);
			view_website.setVisibility(Team.current().hasCloudId() ? View.VISIBLE : View.GONE);
			button_website.setText(getWebsite());
		} else {
			view_website.setVisibility(View.GONE);
		}
		
		if (isNewTeam() || Team.current().isDefaultTeamName()) { 
			view_team_players_button.setVisibility(View.GONE);
			text_team_name.requestFocus();
		} else {
			view_team_players_button.setVisibility(View.VISIBLE);
			button_cancel.requestFocus();
		}
	}
	
	private void populateAndSaveTeam() {
		Team team = isNewTeam() ? new Team() : Team.current();
		team.setName(getTeamName());
		team.setMixed(mixedTeamCheckbox.isChecked());
		team.setDisplayingPlayerNumber(radiogroup_team_playerdisplay.getCheckedRadioButtonId() == R.id.radio_team_playerdisplay_number);
		team.save();
		Team.setCurrentTeamId(team.getTeamId());
	}

	private String getTeamName() {
		return text_team_name.getText().toString().trim();
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
	
	private void goToPlayersActivity() {
		startActivity(new Intent(this, PlayersActivity.class));
	}
	
	private void goToGamesActivity() {
		startActivity(new Intent(this, GamesActivity.class));
	}
	
	private void openWebsiteInBrower() {
		String website = getWebsite();
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
		startActivity(browserIntent);
	}

	private void showTeamUploadDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    CloudTeamUploadDialog uploadDialog = new CloudTeamUploadDialog();
		TeamUploadWorkflow workflow = new TeamUploadWorkflow();
		uploadDialog.setWorkflow(workflow);
		uploadDialog.show(fragmentManager, "dialog");
	}

	private String getWebsite() {
		return Team.current().hasCloudId() ? CloudClient.current().websiteUrlForCloudId(Team.current().getCloudId()) : "";
	}

}
