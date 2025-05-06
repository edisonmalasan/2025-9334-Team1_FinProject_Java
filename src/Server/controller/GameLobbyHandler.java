package Server.controller;

import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import Server.main.GameServer;
import Server.service.PlayerRequestService;
import org.omg.CORBA.*;
import Server.util.WordGenerator;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLobbyHandler {
    public static List<GameLobby> gameLobbies = new ArrayList<>();
    public static List<GameLobby> waitingLobbies = new ArrayList<>();
    public static List<String> previousWords = new ArrayList<>();
    public static boolean checker = false;
    public static String word = "";
    private static ORB orb = GameServer.orb;
    public synchronized static int countdown(GameLobby gameLobby, int time, ClientCallback callback) {
        for (int i = time; i != -1; i--){
            gameLobby.waitingTime = i;
            System.out.println("Waiting time: " + i);
            callback._notify(buildIntList(i));

            try {
                Thread.sleep(1000); // 1000 milliseconds = 1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Player player : gameLobby.players) {
            System.out.println(player.username);
        }
        waitingLobbies.remove(gameLobby);
        return 0;
    }

    public static void startRound(GameLobby gameLobby, ClientCallback callback) {
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
        ValuesList list = buildList(word, gameLobby.gameTime, winner);
        callback._notify(list);
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
