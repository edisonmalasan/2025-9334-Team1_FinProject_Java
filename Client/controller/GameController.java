package controller;

import common.ClientControllerObserver;
import common.game_logic.Game;
import common.referenceClasses.Player;
import common.referenceClasses.ValuesList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import Client.*;
import main.Client;

public class GameController implements ClientControllerObserver {
    private Player player;
    Scanner scanner = new Scanner(System.in);
    private static volatile boolean gameOver = false;
    public GameController(Player player) {
        this.player = player;
    }
    public static Game game = Client.game;

    public void playGame(ValuesList list) {
        AtomicInteger timer = new AtomicInteger(getIntFromList(list));
        String mysteryWord = getStringFromList(list);
        Set<Character> guessedLetters = new HashSet<>();
        final int[] lives = {5};
        gameOver = false;
        Player currentPlayer = new Player(0,"Test123","Tester",0,0,-1,false);;

        // Start 30-second timer
        Thread timerThread = new Thread(() -> {
            for (int i = timer.get(); i != -1; i--){
                timer.set(i);
                System.out.println("Time remaining: " + timer);
                try {
                   Thread.sleep(1000);
                } catch (InterruptedException e) {
                 e.printStackTrace();
                }
            }
            if (!gameOver) {
                System.out.println("Time's up!");
                gameOver = true;
            }
        });

        Thread inputThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            displayWordProgress(mysteryWord, guessedLetters);

            while (!isWordFullyGuessed(mysteryWord, guessedLetters) && lives[0] != 0 && !gameOver) {
                try {
                    if (reader.ready()) {
                        String input = reader.readLine().trim().toLowerCase();

                        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                            System.out.print("⚠️ Please enter a single letter: ");
                            continue;
                        }

                        char letter = input.charAt(0);

                        if (guessedLetters.contains(letter)) {
                            System.out.println("Letter already guessed! Try another one.");
                        }

                        guessedLetters.add(letter);

                        if (!mysteryWord.contains(String.valueOf(letter))) {
                            System.out.println("Wrong guess!");
                            lives[0]--;
                            System.out.println("Guess/es left: " + lives[0]);
                        }

                        displayWordProgress(mysteryWord, guessedLetters);

                        if (isWordFullyGuessed(mysteryWord, guessedLetters)) {
                            System.out.println("You have guessed the word: " + mysteryWord);
                            currentPlayer.time = timer.get();
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException e) {
                    return;
                }
            }
        });

        timerThread.start();
        inputThread.start();

        try {
            timerThread.join();
            inputThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        game.sendTime(currentPlayer);
    }

    @Override
    public void update(ValuesList list) {
        playGame(list);
    }
    public String getStringFromList(ValuesList list) {
        return list.values[0].extract_string();
    }

    public int getIntFromList(ValuesList list) {
        return list.values[1].extract_ulong();
    }

    private static void displayWordProgress(String word, Set<Character> guessedLetters) {
        StringBuilder progress = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessedLetters.contains(c)) {
                progress.append(c);
            } else {
                progress.append("_");
            }
        }
        System.out.println("Word: " + progress.toString().trim());
    }
    private static boolean isWordFullyGuessed(String word, Set<Character> guessedLetters) {
        for (char character : word.toCharArray()) {
            if (!guessedLetters.contains(character)) {
                return false;
            }
        }
        return true;
    }
}
