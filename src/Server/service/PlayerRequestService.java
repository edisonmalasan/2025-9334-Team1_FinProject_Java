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
    public static GameLobby waitingLobby = null;
    public static int waitingTime = 10;
    public static int gameTime = 30;
    private static final Object lobbyLock = new Object();
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
        synchronized (lobbyLock) {
            GameLobbyHandler.addToCallbackList(callback);

            if (waitingLobby == null) {
                // First player starts the lobby and the game loop
                System.out.println("No waiting lobby. Creating new lobby.");

                Player[] players = new Player[] {player};
                waitingLobby = new GameLobby(0, players, waitingTime, gameTime, "", null);
                GameLobbyHandler.gameLobbies.add(waitingLobby);
                GameLobbyHandler.addCountdownCallback(waitingLobby, callback);
                GameLobbyHandler.addToCallbackMap(waitingLobby, callback);

                new Thread(() -> {
                    GameLobbyHandler.countdown(waitingLobby, waitingTime);
                }).start();

            } else {
                GameLobbyHandler.addToCallbackMap(waitingLobby, callback);
                joinGame(player, callback);
            }
        }
    }


    private void joinGame(Player player, ClientCallback callback) {
        GameLobbyHandler.addCountdownCallback(waitingLobby, callback);
        synchronized (lobbyLock) {
            if (waitingLobby == null) return; // Safety check
            Player[] original = waitingLobby.players;
            waitingLobby.players = Stream.concat(Arrays.stream(original), Stream.of(player)).toArray(Player[]::new);
        }

        // Wait until the countdown is finished
        while (waitingLobby != null && waitingLobby.waitingTime > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }
    }


    public static void runGameLobby(GameLobby gameLobby) {
        //gameLobby.waitingTime = GameLobbyHandler.countdown(gameLobby, gameLobby.waitingTime);

        GameLobbyHandler.emptyCallbackList();

        if (gameLobby.players.length == 1) {
            // Not enough players
            for (ClientCallback cb : GameLobbyHandler.getCallbacksForLobby(gameLobby)) {
                cb._notify(GameLobbyHandler.buildList("", -1, "*NOT_ENOUGH_PLAYERS*"));
            }
            synchronized (lobbyLock) {
                waitingLobby = null;
            }
            return;
        }

        synchronized (lobbyLock) {
            waitingLobby = null; // Free up for new lobby
        }

        // ✅ SHARED ROUND LOOP
        while (gameLobby.winner == null) {
            GameLobbyHandler.startRound(gameLobby);
            try {
                Thread.sleep(2000); // Simulate time per round
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        // Game finished
        StringBuilder playerNames = new StringBuilder();
        for (Player p : gameLobby.players) {
            p.hasPlayed = true;
            PlayerDAO.update(p, p.username);
            playerNames.append(p.username).append(", ");
        }

        LobbyDAO.create(playerNames.toString(), gameLobby.winner.username);

        for (ClientCallback cb : GameLobbyHandler.getCallbacksForLobby(gameLobby)) {
            cb._notify(GameLobbyHandler.buildList("", -1, gameLobby.winner.username));
        }

        GameLobbyHandler.emptyCallbackList();
    }

//    public void startGame(Player player, ClientCallback callback) {
//        final GameLobby[] gameLobby = {new GameLobby()};
//        Thread lobbyThread = new Thread(() -> {
//            GameLobbyHandler.addToCallbackList(callback);
//
//            if (waitingLobby == null) {
//                System.out.println("No waiting lobby. Creating new lobby.");
//                Player[] players = new Player[1];
//                players[0] = player;
//                waitingLobby = new GameLobby(0, players, waitingTime, gameTime, "", null);
//
//                gameLobby[0] = waitingLobby;
//
//                GameLobbyHandler.gameLobbies.add(waitingLobby);
//                waitingLobby.waitingTime = GameLobbyHandler.countdown(waitingLobby, waitingLobby.waitingTime);
//                GameLobbyHandler.emptyCallbackList();
//
//                if (waitingLobby.players.length == 1) {
//                    String message = "*NOT_ENOUGH_PLAYERS*";
//                    callback._notify(GameLobbyHandler.buildList("",-1,message));
//                    waitingLobby = null;
//                    return;
//                }
//
//                waitingLobby = null;
//
//            } else {
//                joinGame(player);
//
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException();
//                }
//            }
//
//            while (gameLobby[0].winner == null) {
//                GameLobbyHandler.startRound(gameLobby[0], callback);
//            }
//
//            StringBuilder playerNames = new StringBuilder();
//            for (Player playerInLobby : gameLobby[0].players) {
//                playerInLobby.hasPlayed = true;
//                PlayerDAO.update(playerInLobby, playerInLobby.username);
//                playerNames.append(playerInLobby.username).append(", ");
//            }
//
//            LobbyDAO.create(playerNames.toString(), gameLobby[0].winner.username);
//            callback._notify(GameLobbyHandler.buildList("", -1, gameLobby[0].winner.username));
//
//        });
//        lobbyThread.start();
//    }

//    private void joinGame(Player player) {
//        Player[] original = waitingLobby.players;
//        waitingLobby.players = Stream.concat(Arrays.stream(original), Stream.of(player)).toArray(Player[]::new);
//
//        while (waitingLobby.waitingTime != 0) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException();
//            }
//        }

//        for (GameLobby gameLobby : GameLobbyHandler.gameLobbies) {
//            if (gameLobby.waitingTime != 0) {
//                Player[] original = gameLobby.players;
//                gameLobby.players = Stream.concat(Arrays.stream(original), Stream.of(player)).toArray(Player[]::new);
//                existingLobby = gameLobby;
//            }
//            while (gameLobby.waitingTime != 0) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException();
//                }
//            }
//            break;
//        }

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
