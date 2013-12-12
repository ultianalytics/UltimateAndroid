package com.summithillsoftware.ultimate.ui.game;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.CessationEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class GameActionActivity extends UltimateActivity implements GameActionEventListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_action);
		getFieldFragment().setGameActionEventListener(this);
		getRecentEventsFragment().setGameActionEventListener(this);
		setupActionBar();  // Show the Up button in the action bar.		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_action, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		updateTitle();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return navigateUp();
		case R.id.action_line:
			showLineDialog();
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	private void populateView() {
		updateTitle();
		if (getFieldFragment() != null) {
			getFieldFragment().refresh();
		}
		if (getRecentEventsFragment() != null) {
			getRecentEventsFragment().refresh();
		}
	}
	
	private void updateTitle() {
		String score = GameActivity.formatScore(Game.current(), this);
		setTitle(getString(R.string.common_versus_short) + " " + Game.current().getOpponentName() + " : " + score);
	}
	
	private void showLineDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    LineDialogFragment lineDialog = new LineDialogFragment();
    	lineDialog.show(fragmentManager, "dialog");
	}
	
	public void lineDialogDismissed() { // call back from dialog
		populateView();
	}
	
	private GameActionFieldFragment getFieldFragment() {
		return (GameActionFieldFragment)getSupportFragmentManager().findFragmentById(R.id.fieldFragment);
	}
	
	private GameActionRecentEventsFragment getRecentEventsFragment() {
		return (GameActionRecentEventsFragment)getSupportFragmentManager().findFragmentById(R.id.recentsFragment);
	}

	@Override
	public void newEvent(Event event) {
		Game.current().addEvent(event);
		populateView();
		Game.current().save();
	    if (event.causesDirectionChange()) {
	    	if (event.isGoal() && Game.current().isNextEventImmediatelyAfterHalftime() && !Game.current().isTimeBasedGame()) {
	    		showHalftimeWarning();
	    	} else if (Game.current().doesGameAppearDone()) {
	    		confirmGameOver();
	    	} else if (event.causesLineChange()) {
	    		showLineDialog();
	    	}
	    }
	}

	@Override
	public void removeLastEvent() {
		Game.current().removeLastEvent();
		populateView();
		Game.current().save();
	}

	@Override
	public void initialOffensePlayerSelected(Player player) {
		// no-op ...don't care about this 
	}

	@Override
	public void timeoutInfoRequested() {
		// TODO Auto-generated method stub
	}
	
	private void showHalftimeWarning() {
		// TODO tweet halftime
		//	    if ([[Tweeter getCurrent] isTweetingEvents]) {
		//	        [[Tweeter getCurrent] tweetHalftimeWithoutEvent];
		//	    }
		
		String message = Game.current().isCurrentlyOline() ? getString(R.string.alert_action_halftime_message_receive) : getString(R.string.alert_action_halftime_message_defend);
		if (Game.current().getWind().isSpecified()) {
			message = message + "\n\n" + getString(R.string.alert_action_wind_speed_reminder);
		}
		displayConfirmDialog(
				getString(R.string.alert_action_halftime_title), 
				message, 
				getString(android.R.string.ok), 
				null, 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						showLineDialog();
					}
		 		});

	}
	
	private void confirmGameOver() {
		final Game game = Game.current();
		String message = game.isCurrentlyOline() ? getString(R.string.alert_action_halftime_message_receive) : getString(R.string.alert_action_halftime_message_defend);
		if (game.getWind().isSpecified()) {
			message = message + "\n\n" + getString(R.string.alert_action_wind_speed_reminder);
		}
		displayConfirmDialog(
				getString(R.string.alert_action_gameover_title), 
				getString(R.string.alert_action_gameover_message), 
				getString(R.string.alert_action_gameover_yes), 
				getString(R.string.alert_action_gameover_no), 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						// confirmed: game over
						if (game.isTimeBasedGame()) {
							game.addEvent(createNextPeriodEndEvent());
							game.save();
						}
						// TODO...add tweet
						//	[[Tweeter getCurrent] tweetGameOver: [Game getCurrentGame]];
						finish();
					}
		 		},
		 		new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						// no, game is not over yet
						showLineDialog();
					}
		 		});
	}
	
	
	private CessationEvent createNextPeriodEndEvent() {
	    Action nextPeriodEnd = Game.current().nextPeriodEnd();
	    return CessationEvent.createWithAction(nextPeriodEnd);
	}


	
}
