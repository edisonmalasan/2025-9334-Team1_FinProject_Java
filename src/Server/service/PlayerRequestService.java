package Server.service;

import Server.DataAccessObject.LobbyDAO;
import Server.DataAccessObject.PlayerDAO;
import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.client.player.PlayerRequestType;
import Server.WhatsTheWord.client.player.PlayerServicePOA;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import Server.controller.GameLobbyHandler;
import Server.main.GameServer;
import Server.util.LogManager;
import Server.util.PasswordHashUtility;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Stream;

public class PlayerRequestService extends PlayerServicePOA {
    private static LogManager logManager = new LogManager();
    private static ORB orb = GameServer.orb;
    private static ValuesList list = new ValuesList();
    private static List<String> loggedInUsers = new ArrayList<>();
    public static int waitingTime = 10;
    public static int gameTime = 30;
    public PlayerRequestService() {
    }
    @Override
    public void request(PlayerRequestType type, Player player, ClientCallback playerCallback) {
        if (type.equals(PlayerRequestType.REGISTER)) {
            logManager.logMessage("Registered new player: " + player.username);
            register(player, playerCallback);
        } else if (type.equals(PlayerRequestType.LOGIN)) {
            logManager.logMessage(player.username + " logged in.");
            login(player, playerCallback);
        } else if (type.equals(PlayerRequestType.START_GAME)) {
            logManager.logMessage(player.username + " requested to start game.");
            startGame(player, playerCallback);
        } else if (type.equals(PlayerRequestType.GET_LEADERBOARD)) {
            logManager.logMessage(player.username + " loaded leaderboards.");
            getLeaderboard(playerCallback);
        } else if (type.equals(PlayerRequestType.LOGOUT)) {
            logManager.logMessage(player.username + " logged out.");
            logout(player);
        }
    }

    public void register(Player player, ClientCallback callback) {
        Player playerInList = PlayerDAO.findByUsername(player.username);
        if (!Objects.equals(playerInList.username, null)) {
            list = buildList("USERNAME_ALREADY_EXISTS");
            callback._notify(list);
        } else {
            PlayerDAO.create(player);
            list = buildList("SUCCESSFUL_LOGIN");
            callback._notify(list);
        }
    }

    public void login(Player player, ClientCallback callback) {
        player = PlayerDAO.findByUsername(player.username);
        if (player.username == null) {
            list = buildList("UNSUCCESSFUL_LOGIN");
            callback._notify(list);
        } else if (loggedInUsers.contains(player.username)) {
            list = buildList("USER_ALREADY_LOGGED_IN");
            callback._notify(list);
        } else {
            loggedInUsers.add(player.username);
            list = addPlayerToList(buildList("SUCCESSFUL_LOGIN"), player);
            callback._notify(list);
        }

    }

    public void startGame(Player player, ClientCallback callback) {
        Thread lobbyThread = new Thread(() -> {
            System.out.println(player.username + " has requested to start a game.");
            GameLobby gameLobby = new GameLobby();
            GameLobbyHandler.addToCallbackList(callback);
            if (GameLobbyHandler.waitingLobbies.isEmpty()) {
                System.out.println("No waiting lobbies. Creating new lobby.");
                Player[] players = new Player[1];
                players[0] = player;
                GameLobby newGameLobby = new GameLobby(0, players, waitingTime, gameTime, "", null);
                GameLobbyHandler.gameLobbies.add(newGameLobby);
                GameLobbyHandler.waitingLobbies.add(newGameLobby);
                gameLobby = newGameLobby;
                gameLobby.waitingTime = GameLobbyHandler.countdown(gameLobby, gameLobby.waitingTime);

                if (gameLobby.players.length == 1) {
                    String message = "*NOT_ENOUGH_PLAYERS*";
                    callback._notify(GameLobbyHandler.buildList("",-1,message));
                }

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

            StringBuilder playerNames = new StringBuilder();
            for (Player playerInLobby : gameLobby.players) {
                playerInLobby.hasPlayed = true;
                PlayerDAO.update(playerInLobby, playerInLobby.username);
                playerNames.append(playerInLobby.username).append(", ");
            }

            LobbyDAO.create(playerNames.toString(), gameLobby.winner.username);
            callback._notify(GameLobbyHandler.buildList("", -1, gameLobby.winner.username));

        });
        lobbyThread.start();
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
    public void getLeaderboard(ClientCallback callback) {
        List<Player> playerList = PlayerDAO.findAllPlayers();
        list = encodePlayers(playerList);
        callback._notify(list);
    }

    public static ValuesList encodePlayers(List<Player> players) {
        Any[] result = new Any[players.size()];

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);

            // Inner Any[]
            Any[] playerData = new Any[2];

            playerData[0] = orb.create_any();
            playerData[0].insert_string(player.username);

            playerData[1] = orb.create_any();
            playerData[1].insert_long(player.wins);

            // Serialize to output stream
            org.omg.CORBA.portable.OutputStream out = orb.create_output_stream();
            out.write_long(playerData.length);
            for (Any a : playerData) {
                out.write_any(a);
            }

            // Wrap into outer Any
            org.omg.CORBA.portable.InputStream in = out.create_input_stream();
            Any outerAny = orb.create_any();
            TypeCode sequenceOfAny = orb.create_sequence_tc(0, orb.get_primitive_tc(TCKind.tk_any));
            outerAny.read_value(in, sequenceOfAny);

            result[i] = outerAny;
        }

        return new ValuesList(result);
    }
    public String generateSessionToken() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public ValuesList buildList(Object object) {
        Any[] anyArray = new Any[1];
        Any anyString = orb.create_any();
        if (object instanceof String) {
            anyString.insert_string((String) object);
        }
        anyArray[0] = anyString;
        return new ValuesList(anyArray);
    }

    public static ValuesList addPlayerToList(ValuesList list, Player player) {
        Any[] anyArray = new Any[6];
        Any message = list.values[0];
        Any playerID = orb.create_any();
        Any username = orb.create_any();
        Any password = orb.create_any();
        Any wins = orb.create_any();
        Any hasPlayed = orb.create_any();

        playerID.insert_ulong(player.playerId);
        username.insert_string(player.username);
        password.insert_string(player.password);
        wins.insert_ulong(player.wins);
        hasPlayed.insert_boolean(player.hasPlayed);

        anyArray[0] = message;
        anyArray[1] = playerID;
        anyArray[2] = username;
        anyArray[3] = password;
        anyArray[4] = wins;
        anyArray[5] = hasPlayed;

        return new ValuesList(anyArray);
    }

    private void logout(Player player) {
        loggedInUsers.remove(player.username);
    }

}
