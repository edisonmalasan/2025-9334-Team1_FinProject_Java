package Server.DataAccessObject;

import Server.model.GameSession;

import java.util.List;

public interface GameDAO {
    GameSession save(GameSession game);
    GameSession findById(String gameId);
    void update(GameSession game);
    List<GameSession> findWaitingGames();
    void remove(String gameId);
}