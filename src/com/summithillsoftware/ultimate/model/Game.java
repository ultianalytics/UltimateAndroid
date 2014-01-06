package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;
import static com.summithillsoftware.ultimate.model.Action.EndOfFirstQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfFourthQuarter;
import static com.summithillsoftware.ultimate.model.Action.EndOfOvertime;
import static com.summithillsoftware.ultimate.model.Action.EndOfThirdQuarter;
import static com.summithillsoftware.ultimate.model.Action.GameOver;
import static com.summithillsoftware.ultimate.model.Action.Halftime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.util.Log;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;


public class Game implements Serializable {
	private static final long serialVersionUID = -5725999403548435161L;
	
	public static final int TIME_BASED_GAME_POINT = 1000;
	private static final String GAMES_DIRECTORY_NAME_PREFIX = "games-";	
	private static final String FILE_NAME_PREFIX = "game-";
	private static Game Current;

	private String gameId;
	private Date startDateTime;
	private String opponentName;
	private String tournamentName;
	private int gamePoint;
	private boolean isFirstPointOline;
	private TimeoutDetails timeoutDetails;
	private List<Point> points;
	private Wind wind;
	
	private List<Player> currentLine;	// server transient
	private List<Player> lastDLine;	// server transient
	private List<Player> lastOLine;	// server transient
	private int periodsComplete; // server transient
	@SuppressWarnings("unused")
	private Event firstEventTweeted;  // server transient
	private CessationEvent lastPeriodEnd; // server transient
	
	private transient boolean arePointSummariesValid; // local and server transient
	private transient EventHolder selectedEvent; // local and server transient
	
	public Game() {
		super();
		gameId = Game.generateUniqueFileName();
		points = new ArrayList<Point>();
		currentLine = new ArrayList<Player>();
		opponentName = "";
		tournamentName = "";
	}
	
	public static Game createGame() {
		Game game = new Game();
		game.gamePoint = Preferences.current().getGamePoint();
		game.currentLine = Team.current().getDefaultLine();
		game.startDateTime = new Date();
		return game;
	}
	
	// returns NULL if no current game
	public static Game current() {
		synchronized (FILE_NAME_PREFIX) {
			if (Current == null) {
				String currentTeamId = Team.current().getTeamId();
				String currentGameFileName = Preferences.current().getCurrentGameFileName(); 
				if (currentGameFileName != null) {
					Current = read(currentTeamId, currentGameFileName, true);
				}
			}
			return Current;
		}
	}
	
	public static boolean hasCurrentGame() {
		synchronized (FILE_NAME_PREFIX) {
			return Current != null;
		}
	}
	
	public static void setCurrentGame(Game game) {
		synchronized (FILE_NAME_PREFIX) {
			Current = game;
			if (game == null || game.hasBeenSaved()) {
				Preferences.current().setCurrentGameFileName(game == null ? null : game.getGameId());
				Preferences.current().save();
			}
		}
	}
	
	public static void setCurrentGameId(String gameId) {
		synchronized (FILE_NAME_PREFIX) {
			if (gameId == null) {
				Current =  null;
			} else {
				if (hasCurrentGame() && Current.getGameId().equals(gameId)) {
					// we already have this game...don't re-read it
				} else {
					Current = read(gameId);
				}
			}
			Preferences.current().setCurrentGameFileName(gameId);
			Preferences.current().save();
		}
	}
	
	public static String currentGameId() {
		synchronized (FILE_NAME_PREFIX) {
			Game current = current();
			return current == null ? null : current.getGameId();
		}
	}
	
	public static boolean isCurrentGame(String gameId) {
		synchronized (FILE_NAME_PREFIX) {
			return gameId == null || currentGameId() == null ? false : currentGameId().equals(gameId);
		}
	}
	
	public static Game read(String gameId) {
		return read(Team.current().getTeamId(), gameId, true);
	}

