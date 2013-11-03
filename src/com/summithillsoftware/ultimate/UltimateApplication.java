package com.summithillsoftware.ultimate;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import com.summithillsoftware.ultimate.model.Team;

public class UltimateApplication extends Application {
	private static UltimateApplication Current;
	private boolean isAppStartInProgress;
	
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
        ensureOneTeam();
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

   
}