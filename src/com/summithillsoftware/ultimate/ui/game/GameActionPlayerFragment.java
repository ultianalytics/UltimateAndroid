package com.summithillsoftware.ultimate.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Action;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.OffenseEvent;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionPlayerFragment extends UltimateFragment {
	private Player player;
	private boolean isOffense;
	private boolean isFirstEventOfPoint;
	private boolean isSelected;
	private GameActionEventListener gameActionEventListener;
	
	// widgets
	private GameActionInitiatorPlayerButton initiatorPlayerButton;
	private ImageView passingDirectionArrow;
	private GameActionButton catchButton;
	private GameActionButton dropButton;
	private GameActionButton goalButton;
	private GameActionButton pullButton;
	private GameActionButton deButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_player, null);
		connectWidgets(view);
		registerWidgetListeners();
		populateView();
		return view;
	}
	
	private void connectWidgets(View view) {
		initiatorPlayerButton = (GameActionInitiatorPlayerButton)view.findViewById(R.id.initiatorPlayerButton);
		passingDirectionArrow = (ImageView)view.findViewById(R.id.passingDirectionArrow);
		catchButton = (GameActionButton)view.findViewById(R.id.catchButton);
		dropButton = (GameActionButton)view.findViewById(R.id.dropButton);
		goalButton = (GameActionButton)view.findViewById(R.id.goalButton);
		pullButton = (GameActionButton)view.findViewById(R.id.pullButton);
		deButton = (GameActionButton)view.findViewById(R.id.deButton);
	}
	
	private void registerWidgetListeners() {
		registerActionButtonListener(catchButton, Action.Catch);
		registerActionButtonListener(dropButton, Action.Drop);
		registerActionButtonListener(goalButton, Action.Goal);
		registerActionButtonListener(pullButton, Action.Pull);
		registerActionButtonListener(deButton, Action.De);
		
		initiatorPlayerButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handlePlayerSelected();
			}
		});
		
	}
	
	private void populateView() {
		if (getView() != null) {
			configureButtonVisibility();
			if (player != null) {
				initiatorPlayerButton.setPlayer(player);
				initiatorPlayerButton.setSelected(isSelected);
			} 
		}
	}
	
	private void configureButtonVisibility() {
		initiatorPlayerButton.setVisibility((player != null) ? View.VISIBLE : View.INVISIBLE);
		passingDirectionArrow.setVisibility((player != null) && isOffense && !isFirstEventOfPoint ? View.VISIBLE : View.GONE);
		catchButton.setVisibility((player != null) && isOffense && !isFirstEventOfPoint ? View.VISIBLE : View.GONE);
		dropButton.setVisibility((player != null) && isOffense && !isFirstEventOfPoint ? View.VISIBLE : View.GONE);
		goalButton.setVisibility((player != null) && isOffense && !isFirstEventOfPoint ? View.VISIBLE : View.GONE);
		pullButton.setVisibility((player != null) && !isOffense && isFirstEventOfPoint ? View.VISIBLE : View.GONE);
		deButton.setVisibility((player != null) && !isOffense && !isFirstEventOfPoint ? View.VISIBLE : View.GONE);
	}
	
	private void registerActionButtonListener(Button button, final Action action) {
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleActionClicked(action);
			}
		});
	}
	
	private void handleActionClicked(Action action) {
		switch (action) {
		case Catch:
			notifyNewEvent(new OffenseEvent(action, Player.anonymous(), player));
			break;
		case Drop:
			notifyNewEvent(new OffenseEvent(action, Player.anonymous(), player));
			break;
		case Goal:
			if (isOffense) {
				notifyNewEvent(new OffenseEvent(action, Player.anonymous(), player));
			} else {
				notifyNewEvent(new DefenseEvent(action, Player.anonymous()));
			}
			break;		
		case Throwaway:
			if (isOffense) {
				notifyNewEvent(new OffenseEvent(action, Player.anonymous()));
			} else {
				notifyNewEvent(new DefenseEvent(action, Player.anonymous()));
			}
			break;		
		case De:
			notifyNewEvent(new DefenseEvent(action, player));
			break;				
		default:
			break;
		}
	}
	
	private void handlePlayerSelected() {
		notifyPlayerSelected();
	}
	
	private void notifyNewEvent(Event event) {
		if (gameActionEventListener != null) {
			gameActionEventListener.newEvent(event);
		}
	}
	
	private void notifyPlayerSelected() {
		if (gameActionEventListener != null) {
			gameActionEventListener.initialPlayerSelected(this.player);
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
		populateView();
	}
	public boolean isOffense() {
		return isOffense;
	}
	public void setOffense(boolean isOffense) {
		this.isOffense = isOffense;
		populateView();
	}
	public boolean isFirstEventOfPoint() {
		return isFirstEventOfPoint;
	}
	public void setFirstEventOfPoint(boolean isFirstEventOfPoint) {
		this.isFirstEventOfPoint = isFirstEventOfPoint;
		populateView();
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
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
