package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.model.Action.Callahan;
import static com.summithillsoftware.ultimate.model.Action.Catch;
import static com.summithillsoftware.ultimate.model.Action.Drop;
import static com.summithillsoftware.ultimate.model.Action.Goal;
import static com.summithillsoftware.ultimate.model.Action.MiscPenalty;
import static com.summithillsoftware.ultimate.model.Action.Stall;
import static com.summithillsoftware.ultimate.model.Action.Throwaway;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.summithillsoftware.ultimate.R;

public class OffenseEvent extends Event {
	private static final long serialVersionUID = 4170266503673315946L;
	
	public static final EnumSet<Action> OFFENSE_ACTIONS = EnumSet.of(
			Catch,
			Drop,
			Goal,
			Throwaway,
			Callahan,
			Stall,
			MiscPenalty);
	
	private Player passer;
	private Player receiver;
	
	public OffenseEvent(Action action, Player passer) {
		this(action, passer, null);
	}

	public OffenseEvent(Action action, Player passer, Player receiver) {
		super(action);
		this.passer = passer;
		this.receiver = receiver;
	}
	
	public boolean isOurGoal() {
		return getAction() == Goal;
	}
	
	public boolean isTheirGoal() {
		return getAction() == Callahan;
	}
	
	public boolean isGoal() {
		return isOurGoal() || isTheirGoal();
	}
	
	public boolean isCallahan() {
		return getAction() == Callahan;
	}
	
	public boolean isFinalEventOfPoint() {
		return isGoal();
	}

	public boolean isOffense() {
		return true;
	}
	
	public boolean isPlayEvent() {
		return true;
	}
	
	@Override
	public boolean isTurnover() {
	    return getAction() == Drop || getAction() == Throwaway || getAction() == Stall || getAction() == MiscPenalty || getAction() == Callahan;
	}

	@Override
	public boolean isNextEventOffense() {
		return getAction() == Catch || getAction() == Callahan;
	}

	@Override
	public List<Player> getPlayers() {
		List<Player> players = new ArrayList<Player>();
		if (passer != null) {
			players.add(passer);
		}
		if (receiver != null) {
			players.add(receiver);
		}
	    return players;
	}
	
	public boolean isAnonymous() {
	    return isPasserAnonymous() && isReceiverAnonymous();
	}
	
	public boolean isPasserAnonymous() {
	    return (passer == null || passer.isAnonymous());
	}
	
	public boolean isReceiverAnonymous() {
	    return (receiver == null || receiver.isAnonymous());
	}

	@Override
	protected String getDescriptionForTeamAndOpponent(String teamName,String opponentName) {
		String ourTeam = StringUtil.getString(R.string.event_description_our_team);
	    switch(getAction()) {
	        case Catch: {
	            if (isAnonymous()) {
	            	// {team} pass
	            	return getString(R.string.event_description_pass,(teamName == null ? ourTeam : teamName));
	            } else if (isReceiverAnonymous()) {
	            	// {passer} pass
	            	return getString(R.string.event_description_pass,passer.getName());
	            } else if (isPasserAnonymous()) {
	            	// Pass to {passer}
	            	return getString(R.string.event_description_pass_to, receiver.getName());
	            } else {
	            	// {passer} to {receiver}
	            	return getString(R.string.event_description_pass_from_to, passer.getName(), receiver.getName());
	            }
	        }
	        case Drop: {
	            if (isAnonymous()) {
	            	// {{team} drop
	            	return getString(R.string.event_description_drop,(teamName == null ? ourTeam : teamName));
	            } else if (isReceiverAnonymous()) {
	            	// {{passer} pass dropped
	            	return getString(R.string.event_description_drop_from,passer.getName());
	            } else if (isPasserAnonymous()) {
	            	// {{receiver} dropped pass            	
	            	return getString(R.string.event_description_drop_to, receiver.getName());
	            } else {
	            	// {{receiver} dropped from {passer}
	            	return getString(R.string.event_description_drop_from_to, receiver.getName(), passer.getName());    
	            }
	        }
	        case Throwaway:{
	        	return isAnonymous() ? 
	        			getString(R.string.event_description_throwaway,(teamName == null ? ourTeam : teamName)) : 
	        			getString(R.string.event_description_throwaway,passer.getName());
	        }
	        case Stall:{
	        	return isAnonymous() ? 
	        			getString(R.string.event_description_stalled,(teamName == null ? ourTeam : teamName)) : 
	        			getString(R.string.event_description_stalled,passer.getName());
	        }
	        case MiscPenalty:{
	        	return isAnonymous() ? 
	        			getString(R.string.event_description_penalized,(teamName == null ? ourTeam : teamName)) : 
	        			getString(R.string.event_description_penalized,passer.getName());
	        }
	        case Callahan:{
	        	return isAnonymous() ? 
	        			getString(R.string.event_description_callahaned,(teamName == null ? ourTeam : teamName)) : 
	        			getString(R.string.event_description_callahaned,passer.getName());
	        }
	        case Goal: {
	            if (isAnonymous()) {
	            	// {team} goal
	            	return getString(R.string.event_description_o_goal,(teamName == null ? ourTeam : teamName));
	            } else if (isReceiverAnonymous()) {
	            	// {passer} pass for goal
	            	return getString(R.string.event_description_o_goal_from,passer.getName());	            	
	            } else if (isPasserAnonymous()) {
	            	// {receiver} goal
	            	return getString(R.string.event_description_o_goal, receiver.getName());
	            } else {
	            	// {team} goal ({passer} to {receiver})
	            	return getString(R.string.event_description_o_goal_from_to, (teamName == null ? ourTeam : teamName), passer.getName(), receiver.getName());
	            }
	        }
	        default:
	            return "";
	    }
	}

	public Player getPasser() {
		return passer;
	}

	public void setPasser(Player passer) {
		this.passer = passer;
	}

	public Player getReceiver() {
		return receiver;
	}

	public void setReceiver(Player receiver) {
		this.receiver = receiver;
	}
	
	protected void ensureValid() {
		if (passer == null) {
			passer = Player.anonymous();
		}
		if (receiver == null) {
			receiver = Player.anonymous();
		}		
		if (!OFFENSE_ACTIONS.contains(getAction())) {
			throw new RuntimeException("Invalid action for offense event " + getAction());
		}
	}
	
	public Player getPlayerOne() {
		return passer;
	}

	public Player getPlayerTwo() {
		return receiver;
	}

}