	private static Game read(String teamId, String gameId, boolean mergePlayersWithCurrentTeam) {
		// will answer NULL if error or not found
		Game game = null;
		File existingFile = getFile(teamId, gameId);
		if (existingFile != null && existingFile.exists()) {
			FileInputStream fileInputStream = null;
			ObjectInputStream objectInputStream = null;
			try {
				fileInputStream = new FileInputStream(existingFile);
				objectInputStream = new ObjectInputStream(fileInputStream);
				game = (Game) objectInputStream.readObject();
			} catch (Exception e) {
				Log.e(ULTIMATE, "Error restoring game from file", e);
			} finally {
				try {
					objectInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					Log.e(ULTIMATE, "Unable to close files when restoring game file", e2);
				}
			}
		}
		if (mergePlayersWithCurrentTeam) {
			game.mergePlayersWithCurrentTeam();
		}
		return game;
	}
	
	public static List<String> getAllGameFileNames(String teamId) {
		List<String> fileNames = new ArrayList<String>();
		File teamDir = getTeamDir(teamId);
		if (teamDir != null && teamDir.exists()  && teamDir.isDirectory()) {
			for (String fileName : teamDir.list()) {
				if (fileName.startsWith(FILE_NAME_PREFIX)) {
					fileNames.add(fileName);
				}
			}
		} 
		return fileNames;
	}
	
	public static List<GameDescription> retrieveGameDescriptionsForCurrentTeam() {
		List<GameDescription> descriptions = new ArrayList<GameDescription>();
		String teamId = Team.current().getTeamId();
		for (String gameFileName : getAllGameFileNames(Team.current().getTeamId())) {
			Game game = read(teamId, gameFileName, false);
			GameDescription gameDesc = new GameDescription(game.gameId);
			gameDesc.setOpponentName(game.getOpponentName());
			gameDesc.setTournamentName(game.getTournamentName());
			gameDesc.setScore(game.getScore());
			gameDesc.setStartDate(game.getStartDateTime());
			descriptions.add(gameDesc);
		}
		Collections.sort(descriptions, GameDescription.GameDescriptionListComparator);
		return descriptions;
	}
	
	public static int numberOfGamesForTeam(String teamId) {
		return getAllGameFileNames(teamId).size();
	}
	
	public static void deleteAllGamesForTeam(String teamId) {
		List<String> gameFileNames = getAllGameFileNames(teamId);
		// delete the games
		for (String fileName : gameFileNames) {
			delete(fileName);
		}
		// delete the folder
		File teamDir = getTeamDir(teamId);
		if (teamDir.exists()) {
			boolean didDelete = teamDir.delete();
			if (!didDelete) {
				Log.e(ULTIMATE, "Attempted to delete team folder but it did not delete");
			}
		}
	}
	
	public static void delete(String gameId) {
		if (isCurrentGame(gameId)) {
			setCurrentGame(null);
		}
		File file = getFile(Team.current().getTeamId(), gameId);
		if (file.exists()) {
			boolean didDelete = file.delete();
			if (!didDelete) {
				Log.e(ULTIMATE, "Attempted to delete game file but it did not delete");
			}
		}
	}
	
	public void save() {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(getFile(Team.current().getTeamId(), getGameId()));
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
	   } catch (Exception e) {
		   Log.e(ULTIMATE, "Error saving game file", e);
	   } finally {
			try {
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (Exception e2) {
				Log.e(ULTIMATE, "Unable to close files when saving game file", e2);
			}
	   }
	}
	
	public boolean hasBeenSaved() {
		return getFile(Team.current().getTeamId(), getGameId()).exists();
	}
	
	public void delete() {
		delete(getGameId());
	}

	private static File getFile(String teamId, String gameId) {
		if (teamId == null || gameId == null) {
			return null;
		}
		return new File(getTeamDir(teamId), gameId);
	}
	
	private static String generateUniqueFileName() {
		return FILE_NAME_PREFIX + java.util.UUID.randomUUID().toString();
	}
	
