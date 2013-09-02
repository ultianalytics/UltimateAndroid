package com.summithillsoftware.ultimate.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectStore {
	private static ObjectStore Current;
	
	static {
		Current = new ObjectStore();
	}

	public static ObjectStore current() {
		return Current;
	}
	
	public List<TeamDescription>getTeamDescriptions() {
		// TODO...this is thowaway code...should actually read the directory and find the teams
		List<TeamDescription> teams = new ArrayList<TeamDescription>();
		
		TeamDescription team = new TeamDescription("123", "Foobars", "cloud9");
		teams.add(team);
		team = new TeamDescription("456", "Mama's boys", "cloud10");
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
		} else if (teamId.equals("456")) {
			Team team = new Team();
			team.setTeamId("456");
			team.setName("Mama's boys");
			return team;
		} else {
			return null;
		}
		
	}
	
	public Team getCurrentTeam() {
		// TODO...this is thowaway code...should actually read the team from the file system
		return getTeam(getCurrentTeamId());
	}
	
	public String getCurrentTeamId() {
		// TODO...this is thowaway code...should actually read the team from the file system
		return "123";
	}
}
