package com.summithillsoftware.ultimate.ui.game;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionRecentEventsFragment extends UltimateFragment {
	private GameActionEventListener gameActionEventListener;
	
	// widgets
	private Button undoLastEventButton;
	private Button timeoutButton;
	private Button cessationButton;
	private TextView event1TextView;
	private TextView event2TextView;
	private TextView event3TextView;
	
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
		event1TextView = (TextView)view.findViewById(R.id.event1TextView);
		event2TextView = (TextView)view.findViewById(R.id.event2TextView);
		event3TextView = (TextView)view.findViewById(R.id.event3TextView);
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
					handleCessationButtonPressed();
				}
			}
		});	
	}
	
	private void handleCessationButtonPressed() {
		
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
		List<Event> lastFewEvents = Game.current().getLastEvents(3);
		event1TextView.setText(lastFewEvents.size() >= 1 ? lastFewEvents.get(0).toString() : "");
		event2TextView.setText(lastFewEvents.size() >= 2 ? lastFewEvents.get(1).toString() : "");
		event3TextView.setText(lastFewEvents.size() >= 3 ? lastFewEvents.get(2).toString() : "");
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
	
}
