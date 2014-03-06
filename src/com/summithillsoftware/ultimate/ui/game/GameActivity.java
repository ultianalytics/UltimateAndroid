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
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Preferences;
import com.summithillsoftware.ultimate.model.Score;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.cloud.CloudGameUploadDialog;
import com.summithillsoftware.ultimate.ui.game.action.GameActionActivity;
import com.summithillsoftware.ultimate.ui.game.events.EventsActivity;
import com.summithillsoftware.ultimate.ui.game.timeouts.TimeoutsDialogFragment;
import com.summithillsoftware.ultimate.ui.stats.StatsActivity;
import com.summithillsoftware.ultimate.ui.twitter.TwitterActivity_Game;
import com.summithillsoftware.ultimate.ui.wind.WindActivity;
import com.summithillsoftware.ultimate.util.DateUtil;
import com.summithillsoftware.ultimate.workflow.GameUploadWorkflow;

public class GameActivity extends UltimateActivity {

	private List<Integer> gameToScores;
	private List<String> gameToNames;
	
	// widgets
	private TextView text_game_opponent_name;
	private TextView  text_game_tourament_name;
	private Button  button_save;
	private Button  button_events;
	private Button  button_statistics;
	private TextView label_game_date;
	private TextView label_game_score;
	private RadioGroup radiogroup_game_first_point_oline;
	private Spinner spinner_game_to;

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
		connectWidgets();
		populateView();
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    menu.findItem(R.id.action_delete).setVisible(!isNewGame());
	    menu.findItem(R.id.action_action).setVisible(!isNewGame());
	    menu.findItem(R.id.action_upload).setVisible(!isNewGame());	 	    
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
		case R.id.action_twitter:
			goToTwitterActivity();
			return true;				
		case R.id.action_delete:
			deleteClicked();
			return true;
		case R.id.action_action:
			goToActionActivity();
			return true;
		case R.id.action_upload:
			showGameUploadDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveClicked(View v) {
		if (isGameValid()) {
			populateAndSaveGame();
			goToActionActivity();
		} 
	}
	
	public void eventsClicked(View v) {
		goToEventsActivity();
	}
	
	public void timeoutsClicked(View v) {
		showTimeoutsDialog();
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
	
	public void statisticsClicked(View v) {
		goToStatsActivity();
	}
	
	public void windClicked(View v) {
		goToWindActivity();
	}
	
	private void connectWidgets() {
		text_game_opponent_name = (TextView)findViewById(R.id.gameFragment).findViewById(R.id.text_game_opponent_name);
		text_game_tourament_name = (TextView)findViewById(R.id.gameFragment).findViewById(R.id.text_game_tourament_name);
		button_save = (Button)findViewById(R.id.gameFragment).findViewById(R.id.button_save);
		button_events = (Button)findViewById(R.id.gameFragment).findViewById(R.id.button_events);	
		button_statistics = (Button)findViewById(R.id.gameFragment).findViewById(R.id.button_statistics);	
		label_game_date = (TextView)findViewById(R.id.gameFragment).findViewById(R.id.label_game_date);
		label_game_score = (TextView)findViewById(R.id.gameFragment).findViewById(R.id.label_game_score);
		radiogroup_game_first_point_oline = (RadioGroup)findViewById(R.id.gameFragment).findViewById(R.id.radiogroup_game_first_point_oline);
		spinner_game_to = (Spinner)findViewById(R.id.gameFragment).findViewById(R.id.spinner_game_to);
	}
	
	private void populateView() {
		if (isNewGame()) {
			setTitle(getString(R.string.title_activity_game_new));
			button_save.setText(R.string.button_start);
			text_game_tourament_name.setText(Preferences.current().getTournamentName());
			populateGamePointView(Preferences.current().getGamePoint());
			radiogroup_game_first_point_oline.check(R.id.radio_game_start_offense);
		} else {
			Game game = Game.current();
			text_game_tourament_name.setText(game.getTournamentName());
			text_game_opponent_name.setText(game.getOpponentName());		
			if (game.getStartDateTime() != null) {
				String startDateTime = DateFormat.getTimeInstance().format(game.getStartDateTime());
				if (DateUtil.isToday(game.getStartDateTime())) {
					startDateTime = UltimateApplication.current().getString(R.string.common_today) + " " + startDateTime;
				} else {
					startDateTime = SimpleDateFormat.getDateInstance().format(game.getStartDateTime()) + " " + startDateTime;
				}
				label_game_date.setText(startDateTime);
			}
			String scoreFormatted = formatScore(game, this);
			label_game_score.setText(scoreFormatted);
			label_game_score.setTextColor(getScoreColor(game));
			radiogroup_game_first_point_oline.check(Game.current().isFirstPointOline() ? R.id.radio_game_start_offense : R.id.radio_game_start_defense);
			populateGamePointView(Game.current().getGamePoint());
			text_game_opponent_name.requestFocus();
		}
	
		label_game_date.setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
		label_game_score.setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
		button_events.setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
		button_statistics.setVisibility(isNewGame() ? View.GONE : View.VISIBLE);
	}
	
	private void populateGamePointView(int gamePoint) {
		Spinner spinner = spinner_game_to;
		
        List<String> optionsList = getGameToNames();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optionsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int gameToIndex = getGameToScores().indexOf(gamePoint);
        spinner.setSelection(gameToIndex);
	}
	
	private void populateAndSaveGame() {
		Game game = Game.current();
		game.setOpponentName(getOpponentName());
		game.setTournamentName(getTournamentName());
		game.setFirstPointOline((radiogroup_game_first_point_oline.getCheckedRadioButtonId() == R.id.radio_game_start_offense));
		int gameToIndex = spinner_game_to.getSelectedItemPosition();
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
		return !Game.current().hasBeenSaved();
	}
	
	private String getOpponentName() {
		return text_game_opponent_name.getText().toString().trim();
	}

	private String getTournamentName() {
		return text_game_tourament_name.getText().toString().trim();
	}
	
	private void goToActionActivity() {
		startActivity(new Intent(GameActivity.this, GameActionActivity.class));
	}
	
	private void goToEventsActivity() {
		Intent intent = new Intent(this, EventsActivity.class);
		startActivity(intent);
	}
	
	private void goToStatsActivity() {
		Intent intent = new Intent(this, StatsActivity.class);
		startActivity(intent);
	}

	private void showTimeoutsDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    TimeoutsDialogFragment timeoutsDialog = new TimeoutsDialogFragment();
	    timeoutsDialog.setActionMode(false);
	    timeoutsDialog.show(fragmentManager, "dialog");
	}
	
	public static String formatScore(Game game, Context context) {
		Score score = game.getScore();
		return score.format(context, false);
	}
	
	public static int getScoreColor(Game game) {
		if (game.getScore().isOurLead()) {
			return UltimateApplication.current().getResources().getColor(R.color.ultimate_winning_green);
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
	
	private void showGameUploadDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    CloudGameUploadDialog uploadDialog = new CloudGameUploadDialog();
		GameUploadWorkflow workflow = new GameUploadWorkflow();
		uploadDialog.setWorkflow(workflow);
		uploadDialog.show(fragmentManager, "dialog");
	}
	
	private void goToTwitterActivity() {
		startActivity(new Intent(this, TwitterActivity_Game.class));
	}
	
	private void goToWindActivity() {
		startActivity(new Intent(this, WindActivity.class));
	}

}
