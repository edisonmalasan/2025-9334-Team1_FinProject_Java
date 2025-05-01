package Client.Player.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewManager {
    private static Stage primaryStage;
    private static final Map<String, Parent> views = new HashMap<>();
    private static final Map<String, Object> controllers = new HashMap<>();

    public static void init(Stage primaryStage) throws Exception {
        primaryStage = primaryStage;
        loadAllViews();
        showView("PlayerLogin");
        primaryStage.show();

    }

    private static void loadAllViews() throws IOException {
        loadView("PlayerLogin", "src/Client/Player/view/PlayerLogin.fxml");
        loadView("PlayerRegister", "src/Client/Player/view/PlayerRegister.fxml");
        loadView("PlayerMatchMaking", "src/Client/Player/view/PlayerMatchMaking.fxml");
        loadView("PlayerGameProper", "src/Client/Player/view/PlayerGameProper.fxml");
        loadView("PlayerLeaderboard", "src/Client/Player/view/PlayerLeaderboard.fxml");
        loadView("PlayerResults", "src/Client/Player/view/PlayerResults.fxml");
    }

    private static void loadView(String viewName, String path) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewManager.class.getResource(path));
        Parent viewRoot = loader.load();
        views.put(viewName, viewRoot);
        controllers.put(viewName, loader.getController());
    }

    private static void showView(String viewName) throws IOException {
        Parent view = views.get(viewName);
        if (view != null) {
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
