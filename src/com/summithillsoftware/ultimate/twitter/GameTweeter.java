package com.summithillsoftware.ultimate.twitter;

import com.summithillsoftware.ultimate.model.Preferences;


public class GameTweeter {
	private static GameTweeter Current;

	
	static {
		Current = new GameTweeter();
	}
	
	private GameTweeter() {

	}
	
	public static GameTweeter current() {
		return Current;
	}
	
	public GameTweetLevel getTweetLevel() {
		GameTweetLevel level = Preferences.current().getTweetLevel();
		return level == null ? GameTweetLevel.NONE : level;
	}

	public void setTweetLevel(GameTweetLevel tweetLevel) {
		Preferences.current().setTweetLevel(tweetLevel);
		Preferences.current().save();
	}

}
