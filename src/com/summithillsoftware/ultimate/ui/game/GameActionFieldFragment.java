package com.summithillsoftware.ultimate.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class GameActionFieldFragment extends UltimateFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_game_action_field, container);
		return view;
	}

}
