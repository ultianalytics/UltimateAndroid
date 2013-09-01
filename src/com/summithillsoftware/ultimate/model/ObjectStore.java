package com.summithillsoftware.ultimate.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectStore {
	private static ObjectStore Current;
	
	static {
		Current = new ObjectStore();
	}

	public ObjectStore current() {
		return Current;
	}
	
	public List<TeamDescription>getTeamDescriptions() {
		// TODO...this is thowaway code...should actually read the directory and find the teams
		List<TeamDescription> teams = new ArrayList<TeamDescription>();
		
		TeamDescription team = new TeamDescription("123", "Foobars", "cloud9");
		teams.add(team);
		
		return teams;
		
	}
	
	public Team getTeam(String teamId) {
		// TODO...this is thowaway code...should actually read the team from the file system
		if (teamId.equals("123")) {
			Team team = new Team();
			team.setTeamId("123");
			team.setName("Foobars");
			return team;
		} else {
			return null;
		}
		
	}
}
