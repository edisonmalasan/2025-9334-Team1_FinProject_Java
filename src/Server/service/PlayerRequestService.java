package Server.service;

import common.client.ClientCallback;
import common.client.player.PlayerRequestType;
import common.client.player.PlayerServicePOA;
import common.referenceClasses.GameLobby;
import common.referenceClasses.Player;
import Server.controller.GameLobbyHandler;
import org.omg.CORBA.ORB;

import java.util.Arrays;
import java.util.stream.Stream;

public class PlayerRequestService extends PlayerServicePOA {
    @Override
    public void request(PlayerRequestType type, Player player, ClientCallback callback) {
        if (type.equals(PlayerRequestType.REGISTER)) {
            register();
        } else if (type.equals(PlayerRequestType.LOGIN)) {
            login();
        } else if (type.equals(PlayerRequestType.START_GAME)) {
            startGame(player, callback);
        } else if (type.equals(PlayerRequestType.GET_LEADERBOARD)) {
            getLeaderboard();
        }
    }

    public void register() {

    }

    public void login() {

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
            while (newGameLobby.waitingTime!=0) {
//                System.out.println("Waiting for players...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            joinGame(player);
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
            if (gameLobby.waitingTime == 10) {
                GameLobbyHandler.countdown(gameLobby, gameLobby.waitingTime);
                break;
            }
        }

    }
    public void getLeaderboard() {

    }
}
