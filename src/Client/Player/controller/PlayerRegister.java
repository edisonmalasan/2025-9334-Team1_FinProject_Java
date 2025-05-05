package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import Server.DataAccessObject.PlayerDAO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerRegister implements ClientControllerObserver {
    private Stage stage;

    @FXML
    private Button backHyperLink;

    @FXML
    private Button enterButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameTextField;
    private PlayerService playerService = Client.playerService;

    public void initialize() {
        backHyperLink.setOnAction(event -> {
            try {
                handleBackLink(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void handleBackLink(ActionEvent event) throws IOException {
        Client.callbackImpl.removeAllObservers();
        Client.callbackImpl.addObserver(new PlayerLogin());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLogin.fxml"));
        Parent root = loader.load();
        PlayerLogin loginController = loader.getController();
        loginController.setStage(stage);
        stage.setScene(new Scene(root));
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
        }

        usernameTextField.setText("");
        passwordField.setText("");
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
        displayUpdate(list);
    }
    private void displayUpdate(ValuesList list) {
        String message = getStringFromList(list);
        if (message.equals("USERNAME_ALREADY_EXISTS")) {
            Platform.runLater(() -> showAlert("Register Error!","Username already exists!"));
        } else {
            Platform.runLater(() -> showAlert("Success", "Registration Successful!"));
        }
    }
    public String getStringFromList(ValuesList list) {
        return list.values[0].extract_string();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    // /Client/Player/view/
}
