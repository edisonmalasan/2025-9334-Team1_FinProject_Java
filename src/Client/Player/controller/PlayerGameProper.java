package Client.Player.controller;

import Client.WhatsTheWord.game_logic.Game;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


//Todo revisions
public class PlayerGameProper implements ClientControllerObserver {

    @FXML
    private Label displayGuesses;

    @FXML
    private Label displayLetter;

    @FXML
    private ImageView displayPfp;

    @FXML
    private Label displayRound;

    @FXML
    private Label displayTimer;

    @FXML
    private Label displayUsername;

    @FXML
    private Button forfeitButton;

    @FXML
    private TextField letterField;

    @FXML
    private HBox lettersHbox;

    @FXML
    private Button sendButton;

    private GameController gameController;
    private Set<Character> guessedLetters = new HashSet<>();

    // Game state variables
    private int remainingGuesses = 5;
    private int roundTime = 30; // seconds
    private Timeline timer;
    private String currentWord = "";
    private int currentRound = 1;
    private int totalRounds = 3;
    private List<String> wordList = new ArrayList<>();

//    public void initialize(){
//    gameController = new GameController();
//    //LoadWord();
//    //setupNewRound();
//
//    }

    private Player player;
    private static volatile boolean gameOver = false;
    public static Game game = Client.game;
//    public void LoadWord(){
//        try (InputStream inputStream = getClass().getResourceAsStream("/resources/words.txt");
//             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                wordList.add(line.trim().toUpperCase());
//            }
//            Collections.shuffle(wordList);
//        } catch (IOException e) {
//            showAlert("Warning", "Could not load word list, using default words", Alert.AlertType.WARNING);
//        }
//    }
    private void setupNewRound() {
        if (currentRound > totalRounds) {
            endRound(true);
            return;
        }

        // Reset round state
        remainingGuesses = 5;
        roundTime = 30;
        guessedLetters.clear();
        displayGuesses.setText(String.valueOf(remainingGuesses));
        displayRound.setText("Round " + currentRound + "/" + totalRounds);

        // Get new word from list
        if (wordList.isEmpty()) {
            showGameOver();
            return;
        }

        currentWord = wordList.remove(0);
        displayLetter.setText(currentWord.substring(0, 1));
        startTimer();
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop();
        }

        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            roundTime--;
            updateTimerDisplay();

            if (roundTime <= 0) {
                endRound(false);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    private void updateTimerDisplay() {
        displayTimer.setText(String.format("0:%02d", roundTime));
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSend(ActionEvent event) {
        String input = letterField.getText().toUpperCase();

        // Validate input
        if (input.isEmpty() || !input.matches("[A-Z]")) {
            showAlert("Invalid Input", "Please enter a single letter (A-Z)", Alert.AlertType.WARNING);
            return;
        }

        char guess = input.charAt(0);

        if (guessedLetters.contains(guess)) {
            showAlert("Already Guessed", "You've already guessed this letter!", Alert.AlertType.WARNING);
            return;
        }

        guessedLetters.add(guess);

        if (currentWord.contains(String.valueOf(guess))) {
            // Update display to show next letter
            for (int i = 0; i < currentWord.length(); i++) {
                if (!guessedLetters.contains(currentWord.charAt(i))) {
                    displayLetter.setText(String.valueOf(currentWord.charAt(i)));
                    break;
                }
            }

            // Check if all letters guessed
            boolean allGuessed = true;
            for (char c : currentWord.toCharArray()) {
                if (!guessedLetters.contains(c)) {
                    allGuessed = false;
                    break;
                }
            }

            if (allGuessed) {
                endRound(true);
            }
        } else {
            remainingGuesses--;
            displayGuesses.setText(String.valueOf(remainingGuesses));

            if (remainingGuesses <= 0) {
                endRound(false);
            }
        }

        letterField.clear();
    }

    @FXML
    private void handleForfeit(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Forfeit");
        confirm.setHeaderText("Are you sure you want to forfeit this round?");
        confirm.setContentText("You will lose this round immediately.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                endRound(false);
            }
        });
    }

    private void endRound(boolean won) {
        timer.stop();

        if (won) {
            showAlert("Round Won", "Congratulations! You guessed: " + currentWord,
                    Alert.AlertType.INFORMATION);
        } else {
            showAlert("Round Lost", "The word was: " + currentWord,
                    Alert.AlertType.WARNING);
        }

        currentRound++;
        setupNewRound();
    }

    private void showGameOver() {
        Alert gameOver = new Alert(Alert.AlertType.INFORMATION);
        gameOver.setTitle("Game Over");
        gameOver.setHeaderText("All rounds completed!");
        gameOver.setContentText("Thank you for playing!");
        gameOver.showAndWait();

        // Disable game controls
        sendButton.setDisable(true);
        forfeitButton.setDisable(true);
        letterField.setDisable(true);
    }
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
                Platform.runLater(() -> displayTimer.setText(timer.toString()));
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
                        String input = letterField.getText();

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
