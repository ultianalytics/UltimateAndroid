package com.summithillsoftware.ultimate;

import com.summithillsoftware.ultimate.model.Team;

import android.app.Application;

public class UltimateApplication extends Application {
	private static UltimateApplication Current;
	
	public static UltimateApplication current() {
		if (Current == null) {  // this is for unit testing (should have been populated by onCreate)
			Current = new UltimateApplication();
		}
		return Current;
	}

    public final void onCreate() {
        super.onCreate(); 
        Current = this;
        ensureOneTeam();
    }  
    
    private void ensureOneTeam() {
    	Team.current();
    }
    
}