package com.summithillsoftware.ultimate.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	
	public Event getEventAtMostRecentIndex(int index) {
		// events are stored in ascending order but we are being asked for an index in descending order
		if (events.size() > index ) {
			return events.get(index);
		} else {
			return null;
		}
	}
	
	public Event getLastEvent() {
		if (events.size() > 0) {
			return events.get(events.size() -1);
		} else {
			return null;
		}
		
	}
	
	public Event getLastPlayEvent() {
		for (int i = events.size() - 1; i >= 0; i--) {
			Event evt = events.get(i);
			if (evt.isPlayEvent()) {
				return evt;
			}
		}
		return null;
	}

	public List<Event> getLastEvents(int numberToRetrieve) {
		int actualNumber = Math.min(events.size(),numberToRetrieve);
		if (actualNumber > 0) {
			List<Event> subList = (List<Event>) events.subList(events.size() - actualNumber, events.size());
			Collections.reverse(subList);
			return subList;
		} else {
			return Collections.emptyList();
		}
	}
	
	public void removeLastEvent() {
		if (events.size() > 0) {
			events.remove(events.size() -1);
		}
	}
	
	public boolean isFinished() {
		return events.size() > 0 && getLastEvent().isFinalEventOfPoint();
	}
	
	public boolean isOurPoint() {
		return getLastPlayEvent().isOurGoal();
	}
	
	public boolean isTheirPoint() {
		return getLastPlayEvent().isTheirGoal();
	}
	
	public int numberOfEvents() {
		return events.size();
	}

	public Set<Player>playersInEntirePoint() {
		HashSet<Player> players = new HashSet<Player>(line);
		if (substitutions.size() > 0) {
			for (PlayerSubstitution sub : substitutions) {
				players.remove(sub.getFromPlayer());
				players.remove(sub.getToPlayer());
			}
		}
		return players;
	}
	
	public Set<Player>playersInPartOfPoint() {
		HashSet<Player> players = new HashSet<Player>();
		if (substitutions.size() > 0) {
			for (PlayerSubstitution sub : substitutions) {
				players.add(sub.getFromPlayer());
				players.add(sub.getToPlayer());
			}
		}
		return players;
	}
	
	public boolean isPeriodEnd() {
		return events.size() > 0 && getLastEvent().isPeriodEnd();
	}
	
	public CessationEvent getPeriodEnd() {
		return isPeriodEnd() ? (CessationEvent)getLastEvent() : null;
	}
	
	public void useSharedPlayers() {
		for (Event evt : events) {
			evt.useSharedPlayers();
		}
		for (PlayerSubstitution sub : substitutions) {
			sub.useSharedPlayers();
		}
	}
	
	public Set<Player> getPlayers() {
		HashSet<Player> players = new HashSet<Player>();
		for (Event event : events) {
			players.addAll(event.getPlayers());
		}
		for (PlayerSubstitution sub : substitutions) {
			players.addAll(sub.getPlayers());
		}
		return players;
	}
	
	public PlayerSubstitution lastSubstitution() {
		return substitutions.isEmpty() ? null : substitutions.get(substitutions.size() -1);
	}
	
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public List<Player> getLine() {
		return line;
	}
	public void setLine(List<Player> line) {
		this.line = new ArrayList<Player>(line);
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

	@Override
	public String toString() {
		return "Point [events=" + events + ", line=" + line
				+ ", substitutions=" + substitutions + ", timeStartedSeconds="
				+ timeStartedSeconds + ", timeEndedSeconds=" + timeEndedSeconds
				+ "]";
	}

}
