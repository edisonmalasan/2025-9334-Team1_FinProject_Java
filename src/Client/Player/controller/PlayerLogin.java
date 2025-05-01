package Client.Player.controller;

import Client.Player.view.ViewManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class PlayerLogin {

    @FXML
    private Button enterButton;

    @FXML
    private Button registerHyperLink;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameTextField;

    @FXML
    void handleEnter(ActionEvent event) {
        ViewManager.showView("PlayerMainMenu");
    }

    @FXML
    void handleRegisterLink(ActionEvent event) {
        ViewManager.showView("PlayerRegister");
    }

}
