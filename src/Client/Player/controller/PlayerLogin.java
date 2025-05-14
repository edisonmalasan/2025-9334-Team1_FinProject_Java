package Client.Player.controller;

import Client.Admin.controller.AdminViewController;
import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.admin.AdminRequestType;
import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Admin;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import Server.service.AdminRequestService;
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
import java.util.Objects;

public class PlayerLogin implements ClientControllerObserver {
    private Stage stage;

    @FXML
    private Button enterButton;

    @FXML
    private Button registerHyperLink;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField userNameTextField;
    public static Player player = new Player(0,"","",0,0,0,false);
    public static Admin admin = new Admin(0,"", "",0,0);
    private ClientCallback callback = Client.callback;
    private PlayerService playerService = Client.playerService;
    private AdminService adminService = Client.adminService;
    private boolean checker = false;

    public PlayerLogin() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleEnter(ActionEvent event) {
        String username = userNameTextField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Both username and password are required!");
            userNameTextField.setText("");
            passwordField.setText("");

        } else if(userNameTextField.getText().contains("Admin")) {
            admin.username = username;
            admin.password = password;
            checker = true;
            adminService.request(AdminRequestType.ADMIN_LOGIN, admin, callback);
        } else {

            player.username = username;
            player.password = password;

            playerService.request(PlayerRequestType.LOGIN, player, callback);
        }

        userNameTextField.setText("");
        passwordField.setText("");
    }

    @FXML
    void handleRegisterLink(ActionEvent event) throws IOException {
        Client.callbackImpl.removeAllObservers();
        Client.callbackImpl.addObserver(new PlayerRegister());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerRegister.fxml"));
        Parent root = loader.load();
        PlayerRegister registerController = loader.getController();
        registerController.setStage(stage);
        stage.setScene(new Scene(root));
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

    public static String getStringFromList(ValuesList list) {
        return list.values[0].extract_string();
    }

    public void getPlayerFromList(ValuesList list) {
        player.playerId = list.values[1].extract_ulong();
        player.username = list.values[2].extract_string();
        player.password = list.values[3].extract_string();
        player.wins = list.values[4].extract_ulong();
        player.hasPlayed = list.values[5].extract_boolean();
    }

    private void displayUpdate(ValuesList list) {
        String message = getStringFromList(list);
        if (message.equals("UNSUCCESSFUL_LOGIN")) {
            Platform.runLater(() -> showAlert("Login Error!","Account not found!"));
        } else if (message.equals("USER_ALREADY_LOGGED_IN")) {
            Platform.runLater(() -> showAlert("Login Error!","User already logged in!"));
        } else {
            if (checker) {
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Admin/view/AdminView.fxml"));
                        Parent root = loader.load();
                        AdminViewController adminViewController = loader.getController();
                        Client.callbackImpl.removeAllObservers();
                        Client.callbackImpl.addObserver(adminViewController);
                        adminViewController.setStage(stage);
                        stage.setScene(new Scene(root));
                        playerService.request(PlayerRequestType.GET_LEADERBOARD, player, callback);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {

                getPlayerFromList(list);
                Platform.runLater(() -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerMainMenu.fxml"));
                        Parent root = loader.load();
                        PlayerMainMenu playerMainMenuController = loader.getController();
                        playerMainMenuController.setStage(stage);
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
