package com.summithillsoftware.ultimate.ui.game;

import static com.summithillsoftware.ultimate.model.Game.TIME_BASED_GAME_POINT;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.summithillsoftware.ultimate.DateUtil;
import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Preferences;
import com.summithillsoftware.ultimate.model.Score;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.game.action.GameActionActivity;

public class GameActivity extends UltimateActivity {
	public static final String NEW_GAME = "NewGame";
	private List<Integer> gameToScores;
	private List<String> gameToNames;
	
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
			String scoreFormatted = formatScore(game, this);
			getScoreView().setText(scoreFormatted);
			getScoreView().setTextColor(getScoreColor(game));
			getFirstPointOlineRadioGroup().check(Game.current().isFirstPointOline() ? R.id.radio_game_start_offense : R.id.radio_game_start_defense);
			populateGamePointView(Game.current().getGamePoint());
			getOpponentNameTextView().requestFocus();
		}
	
		getDateView().setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
		getScoreView().setVisibility(isNewGame() ? View.GONE : View.VISIBLE);

	}
	
	private void populateGamePointView(int gamePoint) {
		Spinner spinner = getGameToSpinner();
		
        List<String> optionsList = getGameToNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int gameToIndex = getGameToScores().indexOf(gamePoint);
        spinner.setSelection(gameToIndex);
	}
	
	private void populateAndSaveGame() {
		Game game = isNewGame() ? Game.createGame() : Game.current();
		game.setOpponentName(getOpponentName());
		game.setTournamentName(getTournamentName());
		game.setFirstPointOline((getFirstPointOlineRadioGroup().getCheckedRadioButtonId() == R.id.radio_game_start_offense));
		int gameToIndex = getGameToSpinner().getSelectedItemPosition();
		int gameToPoint = getGameToScores().get(gameToIndex);
		game.setGamePoint(gameToPoint);
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
	
	private Spinner getGameToSpinner() {
		return (Spinner)findViewById(R.id.gameFragment).findViewById(R.id.spinner_game_to);
	}
	
	private void goToActionActivity() {
		startActivity(new Intent(GameActivity.this, GameActionActivity.class));
	}
	
	public static String formatScore(Game game, Context context) {
		Score score = game.getScore();
		return score.format(context, false);
	}
	
	public static int getScoreColor(Game game) {
		if (game.getScore().isOurLead()) {
			return Color.GREEN;
		} else if (game.getScore().isTheirLead()) {
			return Color.RED;
		} else {
			return Color.RED;
		} 
	}
	
	private List<Integer> getGameToScores() {
		if (gameToScores == null) {
			gameToScores = new ArrayList<Integer>();
			gameToScores.add(9);
			gameToScores.add(11);
			gameToScores.add(13);
			gameToScores.add(15);
			gameToScores.add(17);
			gameToScores.add(TIME_BASED_GAME_POINT);
		}
		return gameToScores;
	}
	
	private List<String> getGameToNames() {
		if (gameToNames == null) {
			gameToNames = new ArrayList<String>();
			gameToNames.add(getString(R.string.spinner_game_to_9));
			gameToNames.add(getString(R.string.spinner_game_to_11));
			gameToNames.add(getString(R.string.spinner_game_to_13));
			gameToNames.add(getString(R.string.spinner_game_to_15));
			gameToNames.add(getString(R.string.spinner_game_to_17));
			gameToNames.add(getString(R.string.spinner_game_to_time));
		}
		return gameToNames;
	}

}
