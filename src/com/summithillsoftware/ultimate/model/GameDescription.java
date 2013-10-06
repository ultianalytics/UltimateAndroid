package com.summithillsoftware.ultimate.model;

import java.util.Comparator;
import java.util.Date;

public class GameDescription {
	private String gameId;
	private Date startDate;
	private String opponentName = "";
	private String tournamentName = "";	
	private Score score;
	
	public GameDescription(String gameId) {
		super();
		this.gameId = gameId;
	}
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getOpponentName() {
		return opponentName;
	}
	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}
	public String getTournamentName() {
		return tournamentName;
	}
	public void setTournamentName(String tournamentName) {
		this.tournamentName = tournamentName;
	}
	public Score getScore() {
		return score;
	}
	public void setScore(Score score) {
		this.score = score;
	}

	public static Comparator<GameDescription> GameDescriptionListComparator = new Comparator<GameDescription>() {
		public int compare(GameDescription game1, GameDescription game2) {
			return game2.getStartDate().compareTo(game1.getStartDate());
		}
	};
}
