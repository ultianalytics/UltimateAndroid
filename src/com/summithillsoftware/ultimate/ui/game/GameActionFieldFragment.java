package com.summithillsoftware.ultimate.ui.game;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionFieldFragment extends UltimateFragment {
	// widgets
	private GameActionButton throwawayButton;
	private GameActionButton opponentGoalButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_field, null);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		addPlayerFragments();
		connectWidgets(view);
		registerWidgetListeners();
		super.onViewCreated(view, savedInstanceState);
	}
	
	@Override
	public void onStart() {
		super.onStart();
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
	}
	
	private void handleThrowawayPressed() {
		// TODO...finish
	}

	private void handleOpponentGoalPressed() {
		//	TODO...finish
	}

	private GameActionPlayerFragment getPlayerFragment(int i) {
		return (GameActionPlayerFragment)getChildFragmentManager().findFragmentByTag(fragmentTagForPlayer(i));
	}
	
	private String fragmentTagForPlayer(int i) {
		return "PlayerFrag" + i;
	}


}
