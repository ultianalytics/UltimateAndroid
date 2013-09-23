package com.summithillsoftware.ultimate.model;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.Locale;

public class Player implements Serializable {
	private static final long serialVersionUID = 7079444636975701164L;
	private static Player ANONYMOUS_PLAYER = null;
	private static final String ANON_NAME = "Anonymous";
	
	private String name;
	private String number;
	private boolean isMale;
	private String leaguevineJson;
	private PlayerPosition position;
	
	static {
		Player.ANONYMOUS_PLAYER = new Player();
		Player.ANONYMOUS_PLAYER.name = ANON_NAME;
	}
	
	public Player() {
		super();
		name = "";
		number = "";
		position = PlayerPosition.Any;
	}
	
	public Player(String name) {
		this();
		this.name = name;
	}
	
	public static Player anonymous() {
		return ANONYMOUS_PLAYER;
	}
	
	public boolean isAnonymous() {
		return this.name.equals(ANON_NAME);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if (!this.name.equals(ANON_NAME) && !name.equals(ANON_NAME)) {  // don't allow changing name of anonymous player and don't allow a new player with this name
			this.name = name;
		}
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public boolean isMale() {
		return isMale;
	}
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
	public String getLeaguevineJson() {
		return leaguevineJson;
	}
	public void setLeaguevineJson(String leaguevineJson) {
		this.leaguevineJson = leaguevineJson;
	}
	public PlayerPosition getPosition() {
		return position;
	}
	public void setPosition(PlayerPosition position) {
		this.position = position;
	}
	public boolean isDuplicateNewName(String newName, Team team) {
		if (newName.equalsIgnoreCase(name)) {
			return false;
		} else {
			return team.getPlayers().contains(new Player(newName));
		}
	}

	@SuppressLint("DefaultLocale")
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.toLowerCase(Locale.ENGLISH).hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equalsIgnoreCase(other.name))
			return false;
		return true;
	}

}
