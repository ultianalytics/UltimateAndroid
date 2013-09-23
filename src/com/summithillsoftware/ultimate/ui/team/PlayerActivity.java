package com.summithillsoftware.ultimate.ui.team;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class PlayerActivity extends AbstractActivity {
	public static final String NEW_PLAYER = "NewPlayer";
	public static final String PLAYER_NAME = "PlayerName";
	private Player existingPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		if (isNewPlayer()) {
			
		} else {
			String playerName = getIntent().getStringExtra(PLAYER_NAME);
			existingPlayer = Team.current().getPlayerNamed(playerName);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.player, menu);
		return true;
	}

	
	public void saveClicked(View v) {
//		if (isTeamValid()) {
//			boolean wasNewTeam = isNewTeam();
//			boolean wasDefaultTeamName = isNewTeam() ? false : Team.current().isDefaultTeamName();
//			populateAndSaveTeam();
//			getIntent().removeExtra(NEW_TEAM);
//			if (wasNewTeam || (wasDefaultTeamName && !Team.current().isDefaultTeamName())) {
//				goToPlayersActivity();
//			} else {
//				finish();
//			}
//		} 
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	private void deleteClicked() {
		confirmDelete();
	}
	
	private void confirmDelete() {
//		displayConfirmDialog(getString(R.string.alert_team_confirm_delete_title), getString(R.string.alert_team_confirm_delete_message), getString(android.R.string.yes), getString(android.R.string.no), new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//				Team.current().delete();
//				finish();
//			}
// 		});
	}
	
	private void populateView() {
//		if (!isNewTeam()) {
//			if (!Team.current().isDefaultTeamName()) {
//				getNameTextView().setText(Team.current().getName());
//			}
//			getTeamTypeRadioGroup().check(Team.current().isMixed() ? R.id.radio_team_type_mixed : R.id.radio_team_type_uni);
//			getPlayerDisplayRadioGroup().check(Team.current().isDisplayingPlayerNumber() ? R.id.radio_team_playerdisplay_number : R.id.radio_team_playerdisplay_name);
//		}
	}
	
	private void populateAndSaveTeam() {
//		Team team = isNewTeam() ? new Team() : Team.current();
//		team.setName(getTeamName());
//		team.setMixed(getTeamTypeRadioGroup().getCheckedRadioButtonId() == R.id.radio_team_type_mixed);
//		team.setDisplayingPlayerNumber(getPlayerDisplayRadioGroup().getCheckedRadioButtonId() == R.id.radio_team_playerdisplay_number);
//		team.save();
//		Team.setCurrentTeamId(team.getTeamId());
	}

	private String getPlayerName() {
		return getNameTextView().getText().toString().trim();
	}
	
	private TextView getNameTextView() {
		return (TextView)findViewById(R.id.playerFragment).findViewById(R.id.text_player_name);
	}
	
	private RadioGroup getPositionRadioGroup() {
		return (RadioGroup)findViewById(R.id.playerFragment).findViewById(R.id.radiogroup_player_position);
	}
	
	private RadioGroup getgetGenderRadioGroup() {
		return (RadioGroup)findViewById(R.id.playerFragment).findViewById(R.id.radiogroup_player_gender);
	}
	
	private Button getCancelButton() {
		return (Button)findViewById(R.id.playerFragment).findViewById(R.id.button_cancel);
	}
	
	private boolean isNewPlayer() {
		return getIntent().getBooleanExtra(NEW_PLAYER, false);
	}
	
	private boolean isPlayerValid() {
		if (getPlayerName().isEmpty()) {
			displayErrorMessage(getString(R.string.alert_player_name_required_title), getString(R.string.alert_player_name_required_message));
			return false;
		} else if (Team.isDuplicateTeamName(getPlayerName(), isNewPlayer() ? null : Team.current())) {
			displayErrorMessage(getString(R.string.alert_player_duplicate_name_title), getString(R.string.alert_player_duplicate_name_message));
			return false;
		}
		return true;
	}
	
}
