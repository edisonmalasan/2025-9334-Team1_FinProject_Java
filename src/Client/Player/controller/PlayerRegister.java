package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import Server.DataAccessObject.PlayerDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PlayerRegister implements ClientControllerObserver {

    @FXML
    private Button backHyperLink;

    @FXML
    private Button enterButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;
    private PlayerService playerService = Client.playerService;
    @FXML
    void handleBackLink(ActionEvent event) {
        ViewManager.showView("PlayerLogin");
    }

    @FXML
    void handleEnter(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        // Simple validation to check if username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both username and password are required!");
        } else {
            Player newPlayer= new Player(0, username, password, 0, 0, 0, false);
            playerService.request(PlayerRequestType.REGISTER, newPlayer, Client.callback);
            showAlert("Success", "Registration Successful!");
        }
    }

    // Helper method to show an alert pop-up
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update(ValuesList list) {

    }
}
