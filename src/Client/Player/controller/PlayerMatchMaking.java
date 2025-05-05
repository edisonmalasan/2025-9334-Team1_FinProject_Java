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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

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
            Client.callbackImpl.addObserver(ViewManager.getController("PlayerGameProper"));
            Platform.runLater(() -> ViewManager.showView("PlayerGameProper"));
        }
    }
}
