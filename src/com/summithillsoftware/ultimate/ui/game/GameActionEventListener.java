package com.summithillsoftware.ultimate.ui.game;

import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Player;

public interface GameActionEventListener {
	
	public void newEvent(Event event);
	
	public void removeEvent(Event event);
	
	public void initialOffensePlayerSelected(Player player);

}
