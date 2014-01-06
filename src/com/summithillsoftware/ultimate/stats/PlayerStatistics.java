package com.summithillsoftware.ultimate.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.summithillsoftware.ultimate.model.Event;
import com.summithillsoftware.ultimate.model.Game;
import com.summithillsoftware.ultimate.model.Player;
import com.summithillsoftware.ultimate.model.Point;
import com.summithillsoftware.ultimate.model.Team;

public class PlayerStatistics {
	private static Comparator<PlayerStat> playerStatComparator = new Comparator<PlayerStat>() {
		public int compare(final PlayerStat playerStat1, final PlayerStat playerStat2) {
			return Float.compare(playerStat2.sortValue(), playerStat1.sortValue());
		}
	};
	
	private static List<PlayerStat> accumulateStatsPerPlayer(Game game, boolean includeTournament, StatsAccumulator statsAccumulator, StatNumericType type) {
		Map<String, PlayerStat> statPerPlayer = new HashMap<String, PlayerStat>();
		if (includeTournament && game.getTournamentName() != null && !game.getTournamentName().isEmpty()) {
			String tournament = game.getTournamentName();
			String currentTeamId = Team.current().getTeamId();
			List<String> gameFilesForCurrentTeam = Game.getAllGameFileNames(currentTeamId);
			for (String gameFileId : gameFilesForCurrentTeam) {
				Game anotherGame = Game.read(gameFileId);
				if (anotherGame.getTournamentName().equalsIgnoreCase(tournament)) {
					accumulateStatsPerPlayer(anotherGame, statPerPlayer, statsAccumulator);
				}
			}
		} else {
			accumulateStatsPerPlayer(game, statPerPlayer, statsAccumulator);
		}
		return sortedPlayerStats(statPerPlayer, game, type);
	}
	
	private static List<PlayerStat> sortedPlayerStats(Map<String, PlayerStat> statPerPlayer, Game game, StatNumericType type) {
		// find all of the players
		Set<Player> players = new HashSet<Player>(game.getPlayers());
		players.addAll(Team.current().getPlayers());
		
		// build a list of PlayerStat for every player
		List<PlayerStat> playerStats = new ArrayList<PlayerStat>();
		for (Player player : players) {
			getStatForPlayer(player, statPerPlayer, type);
		}

		// sort them
		return descendingSortedStats(playerStats);
	}
	
	private static PlayerStat getStatForPlayer(Player player, Map<String, PlayerStat> statsPerPlayer, StatNumericType type) {
		PlayerStat playerStat = statsPerPlayer.get(player.getId());
		if (playerStat == null) {
			playerStat = type == StatNumericType.Integer ? new PlayerStat(player, 0) : new PlayerStat(player, 0f);
			statsPerPlayer.put(player.getId(), playerStat);
		}
		return playerStat;
	}
	
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
