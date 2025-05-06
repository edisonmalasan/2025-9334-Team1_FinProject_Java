package Server.service.impl;

import Server.exception.GameException;
import Server.model.Enum.GameState;
import Server.model.Enum.LetterResult;
import Server.model.GameSession;
import Server.service.GameService;
import Server.util.SessionUtil;
import Server.util.WordGenerator;

public class GameServiceImpl implements GameService {
    private GameDAO gameDao;
    private WordGenerator wordGenerator;
    private SessionUtil sessionUtil;

    @Override
    public GameSession createGame(String playerSessionId) throws GameException {
        return null;
    }

    @Override
    public GameSession joinGame(String playerSessionId, String gameId) throws GameException {
        return null;
    }

    @Override
    public LetterResult submitGuess(String gameId, String playerSessionId, char letter) throws GameException {
        return null;
    }

    @Override
    public GameState getGameState(String gameId) {
        return null;
    }

    @Override
    public void cancelGame(String playerSessionId) {

    }
}
