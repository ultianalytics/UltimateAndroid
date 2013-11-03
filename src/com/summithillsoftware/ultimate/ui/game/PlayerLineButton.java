package com.summithillsoftware.ultimate.ui.game;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;

public class PlayerLineButton extends Button {
	private Player player;
	private boolean isButtonOnFieldView;

	public PlayerLineButton(Context context) {
		super(context);
	} 
	public PlayerLineButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public PlayerLineButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
		String description = player.isAnonymous() ? "open" : (Team.current().isDisplayingPlayerNumber() ? player.getNumber() : player.getName());
		this.setText(description);
		setTag(player.getName());
		updateView();
	}

	public boolean isButtonOnFieldView() {
		return isButtonOnFieldView;
	}

	public void setButtonOnFieldView(boolean isOnField) {
		this.isButtonOnFieldView = isOnField;
		updateView();
	}
	
	public void updateView() {
		if (player.isAnonymous() || (!isButtonOnFieldView() && isPlayerOnField(player))) {
			setEnabled(false);
		} else {
			setEnabled(true);
			// TODO...show gender and number of points played
		}
		if (isEnabled()) {
			setTextColor(getResources().getColor(R.color.White));
		} else {
			if (isButtonOnFieldView) {
				setTextColor(getResources().getColor(R.color.DarkOrange));
			} else {
				setTextColor(getResources().getColor(R.color.DarkOliveGreen));
			}
		}
	}

	
	private boolean isPlayerOnField(Player player) {
		return Game.current().getCurrentLine().contains(player);
	}
	
	@Override
	public String toString() {
		return "PlayerLineButton [player=" + player.getName() + ", isOnFieldView="
				+ isButtonOnFieldView + ", tag=" + getTag() +"]";
	}



}
