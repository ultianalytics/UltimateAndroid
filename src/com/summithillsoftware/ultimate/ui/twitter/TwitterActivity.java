package com.summithillsoftware.ultimate.ui.twitter;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class TwitterActivity extends UltimateActivity implements TabListener {
	private ViewPager viewPager;
	private TwitterPagerAdapter pageAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		connectWidgets();
		configureTabs();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.twitter, menu);
		return true;
	}
	
	private void connectWidgets() {
		viewPager = (ViewPager)findViewById(R.id.twitterFragment).findViewById(R.id.pager);
		pageAdapter = new TwitterPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pageAdapter);
	}
	
	private void configureTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        Tab tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_tweet).setTabListener(this);
        getSupportActionBar().addTab(tab, true);
        tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_tweet_log).setTabListener(this);
        getSupportActionBar().addTab(tab, false);
        tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_auto_tweet).setTabListener(this);
        getSupportActionBar().addTab(tab, false);
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

}
