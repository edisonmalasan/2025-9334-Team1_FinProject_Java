package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.common.callback.ClientCallbackImpl;
import Client.main.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.security.auth.callback.Callback;
import java.io.IOException;

public class PlayerLogin implements ClientControllerObserver {

    @FXML
    private Button enterButton;

    @FXML
    private Button registerHyperLink;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameTextField;
    private Player player = new Player();
    private ClientCallback callback = Client.callback;
    private PlayerService playerService = Client.playerService;

    public PlayerLogin() {
    }
    @FXML
    void handleEnter(ActionEvent event) {
        userNameTextField.setText("");
        passwordField.setText("");

        String username = userNameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both username and password are required!");
            userNameTextField.setText("");
            passwordField.setText("");
        } else {

            player.username = username;
            player.password = password;

            playerService.request(PlayerRequestType.LOGIN, player, callback);
        }
    }

    @FXML
    void handleRegisterLink(ActionEvent event) {
        ViewManager.showView("PlayerRegister");
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @Override
    public void update(ValuesList list) {
        Thread updateThread = new Thread(() -> {
            displayUpdate(list);
        });
        updateThread.start();
    }

    public String getStringFromList(ValuesList list) {
        return list.values[0].extract_string();
    }

    public void displayUpdate(ValuesList list) {
        String message = getStringFromList(list);
        if (message.equals("UNSUCCESSFUL_LOGIN")) {
            Platform.runLater(() -> showAlert("Login Error!","Account not found!"));
        } else if (message.equals("USER_ALREADY_LOGGED_IN")) {
            Platform.runLater(() -> showAlert("Login Error!","User already logged in!"));
        } else {
            Platform.runLater(() -> ViewManager.showView("PlayerMainMenu"));
        }
    }
}
