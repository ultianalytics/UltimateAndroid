package com.summithillsoftware.ultimate.model;

public class TeamDescription {
	private String teamId;	
	private String name;
	private String cloudId;
	
	public TeamDescription(String teamId, String name, String cloudId) {
		super();
		this.teamId = teamId;
		this.name = name;
		this.cloudId = cloudId;
	}

	public String getTeamId() {
		return teamId;
	}
	public String getName() {
		return name;
	}
	public String getCloudId() {
		return cloudId;
	}
	public boolean isCurrentTeam() {
		return teamId.equals(ObjectStore.current().getCurrentTeamId());
	}
	
}
