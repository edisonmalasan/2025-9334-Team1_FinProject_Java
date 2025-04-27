package Server.controller;

import Server.WhatsTheWord.game_logic.GamePOA;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameImpl extends GamePOA {
    public static List<GameLobby> gameLobbyList = GameLobbyHandler.gameLobbies;
    private static char letter = 'a';
    @Override
    public void sendLetter(char letter) {
        if (GameImpl.letter == letter) {
            System.out.println("Correct!");
        } else {
            System.out.println("Wrong!");
        }
    }

    @Override
    public void endLobby() {

    }

    @Override
    public void sendTime(Player player) {
        for (GameLobby gameLobby : gameLobbyList) {
            for (Player playerInLobby : gameLobby.players) {
                if (player.username.equals(playerInLobby.username)) {
                    playerInLobby.time = player.time;
                    break;
                }
            }
            gameLobby.players = setScores(gameLobby);
            gameLobby.winner = checkForWinner(gameLobby);
            System.out.println(player.username + ": " + player.time + ": " + player.noOfRoundWins); //Testing

            if (gameLobby.winner != null) {
                for (Player playerInLobby : gameLobby.players) {
                    playerInLobby.noOfRoundWins = 0;
                    playerInLobby.time = 0;
                }
                break;
            }
        }
    }

    public Player[] setScores(GameLobby gameLobby) {
        for (Player player : gameLobby.players) {
            if (player.time == 0) {
                return gameLobby.players;
            }
        }
        List<Player> sortedByTime = Arrays.stream(gameLobby.players)
                .sorted(Comparator.comparingInt((Player p) -> p.time).reversed())
                .collect(Collectors.toList());
        if (sortedByTime.get(0).time != -1)
            sortedByTime.get(0).noOfRoundWins += 1;
        System.out.println(sortedByTime.get(0).username + ": " + sortedByTime.get(0).time);
        sortedByTime = resetTime(sortedByTime);
        return sortedByTime.toArray(new Player[0]);
    }

    public List<Player> resetTime(List<Player> players) {
        for (Player player : players) {
            player.time = 0;
        }
        return players;
    }

    public Player checkForWinner(GameLobby gameLobby) {
        for (Player player : gameLobby.players) {
            if (player.noOfRoundWins == 3) {
                player.wins += 1;
                return player;
            }
        }
        return null;
    }
}
