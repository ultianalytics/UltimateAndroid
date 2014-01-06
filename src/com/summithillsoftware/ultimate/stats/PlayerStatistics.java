package com.summithillsoftware.ultimate.stats;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Point;

public class PlayerStatistics {
	private static Comparator<PlayerStat> playerStatComparator = new Comparator<PlayerStat>() {
		public int compare(final PlayerStat playerStat1, final PlayerStat playerStat2) {
			return Float.compare(playerStat2.sortValue(), playerStat1.sortValue());
		}
	};
	
	
	
	private static List<PlayerStat> descendingSortedStats(List<PlayerStat> stats) {
		Collections.sort(stats, playerStatComparator);
		return stats;
	}
	
	private static Map<String, PlayerStat> accumulateStatsPerPlayer(Game game, StatsAccumulator accumulator) {
		Map<String, PlayerStat> statsPerPlayer = new HashMap<String, PlayerStat>();
		accumulateStatsPerPlayer(game, statsPerPlayer, accumulator);
		return statsPerPlayer;
	}
	
	private static void accumulateStatsPerPlayer(Game game, Map<String, PlayerStat> statsPerPlayer, StatsAccumulator accumulator) {
		// Accumulate stats in a statsPerPlayer.  The accumulator is called for each event.
		StatsEventDetails eventDetails = new StatsEventDetails();
		eventDetails.setAccumulatedStats(statsPerPlayer);
		eventDetails.setGame(game);
		for (Point point : game.getPoints()) {
			boolean firstEvent = true;
			eventDetails.setPoint(point);
			eventDetails.setLine(point.getLine());
			eventDetails.setOlinePoint(game.isPointOline(point));
			for (Event event : point.getEvents()) {
				eventDetails.setEvent(event);
				eventDetails.setFirstEventOfPoint(firstEvent);
				accumulator.updateStats(eventDetails);
				firstEvent = false;
			}
		}
	}

}
