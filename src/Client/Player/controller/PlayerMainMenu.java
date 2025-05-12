package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.main.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerMainMenu {
    private Stage stage;
    @FXML
    private Button leaderBoardButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button startGameButton;
    private Player player = PlayerLogin.player;
    private PlayerService playerService = Client.playerService;
    private ClientCallback callback = Client.callback;

    public void initialize(){
        logoutButton.setOnAction(this::handleLogout);
        leaderBoardButton.setOnAction(event1 -> {
            try {
                handleLeaderboard(event1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        startGameButton.setOnAction(event -> {
            try {
                handleStartGame(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleLeaderboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLeaderboard.fxml"));
        Parent root = loader.load();
        PlayerLeaderboard leaderboardController = loader.getController();
        Client.callbackImpl.removeAllObservers();
        Client.callbackImpl.addObserver(leaderboardController);
        leaderboardController.setStage(stage);
        stage.setScene(new Scene(root));
        playerService.request(PlayerRequestType.GET_LEADERBOARD, player, callback);
    }


    @FXML
    void handleLogout(ActionEvent event) {
        try {
            // Send logout request to server
            playerService.request(PlayerRequestType.LOGOUT, player, callback);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLogin.fxml"));
            Parent root = loader.load();

            PlayerLogin loginController = loader.getController();
            loginController.setStage(stage);
            this.player = null;

            stage.setScene(new Scene(root));
            stage.setTitle("Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleStartGame(ActionEvent event) throws IOException {
        Client.callbackImpl.removeAllObservers();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerMatchMaking.fxml"));
        Parent root = loader.load();
        PlayerMatchMaking playerMatchMakingController = loader.getController();
        Client.callbackImpl.addObserver(playerMatchMakingController);
        playerMatchMakingController.setStage(stage);
        stage.setScene(new Scene(root));
        playerService.request(PlayerRequestType.START_GAME, player, callback);

    }

    // /Client/Player/view/
}
