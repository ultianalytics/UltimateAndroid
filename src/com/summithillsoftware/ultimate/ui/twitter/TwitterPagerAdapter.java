package com.summithillsoftware.ultimate.ui.twitter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TwitterPagerAdapter extends FragmentPagerAdapter {

	public TwitterPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int index) {
		switch (index) {
        case 0:
            return new TweetFragment();
        case 1:
            // Games fragment activity
            return new TweetLogFragment();
        case 2:
            // Movies fragment activity
            return new AutoTweetFragment();
        }
 
        return null;
	}

	@Override
	public int getCount() {
		return 3;
	}



}
