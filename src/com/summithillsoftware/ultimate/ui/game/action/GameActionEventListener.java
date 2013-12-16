package com.summithillsoftware.ultimate.ui.game.action;

import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Player;

public interface GameActionEventListener {
	
	public void newEvent(Event event);
	
	public void removeLastEvent();
	
	public void initialOffensePlayerSelected(Player player);
	
	public void timeoutInfoRequested();

}
