package com.summithillsoftware.ultimate.ui.game;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionFieldFragment extends UltimateFragment {
	// widgets
	private List<GameActionPlayerFragment> playerFragments;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_field, container);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		connectWidgets(view);
//		populateView();
		super.onViewCreated(view, savedInstanceState);
	}
	
	private void connectWidgets(View view) {
		playerFragments = new ArrayList<GameActionPlayerFragment>();
		FragmentManager fragmentManager = getChildFragmentManager();
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment1)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment2)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment3)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment4)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment5)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment6)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragment7)));
		playerFragments.add((GameActionPlayerFragment)(fragmentManager.findFragmentById(R.id.playerFragmentAnonymous)));
	}
	
	private void populateView() {
		List<Player> line = Game.current().currentLineSorted();
		for (int i = 0; i < 7; i++) {
			if (line.size() >= i) {
				playerFragments.get(i).setPlayer(line.get(i));
			} else {
				playerFragments.get(i).setPlayer(null);
			}
		}
		getAnonymousPlayerFragment().setPlayer(Player.anonymous());
	}
	
	private GameActionPlayerFragment getAnonymousPlayerFragment() {
		return playerFragments.get(7);
	}

}
