package com.summithillsoftware.ultimate.twitter;

import com.summithillsoftware.ultimate.util.UltimateLogger;

import android.os.Handler;
import android.os.Looper;

public class TweetQueue {
	private static TweetQueue Current;
	private Handler handler;
	
	static {
		Current = new TweetQueue();
	}
	
	private TweetQueue() {
		createHandler();
	}
	
	public static TweetQueue current() {
		return Current;
	}

	private void createHandler() {
		Thread thread = new Thread() {
		    public void run() {
		        Looper.prepare();
		        handler = new Handler();
		        Looper.loop();
		    }
		};
		thread.start();
	}
	
	public void submitTweet(String text) {
		Tweet tweet = new Tweet(text);
		queueTweet(tweet);
	}
	
	private void queueTweet(final Tweet tweet) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				sendTweet(tweet);
			}
		});
	}
	
	// runs on background looper thread
	private void sendTweet(Tweet tweet) {
		TwitterClient.current().tweet(tweet);
		UltimateLogger.logError("Tweet sent.  Status is " + tweet.getStatus());
	}
	
}
