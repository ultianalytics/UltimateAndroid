package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.summithillsoftware.ultimate.UltimateApplication;

public class Team implements Serializable {
	private static final long serialVersionUID = -6976411750470902715L;
	
	private static final String DEFAULT_TEAM_NAME = "My Team";
	private static final String FILE_NAME_PREFIX = "team-";
	private static Team Current;
	
	private String teamId;	
	private String name;
	private List<Player>players;
	private boolean isMixed;
	private boolean isDisplayingPlayerNumber;
	private String cloudId;
	private String leaguevineJson;
	private boolean isPlayersAreLeaguevine;
	
	public Team() {
		super();
		players = new ArrayList<Player>();
		teamId = generateUniqueFileName();
	}
	
	public static Team current() {
		synchronized (FILE_NAME_PREFIX) {
			if (Current == null) {
				String currentTeamFileName = Preferences.current().getCurrentTeamFileName(); 
				if (currentTeamFileName != null) {
					Current = read(currentTeamFileName);
				}
				if (Current == null) {
					Team newTeam = new Team();
					newTeam.name = DEFAULT_TEAM_NAME;
					newTeam.save();
					Preferences.current().setCurrentTeamFileName(newTeam.teamId);
					Preferences.current().save();
				}
			}
			return Current;
		}
	}
	
	public static void setCurrentTeamId(String newCurrentTeamId) {
		if (Current != null && !Current.teamId.equals(newCurrentTeamId)) {
			// TODO...reset the current game (to null)
		}
		if (Current == null || !Current.teamId.equals(newCurrentTeamId)) {
			Current = read(newCurrentTeamId);
			Preferences.current().setCurrentTeamFileName(newCurrentTeamId);
			Preferences.current().save();
		}
	}
	
	public static boolean isCurrentTeam(String otherTeamId) {
		return otherTeamId.equals(Preferences.current().getCurrentTeamFileName());
	}

	public static List<TeamDescription> retrieveTeamDescriptions() {
		ArrayList<TeamDescription> descriptions = new ArrayList<TeamDescription>();
		for (String teamFileName : getAllTeamFileNames()) {
			Team team = read(teamFileName);
			TeamDescription description = new TeamDescription(teamFileName, team.getName(), team.getCloudId());
			descriptions.add(description);
		}
		return descriptions;
	}
	
	public static int numberOfTeams() {
		return getAllTeamFileNames().size();
	}
	
	public static boolean  isDuplicateTeamName(String newTeamName, Team teamToIgnore) {
		for (TeamDescription existingTeam : retrieveTeamDescriptions()) {
			if (teamToIgnore == null || !existingTeam.getTeamId().equals(teamToIgnore.getTeamId())) {
				if (!existingTeam.getTeamId().equals(teamToIgnore.getTeamId()) && existingTeam.getName().equalsIgnoreCase(newTeamName)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static String getTeamIdForCloudId(String cloudId) {
		for (TeamDescription team : retrieveTeamDescriptions()) {
			if (team.getCloudId().equals(cloudId)) {
				return team.getTeamId();
			}
		}
		return null;
	}
	
	private static List<String> getAllTeamFileNames() {
		ArrayList<String> fileNames = new ArrayList<String>();
		File teamsDir = getTeamsDir();
		for (File file : teamsDir.listFiles()) {
			if (file.isFile() && file.getName().startsWith(FILE_NAME_PREFIX)) {
				fileNames.add(file.getName());
			}
		}
		return fileNames;
	}
	
	private static Team read(String teamId) {
		// will answer NULL if error or not found
		Team team = null;
		File existingFile = getFile(teamId);
		if (existingFile != null && existingFile.exists()) {
			FileInputStream fileInputStream = null;
			ObjectInputStream objectInputStream = null;
			try {
				fileInputStream = new FileInputStream(existingFile);
				objectInputStream = new ObjectInputStream(fileInputStream);
				team = (Team) objectInputStream.readObject();
			} catch (Exception e) {
				Log.e(ULTIMATE, "Error restoring team from file", e);
			} finally {
				try {
					objectInputStream.close();
					fileInputStream.close();
				} catch (Exception e2) {
					Log.e(ULTIMATE, "Unable to close files when restoring team file", e2);
				}
			}
		}
		return team;
	}
	
	private static String generateUniqueFileName() {
		return FILE_NAME_PREFIX + java.util.UUID.randomUUID().toString();
	}
	
	private static File getFile(String teamId) {
		if (teamId == null) {
			return null;
		}
		return new File(getTeamsDir(), teamId);
	}
	
	private static File getTeamsDir() {
		return UltimateApplication.current().getFilesDir();
	}
	
	public void save() {
		FileOutputStream fileOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(getFile(teamId));
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(this);
	   } catch (Exception e) {
		   Log.e(ULTIMATE, "Error saving team file", e);
	   } finally {
			try {
				objectOutputStream.close();
				fileOutputStream.close();
			} catch (Exception e2) {
				Log.e(ULTIMATE, "Unable to close files when saving team file", e2);
			}
	   }
	}
	
	public void delete() {
		// move "current" to another team		
		if (isCurrentTeam(this.getTeamId())) {
			for (TeamDescription team : retrieveTeamDescriptions()) {
				if (!team.getTeamId().equals(this.getTeamId())) {
					setCurrentTeamId(team.getTeamId());
					break;
				}
			}
		}
		
		// delete the associated games
		// TODO...delete the games
		
		// delete the team
		File file = getFile(this.getTeamId());
		boolean deleted = file.delete();
		if (!deleted) {
			Log.e(ULTIMATE, "failed to delete team");
		}
	}

	public boolean hasBeenSaved() {
		return getFile(this.getTeamId()).exists();
	}
	
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Player> getPlayers() {
		return players;
	}
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
	public boolean isMixed() {
		return isMixed;
	}
	public void setMixed(boolean isMixed) {
		this.isMixed = isMixed;
	}
	public boolean isDisplayingPlayerNumber() {
		return isDisplayingPlayerNumber;
	}
	public void setDisplayingPlayerNumber(boolean isDisplayingPlayerNumber) {
		this.isDisplayingPlayerNumber = isDisplayingPlayerNumber;
	}
	public String getCloudId() {
		return cloudId;
	}
	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}
	public String getLeaguevineJson() {
		return leaguevineJson;
	}
	public void setLeaguevineJson(String leaguevineJson) {
		this.leaguevineJson = leaguevineJson;
	}
	public boolean isPlayersAreLeaguevine() {
		return isPlayersAreLeaguevine;
	}
	public void setPlayersAreLeaguevine(boolean isPlayersAreLeaguevine) {
		this.isPlayersAreLeaguevine = isPlayersAreLeaguevine;
	}
}
