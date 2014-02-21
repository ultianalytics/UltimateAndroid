package com.summithillsoftware.ultimate.ui.twitter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.view.Menu;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class TwitterActivity extends UltimateActivity implements TabListener, ViewPager.OnPageChangeListener {
	private ViewPager viewPager;
	private TwitterPagerAdapter pageAdapter;
	private ArrayList<Tab> tabs = new ArrayList<Tab>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter);
		connectWidgets();
		configureTabs();
	    if (savedInstanceState != null) {
	    	getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.twitter, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    super.onPrepareOptionsMenu(menu);
	    menu.findItem(R.id.action_twitter_logout).setVisible(!isSignedIn());
	    return true;
	}
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }
	
	private void connectWidgets() {
		viewPager = (ViewPager)findViewById(R.id.twitterFragment).findViewById(R.id.pager);
		pageAdapter = new TwitterPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(pageAdapter);
		viewPager.setOnPageChangeListener(this);
	}
	
	private void configureTabs() {
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        Tab tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_tweet).setTabListener(this);
        tab.setTag("tweet");
        tabs.add(tab);
        getSupportActionBar().addTab(tab, true);
        tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_tweet_log).setTabListener(this);
        tab.setTag("log");
        tabs.add(tab);
        getSupportActionBar().addTab(tab, false);
        tab = getSupportActionBar().newTab().setText(R.string.tab_twitter_auto_tweet).setTabListener(this);
        tab.setTag("auto");
        tabs.add(tab);
        getSupportActionBar().addTab(tab, false);
	}
	
	
	private boolean isSignedIn() {
		// TODO...finish this
		return true;
	}
	
	/* ActionBar.TabListener */

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// no-op
		
	}

	@Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        for (int i=0; i < tabs.size(); i++) {
            if (tabs.get(i).getTag().equals(tab.getTag())) {
                viewPager.setCurrentItem(i);
            }
        }
    }


	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// no-op
		
	}

	
	/*   ViewPager.OnPageChangeListener */
	
	@Override
	public void onPageScrollStateChanged(int state) {
		// no-op
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// no-op
	}

	@Override
    public void onPageSelected(int position) {
		getSupportActionBar().setSelectedNavigationItem(position);
    }

}