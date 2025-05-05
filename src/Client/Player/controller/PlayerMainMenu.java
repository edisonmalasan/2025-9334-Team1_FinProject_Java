package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.main.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class PlayerMainMenu {

    @FXML
    private Button leaderBoardButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button startGameButton;
    private Player player = PlayerLogin.player;
    private PlayerService playerService = Client.playerService;
    private ClientCallback callback = Client.callback;
    @FXML
    void handleLeaderboard(ActionEvent event) {
        ViewManager.showView("PlayerLeaderboard");
    }

    @FXML
    void handleLogout(ActionEvent event) {
    }

    @FXML
    void handleStartGame(ActionEvent event) {
        Client.callbackImpl.removeAllObservers();
        Client.callbackImpl.addObserver(ViewManager.getController("PlayerMatchMaking"));
        ViewManager.showView("PlayerMatchMaking");
    }
}