	private static File getTeamDir(String teamId) {
		File teamDir = new File(UltimateApplication.current().getFilesDir(), GAMES_DIRECTORY_NAME_PREFIX + teamId);
		if (!teamDir.exists()) {
			teamDir.mkdir();
		}
		return teamDir;
	}
	
	private void mergePlayersWithCurrentTeam() {
		for (Point point : points) {
			point.useSharedPlayers();
		}
		currentLine = Player.replaceAllWithSharedPlayers(currentLine);
		if (lastDLine != null) {
			lastDLine = Player.replaceAllWithSharedPlayers(lastDLine);
		}
		if (lastOLine != null) {
			lastOLine = Player.replaceAllWithSharedPlayers(lastOLine);
		}
	}
	
	private Point getCurrentPoint() {
		if (points.size() == 0) {
			return null;
		} else {
			return points.get(points.size() -1); // last point
		}
	}
	
	private boolean isPointOLine(Point point) {
		updatePointSummaries();
		return point.getSummary().isOline();
	}
	
	private void updateLastLine(Event event) {
		if (event.isFinalEventOfPoint()) {
			if (isPointOLine(getCurrentPoint())) {
				lastOLine = new ArrayList<Player>(currentLine);
			} else {
				lastDLine = new ArrayList<Player>(currentLine);
			}
		}
	}
	
	public void clearPointSummaries() {
		arePointSummariesValid = false;
	}

	public void addEvent(Event event) {
		if (getCurrentPoint() == null || getCurrentPoint().isFinished()) {
			Point newPoint = new Point();
			addPoint(newPoint);
		}
		getCurrentPoint().addEvent(event);
		getCurrentPoint().setLine(currentLine);
		updateLastLine(event);
		clearPointSummaries();
		tweetEvent(event, getCurrentPoint(), false);
	}
	
	public boolean hasEvents() {
		return points.size() > 1 || (getCurrentPoint() != null && getCurrentPoint().numberOfEvents() > 0);
	}
	
	public boolean hasOneEvent() {
		return points.size() == 1 && (getCurrentPoint() != null && getCurrentPoint().numberOfEvents() == 1);
	}
	
	public Event getLastEvent() {
		return getCurrentPoint() == null ? null : getCurrentPoint().getLastEvent();
	}
	
	public void removeLastEvent() {
		Event lastEvent = getLastEvent();
		if (lastEvent != null) {
			tweetEvent(lastEvent, getCurrentPoint(), true);
			getCurrentPoint().removeLastEvent();
			if (getCurrentPoint().numberOfEvents() == 0) {
				points.remove(points.size() - 1); // remove last point
			}
			if (lastEvent.isGoal() && getCurrentPoint() != null && getCurrentPoint().getLine() != null) {
				currentLine = new ArrayList<Player>(getCurrentPoint().getLine());  // copy current line from the point which is now current
			}
			clearPointSummaries();
		}
	}
	
	private List<Point> pointsInMostRecentOrder() {
		List<Point> pointsInMostRecentOrder = new ArrayList<Point>(points);
		Collections.reverse(pointsInMostRecentOrder);
		return pointsInMostRecentOrder;
	}
	
	public List<PointEvent>getLastEvents(int numberToRetrieve) {
		Iterator<Point> reversePointIterator = pointsInMostRecentOrder().iterator();
		
		List<PointEvent> answerList = new ArrayList<PointEvent>();
		while (reversePointIterator.hasNext() && answerList.size() < numberToRetrieve) {
			Point point = reversePointIterator.next();
			List<Event> events = point.getLastEvents(numberToRetrieve - answerList.size());
			for (Event event : events) {
				answerList.add(new PointEvent(point, event));
			}
		}
		return answerList;
	}
	
	public int getNumberOfPoints() {
		return points.size();
	}
	
	public int getNumberOfEvents() {
		int count = 0;
		if (hasEvents()) { 
			for (Point point : points) {
				count = count + point.numberOfEvents();
			}
		}
		return count;
	}
	
