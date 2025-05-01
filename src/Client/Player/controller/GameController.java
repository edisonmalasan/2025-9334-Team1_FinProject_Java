package Client.Player.controller;

import Client.WhatsTheWord.game_logic.Game;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import Client.main.Client;

public class GameController implements ClientControllerObserver {
    private Player player;
    private static volatile boolean gameOver = false;
    public GameController() {
    }
    public static Game game = Client.game;

    public void playGame(ValuesList list) {
        AtomicInteger timer = new AtomicInteger(getIntFromList(list));      // This line gets the round time sent by the server
        String mysteryWord = getStringFromList(list);       // This line gets the word to be guessed sent by the server
        Set<Character> guessedLetters = new HashSet<>();     // For ensuring that repeated letters are not allowed
        final int[] lives = {5};        // Total guesses/lives of user
        gameOver = false;           // Checker if game round is finished
        Player currentPlayer = new Player(0,"Test","Tester",0,0,-1,false);      // Initializes a player, to be connected to other controllers (i.e. PlayerLogin)

        // Thread that starts a 30-second timer
        Thread timerThread = new Thread(() -> {
            for (int i = timer.get(); i != -1; i--){
                timer.set(i);
                System.out.println("Time remaining: " + timer); // This line displays the current time
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

        // New thread for getting input of user
        Thread inputThread = new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            displayWordProgress(mysteryWord, guessedLetters);   // Displays initial word progress, will display only underscores

            while (!isWordFullyGuessed(mysteryWord, guessedLetters) && lives[0] != 0 && !gameOver) {
                try {
                    if (reader.ready()) {
                        String input = reader.readLine().trim().toLowerCase();

                        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {      // Validates user input if more than one character is entered
                            System.out.print("Please enter a single letter: ");     // Error message
                            continue;
                        }

                        char letter = input.charAt(0);      // Gets user input

                        if (guessedLetters.contains(letter)) {      // Validates user input so that repeated letter input is not allowed
                            System.out.println("Letter already guessed! Try another one.");        // Error message
                        }

                        guessedLetters.add(letter);

                        if (!mysteryWord.contains(String.valueOf(letter))) {        // Validates input if it is contained in the mystery word
                            System.out.println("Wrong guess!");
                            lives[0]--;                 // Deducts from the total available guesses
                            System.out.println("Guess/es left: " + lives[0]);           // Displays the current guesses/lives of player
                        }

                        displayWordProgress(mysteryWord, guessedLetters);       // Displays word progress

                        if (isWordFullyGuessed(mysteryWord, guessedLetters)) {      // Checks if word has been guessed
                            System.out.println("You have guessed the word: " + mysteryWord);        // Displays message
                            currentPlayer.time = timer.get();           // Gets the time when the user correctly guessed the word
                        }
                    } else {
                        Thread.sleep(100);
                    }
                } catch (IOException | InterruptedException e) {
                    return;
                }
            }
        });

        timerThread.start();        // Starts timer thread
        inputThread.start();        // Starts thread for user input

        try {
            timerThread.join();
            inputThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        game.sendTime(currentPlayer);       // Sends the time to the server, sends -1 if user failed to guess the letter correctly
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
