package Server.service;

import Server.DataAccessObject.PlayerDAO;
import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.client.player.PlayerRequestType;
import Server.WhatsTheWord.client.player.PlayerServicePOA;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.controller.GameLobbyHandler;
import Server.util.PasswordHashUtility;
import org.omg.CORBA.ORB;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

public class PlayerRequestService extends PlayerServicePOA {
    private PlayerDAO playerDao;

    public PlayerRequestService() {
    }
    public PlayerRequestService(PlayerDAO playerDAO) {
        this.playerDao = playerDAO;
    }
    @Override
    public void request(PlayerRequestType type, Player player, ClientCallback playerCallback) {
        if (type.equals(PlayerRequestType.REGISTER)) {
            register(player, playerCallback);
        } else if (type.equals(PlayerRequestType.LOGIN)) {
            login(player, playerCallback);
        } else if (type.equals(PlayerRequestType.START_GAME)) {
            startGame(player, playerCallback);
        } else if (type.equals(PlayerRequestType.GET_LEADERBOARD)) {
            getLeaderboard();
        }
    }

    public void register(Player player, ClientCallback callback) {
        PlayerDAO.create(player);
    }

    public void login(Player player, ClientCallback callback) {
        player = PlayerDAO.findByUsername(player.username);
    }

    public void startGame(Player player, ClientCallback callback) {
        System.out.println(player.username + " has requested to start a game.");
        GameLobby gameLobby = new GameLobby();
        if (GameLobbyHandler.waitingLobbies.isEmpty()) {
            System.out.println("No waiting lobbies. Creating new lobby.");
            Player[] players = new Player[1];
            players[0] = player;
            GameLobby newGameLobby = new GameLobby(0, players, 10, 30, "", null);
            GameLobbyHandler.gameLobbies.add(newGameLobby);
            GameLobbyHandler.waitingLobbies.add(newGameLobby);
            gameLobby = newGameLobby;

            gameLobby.waitingTime = GameLobbyHandler.countdown(gameLobby, gameLobby.waitingTime);

        } else {
            joinGame(player);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
        while (gameLobby.winner == null) {
            GameLobbyHandler.startRound(gameLobby, callback);
        }
        System.out.println(gameLobby.winner.username);
    }

    private void joinGame(Player player) {
        for (GameLobby gameLobby : GameLobbyHandler.gameLobbies) {
            if (gameLobby.waitingTime != 0) {
                Player[] original = gameLobby.players;
                gameLobby.players = Stream.concat(Arrays.stream(original), Stream.of(player)).toArray(Player[]::new);
            }
            while (gameLobby.waitingTime != 0) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }
            break;
        }
    }
    public void getLeaderboard() {

    }

    public String generateSessionToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

}
