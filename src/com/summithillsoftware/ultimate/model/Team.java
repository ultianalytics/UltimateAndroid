package com.summithillsoftware.ultimate.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
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
