package Client.Player.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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


//Todo revisions
public class PlayerGameProper {

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

    public void initialize(){
    gameController = new GameController();
    LoadWord();
    setupNewRound();

    }


    public void LoadWord(){
        try (InputStream inputStream = getClass().getResourceAsStream("/resources/words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim().toUpperCase());
            }
            Collections.shuffle(wordList);
        } catch (IOException e) {
            showAlert("Warning", "Could not load word list, using default words", Alert.AlertType.WARNING);
        }
    }

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
}
