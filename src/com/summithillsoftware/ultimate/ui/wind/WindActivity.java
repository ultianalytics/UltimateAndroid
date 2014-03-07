package com.summithillsoftware.ultimate.ui.wind;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Wind;
import com.summithillsoftware.ultimate.ui.UltimateActivity;
import com.summithillsoftware.ultimate.ui.wind.WindDirectionView.OnWindDirectionChangedListener;

public class WindActivity extends UltimateActivity {
	private ImageButton buttonDirectionRight;
	private ImageButton buttonDirectionLeft;
	private SeekBar windSpeedSeekBar;
	private WindDirectionView directionView;
	private Button lookupSpeedButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wind);
		connectWidgets();
		registerWidgetListeners();
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
		directionView = (WindDirectionView)findViewById(R.id.windFragment).findViewById(R.id.directionView);
		lookupSpeedButton = (Button)findViewById(R.id.windFragment).findViewById(R.id.lookupSpeedButton);
	}
	
	
	private void registerWidgetListeners() {
		buttonDirectionRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleFirstPullDirectionButtonTap(true);
			}
		});
		buttonDirectionLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handleFirstPullDirectionButtonTap(false);
			}
		});
		directionView.setChangeListener(new OnWindDirectionChangedListener() {
			@Override
			public void onWindDirectionChanged(int degreesFromNorth) {
				getWind().setDirectionDegrees(degreesFromNorth);
			}
		});
	}
	
	private void handleFirstPullDirectionButtonTap(boolean isFirstPullLeftToRight) {
		getWind().setFirstPullLeftToRight(isFirstPullLeftToRight);
		populateView();
	}
	
	private void populateView() {
		updateFirstPullDirectionArrow();
		directionView.setDegreesFromNorth(getWind().getDirectionDegrees());
	}

	private void updateFirstPullDirectionArrow() {
		boolean isRight = getWind() == null || getWind().isFirstPullLeftToRight();
		buttonDirectionRight.setSelected(isRight);
		buttonDirectionLeft.setSelected(!isRight);
	}
	
	private Wind getWind() {
		return Game.current().getWind();
	}
	


}
