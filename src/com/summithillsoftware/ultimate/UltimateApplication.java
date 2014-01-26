package com.summithillsoftware.ultimate;

import android.app.Application;
import android.content.res.Configuration;
import android.webkit.CookieSyncManager;

import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.workflow.Workflow;

public class UltimateApplication extends Application {
	private static UltimateApplication Current;
	private boolean isAppStartInProgress;
	private Workflow activeWorkflow;
	
	public static UltimateApplication current() {
		if (Current == null) {  // this is for unit testing (should have been populated by onCreate)
			Current = new UltimateApplication();
		}
		return Current;
	}

	public UltimateApplication() {
		super();
		isAppStartInProgress = true;
	}
	
	public final void onCreate() {
        super.onCreate(); 
        Current = this;
        CookieSyncManager.createInstance(this);
        ensureOneTeam();
        SoundPlayer.current().loadSounds();
    }  
    
    private void ensureOneTeam() {
    	Team.current();
    }

	public boolean isAppStartInProgress() {
		return isAppStartInProgress;
	}

	public void setAppStartComplete() {
		this.isAppStartInProgress = false;
	}
	
	public boolean isLandscape() {
		return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	public Workflow getActiveWorkflow() {
		return activeWorkflow;
	}

	public void setActiveWorkflow(Workflow activeWorkflow) {
		this.activeWorkflow = activeWorkflow;
	}

	// returns true if the smallest width >= sw600dp
	public boolean isTablet() {
		int smallestScreenWidthDp = getResources().getConfiguration().smallestScreenWidthDp;
		//UltimateLogger.logInfo( "smallestScreenWidthDp = " + smallestScreenWidthDp);
		return smallestScreenWidthDp >= 600;
	}
}