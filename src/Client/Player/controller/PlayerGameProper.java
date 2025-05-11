package Client.Player.controller;

import Client.WhatsTheWord.game_logic.Game;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


//Todo revisions
public class PlayerGameProper implements ClientControllerObserver {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

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

    @FXML
    private Label gameMessage;
    private int roundCount = 0;
    private String mysteryWord = "";
    private Set<Character> guessedLetters = new HashSet<>();
    private Player currentPlayer = PlayerLogin.player;
    private int[] lives = {5};
    private AtomicInteger timer = new AtomicInteger();
    private static volatile boolean gameOver = false;
    public static Game game = Client.game;
    public static String winner = "";

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSend(ActionEvent event) {
        gameMessage.setText("");
        String input = letterField.getText().trim().toLowerCase();

        if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {      // Validates user input if more than one character is entered
            gameMessage.setText("Please enter a single letter!");     // Error message
        }

        char letter = input.charAt(0);

        if (guessedLetters.contains(letter)) {      // Validates user input so that repeated letter input is not allowed
            gameMessage.setText("Letter already guessed! Try another one.");        // Error message
        }

        guessedLetters.add(letter);

        if (!mysteryWord.contains(String.valueOf(letter))) {        // Validates input if it is contained in the mystery word
            gameMessage.setText("Wrong guess!");
            lives[0]--;                 // Deducts from the total available guesses
            displayGuesses.setText(String.valueOf(lives[0]));           // Displays the current guesses/lives of player
        }

        if (lives[0] == 0) {
            gameMessage.setText("No more guesses left!");
            sendButton.setDisable(true);
        }

        displayLetter.setText(displayWordProgress(mysteryWord, guessedLetters));       // Displays word progress

        if (isWordFullyGuessed(mysteryWord, guessedLetters)) {      // Checks if word has been guessed
            gameMessage.setText("You have guessed the word: " + mysteryWord);        // Displays message
            sendButton.setDisable(true);
            currentPlayer.time = timer.get();           // Gets the time when the user correctly guessed the word
        }
        letterField.setText("");
    }

    @FXML
    private void handleForfeit(ActionEvent event) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Forfeit");
        confirm.setHeaderText("Are you sure you want to forfeit this round?");
        confirm.setContentText("You will lose this round immediately.");

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                //endRound(false);
            }
        });
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
        winner = getWinnerFromList(list);
        if (!Objects.equals(winner, "")) {
            Platform.runLater(this::switchToResults);
        }

        timer = new AtomicInteger(getIntFromList(list));      // This line gets the round time sent by the server
        mysteryWord = getStringFromList(list);       // This line gets the word to be guessed sent by the server
        gameOver = false;           // Checker if game round is finished
        lives = new int[]{5};
        guessedLetters.clear();
        currentPlayer.time = 10;
        sendButton.setDisable(false);

        Platform.runLater(() -> displayRound.setText("Round " + roundCount));
        roundCount++;

        // Thread that starts a 30-second timer
        Task<Void> timerTask = new Task() {
            @Override
            protected Void call() throws Exception {
                for (int i = timer.get(); i >= 0; i--) {
                    timer.set(i);
                    updateMessage(String.valueOf(i));
                    Thread.sleep(1000);
                }
                return null;
            }

            @Override
            protected void succeeded() {
                if (!gameOver) {
                    gameMessage.setText("Time's up!");
                    gameOver = true;
                }
            }
        };

        Platform.runLater(() -> displayTimer.textProperty().bind(timerTask.messageProperty()));
        Thread timerThread = new Thread(timerTask);

        // New thread for getting input of user
        Thread initialThread = new Thread(() -> {
            Platform.runLater(() -> displayLetter.setText(displayWordProgress(mysteryWord, guessedLetters)));   // Displays initial word progress, will display only underscores
            Platform.runLater(() -> displayGuesses.setText(String.valueOf(lives[0])));
        });

        timerThread.start();        // Starts timer thread
        initialThread.start();        // Starts thread for user input

        try {
            timerThread.join();
            initialThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        game.sendTime(currentPlayer);       // Sends the time to the server, sends -1 if user failed to guess the letter correctly
    }
    @Override
    public void update(ValuesList list) {
        Platform.runLater(() -> displayUsername.setText(currentPlayer.username));
        Platform.runLater(() -> gameMessage.setText("WHAT'S THE WORD?"));
        playGame(list);
    }

    public String getStringFromList(ValuesList list) {
        return list.values[0].extract_string();
    }

    public int getIntFromList(ValuesList list) {
        return list.values[1].extract_ulong();
    }

    public String getWinnerFromList(ValuesList list) {
        return list.values[2].extract_string();
    }

    private static String displayWordProgress(String word, Set<Character> guessedLetters) {
        StringBuilder progress = new StringBuilder();
        for (char c : word.toCharArray()) {
            if (guessedLetters.contains(c)) {
                progress.append(c);
            } else {
                progress.append("_ ");
            }
        }
        return progress.toString().trim();
    }
    private static boolean isWordFullyGuessed(String word, Set<Character> guessedLetters) {
        for (char character : word.toCharArray()) {
            if (!guessedLetters.contains(character)) {
                return false;
            }
        }
        return true;
    }

    private void switchToResults() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerResults.fxml"));
            Parent root = loader.load();
            PlayerResults playerResultsController = loader.getController();
            Client.callbackImpl.removeAllObservers();
            Client.callbackImpl.addObserver(playerResultsController);
            playerResultsController.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
