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
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionRecentEventsFragment extends UltimateFragment {
	
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
				// TODO...finish
			}
		});
		timeoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO...finish
			}
		});		
		cessationButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO...finish
			}
		});	
	}
	
	private void populateView() {
		populateRecentEvents();
		// TODO...populate timeouts button
		// TODO...populate cessation button		
	}
	
	private void populateRecentEvents() {
		List<Event> lastFewEvents = Game.current().getLastEvents(3);
		event1TextView.setText(lastFewEvents.size() >= 1 ? lastFewEvents.get(0).toString() : "");
		event2TextView.setText(lastFewEvents.size() >= 2 ? lastFewEvents.get(1).toString() : "");
		event3TextView.setText(lastFewEvents.size() >= 3 ? lastFewEvents.get(2).toString() : "");
	}
	
	public void refresh() {
		populateView();
	}
	
}
