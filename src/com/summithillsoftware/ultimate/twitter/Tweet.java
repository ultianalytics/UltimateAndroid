package com.summithillsoftware.ultimate.twitter;

import twitter4j.RateLimitStatus;

import com.summithillsoftware.ultimate.model.Event;

public class Tweet {
	private String text;
	private long time;
	private TweetProgressStatus progressStatus;
	private TweetSendStatus sendStatus;
	private RateLimitStatus limitStatus;
	private Event event;
	private boolean isUndo;
	private boolean isOptional;
	
	public Tweet() {
		super();
		time = TwitterTimestampGenerator.current().uniqueTimeIntervalSinceReferenceDateMilliseconds();
		progressStatus = TweetProgressStatus.Queued;
		sendStatus = TweetSendStatus.NotStarted;
	}
	
	public Tweet(String text, Event event) {
		this(text);
		this.event = event;
	}
	
	public Tweet(String text) {
		this();
		this.text = text;
	}
	
	public boolean isAdHoc() {
		return event == null;
	}
	
	public boolean isUndoOfTweet(Tweet otherTweet) {
		if (isUndo && event != null) {
			return event.equals(otherTweet.event);
		}
		return false;
	}
	
	public boolean isCancelled() {
		return progressStatus == TweetProgressStatus.Cancelled;
	}
	
	public boolean isSkipped() {
		return progressStatus == TweetProgressStatus.Skipped;
	}
	
	public boolean isWaiting() {
		return progressStatus == TweetProgressStatus.Queued;
	}
	
	public boolean isSentAndAccepted() {
		return progressStatus == TweetProgressStatus.Sent && sendStatus == TweetSendStatus.OK;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public TweetSendStatus getSendStatus() {
		return sendStatus;
	}

	public void setCompletionStatus(TweetSendStatus sendStatus) {
		this.sendStatus = sendStatus;
	}
	
	public boolean wasSendSuccessful() {
		return sendStatus == TweetSendStatus.OK;
	}

	public RateLimitStatus getLimitStatus() {
		return limitStatus;
	}

	public void setLimitStatus(RateLimitStatus limitStatus) {
		this.limitStatus = limitStatus;
	}
	
	public long getTime() {
		return time;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	public long getId() {
		return time;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (time != other.time)
			return false;
		return true;
	}

	public TweetProgressStatus getProgressStatus() {
		return progressStatus;
	}

	public void setProgressStatus(TweetProgressStatus progressStatus) {
		this.progressStatus = progressStatus;
	}

	public boolean isUndo() {
		return isUndo;
	}

	public void setUndo(boolean isUndo) {
		this.isUndo = isUndo;
	}

	public boolean isOptional() {
		return isOptional;
	}

	public void setOptional(boolean isOptional) {
		this.isOptional = isOptional;
	}

}
