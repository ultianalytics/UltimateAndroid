package com.summithillsoftware.ultimate.ui.game.action;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.PointEvent;
import com.summithillsoftware.ultimate.ui.DefaultAnimationListener;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionRecentEventsFragment extends UltimateFragment {
	private GameActionEventListener gameActionEventListener;
	
	// widgets
	private Button undoLastEventButton;
	private Button timeoutButton;
	private Button cessationButton;
	private RecentEventButton event1Button;
	private View event2ButtonSeparator;
	private RecentEventButton event2Button;
	private View event3ButtonSeparator;	
	private RecentEventButton event3Button;
	private TextView ephemeralMessage;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_recent_events, null);
		return view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		connectWidgets(view);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		registerWidgetListeners();
		populateView();
	}
	
	private void connectWidgets(View view) {
		undoLastEventButton = (Button)view.findViewById(R.id.undoLastEventButton);
		timeoutButton = (Button)view.findViewById(R.id.timeoutButton);
		cessationButton = (Button)view.findViewById(R.id.cessationButton);
		event1Button = (RecentEventButton)view.findViewById(R.id.event1Button);
		event2ButtonSeparator = view.findViewById(R.id.event2ButtonSeparator);
		event2Button = (RecentEventButton)view.findViewById(R.id.event2Button);
		event3ButtonSeparator = view.findViewById(R.id.event3ButtonSeparator);
		event3Button = (RecentEventButton)view.findViewById(R.id.event3Button);
		ephemeralMessage = (TextView)view.findViewById(R.id.ephemeralMessage);
	}
	
	private void registerWidgetListeners() {
		undoLastEventButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (gameActionEventListener != null) {
					gameActionEventListener.removeLastEvent();
				}
			}
		});
		timeoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (gameActionEventListener != null) {
					gameActionEventListener.timeoutInfoRequested();
				}
			}
		});		
		cessationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (gameActionEventListener != null) {
					gameActionEventListener.cessationRequested();
				}
			}
		});	
		event1Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleEventEditPressed(1);
			}
		});		
		event2Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleEventEditPressed(2);
			}
		});	
		event3Button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleEventEditPressed(3);
			}
		});			
	}
	
	private void handleEventEditPressed(int inverseOrderPostion) { // position starts at 1
		List<PointEvent> lastFewEvents = Game.current().getLastEvents(inverseOrderPostion);
		if (lastFewEvents.size() == inverseOrderPostion) {
			PointEvent eventToEdit = lastFewEvents.get(inverseOrderPostion - 1);
			if (gameActionEventListener != null) {
				gameActionEventListener.onEventEditRequest(eventToEdit);
			}
		}
	}
	
	private void populateView() {
		populateRecentEvents();
	    timeoutButton.setText(getString(R.string.button_action_timeouts, Game.current().availableTimeouts()));
	    updateCessationButtonText();
	}
	
	private void updateCessationButtonText() {
		String buttonText = "";
		if (Game.current().isTimeBasedGame()) {
			Action nextPeriodEnd = Game.current().nextPeriodEnd();
	        switch (nextPeriodEnd) {
			case EndOfFirstQuarter:
				buttonText = getString(R.string.button_action_cessation_first_quarter);
				break;
			case Halftime:
				buttonText = getString(R.string.button_action_cessation_halftime);
				break;
			case EndOfThirdQuarter:
				buttonText = getString(R.string.button_action_cessation_third_quarter);
				break;
			case EndOfFourthQuarter:
				buttonText = getString(R.string.button_action_cessation_fourth_quarter);
				break;
			case EndOfOvertime:
				buttonText = getString(R.string.button_action_cessation_overtime);
				break;				
			default:
				buttonText = getString(R.string.button_action_cessation_game_over);
				break;
			}
	    } else {
	    	buttonText = getString(R.string.button_action_cessation_game_over);
	    }
		cessationButton.setText(buttonText);
	}

	private void populateRecentEvents() {
		List<PointEvent> lastFewEvents = Game.current().getLastEvents(3);
		event1Button.setEventDescription(lastFewEvents.size() >= 1 ? lastFewEvents.get(0).getEvent().toString() : "");
		event2Button.setEventDescription(lastFewEvents.size() >= 2 ? lastFewEvents.get(1).getEvent().toString() : "");
		event3Button.setEventDescription(lastFewEvents.size() >= 3 ? lastFewEvents.get(2).getEvent().toString() : "");
		event1Button.setEventImage(lastFewEvents.size() >= 1 ? lastFewEvents.get(0).getEvent().imageMonochrome() : blankEventImage());
		event2ButtonSeparator.setVisibility(lastFewEvents.size() >= 2 ? View.VISIBLE :View.INVISIBLE);
		event2Button.setEventImage(lastFewEvents.size() >= 2 ? lastFewEvents.get(1).getEvent().imageMonochrome() : blankEventImage());
		event3ButtonSeparator.setVisibility(lastFewEvents.size() >= 3 ? View.VISIBLE :View.INVISIBLE);
		event3Button.setEventImage(lastFewEvents.size() >= 3 ? lastFewEvents.get(2).getEvent().imageMonochrome() : blankEventImage());
		undoLastEventButton.setVisibility(lastFewEvents.size() > 0 ? View.VISIBLE :View.INVISIBLE);
	}
	
	public void refresh() {
		populateView();
	}

	public GameActionEventListener getGameActionEventListener() {
		return gameActionEventListener;
	}

	public void setGameActionEventListener(
			GameActionEventListener gameActionEventListener) {
		this.gameActionEventListener = gameActionEventListener;
	}
	
	private int blankEventImage() {
		return R.drawable.unknown_event;
	}
	
	public void displayEphemeralMessage(String message, final int seconds) {
		ephemeralMessage.setText(message);
		
    	Animation animation = new AlphaAnimation(0.0f, 1.0f);
    	animation.setDuration(500);
    	animation.setFillAfter(true); // persist animation
    	animation.setAnimationListener(new DefaultAnimationListener() {
    		@Override
    		public void onAnimationEnd(Animation paramAnimation) {
    			
    			Timer timer = new Timer();
    			TimerTask task = new TimerTask() {
    				@Override
    				public void run() {
    					getActivity().runOnUiThread(new Runnable(){
    						@Override
    						public void run() {
    							if (ephemeralMessage != null) {
    						    	Animation animation = new AlphaAnimation(1.0f, 0.0f);
    						    	animation.setDuration(500);
    						    	animation.setFillAfter(true); 
    						    	ephemeralMessage.startAnimation(animation);
    							}
    						}
    					});
    				}
    			};
    			timer.schedule(task, seconds * 1000);
    		}
    	});
    	ephemeralMessage.setVisibility(View.VISIBLE);
    	ephemeralMessage.startAnimation(animation);
	}
	
}
