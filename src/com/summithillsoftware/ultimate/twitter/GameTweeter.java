package com.summithillsoftware.ultimate.twitter;

import java.text.DateFormat;
import java.util.Date;

import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.DefenseEvent;
import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.OffenseEvent;
import com.summithillsoftware.ultimate.model.Point;
import com.summithillsoftware.ultimate.model.Preferences;
import com.summithillsoftware.ultimate.model.Score;
import com.summithillsoftware.ultimate.model.Team;

public class GameTweeter {
	private static GameTweeter Current;
	
	private DateFormat timeFormatter;
	
	static {
		Current = new GameTweeter();
	}
	
	private GameTweeter() {
		timeFormatter = android.text.format.DateFormat.getTimeFormat(UltimateApplication.current());
	}
	
	public static GameTweeter current() {
		return Current;
	}
	
	public AutoTweetLevel getTweetLevel() {
		AutoTweetLevel level = Preferences.current().getTweetLevel();
		return level == null ? AutoTweetLevel.NONE : level;
	}

	public void setTweetLevel(AutoTweetLevel tweetLevel) {
		Preferences.current().setTweetLevel(tweetLevel);
		Preferences.current().save();
	}
	
	public void tweetEvent(Event event, Point point, boolean isUndo) {
		if (isTweetingEvents()) {
			
		}
		
	}
//	-(void)tweetEvent:(Event*) event forGame: (Game*) game point: (UPoint*) point isUndo: (BOOL) isUndo {
//	    if ([self isTweetingEvents]) {
//	        [self tweetFirstEventOfGameIfNecessary:event forGame:game point:point isUndo:isUndo];
//	        if ([event isGoal]) {
//	            NSString* message = [self goalTweetMessage:event forGame:game isUndo:isUndo]; 
//	            Tweet* tweet = [[Tweet alloc] initMessage:[NSString stringWithFormat:@"%@  %@", message, [self getTime]] type:@"Event"];
//	            tweet.isUndo = isUndo;
//	            tweet.associatedEvent = event;
//	            [self tweet: tweet];
//	            if ([game isNextEventImmediatelyAfterHalftime]) {
//	                NSString* halftimeMessage = [self halftimeTweetMessageIsUndo: isUndo]; 
//	                Tweet* tweet = [[Tweet alloc] initMessage:[NSString stringWithFormat:@"%@  %@", halftimeMessage, [self getTime]] type:@"Halftime"];
//	                tweet.isUndo = isUndo;
//	                tweet.associatedEvent = event;
//	                [self tweet: tweet];
//	            }
//	            [self updateGameTweeted:game event: event undo: isUndo];
//	        } else if ([event isTurnover] && [self getAutoTweetLevel] == TweetGoalsAndTurns) {
//	            NSString* message = [self turnoverTweetMessage:event forGame:game isUndo:isUndo]; 
//	            Tweet* tweet = [[Tweet alloc] initMessage:[NSString stringWithFormat:@"%@  %@", message, [self getTime]] type:@"Event"];
//	            tweet.isUndo = isUndo;
//	            tweet.associatedEvent = event;
//	            tweet.isOptional = YES;
//	            [self tweet: tweet]; 
//	            [self updateGameTweeted:game event: event undo: isUndo];
//	        }
//	    }
//	}
	
	private void tweetFirstEventOfGameIfNecessary(Event event, Point point, boolean isUndo) {
		if (!hasGameBeenTweeted() || (isUndo && game().getFirstEventTweeted().equals(event))) {
			String message = game().isFirstPoint(point) ? gameBeginTweetMessage(event, isUndo) : gameBeginInProgressTweetMessage(event, isUndo);
			Tweet tweet = createTweet(message, event, isUndo);
			tweet(tweet);	
			updateGameTweeted(event, isUndo);
		}
	}
	
	private boolean hasGameBeenTweeted() {
		return game().getFirstEventTweeted() != null;
	}

	private void updateGameTweeted(Event event, boolean isUndo) {
		if (isUndo) {
			if (event.equals(game().getFirstEventTweeted())) {
				game().setFirstEventTweeted(null);
			}
		} else if (game().getFirstEventTweeted() == null) {
			game().setFirstEventTweeted(event);
		}
	}

	private String getGameScoreDescription() {
	    Score score = game().getScore();
	    return score.getOurs() + "-" + score.getTheirs() + (score.getOurs() == score.getTheirs() ? "" : score.getOurs() > score.getTheirs() ?
	            Team.current().getName() : game().getOpponentName());
	}
	
	private String gameBeginTweetMessage(Event event, boolean isUndo) {
		Game game = game();
		String message = null;
		if (isUndo) {
			message = "New game was a boo-boo...never mind.";
		} else {
			String windDescription = game.getWind() != null && game.getWind().getMph() > 0 ? " Wind: " + game.getWind().getMph() + "mph." : "";
			message = "New game vs. " + game.getOpponentName() + ".  Game point: " + game.getGamePoint() + "." + windDescription;			
		}
		return message;
	}
	
	private String gameBeginInProgressTweetMessage(Event event, boolean isUndo) {
		Game game = game();
		String message = null;
		if (isUndo) {
			message = "New game in progress was a boo-boo...never mind.";
		} else {
			String windDescription = game.getWind() != null && game.getWind().getMph() > 0 ? " Wind: " + game.getWind().getMph() + "mph. " : " ";
			message = "New game in progress vs. " + game.getOpponentName() + ".  Game point: " + game.getGamePoint() + "." + windDescription  + "Current score: " + getGameScoreDescription();			
		}
		return message;
	}
	
	private String goalTweetMessage(Event event, boolean isUndo) {
		Game game = game();
		String message = null;
		if (event.isCallahan()) {
			message = "Callahan " + (event.isOffense() ? game.getOpponentName() : ourTeam());
		} else {
			message = "Goal " + (event.isOurGoal() ? ourTeam() : game.getOpponentName());
		}
		
		if (isUndo) {
			message = message + " was a boo-boo...never mind";
		} else {
			if (event.isOurGoal()) {
				if (event.isCallahan()) {
					message = message + "!!! " + ((DefenseEvent)event).getDefender().getName() + ". " + getGameScoreDescription()  + ".";
				} else {
					message = message + "!!! " + ((OffenseEvent)event).getPasser().getName() + " to " + ((OffenseEvent)event).getReceiver().getName() + ". " + getGameScoreDescription() + ".";
				}
			} else {
				message = message + ". " + getGameScoreDescription();
			}
		}
		return message;
	}
	
	private boolean isTweetingEvents() {
		return getTweetLevel() != AutoTweetLevel.NONE;
	}
	
	private Game game() {
		return Game.current();
	}
	
	private String ourTeam() {
		return Team.current().getName();
	}
	
	private String prependTime(String message) {
		return timeFormatter.format(new Date()) + " " + message;
	}
	
	private Tweet createTweet(String message) {
		Tweet tweet = new Tweet(prependTime(message));
		return tweet;
	}
	
	private Tweet createTweet(String message, Event event, boolean isUndo) {
		Tweet tweet = createTweet(message);
		tweet.setEvent(event);
		tweet.setUndo(isUndo);
		return tweet;
	}
	
	private void tweet(Tweet tweet) {
		TweetQueue.current().submitTweet(tweet);
	}

}
