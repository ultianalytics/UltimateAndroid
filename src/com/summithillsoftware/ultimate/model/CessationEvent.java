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

import com.summithillsoftware.ultimate.R;

public class CessationEvent extends Event {
	private static final long serialVersionUID = 8990822261864009064L;
	private static final String NEXT_PERIOD_START_ONLINE_DETAIL = "nextPeriodStartO";

	public static CessationEvent createWithAction(Action action) {
		CessationEvent evt = new CessationEvent();
		evt.setAction(action);
		if (CESSATION_ACTIONS.contains(action)) {
			return evt;
		} else {
			throw new RuntimeException("Invalid action for a CessationEvent");
		}
	}
	
	public static CessationEvent createEndOfFourthQuarterWithOlineStartNextPeriod(boolean startOline) {
		CessationEvent evt = CessationEvent.createWithAction(EndOfFourthQuarter);
		evt.setDetailBooleanValue(NEXT_PERIOD_START_ONLINE_DETAIL,startOline);
		return evt;
	}
	
	public static CessationEvent createEndOfOvertimeWithOlineStartNextPeriod(boolean startOline) {
		CessationEvent evt = CessationEvent.createWithAction(EndOfFourthQuarter);
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
	
	public  Player getPlayerOne() {
		return null;
	}
	
	public Player getPlayerTwo() {
		return null;
	}

	@Override
	protected String getDescriptionForTeamAndOpponent(String teamName, String opponentName) {
		switch (getAction()) {
        case EndOfFirstQuarter:
        	return getString(R.string.event_description_end_of_first_quarter);
        case EndOfThirdQuarter:
        	return getString(R.string.event_description_end_of_third_quarter);
        case Halftime:
        	return getString(R.string.event_description_halftime);
        case GameOver:
        	return getString(R.string.event_description_game_over);
        case EndOfFourthQuarter:
        	return getString(R.string.event_description_end_of_fourth_quarter);
        case EndOfOvertime:
        	return getString(R.string.event_description_end_of_overtime);
        default:
            return "";
		}
	}
	
	public int image() {
		switch (getAction()) {
		case GameOver:
			return R.drawable.game_over;
		default:
			return R.drawable.period_end;
		}
	}
	public void useSharedPlayers() {
		// no-op
	}
}


