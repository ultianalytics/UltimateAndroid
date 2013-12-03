package com.summithillsoftware.ultimate.ui.game;

import java.util.List;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Team;

public class PlayerLineButton extends Button {
	private static final int[] LINE_STATE_CHANGED = { R.attr.state_changed };
	
	private Player player;
	private boolean isButtonOnFieldView;
	boolean playerLineStatusChanged;

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
	
	@Override
	public String toString() {
		return "PlayerLineButton [player=" + player.getName() + ", isOnFieldView="
				+ isButtonOnFieldView + ", tag=" + getTag() +"]";
	}

	public void setPlayer(Player player) {
		this.player = player;
		String description = player.isAnonymous() ? 
				UltimateApplication.current().getString(R.string.button_line_field_slot_open) : 
					(Team.current().isDisplayingPlayerNumber() ? player.getPlayerNumberDescription() : player.getName());
		this.setText(description);
		setTag(player.getName());
	}

	public boolean isButtonOnFieldView() {
		return isButtonOnFieldView;
	}

	public void setButtonOnFieldView(boolean isOnField, List<Player> playersOnField, Set<Player>originalLine) {
		this.isButtonOnFieldView = isOnField;
		updateView(playersOnField, originalLine);
	}
	
	public void updateView(List<Player> playersOnField, Set<Player>originalLine) {
		boolean isPlayerOnField = !player.isAnonymous() && playersOnField.contains(player);
		if (player.isAnonymous() || (!isButtonOnFieldView() && isPlayerOnField)) {
			setEnabled(false);
		} else {
			setEnabled(true);
			updateViewForChangeStatus(isPlayerOnField, originalLine);
			// TODO...show gender and number of points played
		}
		updateViewForEnabledStatus();
	}
	private void updateViewForEnabledStatus() {
		if (isEnabled()) {
			setTextColor(getResources().getColor(R.color.White));
			setShadowLayer(0.6f, 1, 1, 0xFF000000);
		} else {
			setShadowLayer(0.6f, 1, 1, 0x00000000); // hide the shadow by making it transparent alpha
			if (isButtonOnFieldView) {
				setTextColor(getResources().getColor(R.color.DarkOrange));
			} else {
				setTextColor(getResources().getColor(R.color.DarkOliveGreen));
			}
		}
	}
	
	private void updateViewForChangeStatus(boolean isPlayerOnField, Set<Player>originalLine) {
		playerLineStatusChanged = (isPlayerOnField != originalLine.contains(player));
		refreshDrawableState();
	}

	@Override
    protected int[] onCreateDrawableState(int extraSpace) {
        // If the player has changed from/top line/bench then we merge our custom message unread state into
        // the existing drawable state before returning it.
        if (playerLineStatusChanged && isEnabled()) {
            // Add extra state.
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
 
            mergeDrawableStates(drawableState, LINE_STATE_CHANGED);
 
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }

}
