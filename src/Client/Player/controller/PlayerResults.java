package Client.Player.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


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

    public void initialize(){
        backToMainMenuButton.setOnAction(this::handleBackToMainMenu);
    }

    private void handleBackToMainMenu(ActionEvent actionEvent) {
        try {
            Stage currentstage = (Stage) backToMainMenuButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Client/Player/view/PlayerMainMenu.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            currentstage.setScene(scene);
            currentstage.setTitle("Main Menu");
            currentstage.show();

        }catch (Exception e){
            e.printStackTrace();
            System.err.println("Error returning to main menu: " + e.getMessage());
        }
    }

    public void setGameResult(String result){
        gameResult.setText(result.toUpperCase());
    }

    public void setResultVisible(boolean visible) {
        resultDisplay.setVisible(visible);
    }
    public void setPlayerInfo(String username, int points, Image profileImage) {
        playerName.setText(username);
        playerPoints.setText(points + " Points");

        if (profileImage != null) {
            displayPfp.setImage(profileImage);
        }
    }
}