package Client.Player.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.Objects;


public class PlayerResults {

    @FXML
    private Button backToMainMenuButton;

    @FXML
    private Label gameResult;

    @FXML
    private Label playerName;

    @FXML
    private Label playerPoints;

    @FXML
    private Pane resultDisplay;
    @FXML
    private ImageView displayPfp;
    private Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(){
        backToMainMenuButton.setOnAction(this::handleBackToMainMenu);
        if (Objects.equals(PlayerGameProper.winner, "*NOT_ENOUGH_PLAYERS*")) {
            gameResult.setText("SO SAD!");
            playerName.setText("NOT ENOUGH PLAYERS");
            playerPoints.setText("");
        } else {
            playerName.setText(PlayerGameProper.winner);
            playerPoints.setText("Rounds: " + PlayerGameProper.roundCount);
        }
    }

    private void handleBackToMainMenu(ActionEvent actionEvent) {
        try {
            Stage currentstage = (Stage) backToMainMenuButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerMainMenu.fxml"));
            Parent root = loader.load();
            PlayerMainMenu mainMenuController = loader.getController();
            mainMenuController.setStage(currentstage);
            currentstage.setScene(new Scene(root));
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Error returning to main menu: " + e.getMessage());
        }
    }
}