	private Point getPointAtMostRecentIndex(int index) {
	    // points are stored in ascending order but we are being asked for an index in descending order
		return points.isEmpty() ? null : points.get(points.size() - index - 1);
	}
	
	public Score getScoreAtMostRecentIndex(int index) {
		updatePointSummaries();
		Point point = getPointAtMostRecentIndex(index);
		return point == null ? null : point.getSummary().getScore();
	}
	
	private Point lastPoint() {
		return points.isEmpty() ? null : points.get(points.size() - 1);
	}
	
	public String getPointNameForScore(Score score, boolean isMostRecent) {
		if (isMostRecent && !(lastPoint().isFinished())) {
			return getString(R.string.point_description_current);
		} else {
			return score.format(UltimateApplication.current(), false);
		}
	}
	
	public String getPointNameAtMostRecentIndex(int index) {
		updatePointSummaries();
		Score score = getScoreAtMostRecentIndex(index);
		return getPointNameForScore(score, index == 0);
	}
	
	public List<Point> getPointInMostRecentOrder() {
		updatePointSummaries();
		return pointsInMostRecentOrder();
	}
	
	public int getHalftimePoint() {
		return ((getGamePoint() == 0 ? Preferences.current().getGamePoint() : getGamePoint()) + 1) / 2;
	}
	
	public boolean isTie() {
		return getScore().isTie();
	}
	
	public boolean doesGameAppearDone() {
		updatePointSummaries();
		if (isTimeBasedGame()) {
			return hasEvents() && getLastEvent().getAction() == GameOver;
		} else {
			// have we reached the end point and leader has >= 2 lead?  
			return getScore().getLeadingScore() >= getGamePoint() && 
					getScore().getLeadingScore() >= getScore().getTrailingScore() + 2;
		}
	}
	
	public Action nextPeriodEnd() {
		switch (getPeriodsComplete()) {
		case 0:
			return EndOfFirstQuarter;
		case 1:
			return Halftime;
		case 2:
			return EndOfThirdQuarter;
		case 3:
			if (isTie()) {
				return EndOfFourthQuarter;
			} else {
				return GameOver;
			}
		default:
			if (isTie()) {
				return EndOfOvertime;
			} else {
				return GameOver;
			}
		}
	}
	
	public boolean isHalftime() {
		updatePointSummaries();
		if (isTimeBasedGame()) {
			Event lastEvent = getLastEvent();
			return lastEvent.isCessationEvent() && ((CessationEvent)lastEvent).isHalftime();
		} else {
			return isNextEventImmediatelyAfterHalftime();
		}
	}
	
	public boolean isNextEventImmediatelyAfterHalftime() {
		updatePointSummaries();
		if (isTimeBasedGame()) {
			return isHalftime();
		} else {
			if (getCurrentPoint() != null && getCurrentPoint().isFinished() && (!getCurrentPoint().getSummary().isAfterHalftime())) {
				return getScore().getLeadingScore() == getHalftimePoint();
			}
			return false;
		}
		
	}
	
	public boolean arePlayingOffense() {
		updatePointSummaries();
		if (getCurrentPoint() == null) {
			return isFirstPointOline;
		} else {
			Event lastEvent = getLastEvent();
			if (isNextEventImmediatelyAfterHalftime() || lastEvent.isPeriodEnd()) {
				return isNextPointAfterPeriodEndOline();
			} else {
				return lastEvent.isNextEventOffense();
			}
		}
	}
	
	public boolean isPointOline(Point point) {
		updatePointSummaries();
		return point.getSummary().isOline();
	}
	
	public boolean isFirstPoint(Point point) {
		return !points.isEmpty() && points.get(0) == point;
	}
	
	public boolean isCurrentlyOline() {
		updatePointSummaries();
		if (getCurrentPoint() == null) {
			return isFirstPointOline;
		} else if (isNextEventImmediatelyAfterHalftime() || getLastEvent().isPeriodEnd()) {
			return isNextPointAfterPeriodEndOline();
		} else if (getCurrentPoint().isFinished()) {
			return !getCurrentPoint().isOurPoint();
		} 
		return isPointOline(getCurrentPoint());
	}
	
