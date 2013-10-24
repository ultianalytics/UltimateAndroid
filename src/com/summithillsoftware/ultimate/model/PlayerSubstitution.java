package com.summithillsoftware.ultimate.model;

import java.io.Serializable;

public class PlayerSubstitution implements Serializable {
	private static final long serialVersionUID = 23582896191206433L;

	private Player fromPlayer;
	private Player toPlayer;
	private SubstitutionReason reason;
	private long timestamp;
	
	public Player getFromPlayer() {
		return fromPlayer;
	}
	public void setFromPlayer(Player fromPlayer) {
		this.fromPlayer = fromPlayer;
	}
	public SubstitutionReason getReason() {
		return reason;
	}
	public void setReason(SubstitutionReason reason) {
		this.reason = reason;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Player getToPlayer() {
		return toPlayer;
	}
	public void setToPlayer(Player toPlayer) {
		this.toPlayer = toPlayer;
	}
	
	@Override
	public String toString() {
		return "PlayerSubstitution [fromPlayer=" + fromPlayer + ", toPlayer="
				+ toPlayer + ", reason=" + reason + ", timestamp=" + timestamp
				+ "]";
	}
	

	
}
