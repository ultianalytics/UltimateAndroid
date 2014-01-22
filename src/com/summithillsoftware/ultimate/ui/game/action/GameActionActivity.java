package com.summithillsoftware.ultimate.ui.game.action;

import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.CessationEvent;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.EventHolder;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.PointEvent;
import com.summithillsoftware.ultimate.ui.Refreshable;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.UltimateGestureHelper;
import com.summithillsoftware.ultimate.ui.game.GameActivity;
import com.summithillsoftware.ultimate.ui.game.event.EventDialogFragment;
import com.summithillsoftware.ultimate.ui.game.events.EventsActivity;
import com.summithillsoftware.ultimate.ui.game.line.LineDialogFragment;
import com.summithillsoftware.ultimate.ui.game.pull.PullDialogFragment;
import com.summithillsoftware.ultimate.ui.game.specialevent.SpecialEventDialogFragment;
import com.summithillsoftware.ultimate.ui.game.timeouts.TimeoutsDialogFragment;

public class GameActionActivity extends UltimateActivity implements GameActionEventListener, Refreshable {
	private GameActionFieldFragment fieldFragment;
	private GameActionRecentEventsFragment recentsFragment;
	private GestureOverlayView swipeGestureOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_action);
		connectWidgets();
		registerListeners();
		fieldFragment.setGameActionEventListener(this);
		recentsFragment.setGameActionEventListener(this);
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
	
	private void connectWidgets() {
		fieldFragment = (GameActionFieldFragment)getSupportFragmentManager().findFragmentById(R.id.fieldFragment);
		recentsFragment = (GameActionRecentEventsFragment)getSupportFragmentManager().findFragmentById(R.id.recentsFragment);
		swipeGestureOverlay = (GestureOverlayView)findViewById(R.id.swipeGestureOverlay);
	}
	
	private void registerListeners() {
		// swipe-up detector to show events
		swipeGestureOverlay.addOnGesturePerformedListener(new OnGesturePerformedListener() {
			@Override
			public void onGesturePerformed(GestureOverlayView overlayView, Gesture gesture) {
				if (UltimateGestureHelper.current().isSwipeUp(gesture)) {
					showEventsActivity();
				}
			}
		});

	}

	private void populateView() {
		updateTitle();
		if (fieldFragment != null) {
			fieldFragment.refresh();
		}
		if (recentsFragment != null) {
			recentsFragment.refresh();
		}
	}
	
	private void updateTitle() {
		String score = GameActivity.formatScore(game(), this);
		setTitle(score + " " + getString(R.string.common_versus_short) + " " + game().getOpponentName());
	}
	
	private void showLineDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    LineDialogFragment lineDialog = new LineDialogFragment();
    	lineDialog.show(fragmentManager, "dialog");
	}
	
	public void lineDialogDismissed() { // call back from dialog
		populateView();
	}
	
	private void showPullDialog(DefenseEvent pullEvent) {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    PullDialogFragment pullDialog = new PullDialogFragment();
	    pullDialog.setPlayer(pullEvent.getDefender());
	    pullDialog.show(fragmentManager, "dialog");
	}
	
	private void showSpecialEventDialog(Event event) {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    SpecialEventDialogFragment spcEvtDialog = new SpecialEventDialogFragment();
	    spcEvtDialog.setOriginalEvent(event);
	    spcEvtDialog.show(fragmentManager, "dialog");
	}
	
	private void showEventsActivity() {
		Intent intent = new Intent(this, EventsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
	}
	
	private void showEventDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    EventDialogFragment eventDialog = new EventDialogFragment();
	    eventDialog.show(fragmentManager, "dialog");
	}
	
	private void showTimeoutsDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    TimeoutsDialogFragment timeoutsDialog = new TimeoutsDialogFragment();
	    timeoutsDialog.show(fragmentManager, "dialog");
	}
	
	public void newPull(DefenseEvent pullEvent) { // call back from dialog
		if (pullEvent != null) {
			game().addEvent(pullEvent);
			populateView();
			game().save();
		}
	}

	@Override
	public void newEvent(Event event) {
		if (event.isPull()) {
			showPullDialog((DefenseEvent)event);
		} else {
			game().addEvent(event);
			populateView();
			game().save();
		    if (event.causesDirectionChange()) {
		    	if (event.isGoal() && game().isNextEventImmediatelyAfterHalftime() && !game().isTimeBasedGame()) {
		    		showHalftimeWarning();
		    	} else if (event.isGoal() && game().doesGameAppearDone()) {
		    		confirmGameOver(false);
		    	} else if (event.causesLineChange()) {
		    		showLineDialog();
		    	}
		    }
		}
	}
	
	@Override
	public void potentialNewEvent(Event event) {
		showSpecialEventDialog(event);
	}

	@Override
	public void removeLastEvent() {
		game().removeLastEvent();
		populateView();
		game().save();
	}

	@Override
	public void initialOffensePlayerSelected(Player player) {
		// no-op ...don't care about this 
	}

	@Override
	public void onEventEditRequest(PointEvent pointEvent) {
		game().setSelectedEvent(new EventHolder(pointEvent));
		showEventDialog();
	}
	
	@Override
	public void timeoutInfoRequested() {
		showTimeoutsDialog();
	}
	
	@Override
	public void cessationRequested() {
		if (game().isTimeBasedGame()) {
			Action nextPeriodEnd = game().nextPeriodEnd();
	        if (nextPeriodEnd == Action.EndOfFourthQuarter || nextPeriodEnd == Action.EndOfOvertime) {
	            promptForOvertimeReceiveOrDefend();
	        } else if (nextPeriodEnd != Action.GameOver) {
	        	addEvent(createNextPeriodEndEvent());
	        } else {
	        	confirmGameOver(true);
	        }
	    } else {
	    	confirmGameOver(true);
	    }
	}
	
	private void addEvent(Event event) {
		newEvent(event);
	}
	
	private void showHalftimeWarning() {
		// TODO tweet halftime
		//	    if ([[Tweeter getCurrent] isTweetingEvents]) {
		//	        [[Tweeter getCurrent] tweetHalftimeWithoutEvent];
		//	    }
		
		String message = game().isCurrentlyOline() ? getString(R.string.alert_action_halftime_message_receive) : getString(R.string.alert_action_halftime_message_defend);
		if (game().getWind().isSpecified()) {
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
	
	private void promptForOvertimeReceiveOrDefend() {
		displayThreeButtonDialog(
				getString(R.string.alert_overtime_recieve_or_defend_title), 
				getString(R.string.alert_overtime_recieve_or_defend_message), 
				getString(R.string.button_overtime_receive), 
				getString(R.string.button_overtime_defend), 
				getString(android.R.string.cancel), 
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						Action nextPeriodEnd = game().nextPeriodEnd();
				        CessationEvent periodEndEvent = (nextPeriodEnd == Action.EndOfFourthQuarter) ?
				            CessationEvent.createEndOfFourthQuarterWithOlineStartNextPeriod(true):
				        	CessationEvent.createEndOfOvertimeWithOlineStartNextPeriod(true);
				        addEvent(periodEndEvent);
						game().save();
					}
		 		},
		 		new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						Action nextPeriodEnd = game().nextPeriodEnd();
				        CessationEvent periodEndEvent = (nextPeriodEnd == Action.EndOfFourthQuarter) ?
				            CessationEvent.createEndOfFourthQuarterWithOlineStartNextPeriod(false):
				        	CessationEvent.createEndOfOvertimeWithOlineStartNextPeriod(false);
				        addEvent(periodEndEvent);
						game().save();
					}
		 		},
		 		new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface paramDialogInterface, int paramInt) {
						// no-op
					}
		 		});		
	}
	
	private void confirmGameOver(final boolean userClickedGameOver) {
		final Game game = game();
		String message = game.isCurrentlyOline() ? getString(R.string.alert_action_halftime_message_receive) : getString(R.string.alert_action_halftime_message_defend);
		if (game.getWind().isSpecified()) {
			message = message + "\n\n" + getString(R.string.alert_action_wind_speed_reminder);
		}
		displayConfirmDialog(
				getString(R.string.alert_action_gameover_title), 
				getString(userClickedGameOver ? R.string.alert_action_gameover_message : R.string.alert_action_gameover_probable_message), 
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
						if (!userClickedGameOver) {
							showLineDialog();	
						}
					}
		 		});
	}
	
	private CessationEvent createNextPeriodEndEvent() {
	    Action nextPeriodEnd = game().nextPeriodEnd();
	    return CessationEvent.createWithAction(nextPeriodEnd);
	}
	
	public void refresh() {
		populateView();
	}

	private Game game() {
		return Game.current();
	}

	
}
