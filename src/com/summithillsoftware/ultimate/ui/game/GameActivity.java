package com.summithillsoftware.ultimate.ui.game;

import static com.summithillsoftware.ultimate.model.Game.TIME_BASED_GAME_POINT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.DateUtil;
import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Preferences;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class GameActivity extends UltimateActivity {
	public static final String NEW_GAME = "NewGame";
	
//	private Menu menu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		if (isNewGame()) {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
		}
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		populateView();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    menu.findItem(R.id.action_delete).setVisible(!isNewGame());
	    menu.findItem(R.id.action_action).setVisible(!isNewGame());
	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
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
		case R.id.action_action:
			goToActionActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveClicked(View v) {
		boolean isNew = isNewGame();
		if (isGameValid()) {
			populateAndSaveGame();
			if (isNew) {
				getIntent().removeExtra(NEW_GAME);
				goToActionActivity();
			}
		} 
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	private void deleteClicked() {
		confirmAndDeleteGame();
	}
	
	public void dateClicked(View v) {
		
	}
	
	public void scoreClicked(View v) {
		goToActionActivity();
	}
	
	private void populateView() {
		if (isNewGame()) {
			setTitle(getString(R.string.title_activity_game_new));
			getSaveButton().setText(R.string.button_start);
			getTournamentNameTextView().setText(Preferences.current().getTournamentName());
			populateGamePointView(Preferences.current().getGamePoint());
			getFirstPointOlineRadioGroup().check(R.id.radio_game_start_offense);
		} else {
			Game game = Game.current();
			getTournamentNameTextView().setText(game.getTournamentName());
			getOpponentNameTextView().setText(game.getOpponentName());		
			if (game.getStartDateTime() != null) {
				String startDateTime = DateFormat.getTimeInstance().format(game.getStartDateTime());
				if (DateUtil.isToday(game.getStartDateTime())) {
					startDateTime = UltimateApplication.current().getString(R.string.common_today) + " " + startDateTime;
				} else {
					startDateTime = SimpleDateFormat.getDateInstance().format(game.getStartDateTime()) + " " + startDateTime;
				}
				getDateView().setText(startDateTime);
			}
			String scoreFormatted = game.getScore().getOurs() + "-" + game.getScore().getTheirs() + " ";
			if (game.getScore().isOurLead()) {
				getScoreView().setTextColor(Color.GREEN);
				scoreFormatted += "(" + getString(R.string.common_us) + ")";
			} else if (game.getScore().isTheirLead()) {
				getScoreView().setTextColor(Color.RED);
				scoreFormatted += "(" + getString(R.string.common_them) + ")";
			} else {
				getScoreView().setTextColor(Color.WHITE);
				scoreFormatted += "(" + getString(R.string.common_tied) + ")";
			} 
			getScoreView().setText(scoreFormatted);
			getFirstPointOlineRadioGroup().check(Game.current().isFirstPointOline() ? R.id.radio_game_start_offense : R.id.radio_game_start_defense);
			populateGamePointView(Game.current().getGamePoint());
			getOpponentNameTextView().requestFocus();
		}
	
		getDateView().setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
		getScoreView().setVisibility(isNewGame() ? View.GONE : View.VISIBLE);

	}
	
	private void populateGamePointView(int gamePoint) {
		int radioButtonId;
		switch (gamePoint) {
		case 9:
			radioButtonId = R.id.radio_game_to_9;
			break;
		case 11:
			radioButtonId = R.id.radio_game_to_11;
			break;
		case 13:
			radioButtonId = R.id.radio_game_to_13;
			break;
		case 15:
			radioButtonId = R.id.radio_game_to_15;
			break;
		case 17:
			radioButtonId = R.id.radio_game_to_17;
			break;
		case TIME_BASED_GAME_POINT:
			radioButtonId = R.id.radio_game_to_time;
			break;			
		default:
			radioButtonId = R.id.radio_game_to_13;
			break;
		}
		getGameToRadioGroup().check(radioButtonId);	
	}
	
	private void populateAndSaveGame() {
		Game game = isNewGame() ? Game.createGame() : Game.current();
		game.setOpponentName(getOpponentName());
		game.setTournamentName(getTournamentName());
		game.setFirstPointOline((getFirstPointOlineRadioGroup().getCheckedRadioButtonId() == R.id.radio_game_start_offense));
		switch (getGameToRadioGroup().getCheckedRadioButtonId()) {
		case R.id.radio_game_to_9:
			game.setGamePoint(9);
			break;
		case R.id.radio_game_to_11:
			game.setGamePoint(11);
			break;
		case R.id.radio_game_to_13:
			game.setGamePoint(13);
			break;
		case R.id.radio_game_to_15:
			game.setGamePoint(15);
			break;
		case R.id.radio_game_to_17:
			game.setGamePoint(17);
			break;
		case R.id.radio_game_to_time:
			game.setGamePoint(TIME_BASED_GAME_POINT);
			break;			
		default:
			game.setGamePoint(Preferences.current().getGamePoint());
			break;
		}
		game.save();
		Game.setCurrentGame(game);
		Preferences.current().setGamePoint(game.getGamePoint());
		Preferences.current().setTournamentName(game.getTournamentName());
		Preferences.current().save();
	}
	
	private void confirmAndDeleteGame() {
		String message = getString(R.string.alert_game_confirm_delete_message, Game.current().getOpponentName());
		displayConfirmDialog(getString(R.string.alert_game_confirm_delete_title), message, getString(android.R.string.yes), getString(android.R.string.no), new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface paramDialogInterface, int paramInt) {
				Game.current().delete();
				finish();
			}
 		});
	}
	
	private boolean isGameValid() {
		if (getOpponentName().isEmpty()) {
			displayErrorMessage(getString(R.string.alert_game_opponent_required_title), getString(R.string.alert_game_opponent_required_message));
			return false;
		} 
		return true;
	}
	
	private boolean isNewGame() {
		return getIntent().getBooleanExtra(NEW_GAME, false);
	}
	
	private TextView getOpponentNameTextView() {
		return (TextView)findViewById(R.id.gameFragment).findViewById(R.id.text_game_opponent_name);
	}
	
	private String getOpponentName() {
		return getOpponentNameTextView().getText().toString().trim();
	}
	
	private TextView getTournamentNameTextView() {
		return (TextView)findViewById(R.id.gameFragment).findViewById(R.id.text_game_tourament_name);
	}

	private String getTournamentName() {
		return getTournamentNameTextView().getText().toString().trim();
	}
	
	private Button getSaveButton() {
		return (Button)findViewById(R.id.gameFragment).findViewById(R.id.button_save);
	}
	
	private TextView getDateView() {
		return (TextView)findViewById(R.id.gameFragment).findViewById(R.id.label_game_date);
	}
	
	private TextView getScoreView() {
		return (TextView)findViewById(R.id.gameFragment).findViewById(R.id.label_game_score);
	}
	
	private RadioGroup getFirstPointOlineRadioGroup() {
		return (RadioGroup)findViewById(R.id.gameFragment).findViewById(R.id.radiogroup_game_first_point_oline);
	}
	
	private RadioGroup getGameToRadioGroup() {
		return (RadioGroup)findViewById(R.id.gameFragment).findViewById(R.id.radiogroup_game_game_to);
	}
	
	private void goToActionActivity() {
		startActivity(new Intent(GameActivity.this, GameActionActivity.class));
	}

}
