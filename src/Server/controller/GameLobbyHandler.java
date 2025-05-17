package Server.controller;

import Server.DataAccessObject.LobbyDAO;
import Server.DataAccessObject.PlayerDAO;
import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import Server.main.GameServer;
import Server.service.PlayerRequestService;
import org.omg.CORBA.*;
import Server.util.WordGenerator;

import java.lang.Object;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static Server.service.PlayerRequestService.runGameLobby;

public class GameLobbyHandler {
    public static Map<GameLobby, List<ClientCallback>> gameLobbyListMap = new ConcurrentHashMap<>();
    private static final Map<GameLobby, List<ClientCallback>> countdownCallbackMap = new ConcurrentHashMap<>();

    public static List<GameLobby> gameLobbies = new ArrayList<>();
    public static List<GameLobby> waitingLobbies = new ArrayList<>();
    public static List<ClientCallback> countdownCallbacks = new ArrayList<>();
    public static List<String> previousWords = new ArrayList<>();
    public static boolean checker = false;
    public static String word = "";
    private static ORB orb = GameServer.orb;
    public static void countdown(GameLobby gameLobby, int time) {
        for (int i = time; i >= 0; i--) {
            gameLobby.waitingTime = i;

            int finalI = i;
            getCountdownCallbacks(gameLobby).parallelStream().forEach(callback -> {
                try {
                    callback._notify(buildIntList(finalI));
                } catch (Exception e) {
                    System.err.println("Countdown notify failed: " + e.getMessage());
                }
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        removeCountdownCallbacks(gameLobby);

        if (gameLobby.players.length < 2) {
            getCallbacksForLobby(gameLobby).forEach(callback -> {
                try {
                    String message = "*NOT_ENOUGH_PLAYERS*";
                    callback._notify(GameLobbyHandler.buildList("",-1,message));
                    PlayerRequestService.waitingLobby = null;
                } catch (Exception e) {}
            });
            return;
        }

        // Start game now that countdown is over
        new Thread(() -> runGameLobby(gameLobby)).start();
    }


    public static void startRound(GameLobby gameLobby) {
        Random random = new Random();
        int randomNumber = random.nextInt(16) + 6;

        // Checker to ensure that only one word is generated for all players
        if (gameLobby.players != null) {
            word = WordGenerator.generateWord(randomNumber);

            // Check if word has already been used
            while (previousWords.contains(word))
                word = WordGenerator.generateWord(randomNumber);

            System.out.println("Word to be guessed: " + word);
        }
        String winner = "";
        if (gameLobby.winner != null) {
            winner = gameLobby.winner.username;
        }
        // Build list and send to clients via callback
        int gameTime = PlayerRequestService.gameTime;
        ValuesList list = buildList(word, gameTime, winner);

        getCallbacksForLobby(gameLobby).parallelStream().forEach(callback -> {
            try {
                callback._notify(list);
            } catch (Exception e) {
                System.err.println("Failed to notify client: " + e.getMessage());
            }
        });
//
//        for (ClientCallback callback : callbacks)
//            callback._notify(list);
    }

    public static void endGameLobby(GameLobby lobby) {
        System.out.println("Ending game lobby for winner: " + (lobby.winner != null ? lobby.winner.username : "unknown"));

        // Notify players that game has ended
        List<ClientCallback> callbacks = getCallbacksForLobby(lobby);
        callbacks.parallelStream().forEach(cb -> {
            try {
                StringBuilder playerNames = new StringBuilder();
                for (Player p : lobby.players) {
                    p.hasPlayed = true;
                    PlayerDAO.update(p, p.username);
                    playerNames.append(p.username).append(", ");
                }

                LobbyDAO.create(playerNames.toString(), lobby.winner.username);
                cb._notify(GameLobbyHandler.buildList("", -1, lobby.winner.username));
            } catch (Exception e) {
                System.err.println("Failed to notify: " + e.getMessage());
            }
        });

        // Clear players
        lobby.players = new Player[0];

        // Remove from active lists
        gameLobbies.remove(lobby);
        gameLobbyListMap.remove(lobby); // optional if you're storing callbacks this way
    }

    public static void addToCallbackMap(GameLobby lobby, ClientCallback callback) {
        gameLobbyListMap.computeIfAbsent(lobby, k -> Collections.synchronizedList(new ArrayList<>())).add(callback);
    }

    public static List<ClientCallback> getCallbacksForLobby(GameLobby lobby) {
        return gameLobbyListMap.getOrDefault(lobby, Collections.emptyList());
    }
    public static void addToCallbackList(ClientCallback callback) {
        countdownCallbacks.add(callback);
    }

    public static void emptyCallbackList() {
        countdownCallbacks.clear();
    }

    public static void addCountdownCallback(GameLobby lobby, ClientCallback cb) {
        countdownCallbackMap
                .computeIfAbsent(lobby, k -> Collections.synchronizedList(new ArrayList<>()))
                .add(cb);
    }

    public static List<ClientCallback> getCountdownCallbacks(GameLobby lobby) {
        return countdownCallbackMap.getOrDefault(lobby, Collections.emptyList());
    }

    public static void removeCountdownCallbacks(GameLobby lobby) {
        countdownCallbackMap.remove(lobby);
    }
    public static ValuesList buildList(String word, int waitingTime, String winner) {
        Any[] anyArray = new Any[3];
        Any wordString = orb.create_any();
        Any timeInt = orb.create_any();
        Any winnerString = orb.create_any();

        wordString.insert_string(word);
        timeInt.insert_ulong(waitingTime);
        winnerString.insert_string(winner);

        anyArray[0] = wordString;
        anyArray[1] = timeInt;
        anyArray[2] = winnerString;

        return new ValuesList(anyArray);
    }

    public static ValuesList buildIntList(Object object) {
        Any[] intArray = new Any[1];
        Any anyInt = orb.create_any();
        anyInt.insert_ulong((int) object);
        intArray[0] = anyInt;
        return new ValuesList(intArray);
    }
}
