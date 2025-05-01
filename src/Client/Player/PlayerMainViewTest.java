package Client.Player;

import Client.Player.view.ViewManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class PlayerMainViewTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            ViewManager.initialize(primaryStage);
            primaryStage.setTitle("What's The Word Game");
            primaryStage.setResizable(false);
        } catch (Exception e){
          e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
