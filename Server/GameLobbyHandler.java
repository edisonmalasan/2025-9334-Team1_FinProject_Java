import common.client.ClientCallback;
import common.referenceClasses.GameLobby;
import common.referenceClasses.Player;
import common.referenceClasses.ValuesList;
import org.omg.CORBA.*;

import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLobbyHandler {
    public static List<GameLobby> gameLobbies = new ArrayList<>();
    public static List<GameLobby> waitingLobbies = new ArrayList<>();
    public static boolean checker = false;
    public static String word = "";
    private static ORB orb = GameServer.orb;
    public synchronized static void countdown(GameLobby gameLobby, int time) {
        for (int i = time; i != -1; i--){
            gameLobby.waitingTime = i;
            System.out.println("Waiting time: " + i);
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
    }

    public static void startRound(GameLobby gameLobby, ClientCallback callback) {
        Random random = new Random();
        int randomNumber = random.nextInt(16) + 6;
        if (gameLobby.players != null) {  // Checker to ensure that only one word is generated for all players
            word = WordGenerator.generateWord(randomNumber);
            System.out.println("Word to be guessed: " + word);
        }
        ValuesList list = buildList(word);
        callback._notify(list);
    }

    public static ValuesList buildList(Object object) {
        Any[] anyArray = new Any[2];
        Any anyString = orb.create_any();
        Any anyInt = orb.create_any();
        if (object instanceof String) {
            anyString.insert_string((String) object);
        }
        anyInt.insert_ulong(30);

        anyArray[0] = anyString;
        anyArray[1] = anyInt;
        return new ValuesList(anyArray);
    }
}
