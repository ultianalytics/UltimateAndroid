package com.summithillsoftware.ultimate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Point implements Serializable {
	private static final long serialVersionUID = 8897190640184011607L;
	
	private List<Event> events;
	private List<Player> line;
	private List<PlayerSubstitution> substitutions;
	private int timeStartedSeconds; // since epoch
	private int timeEndedSeconds; // since epoch	
	private transient PointSummary summary;
	
	public Point() {
		super();
		events = new ArrayList<Event>();
		line = new ArrayList<Player>();
		substitutions = new ArrayList<PlayerSubstitution>();
	}
	
	public void addEvent(Event event) {
		int now = UniqueTimestampGenerator.current().uniqueTimeIntervalSinceReferenceDateSeconds();
		// first event of point?
		if (events.size() == 0) {
	        // if O-line, reduce the start time to account for the pull
	        if (!event.isPull()) {
	            now -= 5;  // assume 5 seconds
	        }
	        timeStartedSeconds = now;
		}
		event.setTimestamp(now);
		timeEndedSeconds = UniqueTimestampGenerator.current().uniqueTimeIntervalSinceReferenceDateSeconds();;
		events.add(event);
	}
	
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public List<Player> getLine() {
		return line;
	}
	public void setLine(List<Player> line) {
		this.line = line;
	}
	public List<PlayerSubstitution> getSubstitutions() {
		return substitutions;
	}
	public void setSubstitutions(List<PlayerSubstitution> substitutions) {
		this.substitutions = substitutions;
	}
	public int getTimeStartedSeconds() {
		return timeStartedSeconds;
	}
	public void setTimeStartedSeconds(int timeStartedSeconds) {
		this.timeStartedSeconds = timeStartedSeconds;
	}
	public int getTimeEndedSeconds() {
		return timeEndedSeconds;
	}
	public void setTimeEndedSeconds(int timeEndedSeconds) {
		this.timeEndedSeconds = timeEndedSeconds;
	}
	public PointSummary getSummary() {
		return summary;
	}
	public void setSummary(PointSummary summary) {
		this.summary = summary;
	}

}
