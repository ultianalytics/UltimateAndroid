package com.summithillsoftware.ultimate.ui.team;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.summithillsoftware.ultimate.R;
import com.summithillsoftware.ultimate.model.Team;
import com.summithillsoftware.ultimate.model.TeamDescription;

public class TeamsListAdaptor extends BaseAdapter {
	private Context context;
	private List<TeamDescription> teams;

	public TeamsListAdaptor(Context context) {
		super();
		this.context = context;
		resetTeams();
	}
	
	private void resetTeams() {
		teams = Team.retrieveTeamDescriptions();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return teams.size();
	}

	@Override
	public Object getItem(int index) {
		return teams.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int index, View reusableRowView, ViewGroup parent) {
		View rowView = reusableRowView;
		if (reusableRowView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout_teams, null);
		}
	
		TextView teamNameTextView = (TextView)rowView.findViewById(R.id.text_team_name);
		TextView cloudIdTextView = (TextView)rowView.findViewById(R.id.text_team_cloud_id);
		
		TeamDescription team = teams.get(index);
		teamNameTextView.setText(team.getName());
		cloudIdTextView.setText(team.getCloudId());	
		teamNameTextView.setTextColor(context.getResources().getColor(team.isCurrentTeam() ? R.color.list_name_current:R.color.list_name));
		
		return rowView;
	}
	




}
