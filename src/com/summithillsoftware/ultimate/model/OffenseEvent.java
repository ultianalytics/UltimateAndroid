package com.summithillsoftware.ultimate.model;

import static com.summithillsoftware.ultimate.model.Action.Callahan;
import static com.summithillsoftware.ultimate.model.Action.Catch;
import static com.summithillsoftware.ultimate.model.Action.De;
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
			De,
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
		String our = StringUtil.current().ourCaptialized();
	    switch(getAction()) {
	        case Catch: {
	            if (isAnonymous()) {
	            	// {team} pass
	            	return getString(R.string.event_description_pass,(teamName == null ? our : teamName));
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
	            	return getString(R.string.event_description_drop,(teamName == null ? our : teamName));
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
//	        case Throwaway:{
//	            return self.isAnonymous ?  [NSString stringWithFormat:@"%@ throwaway", (teamName == nil ? @"Our" : teamName)] : [NSString stringWithFormat:@"%@ throwaway", self.passer.name];     
//	        }
//	        case Stall:{
//	            return self.isAnonymous ?  [NSString stringWithFormat:@"%@ was stalled", (teamName == nil ? @"Our" : teamName)] : [NSString stringWithFormat:@"%@ was stalled", self.passer.name];
//	        }
//	        case MiscPenalty:{
//	            return self.isAnonymous ?  [NSString stringWithFormat:@"%@ penalized", (teamName == nil ? @"Our" : teamName)] : [NSString stringWithFormat:@"%@ penalized", self.passer.name];
//	        }
//	        case Goal: {
//	            if (self.isAnonymous) {
//	                return [NSString stringWithFormat:@"%@ goal", (teamName == nil ? @"Our" : teamName)]; 
//	            } else if (self.isReceiverAnonymous) {
//	                return [NSString stringWithFormat:@"%@ pass for goal", self.passer.name];
//	            } else if (self.isPasserAnonymous) {
//	                return [NSString stringWithFormat:@"%@ goal", self.receiver.name];
//	            } else {
//	                return [NSString stringWithFormat:@"%@ goal (%@ to %@)", (teamName == nil ? @"Our" : teamName), self.passer.name, self.receiver.name];            
//	            }
//	        }
//	        case Callahan:{
//	            return self.isAnonymous ?  [NSString stringWithFormat:@"%@ callahan'd", (teamName == nil ? @"Our" : teamName)] : [NSString stringWithFormat:@"%@ callahan'd", self.passer.name];
//	        }
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