	public Point findPreviousPoint(Point startingPoint) {
		updatePointSummaries();
		return startingPoint.getSummary().getPreviousPoint();
	}
	
	public List<Player> currentLineSorted() {
		ArrayList<Player> sortedList = new ArrayList<Player>(currentLine);
		if (Team.current().isDisplayingPlayerNumber()) {
			Collections.sort(sortedList, Player.PlayerNumberComparator);
		} else {
			Collections.sort(sortedList, Player.PlayerNameComparator);
		}
		return sortedList;
	}
	
	public void clearCurrentLine() {
		currentLine.clear();
	}
	
	public void resetCurrentLine() {
		currentLine = new ArrayList<Player>(currentLine);
	}
	
	public void makeCurentLineLastLine(boolean useOline) {
		currentLine = new ArrayList<Player>(useOline ? lastOLine : lastDLine);
	}
	
	public boolean isAfterHalftimeStarted() {
		updatePointSummaries();
		if (isTimeBasedGame()) {
			return getPeriodsComplete() >= 2 && !isHalftime();
		} else {
			return getCurrentPoint() != null && getCurrentPoint().getSummary().isAfterHalftime();
		}
	
	}
	
	public boolean isAfterHalftime() {
		updatePointSummaries();
		if (isTimeBasedGame()) {
			return getPeriodsComplete() >= 2;
		} else {
			return isAfterHalftimeStarted() || isNextEventImmediatelyAfterHalftime();
		}
	}
	
	public CessationEvent getLastPeriodEnd() {
		updatePointSummaries();
		return lastPeriodEnd;
	}
	
	private boolean isNextPointOlineAfterPeriodsFinished(int periodsFinished) {
		return (((isFirstPointOline()  ? 1 : 0) + periodsFinished) % 2) == 1;
	}
	
	public boolean canNextPointBePull() {
		Event lastEvent = getLastEvent();
		return lastEvent == null  ? true : lastEvent.isGoal() || lastEvent.isPeriodEnd();
	}
	
	public boolean canNextPointBeDLinePull() {
		Event lastEvent = getLastEvent();
		if (lastEvent == null) {
			return !isFirstPointOline();
		}
		if (isTimeBasedGame()) {
			if (lastEvent.isOurGoal()) {
				return true;
			} else if (lastEvent.isPeriodEnd()) {
				boolean isNextPointOline = isNextPointAfterPeriodEndOline();
				return !isNextPointOline;
			} else {
				return false;
			}
		} else {
			return lastEvent.isOurGoal() || (lastEvent.isTheirGoal() && isNextEventImmediatelyAfterHalftime() && isFirstPointOline);
		}
	}
	
	private boolean isNextPointAfterPeriodEndOline() {
		int periodsFinished = isTimeBasedGame() ? getPeriodsComplete() : (isAfterHalftime() ? 1 : 0);
	    if (periodsFinished >= 4) {
	        return getLastPeriodEnd().isNextOvertimePeriodStartingOline();
	    } else {
	        return isNextPointOlineAfterPeriodsFinished(periodsFinished);
	    }

	}
	
	public boolean isPointInProgess() {
		Event lastEvent = getLastEvent();
		return lastEvent != null && !lastEvent.isGoal() && !lastEvent.isPeriodEnd();
		
	}
	
	public Set<Player> getPlayers() {
		HashSet<Player> players = new HashSet<Player>();
		players.addAll(currentLine);
		players.addAll(lastOLine);
		players.addAll(lastDLine);
		for (Point point : points) {
			players.addAll(point.getPlayers());
		}
		return players;
	}
	
