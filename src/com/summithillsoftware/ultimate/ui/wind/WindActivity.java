package com.summithillsoftware.ultimate.ui.wind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Wind;
import com.summithillsoftware.ultimate.ui.UltimateActivity;

public class WindActivity extends UltimateActivity {
	public final static String WIND_EXTRA = "wind";
	
	private ImageButton buttonDirectionRight;
	private ImageButton buttonDirectionLeft;
	private SeekBar windSpeedSeekBar;
	private View directionView;
	private Button lookupSpeedButton;
	
	private Wind wind;


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
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(WIND_EXTRA, getWind());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		wind = (Wind)savedInstanceState.getSerializable(WIND_EXTRA);
	}
	
	
	private void connectWidgets() {
		buttonDirectionRight = (ImageButton)findViewById(R.id.windFragment).findViewById(R.id.buttonDirectionRight);
		buttonDirectionLeft = (ImageButton)findViewById(R.id.windFragment).findViewById(R.id.buttonDirectionLeft);
		windSpeedSeekBar = (SeekBar)findViewById(R.id.windFragment).findViewById(R.id.windSpeedSeekBar);
		directionView = (View)findViewById(R.id.windFragment).findViewById(R.id.directionView);
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
	}
	
	private void handleFirstPullDirectionButtonTap(boolean isFirstPullLeftToRight) {
		getWind().setFirstPullLeftToRight(isFirstPullLeftToRight);
		updateResult();
		populateView();
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
		if (wind == null) {
			wind = (Wind)getIntent().getExtras().getSerializable(WIND_EXTRA);
			if (wind == null) {
				wind = new Wind();
			}
		}
		return wind;
	}
	
	private void updateResult() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(WIND_EXTRA, getWind());
		setResult(RESULT_OK, resultIntent);
		finish();
	}

}
