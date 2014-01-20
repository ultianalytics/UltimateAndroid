package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.Constants.ULTIMATE;

import java.io.Externalizable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.summithillsoftware.ultimate.UltimateApplication;

public class Team implements Externalizable {
	private static final String JSON_TEAM_ID = "teamId";
	private static final String JSON_CLOUD_ID = "cloudId";
	private static final String JSON_NAME = "name";
	private static final String JSON_PLAYERS = "players";
	private static final String JSON_IS_MIXED = "mixed";
	private static final String JSON_IS_DISPLAYING_PLAYER_NUMBER = "displayPlayerNumber";
	private static final String JSON_IS_LEAGUEVINE_PLAYERS = "playersAreLeaguevine";
	private static final String JSON_LEAGUEVINE_JSON = "leaguevineJson";
	
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
		synchronized (FILE_NAME_PREFIX) {		
			if (Current != null && !Current.teamId.equals(newCurrentTeamId)) {
				Game.setCurrentGame(null);  // clear current game if switching teams
			}
			if (Current == null || !Current.teamId.equals(newCurrentTeamId)) {
				if (newCurrentTeamId != null) {
					Current = read(newCurrentTeamId);
					if (Current == null) {
						throw new RuntimeException("Team has not been saved");
					}
				}
				Preferences.current().setCurrentTeamFileName(newCurrentTeamId);
				Preferences.current().save();
			}
		}
	}
	
	public static void setCurrentTeam(Team team) {
		synchronized (FILE_NAME_PREFIX) {
			if (team != null && !team.hasBeenSaved()) {
				throw new RuntimeException("Team has not been saved");
			}
			if (team == null || (Current != null && !Current.teamId.equals(team.getTeamId()))) {
				Game.setCurrentGame(null);  // clear current game if switching teams
			}
			if (Current == null || team == null || !Current.teamId.equals(team.getTeamId())) {
				Current = team;
				Preferences.current().setCurrentTeamFileName(team == null ? null : team.getTeamId());
				Preferences.current().save();
			}
		}
	}
	
	public static boolean isCurrentTeam(String otherTeamId) {
		synchronized (FILE_NAME_PREFIX) {
			return otherTeamId.equals(Preferences.current().getCurrentTeamFileName());
		}
	}

	public static List<TeamDescription> retrieveTeamDescriptions() {
		ArrayList<TeamDescription> descriptions = new ArrayList<TeamDescription>();
		for (String teamFileName : getAllTeamFileNames()) {
			Team team = read(teamFileName);
			TeamDescription description = new TeamDescription(teamFileName,
					team.getName(), team.getCloudId());
			descriptions.add(description);
		}
		return descriptions;
	}
	
	public static int numberOfTeams() {
		return getAllTeamFileNames().size();
	}
	
	public static boolean  isDuplicateTeamName(String newTeamName, Team teamToIgnore) {
		for (TeamDescription existingTeam : retrieveTeamDescriptions()) {
			if (teamToIgnore == null) {
				if (existingTeam.getName().equalsIgnoreCase(newTeamName)) {
					return true;
				}
			} else if (!existingTeam.getTeamId().equals(teamToIgnore.getTeamId())) {
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
		Game.deleteAllGamesForTeam(this.getTeamId());
		
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
	
	public boolean isDefaultTeamName() {
		return getName() != null && getName().equalsIgnoreCase(DEFAULT_TEAM_NAME);
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
	public List<Player> getPlayersSorted() {
		List<Player> playersSorted = getPlayers();
		Collections.sort(playersSorted, new Comparator<Player>() {
			@Override
			public int compare(Player lhs, Player rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
		});
		return playersSorted;
	}
	public Player getPlayer(String playerName) {
		for (Player player : getPlayers()) {
			if (playerName.equalsIgnoreCase(player.getName())) {
				return player;
			}
		}
		return null;
	}
	public static Player getPlayerNamed(String playerName) {
		if (playerName == null) {
			return Player.anonymous();
		}
		Player player = Team.current().getPlayer(playerName);
		if (player == null) {
			player = new Player(playerName);
			Team.current().addPlayer(player);
		}
		return player;
	}
	
	
//    if (playerName == nil) {
//        return nil;
//    }
//    Player* player = [[Team getCurrentTeam] getPlayer:playerName];
//    if (!player) {
//        player = [[Player alloc] initName:playerName];
//        [[Team getCurrentTeam] addPlayer:player];
//    }
//    return player;
	
	public boolean addPlayer(Player player) {
		Boolean replaced = removePlayer(player);
		getPlayers().add(player); 
		return replaced;
	}
	
	public boolean removePlayer(Player player) {
		Boolean replaced = getPlayers().remove(player);
		return replaced;
	}
	
	public boolean isDuplicatePlayerName(String newPlayerName, Player playerToIgnore) {
		for (Player player : getPlayers()) {
			if (playerToIgnore == null || playerToIgnore != player) {
				if (player.getName().equalsIgnoreCase(newPlayerName)) {
					return true;
				}
			} 
		}
		return false;
	}
	
	public List<Player> getDefaultLine() {
		List<Player> sortedPlayers = getPlayersSorted(); 
		List<Player> line = new ArrayList<Player>();
	    int maleCount = 0;
	    int femaleCount = 0;
	    for (Player player : sortedPlayers) {
	    	if (!player.isAnonymous()) {
				if (isMixed()) {
					if (player.isMale() && maleCount < 4) {
						line.add(player);
						maleCount++;
					} else if (!player.isMale() && femaleCount < 4) {
						line.add(player);
						femaleCount++;
					}
				} else {
					line.add(player);
				}
				if (line.size() >= 7) {
					break;
				}
	    	}
		}
	    return line;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void readExternal(ObjectInput input) throws IOException, ClassNotFoundException {
		teamId = (String)input.readObject();
		name = (String)input.readObject();
		cloudId = (String)input.readObject();
		isMixed = input.readBoolean();
		isDisplayingPlayerNumber = input.readBoolean();		
		players = (List<Player>)input.readObject();
		isPlayersAreLeaguevine = input.readBoolean();	
		leaguevineJson = (String)input.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput output) throws IOException {
		output.writeObject(teamId);
		output.writeObject(name);
		output.writeObject(cloudId);
		output.writeBoolean(isMixed);
		output.writeBoolean(isDisplayingPlayerNumber);
		output.writeObject(players);
		output.writeBoolean(isPlayersAreLeaguevine);
		output.writeObject(leaguevineJson);
	}

	public JSONObject toJsonObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(JSON_TEAM_ID, teamId);
		jsonObject.put(JSON_CLOUD_ID, cloudId);
		jsonObject.put(JSON_NAME, name);
		if (players != null && !players.isEmpty()) {
			JSONArray jsonPlayers = new JSONArray();
			for (Player player : players) {
				if (!player.isAnonymous()) {
					jsonPlayers.put(player.toJsonObject());
				}
			}
			jsonObject.put(JSON_PLAYERS, jsonPlayers);
		}
		jsonObject.put(JSON_IS_MIXED, isMixed);
		jsonObject.put(JSON_IS_DISPLAYING_PLAYER_NUMBER, isDisplayingPlayerNumber);
		jsonObject.put(JSON_IS_LEAGUEVINE_PLAYERS, isPlayersAreLeaguevine);
		jsonObject.put(JSON_LEAGUEVINE_JSON, leaguevineJson);
		return jsonObject;
	}
	
	public static Team fromJsonObject(JSONObject jsonObject) throws JSONException {
		if (jsonObject == null) {
			return null;
		} else {
			Team team = new Team();
			if (jsonObject.has(JSON_TEAM_ID)) {
				team.setTeamId(jsonObject.getString(JSON_TEAM_ID));
			}
			if (jsonObject.has(JSON_CLOUD_ID)) {
				team.setCloudId(jsonObject.getString(JSON_CLOUD_ID));
			}
			if (jsonObject.has(JSON_NAME)) {
				team.setName(jsonObject.getString(JSON_NAME));
			}
			if (jsonObject.has(JSON_PLAYERS)) {
				JSONArray jsonArray = jsonObject.getJSONArray(JSON_PLAYERS);
				for (int i = 0; i < jsonArray.length(); i++) {
					Player player = Player.fromJsonObject((JSONObject)jsonArray.get(i));
					if (!player.isAnonymous()) {
						team.players.add(player);
					}
				}
			}
			if (jsonObject.has(JSON_IS_MIXED)) {
				team.setMixed(jsonObject.getBoolean(JSON_IS_MIXED));
			}
			if (jsonObject.has(JSON_IS_DISPLAYING_PLAYER_NUMBER)) {
				team.setDisplayingPlayerNumber(jsonObject.getBoolean(JSON_IS_DISPLAYING_PLAYER_NUMBER));
			}
			if (jsonObject.has(JSON_IS_LEAGUEVINE_PLAYERS)) {
				team.setPlayersAreLeaguevine(jsonObject.getBoolean(JSON_IS_LEAGUEVINE_PLAYERS));
			}
			if (jsonObject.has(JSON_LEAGUEVINE_JSON)) {
				team.setLeaguevineJson(jsonObject.getString(JSON_LEAGUEVINE_JSON));
			}

			return team;
		}
	}
	
	public static Comparator<Team> TeamListComparator = new Comparator<Team>() {
		public int compare(Team team1, Team team2) {
			return team1.getName().compareTo(team2.getName());
		}
	};
}
