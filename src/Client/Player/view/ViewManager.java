package Client.Player.view;

import Client.Player.controller.PlayerLogin;
import Client.Player.controller.PlayerRegister;
import Client.main.Client;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
    private static Stage primaryStage;
    private static final Map<String, Parent> views = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();

    public static void initialize(Stage stage) {
        primaryStage = stage;
        try {
            loadAllViews();
            showView("PlayerLogin");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load views: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void loadAllViews() throws IOException {
        loadView("PlayerLogin", getFXMLPath("PlayerLogin.fxml"));
        loadView("PlayerRegister", getFXMLPath("PlayerRegister.fxml"));
        loadView("PlayerMainMenu", getFXMLPath("PlayerMainMenu.fxml"));
        loadView("PlayerMatchMaking", getFXMLPath("PlayerMatchMaking.fxml"));
        loadView("PlayerGameProper", getFXMLPath("PlayerGameProper.fxml"));
        loadView("PlayerLeaderboard", getFXMLPath("PlayerLeaderboard.fxml"));
        loadView("PlayerResults", getFXMLPath("PlayerResults.fxml"));
    }

    private static URL getFXMLPath(String filename) {
        return ViewManager.class.getClassLoader().getResource("Client/Player/view/" + filename);
    }

    private static void loadView(String viewName, URL path) throws IOException {
        if (path == null) {
            throw new IOException(viewName + " not found");
        }

        switch (viewName) {
            case "PlayerLogin" :
                PlayerLogin playerLogin = new PlayerLogin();
                Client.callbackImpl.addObserver(playerLogin);
                break;
            case "PlayerRegister" :
                PlayerRegister playerRegister = new PlayerRegister();
                Client.callbackImpl.addObserver(playerRegister);
                break;
        }

        FXMLLoader loader = new FXMLLoader(path);
        Parent view = loader.load();
        views.put(viewName, view);
        controllers.put(viewName, loader.getController());
    }

    public static void showView(String viewName) {
        Parent view = views.get(viewName);
        if (view == null) {
            throw new IllegalArgumentException("View not found: " + viewName);
        }

        if (primaryStage.getScene() == null) {
            primaryStage.setScene(new Scene(view));
        } else {
            primaryStage.getScene().setRoot(view);
        }
        primaryStage.sizeToScene();
    }

    public static <T> T getController(String viewName) {
        return (T) controllers.get(viewName);
    }
}
