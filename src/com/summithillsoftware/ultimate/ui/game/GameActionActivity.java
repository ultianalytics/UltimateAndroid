package com.summithillsoftware.ultimate.ui.game;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class GameActionActivity extends UltimateActivity implements GameActionEventListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_action);
		getFieldFragment().setGameActionEventListener(this);
		getRecentEventsFragment().setGameActionEventListener(this);
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
		updateTitle();
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
	
	private void populateView() {
		updateTitle();
		getFieldFragment().refresh();
		getRecentEventsFragment().refresh();
	}
	
	private void updateTitle() {
		String score = GameActivity.formatScore(Game.current(), this);
		setTitle(getString(R.string.common_versus_short) + " " + Game.current().getOpponentName() + " : " + score);
	}
	
	private void showLineDialog() {
	    FragmentManager fragmentManager = getSupportFragmentManager();
	    LineDialogFragment lineDialog = new LineDialogFragment();
    	lineDialog.show(fragmentManager, "dialog");
	    lineDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface paramDialogInterface) {
				populateView();
			}
		});
	}
	
	private GameActionFieldFragment getFieldFragment() {
		return (GameActionFieldFragment)getSupportFragmentManager().findFragmentById(R.id.fieldFragment);
	}
	
	private GameActionRecentEventsFragment getRecentEventsFragment() {
		return (GameActionRecentEventsFragment)getSupportFragmentManager().findFragmentById(R.id.recentsFragment);
	}

	@Override
	public void newEvent(Event event) {
		Game.current().addEvent(event);
		populateView();
		Game.current().save();
	}

	@Override
	public void removeLastEvent() {
		Game.current().removeLastEvent();
		populateView();
		Game.current().save();
	}

	@Override
	public void initialOffensePlayerSelected(Player player) {
		// no-op ...don't care about this 
	}

	@Override
	public void timeoutInfoRequested() {
		// TODO Auto-generated method stub
	}


}
