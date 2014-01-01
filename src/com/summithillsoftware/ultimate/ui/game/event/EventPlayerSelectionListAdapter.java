package com.summithillsoftware.ultimate.ui.game.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Point;
import com.summithillsoftware.ultimate.model.Team;

public class EventPlayerSelectionListAdapter extends BaseAdapter {
	private Player selectedPlayer;
	private List<Player> sortedPlayers;
	private boolean isShowingAllPlayers;
	private Context context;

	public EventPlayerSelectionListAdapter(Context context) {
		super();
		this.context = context;
		resetPlayers();
	}
	
	public void resetPlayers() {
		sortedPlayers = null;
		notifyDataSetChanged();
	}
	
	public void setSelectedPlayer(Player player) {
		selectedPlayer = player;
	}
	
	private List<Player>getSortedPlayers() {
		if (sortedPlayers == null) {
			Set<Player> players = new HashSet<Player>(point().getPlayers());
			if (isShowingAllPlayers) {
				players.addAll(Team.current().getPlayers());
			}
			players.remove(Player.anonymous());
			sortedPlayers = new ArrayList<Player>(players);
			
			Collections.sort(sortedPlayers, Team.current().isDisplayingPlayerNumber() ? Player.PlayerNumberComparator : Player.PlayerNameComparator);
			sortedPlayers.add(0, Player.anonymous());
		}
		return sortedPlayers;
	}

	@Override
	public int getCount() {
		return getSortedPlayers().size();
	}

	@Override
	public Object getItem(int position) {
		return getSortedPlayers().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int index, View reusableRowView, ViewGroup parent) {
		View rowView = reusableRowView;
		Player player = getSortedPlayers().get(index);
		boolean isSelectedPlayer = player.equals(selectedPlayer);
		
		if (reusableRowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout_event_player, null);
		}
	
		TextView playerDescriptionTextView = (TextView)rowView.findViewById(R.id.playerDescriptionTextView);
		if (player.isAnonymous()) {
			playerDescriptionTextView.setText(context.getString(R.string.common_unknown_player));
		} else {
			playerDescriptionTextView.setText(Team.current().isDisplayingPlayerNumber() ? player.getPlayerNumberDescription() : player.getName());
		}
		int textColor = context.getResources().getColor(isSelectedPlayer ? R.color.White : R.color.DarkGray);
		playerDescriptionTextView.setTextColor(textColor);

		ImageView selectedImageView = (ImageView)rowView.findViewById(R.id.playerSelectedImageView);
		selectedImageView.setVisibility(isSelectedPlayer ? View.VISIBLE : View.INVISIBLE);
		
		return rowView;
	}
	
	private Point point() {
		return Game.current().getSelectedEvent().getPoint();
	}
	
	public void setShowingAllPlayers(boolean isShowingAllPlayers) {
		if (isShowingAllPlayers != this.isShowingAllPlayers) {
			this.isShowingAllPlayers = isShowingAllPlayers;
			resetPlayers();
		}
	}

}

