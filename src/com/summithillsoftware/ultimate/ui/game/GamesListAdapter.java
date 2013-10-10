package com.summithillsoftware.ultimate.ui.game;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summithillsoftware.ultimate.DateUtil;
import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.UltimateApplication;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.GameDescription;

public class GamesListAdapter extends BaseAdapter {
	private List<GameDescription> sortedGames;
	private Context context;

	public GamesListAdapter(Context context) {
		super();
		this.context = context;
		resetPlayers();
	}
	
	public void resetPlayers() {
		sortedGames = null;
		notifyDataSetChanged();
	}
	
	private List<GameDescription>getSortedGames() {
		if (sortedGames == null) {
			sortedGames = Game.retrieveGameDescriptionsForCurrentTeam();
		}
		return sortedGames;
	}

	@Override
	public int getCount() {
		return getSortedGames().size();
	}

	@Override
	public Object getItem(int position) {
		return getSortedGames().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int index, View reusableRowView, ViewGroup parent) {
		View rowView = reusableRowView;
		if (reusableRowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout_games, null);
		}
	
		TextView opponentNameView = (TextView)rowView.findViewById(R.id.text_game_opponent);
		TextView dateView = (TextView)rowView.findViewById(R.id.text_game_date);
		TextView tournamentView = (TextView)rowView.findViewById(R.id.text_game_tournament);
		
		GameDescription game = getSortedGames().get(index);
		opponentNameView.setText(game.getOpponentName());
		if (game.getStartDate() != null) {
			String startDateTime = DateFormat.getTimeInstance().format(game.getStartDate());
			if (DateUtil.isToday(game.getStartDate())) {
				startDateTime = UltimateApplication.current().getString(R.string.common_today) + " " + startDateTime;
			} else {
				startDateTime = SimpleDateFormat.getDateInstance().format(game.getStartDate()) + " " + startDateTime;
			}
			dateView.setText(startDateTime);
		}
		tournamentView.setText(game.getOpponentName());
		
		return rowView;
	}
	
}
