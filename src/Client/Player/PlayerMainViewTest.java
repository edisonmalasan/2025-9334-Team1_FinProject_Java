package Client.Player;

import Client.Player.controller.PlayerLogin;
import Client.Player.view.ViewManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PlayerMainViewTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLogin.fxml"));
            Parent root = loader.load();
            PlayerLogin loginController = loader.getController();
            loginController.setStage(primaryStage);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("What's The Word Game");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Using ViewManager
//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        try {
//            ViewManager.initialize(primaryStage);
//            primaryStage.setTitle("What's The Word Game");
//            primaryStage.setResizable(false);
//        } catch (Exception e){
//          e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
}
