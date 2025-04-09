package Server.service;

import Server.model.LeaderboardEntry;

import java.util.List;

public interface LeaderboardService {
    List<LeaderboardEntry> getTopPlayers(int count);
    void recordGameResult(String winnerSessionId, String gameId);
}
