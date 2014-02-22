package com.summithillsoftware.ultimate.twitter;

import twitter4j.RateLimitStatus;

public class Tweet {
	private String text;
	private TweetStatus status;
	private RateLimitStatus limitStatus;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Tweet(String text) {
		super();
		this.text = text;
	}

	public TweetStatus getStatus() {
		return status;
	}

	public void setStatus(TweetStatus status) {
		this.status = status;
	}
	
	public boolean wasSendSuccessful() {
		return status == TweetStatus.OK;
	}

	@Override
	public String toString() {
		return "Tweet [text=" + text + ", status=" + status + "]";
	}

	public RateLimitStatus getLimitStatus() {
		return limitStatus;
	}

	public void setLimitStatus(RateLimitStatus limitStatus) {
		this.limitStatus = limitStatus;
	}

}