	private void updatePointSummaries() {
		if (!arePointSummariesValid) {
			int periodEndCount = 0;
			Score score = new Score(0,0);
			Point lastPoint = null;
			for (Point point : points) {
				PointSummary summary = new PointSummary();
				summary.setFinished(point.isFinished());
				if (point.isPeriodEnd()) {
					lastPeriodEnd = point.getPeriodEnd();
				}
				if (summary.isFinished()) {
					if (point.isOurPoint()) {
						score.incOurs();
					} else {
						score.incTheirs();
					}
				}
				summary.setScore(score.copy());
				if (isTimeBasedGame()) {
					summary.setAfterHalftime(periodEndCount > 2);
					if (lastPoint == null || lastPoint.isPeriodEnd()) {
						summary.setOline(isNextPointOlineAfterPeriodsFinished(periodEndCount));
					} else {
						summary.setOline(!lastPoint.isOurPoint());
					}
				} else {
					summary.setAfterHalftime(lastPoint != null && (getHalftimePoint() <= lastPoint.getSummary().getScore().getLeadingScore()));
					boolean isFirstPointAfterHalftime = lastPoint != null &&
							summary.isAfterHalftime() && !lastPoint.getSummary().isAfterHalftime();
					summary.setOline(lastPoint == null ? isFirstPointOline : (isFirstPointAfterHalftime ? !isFirstPointOline : !lastPoint.isOurPoint()));
				}
				summary.setElapsedSeconds(point.getTimeEndedSeconds() - point.getTimeStartedSeconds());
				summary.setPreviousPoint(lastPoint);
				point.setSummary(summary);
				lastPoint = point;
			}
			periodsComplete = periodEndCount;
			arePointSummariesValid = true;
		}
	}
	
	private void tweetEvent(Event event, Point point, boolean isUndo) {
		// TODO...finish this
	}
	
