package Client.Player.controller;

import Client.Player.view.ViewManager;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.player.PlayerRequestType;
import Client.WhatsTheWord.client.player.PlayerService;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerMatchMaking implements ClientControllerObserver {

    @FXML
    private Button cancelButton;        // Change to start button

    @FXML
    private Pane inQueueDisplay;

    @FXML
    private Label inQueueTimer;

    @FXML
    private Pane matchFoundDisplay;

    @FXML
    private Pane playerDisplay;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private Label playerName;
    private static Player player = PlayerLogin.player;
    private static PlayerService playerService = Client.playerService;
    private static ClientCallback callback = Client.callback;

    public static void startMatch() {
        playerService.request(PlayerRequestType.START_GAME, player, callback);
    }
    @Override
    public void update(ValuesList list) {
       Thread updateThread = new Thread(() -> {
            displayUpdate(list);
       });
       updateThread.start();
    }
    public int getIntFromList(ValuesList list) {
        return list.values[0].extract_ulong();
    }

    private void displayUpdate(ValuesList list) {
        String time = String.valueOf(getIntFromList(list));
        System.out.println(time);
        Platform.runLater(() -> inQueueTimer.setText(time));
        if (time.equals("0")) {
            Client.callbackImpl.removeAllObservers();
            Client.callbackImpl.addObserver(new PlayerGameProper());
            Platform.runLater(() -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerGameProper.fxml"));
                    Parent root = loader.load();
                    PlayerGameProper playerGameProperController = loader.getController();
                    playerGameProperController.setStage(stage);
                    stage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }



    // /Client/Player/view/
}
