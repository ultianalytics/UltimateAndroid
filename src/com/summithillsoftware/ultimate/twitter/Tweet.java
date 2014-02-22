package com.summithillsoftware.ultimate.twitter;

public class Tweet {
	private String text;

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

}
