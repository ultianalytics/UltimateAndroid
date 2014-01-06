package com.summithillsoftware.ultimate.stats;

import com.summithillsoftware.ultimate.model.Player;

public class PlayerStat {
	private Player player;
	private float floatValue;
	private int intValue;
	private StatNumericType type;
	
	public PlayerStat(Player player, float floatValue) {
		super();
		this.player = player;
		this.floatValue = floatValue;
		this.type = StatNumericType.Float;
	}
	
	public PlayerStat(Player player, int intValue) {
		super();
		this.player = player;
		this.intValue = intValue;
		this.type = StatNumericType.Integer;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(float floatValue) {
		this.floatValue = floatValue;
	}
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public float sortValue() {
		return type == StatNumericType.Integer ? (float)intValue : floatValue;
	}
	public StatNumericType getType() {
		return type;
	}
	public void setType(StatNumericType type) {
		this.type = type;
	}

}
