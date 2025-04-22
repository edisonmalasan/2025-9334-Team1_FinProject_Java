package Server.service;

import Server.exception.GameException;
import Server.model.Enum.GameState;
import Server.model.Enum.LetterResult;
import Server.model.GameSession;

public interface GameService {
    GameSession createGame(String playerSessionId) throws GameException;
    GameSession joinGame(String playerSessionId, String gameId) throws GameException;
    LetterResult submitGuess(String gameId, String playerSessionId, char letter) throws GameException;
    GameState getGameState(String gameId);
    void cancelGame(String playerSessionId);
}
