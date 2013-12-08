package com.summithillsoftware.ultimate.ui.game;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.OffenseEvent;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionFieldFragment extends UltimateFragment implements GameActionEventListener {
	private GameActionEventListener gameActionEventListener;
	
	// widgets
	private GameActionButton throwawayButton;
	private GameActionButton opponentGoalButton;
	private TextView pickInitialPlayerInstructionsTextView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_field, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		addPlayerFragments();
		connectWidgets(view);
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		registerWidgetListeners();
		populateView();
	}
	
	public void refresh() {
		populateView();
	}
	
	private void addPlayerFragments() {
		if (getAnonymousPlayerFragment() == null) {  // don't add if already there
			FragmentTransaction ft = getChildFragmentManager().beginTransaction();
			for (int i = 0; i <= 7; i++) {
				GameActionPlayerFragment playerFragment = new GameActionPlayerFragment();
				ft.add(R.id.playerFragments, playerFragment, fragmentTagForPlayer(i));
			}
			ft.commit(); 
		}
	}
	
	private void connectWidgets(View view) {
		throwawayButton = (GameActionButton)view.findViewById(R.id.throwawayButton);
		opponentGoalButton = (GameActionButton)view.findViewById(R.id.opponentGoalButton);
		pickInitialPlayerInstructionsTextView = (TextView)view.findViewById(R.id.pickInitialPlayerInstructionsTextView);
	}
	
	private void populateView() {
		List<Player> line = Game.current().currentLineSorted();
		for (int i = 0; i < 7; i++) {
			if (line.size() > i) {
				getPlayerFragment(i).setPlayer(line.get(i));
			} else {
				getPlayerFragment(i).setPlayer(null);
			}
		}
		getAnonymousPlayerFragment().setPlayer(Player.anonymous());
		refreshButtons();
	}
	
	private void refreshButtons() {
		boolean isOffense = Game.current().arePlayingOffense();
		boolean isFirstEventOfPoint = !Game.current().isPointInProgess();		
		if (isOffense) {
			opponentGoalButton.setVisibility(View.GONE);
			throwawayButton.setVisibility(View.GONE);  // will be made visible later if a passer is selected
			pickInitialPlayerInstructionsTextView.setVisibility(View.VISIBLE);  // will be made invisible later if a passer is selected
		} else {
			opponentGoalButton.setVisibility(isFirstEventOfPoint ? View.GONE : View.VISIBLE);			
			throwawayButton.setVisibility(isFirstEventOfPoint ? View.GONE : View.VISIBLE);  
			pickInitialPlayerInstructionsTextView.setVisibility(View.GONE);
		}
		for (int i = 0; i <= 7; i++) {
			GameActionPlayerFragment playerFragment = getPlayerFragment(i);
			playerFragment.setOffense(isOffense);
			playerFragment.setFirstPointOfEvent(isFirstEventOfPoint);
		}
		
		Player selectedPlayer = null;
		if (isOffense && !isFirstEventOfPoint) {
			Event lastEvent = Game.current().getLastEvent();
			if (lastEvent != null && lastEvent.getAction() == Action.Catch) {
				selectedPlayer = ((OffenseEvent)lastEvent).getReceiver();
			}
		} 
		updateSelectedPasser(selectedPlayer);
	}
	
	private void updateSelectedPasser(Player selectedPlayer) {
		for (int i = 0; i <= 7; i++) {
			GameActionPlayerFragment playerFragment = getPlayerFragment(i);
			playerFragment.setSelected(playerFragment.isFragmentForPlayer(selectedPlayer));
			playerFragment.setInitialPlayerBeenSelected(selectedPlayer != null);
		}
		if (selectedPlayer != null) {
			throwawayButton.setVisibility(View.VISIBLE);
			pickInitialPlayerInstructionsTextView.setVisibility(View.GONE);
		}
	}
        
	private GameActionPlayerFragment getAnonymousPlayerFragment() {
		return getPlayerFragment(7);
	}
	
	private void registerWidgetListeners() {
		throwawayButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleThrowawayPressed();
			}
		});
		opponentGoalButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleOpponentGoalPressed();
			}
		});		
		for (int i = 0; i <= 7; i++) {
			GameActionPlayerFragment playerFragment = getPlayerFragment(i);
			playerFragment.setGameActionEventListener(this);
		}
	}
	
	private GameActionPlayerFragment getSelectedPlayerFragment() {
		for (int i = 0; i < 7; i++) {
			GameActionPlayerFragment fragment = getPlayerFragment(i);
			if (fragment.isSelected()) {
				return fragment;
			}
		}
		return null;
	}
	
	private void handleThrowawayPressed() {
		if (Game.current().arePlayingOffense()) {
			GameActionPlayerFragment selectedFragment = getSelectedPlayerFragment();
			if (selectedFragment != null && selectedFragment.getPlayer() != null) {
				notifyNewEvent(new OffenseEvent(Action.Throwaway, selectedFragment.getPlayer()));
			}
		} else {
			notifyNewEvent(new DefenseEvent(Action.Throwaway, Player.anonymous()));
		}
	}

	private void handleOpponentGoalPressed() {
		notifyNewEvent(new DefenseEvent(Action.Goal, Player.anonymous()));
	}

	private GameActionPlayerFragment getPlayerFragment(int i) {
		return (GameActionPlayerFragment)getChildFragmentManager().findFragmentByTag(fragmentTagForPlayer(i));
	}
	
	private String fragmentTagForPlayer(int i) {
		return "PlayerFrag" + i;
	}

	public GameActionEventListener getGameActionEventListener() {
		return gameActionEventListener;
	}

	public void setGameActionEventListener(
			GameActionEventListener gameActionEventListener) {
		this.gameActionEventListener = gameActionEventListener;
	}
	
	private void notifyNewEvent(Event event) {
		if (gameActionEventListener != null) {
			gameActionEventListener.newEvent(event);
		}
	}
	
	@Override
	public void newEvent(Event event) {
		GameActionPlayerFragment selectedFragment = getSelectedPlayerFragment();
		if (event.isOffense()) {
			if (selectedFragment != null) {
				((OffenseEvent)event).setPasser(selectedFragment.getPlayer());
			}
		} else {
			if (selectedFragment != null && event.isD()) {
				((DefenseEvent)event).setDefender(selectedFragment.getPlayer());
			}
		}
		notifyNewEvent(event);
	}

	@Override
	public void removeEvent(Event event) {
		// no-op...don't remove events in this fragment
	}

	@Override
	public void initialOffensePlayerSelected(Player selectedPlayer) {
		updateSelectedPasser(selectedPlayer);
	}


}
