package Server.DataAccessObject;

import Server.model.LeaderboardEntry;

import java.util.List;

public interface LeaderboardDAO {
    void recordWin(String playerId);
    List<LeaderboardEntry> getTopPlayers(int count);
}