package Client.Player.controller;

import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.omg.CORBA.Any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerLeaderboard implements ClientControllerObserver {
    @FXML
    public TableColumn<Player, Integer> rankColumn;
    @FXML
    public TableColumn<Player, String> usernameColumn;
    @FXML
    public TableColumn<Player, Integer> winsColumn;
    private Stage stage;
    @FXML
    private Button backButton;

    @FXML
    private TableView<Player> leaderBoardTable;
    private static List<Player> leaderboards = new ArrayList<>();

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

    @Override
    public void update(ValuesList list) {
        leaderboards = decodePlayers(list.values);

        Platform.runLater(() -> {
            rankColumn.setCellValueFactory(cellData ->
                    new ReadOnlyObjectWrapper<>(leaderBoardTable.getItems().indexOf(cellData.getValue()) + 1));
            rankColumn.setSortable(false);
            usernameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().username));
            winsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().wins).asObject());

            ObservableList<Player> playerObservableList = FXCollections.observableArrayList(
                    leaderboards
            );
            leaderBoardTable.setItems(playerObservableList);
        });
    }

    public static List<Player> decodePlayers(Any[] encodedContacts) {
        List<Player> result = new ArrayList<>();

        for (Any outerAny : encodedContacts) {
            Any[] contactData = extractInnerAnyArray(outerAny);

            String username = contactData[0].extract_string();
            int wins = contactData[1].extract_long();

            result.add(new Player(0, username, "", wins, 0, 0, true));

            System.out.println(username + ": " + wins);
        }

        return result;
    }

    public static Any[] extractInnerAnyArray(Any outerAny) {
        org.omg.CORBA.portable.InputStream in = outerAny.create_input_stream();

        int length = in.read_long();
        Any[] result = new Any[length];

        for (int i = 0; i < length; i++) {
            result[i] = in.read_any();
        }

        return result;
    }


    // /Client/Player/view/

}