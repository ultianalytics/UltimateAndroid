package com.summithillsoftware.ultimate.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.summithillsoftware.ultimate.model.Action.*;

public abstract class Event implements Serializable {
	private static final long serialVersionUID = 5756168445956393971L;
	
	private Action action;
	private long timestamp;
	private Map<String, Object> details;
	private boolean isHalftimeCause;
	
	public Event() {
		super();
		timestamp = System.currentTimeMillis();
	}
	
	public Event(Action action) {
		this();
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
		ensureValid();
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Map<String, Object> getDetails() {
		if (details == null) {
			details= new HashMap<String, Object>();
		}
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details;
	}

	public boolean isHalftimeCause() {
		return isHalftimeCause;
	}

	public void setHalftimeCause(boolean isHalftimeCause) {
		this.isHalftimeCause = isHalftimeCause;
	    if (Game.hasCurrentGame()) {
	        Game.current().clearPointSummaries();
	    }
	}

	public boolean isOffense() {
		return false;
	}
	
	public boolean isDefense() {
		return false;
	}
	
	public boolean isGoal() {
		return false;
	}

	public boolean isD() {
		return false;
	}

	public boolean isDrop() {
		return false;
	}
	
	public boolean isThrowaway() {
		return action == Throwaway;
	}
	
	public boolean isOffenseThrowaway() {
		return action == Throwaway && isOffense();
	}
	
	public boolean isDefenseThrowaway() {
		return action == Throwaway && isDefense();
	}
	
	public boolean isOurGoal() {
		return false;
	}
	
	public boolean isTheirGoal() {
		return false;
	}
	
	public abstract boolean isTurnover();
	
	public boolean isPull() {
		return false;
	}

	public boolean isPullIb() {
		return false;
	}
	
	public boolean isPullOb() {
		return false;
	}
	
	public boolean isCallahan() {
		return false;
	}
	
	public boolean isFinalEventOfPoint() {
		return false;
	}

	public boolean isCessationEvent() {
		return false;
	}
	
	public boolean isPlayEvent() {
		return false;
	}
	
	public boolean isPeriodEnd() {
		return false;
	}
	
	public boolean causesDirectionChange() {
		return !(action == Catch || action == Pull || action == PullOb);
	}
	
	public boolean causesLineChange() {
		return isGoal();
	}
	
	public abstract boolean isNextEventOffense();
	
	public abstract List<Player> getPlayers();
	
	public boolean isAnonymous() {
		return false;
	}
	
	protected void ensureValid() {
		// no-op...subclasses can re-implement
	}

	public Player playerOne() {
		return null;
	}
	
	public Player playerTwo() {
		return null;
	}
	
	public void setDetailIntValue(String key, int value) {
		getDetails().put(key, value);
	}
	
	public int getDetailIntValue(String key) {
		Integer value = (Integer)getDetails().get(key);
		return value == null ? 0 : value.intValue();
	}

	protected abstract String getDescriptionForTeamAndOpponent(String teamName, String opponentName);

	protected String getDescription() {
		return getDescriptionForTeamAndOpponent(Team.current().getName(), Game.hasCurrentGame() ? Game.current().getOpponentName() : "");
	}
	
	public String toString() {
		return getDescription();
	}
	
}
