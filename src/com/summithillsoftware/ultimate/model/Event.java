package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.model.Action.Callahan;
import static com.summithillsoftware.ultimate.model.Action.Catch;
import static com.summithillsoftware.ultimate.model.Action.Drop;
import static com.summithillsoftware.ultimate.model.Action.EndOfFirstQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfFourthQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfOvertime;
import static com.summithillsoftware.ultimate.model.Action.EndOfThirdQuarter;
import static com.summithillsoftware.ultimate.model.Action.GameOver;
import static com.summithillsoftware.ultimate.model.Action.Halftime;
import static com.summithillsoftware.ultimate.model.Action.MiscPenalty;
import static com.summithillsoftware.ultimate.model.Action.Pull;
import static com.summithillsoftware.ultimate.model.Action.PullOb;
import static com.summithillsoftware.ultimate.model.Action.Stall;
import static com.summithillsoftware.ultimate.model.Action.Throwaway;
import static com.summithillsoftware.ultimate.model.Action.Timeout;

import java.io.Serializable;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.summithillsoftware.ultimate.UltimateApplication;

public abstract class Event implements Serializable {
	private static final long serialVersionUID = 5756168445956393971L;
	public static final EnumSet<Action> CESSATION_ACTIONS = EnumSet.of(
			EndOfFirstQuarter,
			Halftime,
			EndOfThirdQuarter,
			EndOfFourthQuarter,
			EndOfOvertime,
			GameOver,
			Timeout);
	
	public static final EnumSet<Action> TURNOVER_ACTIONS = EnumSet.of(
			Drop,
			Throwaway,
			Stall,
			MiscPenalty,
			Callahan);
		
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
	
	public void setDetailBooleanValue(String key, boolean value) {
		getDetails().put(key, value);
	}
	
	public int getDetailIntValue(String key, int defaultValue) {
		Integer value = (Integer)getDetails().get(key);
		return value == null ? defaultValue : value.intValue();
	}
	
	public boolean getDetailBooleanValue(String key, boolean defaultValue) {
		Boolean value = (Boolean)getDetails().get(key);
		return value == null ? defaultValue : value.booleanValue();
	}

	protected abstract String getDescriptionForTeamAndOpponent(String teamName, String opponentName);

	protected String getDescription() {
		return getDescriptionForTeamAndOpponent(Team.current().getName(), Game.hasCurrentGame() ? Game.current().getOpponentName() : "");
	}
	
	public String toString() {
		return getDescription();
	}
	
	public String getString(int resId) {
		return UltimateApplication.current().getString(resId);
	}
	
	public String getString(int resId, Object...formatArgs) {
		return UltimateApplication.current().getString(resId, formatArgs);
	}
	
	public abstract void useSharedPlayers();
}
