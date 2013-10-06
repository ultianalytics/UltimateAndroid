package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.summithillsoftware.ultimate.UltimateApplication;

public class Game implements Serializable {
	private static final long serialVersionUID = -5725999403548435161L;
	private static final String FILE_NAME_PREFIX = "game-";
	private static Game Current;

	private String gameId;
	private Date startDateTime;
	private String opponentName;
	private String tournamentName;	
	private List<Player> currentLine;		
	
	public Game() {
		super();
		this.gameId = Game.generateUniqueFileName();
		this.startDateTime = new Date();
		this.opponentName = "";
		this.tournamentName = "";
		this.currentLine = new ArrayList<Player>();
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
		return Current != null;
	}
	
	public static void setCurrentGame(String gameId) {
		if (gameId != null) {
			Current = read(gameId);
		}
		Preferences.current().setCurrentGameFileName(gameId);
		Preferences.current().save();
	}
	
	public static String currentGameId() {
		Game current = current();
		return current == null ? null : current.getGameId();
	}
	
	public static boolean isCurrentGame(String gameId) {
		return gameId == null ? false : currentGameId().equals(gameId);
	}
	
	private static Game read(String gameId) {
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
		return game;
	}
	
	private static List<String> getAllGameFileNames(String teamId) {
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
		return new File(getTeamDir(teamId), teamId);
	}
	
	private static String generateUniqueFileName() {
		return FILE_NAME_PREFIX + java.util.UUID.randomUUID().toString();
	}
	
	private static File getTeamDir(String teamId) {
		return new File(getTeamsDir(), teamId);
	}
	
	private static File getTeamsDir() {
		return UltimateApplication.current().getFilesDir();
	}

	public Date getStartDateTime() {
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

	public void setCurrentLine(List<Player> currentLine) {
		this.currentLine = currentLine;
	}

	public String getGameId() {
		return gameId;
	}
	
	public Score getScore() {
		// TODO...FINISH
		return new Score();
	}


}
