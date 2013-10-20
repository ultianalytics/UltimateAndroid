package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.model.Action.EndOfFirstQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfFourthQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfOvertime;
import static com.summithillsoftware.ultimate.model.Action.EndOfThirdQuarter;
import static com.summithillsoftware.ultimate.model.Action.GameOver;
import static com.summithillsoftware.ultimate.model.Action.Halftime;
import static com.summithillsoftware.ultimate.model.Action.Timeout;

import java.util.Collections;
import java.util.List;

public class CessationEvent extends Event {
	private static final long serialVersionUID = 8990822261864009064L;
	private static final String NEXT_PERIOD_START_ONLINE_DETAIL = "nextPeriodStartO";

	public static CessationEvent createWithEvent(Action action) {
		CessationEvent evt = new CessationEvent();
		evt.setAction(action);
		if (CESSATION_ACTIONS.contains(action)) {
			return evt;
		} else {
			throw new RuntimeException("Invalid action for a CessationEvent");
		}
	}
	
	public static CessationEvent createEndOfFourthQuarterWithOlineStartNextPeriod(boolean startOline) {
		CessationEvent evt = CessationEvent.createWithEvent(EndOfFourthQuarter);
		evt.setDetailBooleanValue(NEXT_PERIOD_START_ONLINE_DETAIL,startOline);
		return evt;
	}
	
	public static CessationEvent createEndOfOvertimeWithOlineStartNextPeriod(boolean startOline) {
		CessationEvent evt = CessationEvent.createWithEvent(EndOfFourthQuarter);
		evt.setDetailBooleanValue(NEXT_PERIOD_START_ONLINE_DETAIL,startOline);
		return evt;
	}
	
	public boolean isCessationEvent() {
		return true;
	}
	
	public boolean isNextOvertimePeriodStartingOline() {
		if (getAction() == EndOfFourthQuarter || getAction() == EndOfOvertime) { // only overtime related periods know if next period starts O-line
			return getDetailBooleanValue(NEXT_PERIOD_START_ONLINE_DETAIL, false);
		} else {
			return false;
		}
	}

	public boolean isTimeout() {
		return getAction() == Timeout;
	}
	
	public boolean isEndOfFirstQuarter() {
		return getAction() == EndOfFirstQuarter;
	}
	
	public boolean isEndOfThirdQuarter() {
		return getAction() == EndOfThirdQuarter;
	}
	
	public boolean isHalftime() {
		return getAction() == Halftime;
	}
	
	public boolean isPreHalftime() {
		return getAction() == EndOfFirstQuarter;
	}
	
	public boolean isEndOfFourthQuarter() {
		return getAction() == EndOfFourthQuarter;
	}
	
	public boolean isGameOver() {
		return getAction() == GameOver;
	}
	
	public boolean isEndOfOvertime() {
		return getAction() == EndOfOvertime;
	}

	public boolean isPeriodEnd() {
		return !isTimeout();
	}
	
	public boolean isFinalEventOfPoint() {
		return isPeriodEnd();
	}
	
	public boolean isHalftimeCause() {
		return getAction() == Halftime;
	}
	
	public boolean causesDirectionChange() {
		return true;
	}

	public boolean causesLineChange() {
		return true;
	}
	
	@Override
	public boolean isTurnover() {
		return false;
	}
	
	@Override
	public boolean isNextEventOffense() {
		return false;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getPlayers() {
		return (List<Player>)Collections.EMPTY_LIST;
	}

	@Override
	protected String getDescriptionForTeamAndOpponent(String teamName, String opponentName) {
		switch (getAction()) {
        case EndOfFirstQuarter:
            return "End of 1st Qtr";
        case EndOfThirdQuarter:
            return "End of 3rd Qtr";
        case Halftime:
            return "Halftime";
        case GameOver:
            return "Game Over";
        case EndOfFourthQuarter:
            return "End of 4th Qtr";
        case EndOfOvertime:
            return "End of an overtime";
        default:
            return "";
		}
	}
}

