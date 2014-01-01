package com.summithillsoftware.ultimate.ui.game.event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

import com.summithillsoftware.ultimate.model.Player;

public class EventPlayerSelectionListView extends ListView {

	public EventPlayerSelectionListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public EventPlayerSelectionListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public EventPlayerSelectionListView(Context context) {
		super(context);
	}
	
	public void setSelectedPlayer(Player player) {
		((EventPlayerSelectionListAdapter)getAdapter()).setSelectedPlayer(player);
		refreshVisibleRows();
	}
	
	public Player getSelectedPlayer(int position) {
		return (Player)getAdapter().getItem(position);
	}
	
	private void refreshVisibleRows() {
		int start = getFirstVisiblePosition();
		for(int i=start, j=getLastVisiblePosition(); i<=j; i++) {
			View existingRowView = getChildAt(i-start);  // optimization...don't need to inflate another row
			getAdapter().getView(i, existingRowView, this);
		}
	}

}
