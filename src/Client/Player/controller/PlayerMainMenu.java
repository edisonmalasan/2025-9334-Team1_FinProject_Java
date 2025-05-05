package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
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

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleLeaderboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLeaderboard.fxml"));
        Parent root = loader.load();
        PlayerLeaderboard leaderboardController = loader.getController();
        leaderboardController.setStage(stage);
        stage.setScene(new Scene(root));
    }


    @FXML
    void handleLogout(ActionEvent event) {
    }

    @FXML
    void handleStartGame(ActionEvent event) throws IOException {
        Client.callbackImpl.removeAllObservers();
        Client.callbackImpl.addObserver(new PlayerMatchMaking());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerMatchMaking.fxml"));
        Parent root = loader.load();
        PlayerMatchMaking controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root));
    }

    // /Client/Player/view/
}
