package com.summithillsoftware.ultimate.model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class PlayerSubstitution implements Serializable {
	private static final long serialVersionUID = 23582896191206433L;

	private Player fromPlayer;
	private Player toPlayer;
	private SubstitutionReason reason;
	private int timestamp;
	
	public void useSharedPlayers() {
		fromPlayer = Player.replaceWithSharedPlayer(fromPlayer);
		toPlayer = Player.replaceWithSharedPlayer(toPlayer);
	}
	
	public Set<Player> getPlayers() {
		HashSet<Player> players = new HashSet<Player>();
		if (fromPlayer != null) {
			players.add(fromPlayer);
		}
		if (toPlayer != null) {
			players.add(toPlayer);
		}
		return players;
	}
	
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
	public int getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(int timestamp) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
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
		PlayerSubstitution other = (PlayerSubstitution) obj;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
	
	public static Comparator<PlayerSubstitution> TimestampAndNameComparator = new Comparator<PlayerSubstitution>() {
		public int compare(PlayerSubstitution playerSub1, PlayerSubstitution playerSub2) {
			if (playerSub1.getTimestamp() == playerSub2.getTimestamp()) {
				return playerSub1.getFromPlayer().getName().compareTo(playerSub2.getFromPlayer().getName());
			}
			// descending order
			return Integer.valueOf(playerSub2.getTimestamp()).compareTo(Integer.valueOf(playerSub1.getTimestamp()));
		}
	};
	

	
}
