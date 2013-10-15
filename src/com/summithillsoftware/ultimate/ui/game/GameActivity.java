package com.summithillsoftware.ultimate.ui.game;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class GameActivity extends AbstractActivity {
	public static final String NEW_GAME = "NewGame";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
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
	    return true;
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_delete:
			deleteClicked();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void saveClicked(View v) {
		if (isGameValid()) {
			populateAndSaveGame();
			finish();
		} 
	}

	public void cancelClicked(View v) {
		finish();
	}
	
	private void deleteClicked() {
		confirmAndDeleteGame();
	}
	
	private void populateView() {
		if (isNewGame()) {
			setTitle(getString(R.string.title_activity_game_new));
			getSaveButton().setText(R.string.button_start);
		} else {
			getTournamentNameTextView().setText(Game.current().getTournamentName());
			getOpponentNameTextView().setText(Game.current().getOpponentName());			
		}
	}
	
	private void populateAndSaveGame() {
		Game game = isNewGame() ? new Game() : Game.current();
		game.setOpponentName(getOpponentName());
		game.setTournamentName(getTournamentName());
		game.save();
		Game.setCurrentGame(game);
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
	
//	private RadioGroup getFirstPointOlineRadioGroup() {
//		return (RadioGroup)findViewById(R.id.gameFragment).findViewById(R.id.radiogroup_game_first_point_oline);
//	}
//	
//	private RadioGroup getGameToRadioGroup() {
//		return (RadioGroup)findViewById(R.id.gameFragment).findViewById(R.id.radiogroup_game_game_to);
//	}

}
