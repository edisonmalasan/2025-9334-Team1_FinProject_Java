package Client.Player.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class PlayerLeaderboard {
    private Stage stage;
    @FXML
    private Button backButton;

    @FXML
    private TableView<?> leaderBoardTable;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        backButton.setOnAction(event -> {
            try {
                handleBack(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerMainMenu.fxml"));
        Parent root = loader.load();
        PlayerMainMenu controller = loader.getController();
        controller.setStage(stage);
        stage.setScene(new Scene(root));
    }

    // /Client/Player/view/

}