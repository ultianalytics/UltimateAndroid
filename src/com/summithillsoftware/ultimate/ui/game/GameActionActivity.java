package com.summithillsoftware.ultimate.ui.game;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.ui.AbstractActivity;

public class GameActionActivity extends AbstractActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_action);
		setupActionBar();  // Show the Up button in the action bar.		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.game_action, menu);
		return true;
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setTitle(getString(R.string.common_versus_short) + " " + Game.current().getOpponentName());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			return navigateUp();
		case R.id.action_line:
			showLineDialog();
			return true;
		}		
		return super.onOptionsItemSelected(item);
	}
	
	private void showLineDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    LineDialogFragment lineDialog = new LineDialogFragment();
	 // TODO...make this conditional based on screen size	    
	    boolean isLargeScreen = true;
	    
	    if (isLargeScreen) { 
	        // The device is using a large layout, so show the fragment as a dialog
	    	lineDialog.show(fragmentManager, "dialog");
	    } else {
	        // The device is smaller, so show the fragment fullscreen
	        FragmentTransaction transaction = fragmentManager.beginTransaction();
	        // For a little polish, specify a transition animation
	        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
	        // To make it fullscreen, use the 'content' root view as the container
	        // for the fragment, which is always the root view for the activity
	        transaction.add(android.R.id.content, lineDialog).addToBackStack(null).commit();
	    }
	}


}
