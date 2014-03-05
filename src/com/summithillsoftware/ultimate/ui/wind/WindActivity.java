package com.summithillsoftware.ultimate.ui.wind;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Wind;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class WindActivity extends UltimateActivity {
	private ImageButton buttonDirectionRight;
	private ImageButton buttonDirectionLeft;
	private SeekBar windSpeedSeekBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wind);
		connectWidgets();
		populateView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wind, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	private void connectWidgets() {
		buttonDirectionRight = (ImageButton)findViewById(R.id.windFragment).findViewById(R.id.buttonDirectionRight);
		buttonDirectionLeft = (ImageButton)findViewById(R.id.windFragment).findViewById(R.id.buttonDirectionLeft);
		windSpeedSeekBar = (SeekBar)findViewById(R.id.windFragment).findViewById(R.id.windSpeedSeekBar);
	}
	
	private void populateView() {
		updateFirstPullDirectionArrow();
	}

	private void updateFirstPullDirectionArrow() {
		boolean isRight = getWind() == null || getWind().isFirstPullLeftToRight();
		buttonDirectionRight.setSelected(isRight);
		buttonDirectionLeft.setSelected(!isRight);
	}
	
	private Wind getWind() {
		return null;
	}
}