	public Date getStartDateTime() {
		if (startDateTime == null) {
			startDateTime = new Date();
		}
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
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

	public List<Player> getCurrentLine() {
		return currentLine;
	}
	
	public void addToCurrentLine(Player player) {
		if (currentLine.size() < 7) {
			currentLine.add(player);
		}
	}
	
	public void removeFromCurrentLine(Player player) {
		currentLine.remove(player);
	}

	public void setCurrentLine(List<Player> currentLine) {
		this.currentLine = currentLine;
	}

	public String getGameId() {
		return gameId;
	}
	
	public Score getScore() {
		updatePointSummaries();
		if (getCurrentPoint() == null) {
			return new Score(0,0);
		} else {
			return getCurrentPoint().getSummary().getScore();
		}
	}

	public int getGamePoint() {
		if (gamePoint == 0) {
			gamePoint = Preferences.current().getGamePoint();
		}
		return gamePoint;
	}

	public void setGamePoint(int gamePoint) {
		this.gamePoint = gamePoint;
	}

	public boolean isFirstPointOline() {
		return isFirstPointOline;
	}

	public void setFirstPointOline(boolean isFirstPointOline) {
		this.isFirstPointOline = isFirstPointOline;
	}

	public Wind getWind() {
		if (wind == null) {
			wind = new Wind();
		}
		return wind;
	}

	public void setWind(Wind wind) {
		this.wind = wind;
	}

	public List<Player> getLastDLine() {
		return lastDLine;
	}

	public void setLastDLine(List<Player> lastDLine) {
		this.lastDLine = lastDLine;
	}

	public List<Player> getLastOLine() {
		return lastOLine;
	}

	public void setLastOLine(List<Player> lastOLine) {
		this.lastOLine = lastOLine;
	}

	public List<Point> getPoints() {
		return points;
	}

	private void addPoint(Point point) {
		points.add(point);
		clearPointSummaries();
	}
	
	public EventHolder getSelectedEvent() {
		return selectedEvent;
	}

	public void setSelectedEvent(EventHolder selectedEvent) {
		this.selectedEvent = selectedEvent;
	}

	public boolean isTimeBasedGame() {
		return getGamePoint() == TIME_BASED_GAME_POINT;
	}
	
	public String getString(int resId) {
		return UltimateApplication.current().getString(resId);
	}
	
	public String getString(int resId, Object...formatArgs) {
		return UltimateApplication.current().getString(resId, formatArgs);
	}
	
	public int getPeriodsComplete() {
		updatePointSummaries();
		return periodsComplete;
	}

	/*
	 * Substitutions
	 */
	
	public void addSubstitution(PlayerSubstitution substitution) {
		if (isPointInProgess()) {
			getCurrentPoint().getSubstitutions().add(substitution);
			adjustLineForSubstitution(substitution);
		} else {
			Log.e(ULTIMATE,"Error..can't add subsitution to a point which is not in progress");
		}
	}
	
	public boolean removeLastSubstitutionForCurrentPoint() {
		PlayerSubstitution lastSub = getCurrentPoint().lastSubstitution();
		return removeSubstitutionForCurrentPoint(lastSub);
	}
	
	public boolean removeSubstitutionForCurrentPoint(PlayerSubstitution substitution) {
		if (substitution != null) {
			getCurrentPoint().getSubstitutions().remove(substitution);
			return adjustLineForSubstitutionUndo(substitution);
		}
		return false;
	}
	
	// most recent first 
	public List<PlayerSubstitution> substitutionsForCurrentPoint() {
		Point point = getCurrentPoint();
		if (doesCurrentPointHaveSubstitutions()) {
			List<PlayerSubstitution> subs = new ArrayList<PlayerSubstitution>(point.getSubstitutions());
			Collections.reverse(subs);
			return subs;
		} else {
			return Collections.emptyList();
		}
	}
	
	public boolean doesCurrentPointHaveSubstitutions() {
		Point point = getCurrentPoint();
		return point == null ? false : !point.getSubstitutions().isEmpty();
	}
	
	public boolean isCurrentPointFinished() {
		Point point = getCurrentPoint();
		return point != null && point.isFinished();
	}
	
	private void adjustLineForSubstitution(PlayerSubstitution sub) {
		currentLine.remove(sub.getFromPlayer());
		if (currentLine.size() < 7) {
			currentLine.add(sub.getToPlayer());
		}
	}
	
	private boolean adjustLineForSubstitutionUndo(PlayerSubstitution sub) {
	    if (!currentLine.contains(sub.getToPlayer()) || currentLine.contains(sub.getFromPlayer())) {
	        return false;
	    }
	    currentLine.remove(sub.getToPlayer());
	    if (currentLine.size() < 7) {
	        currentLine.add(sub.getFromPlayer());
	    }
	    return true;
	}
	
	/*
	 * Timeouts
	 */
	
	public void setTimeoutDetails(TimeoutDetails timeoutDetails) {
		this.timeoutDetails = timeoutDetails;
	}
	
	public TimeoutDetails getTimeoutDetails() {
		if (timeoutDetails == null) {
			timeoutDetails = new TimeoutDetails();
			timeoutDetails.setQuotaPerHalf(Preferences.current().getTimeoutsPerHalf());
			timeoutDetails.setQuotaFloaters(Preferences.current().getTimeoutFloatersPerGame());
		}
		return timeoutDetails;
	}

	public int availableTimeouts() {
		int totalAvailableFirstHalf = getTimeoutDetails().getQuotaPerHalf() + getTimeoutDetails().getQuotaFloaters();
	    if (isAfterHalftime()) {
	        int floatersAvailableAfterFirstHalf = Math.min(totalAvailableFirstHalf - getTimeoutDetails().getTakenFirstHalf(), getTimeoutDetails().getQuotaFloaters());
	        int totalAvailableSecondHalf = getTimeoutDetails().getQuotaPerHalf() + floatersAvailableAfterFirstHalf;
	        return totalAvailableSecondHalf - getTimeoutDetails().getTakenSecondHalf();
	    } else {
	        return totalAvailableFirstHalf - getTimeoutDetails().getTakenFirstHalf();
	    }
	}



	
}
