package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.model.Action.Callahan;
import static com.summithillsoftware.ultimate.model.Action.De;
import static com.summithillsoftware.ultimate.model.Action.Goal;
import static com.summithillsoftware.ultimate.model.Action.Pull;
import static com.summithillsoftware.ultimate.model.Action.PullOb;
import static com.summithillsoftware.ultimate.model.Action.Throwaway;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.summithillsoftware.ultimate.R;

public class DefenseEvent extends Event {
	private static final long serialVersionUID = 4170266503673315946L;
	private static final String HANGTIME_DETAIL_PROPERTY_NAME = "hangtime";
	
	public static final EnumSet<Action> DEFENSE_ACTIONS = EnumSet.of(
			Pull,
			PullOb,
			Goal,
			Throwaway,
			De,
			Callahan);
	
	private Player defender;
	
	public DefenseEvent(Action action, Player defender) {
		super(action);
		this.defender = defender;
	}
	
	public boolean isOurGoal() {
		return getAction() == Callahan;
	}
	
	public boolean isTheirGoal() {
		return getAction() == Goal;
	}
	
	public boolean isGoal() {
		return isOurGoal() || isTheirGoal();
	}
	
	public boolean isCallahan() {
		return getAction() == Callahan;
	}
	
	public boolean isPull() {
		return getAction() == Pull || getAction() == PullOb;
	}
	
	public boolean isPullIb() {
		return getAction() == Pull;
	}
	
	public boolean isPullOb() {
		return getAction() == PullOb;
	}
	
	public boolean isFinalEventOfPoint() {
		return isGoal();
	}
	
	public boolean isDefense() {
		return true;
	}
	
	public boolean isPlayEvent() {
		return true;
	}
	
	@Override
	public boolean isTurnover() {
	    return getAction() == De || getAction() == Throwaway || getAction() == Callahan;
	}

	@Override
	public boolean isNextEventOffense() {
		return !isCallahan() && (isTurnover() || isGoal());
	}

	@Override
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		if (defender != null) {
			players.add(defender);
		}
	    return players;
	}
	
	public boolean isAnonymous() {
		return (defender == null || defender.isAnonymous());
	}

	@Override
	protected String getDescriptionForTeamAndOpponent(String teamName,String opponentName) {
		String ourTeam = StringUtil.getString(R.string.event_description_our_team);
	    switch(getAction()) {
	        case Pull: {
	        	String hangtimeString = "";
	        	int hangTime = getPullHangtimeMilliseconds();
	        	if (hangTime > 0) {
	        		DecimalFormat formatter = new DecimalFormat("##0.0");
	        		hangtimeString = "(" + getString(R.string.event_description_pass_to, formatter.format(getPullHangtimeMilliseconds())) + ")";
	        	}
	            if (isAnonymous()) {
	            	// {team} pull ({seconds} sec)
	            	return getString(R.string.event_description_pull,(teamName == null ? ourTeam : teamName), hangtimeString);
	            } else {
	            	// Pull from {player} ({seconds} sec)	
	            	return getString(R.string.event_description_pull_from,defender.getName(), hangtimeString);	            	
	            }
	        }
	        case PullOb: {
	            if (isAnonymous()) {
	            	// {team} OB pull
	            	return getString(R.string.event_description_pull_ob,(teamName == null ? ourTeam : teamName));
	            } else {
	            	// OB Pull from {player}
	            	return getString(R.string.event_description_pull_ob_from,defender.getName());	
	            }
	        }
	        case Goal: {
	        	// Opponent goal || {opponent} Goal
	        	return opponentName == null ? getString(R.string.event_description_opponent_goal) : getString(R.string.event_description_opponent_goal,opponentName);
	        }
	        case Throwaway:{
	        	// Opponent throwaway || {opponent} throwaway	        	
	        	return opponentName == null ? getString(R.string.event_description_opponent_throwaway) : getString(R.string.event_description_opponent_throwaway_by,opponentName);
	        }
	        case De: {
	        	// {team|our} D || D by {player} 	        	
	        	return defender == null ? getString(R.string.event_description_d, (teamName == null ? ourTeam : teamName)) : getString(R.string.event_description_d_by,defender.getName());
	        }
	        case Callahan: {
	        	// {team|our} Callahan || Callahan by {player} 	        	
	        	return defender == null ? getString(R.string.event_description_callahan, (teamName == null ? ourTeam : teamName)) : getString(R.string.event_description_callahan_by,defender.getName());	        	
	        }
	        default:
	            return "";
	    }
	}

	public Player getDefender() {
		return defender;
	}

	public void setDefender(Player defender) {
		this.defender = defender;
	}

	
	protected void ensureValid() {
		if (defender == null) {
			defender = Player.anonymous();
		}

		if (!DEFENSE_ACTIONS.contains(getAction())) {
			throw new RuntimeException("Invalid action for defense event " + getAction());
		}
	}
	
	public Player getPlayerOne() {
		return defender;
	}

	public void setPullHangtimeMilliseconds(int hangtimeMs) {
		setDetailIntValue(HANGTIME_DETAIL_PROPERTY_NAME, hangtimeMs);
	}
	
	public int getPullHangtimeMilliseconds() {
		return getDetailIntValue(HANGTIME_DETAIL_PROPERTY_NAME, 0);
	}
	
	public void useSharedPlayers() {
		defender = Player.replaceWithSharedPlayer(defender);
	}
}
