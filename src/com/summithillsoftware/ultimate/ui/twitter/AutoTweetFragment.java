package com.summithillsoftware.ultimate.ui.twitter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateFragment;

public class AutoTweetFragment extends UltimateFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_twitter_autotweet, null);
		return view;
	}


}
