package com.summithillsoftware.ultimate.ui.team;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Team;

public class TeamActivity extends FragmentActivity {
	public static final String NEW_TEAM = "NewTeam";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team, menu);
		return true;
	}

	public void saveClicked(View v) {
		if (isTeamValid()) {
			if (isNewTeam()) {
				Team newTeam = new Team();
				newTeam.setName(getNameTextView().getText().toString());
				newTeam.save();
				Team.setCurrentTeamId(newTeam.getTeamId());
				finish();
			} else {
				
			}
		}
	}

	public void cancelClicked(View v) {
		
	}
	
	private TextView getNameTextView() {
		return (TextView)findViewById(R.id.teamFragment).findViewById(R.id.text_team_name);
	}
	
	private boolean isNewTeam() {
		return getIntent().getBooleanExtra(NEW_TEAM, false);
	}
	
	private boolean isTeamValid() {
		return true;
	}
	
}
