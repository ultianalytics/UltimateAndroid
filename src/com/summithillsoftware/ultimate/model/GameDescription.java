package com.summithillsoftware.ultimate.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

public class GameDescription {
	private static final String JSON_GAME_ID = "gameId";
	private static final String JSON_OPPONENT_NAME = "opponentName";
	private static final String JSON_TOURNAMENT_NAME = "tournamentName";
	private static final String JSON_START_DATE_TIME = "timestamp";
	public static final String JSON_START_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
	
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

	public static GameDescription fromJsonObject(JSONObject jsonObject)
			throws JSONException {
		if (jsonObject == null) {
			return null;
		} else {
			String gameId = jsonObject.getString(JSON_GAME_ID);
			if (gameId == null) {
				return null;
			}
			GameDescription game = new GameDescription(gameId);
			if (jsonObject.has(JSON_OPPONENT_NAME)) {
				game.setOpponentName(jsonObject.getString(JSON_OPPONENT_NAME));
			}
			if (jsonObject.has(JSON_TOURNAMENT_NAME)) {
				game.setTournamentName(jsonObject
						.getString(JSON_TOURNAMENT_NAME));
			}
			if (jsonObject.has(JSON_START_DATE_TIME)) {
				SimpleDateFormat parser = new SimpleDateFormat(
						JSON_START_DATE_TIME_FORMAT, Locale.US);
				try {
					game.setStartDate(parser.parse(jsonObject
							.getString(JSON_START_DATE_TIME)));
				} catch (ParseException e) {
					throw new JSONException(e.toString());
				}
			}
			return game;
		}
	}
	public static Comparator<GameDescription> GameDescriptionListComparator = new Comparator<GameDescription>() {
		public int compare(GameDescription game1, GameDescription game2) {
			return game2.getStartDate().compareTo(game1.getStartDate());
		}
	};
}
