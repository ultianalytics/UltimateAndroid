package com.summithillsoftware.ultimate.ui.game;

import android.content.Context;
import android.widget.Button;

import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;

public class PlayerLineButton extends Button {
	private Player player;
	private boolean isOnField;

	public PlayerLineButton(Context context) {
		super(context);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		String description = player.isAnonymous() ? "open" : (Team.current().isDisplayingPlayerNumber() ? player.getNumber() : player.getName());
		this.setText(description);
		updateStyle();
	}

	public boolean isOnField() {
		return isOnField;
	}

	public void setOnField(boolean isOnField) {
		this.isOnField = isOnField;
		updateStyle();
	}
	
	private void updateStyle() {
		if (player.isAnonymous() || (!isOnField() && isPlayerOnField(player))) {
//			setBackgroundColor(getResources().getColor(android.R.color.transparent));
		} else {
//			setBackgroundColor(color.Red);
		}
	}
	
	private boolean isPlayerOnField(Player player) {
		return Game.current().getCurrentLine().contains(player);
	}

}
