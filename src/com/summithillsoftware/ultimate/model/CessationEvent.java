package com.summithillsoftware.ultimate.model;

import java.util.Collections;
import java.util.List;

public class CessationEvent extends Event {
	private static final long serialVersionUID = 8990822261864009064L;

	public boolean isCessationEvent() {
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

