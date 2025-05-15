package Client.Admin.controller;

import Client.Player.controller.PlayerLogin;
import Client.Player.controller.PlayerResults;
import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.admin.AdminRequestType;
import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.referenceClasses.Admin;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.WhatsTheWord.referenceClasses.ValuesList;
import Client.common.ClientControllerObserver;
import Client.main.Client;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.omg.CORBA.Any;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Client.Player.controller.PlayerLogin.getStringFromList;


public class AdminViewController implements ClientControllerObserver {

    @FXML private PasswordField addPasswordField;
    @FXML private Button addPlayerButton;
    @FXML private TextField addUsernameTextField;
    @FXML private Label adminNameLabel;
    @FXML private Button deleteButton;
    @FXML private TextField gameTimeTextField;
    @FXML private TableView<Player> playersTable;
    @FXML private Button refreshButton;
    @FXML private Button saveButton;
    @FXML private TextField searchTextField;
    @FXML private Button setGameTimeButton;
    @FXML private Button setWaitingTimeButton;
    @FXML private TableColumn<Player, String> userNameColumn;
    @FXML private TextField usernameTextField;
    @FXML private TextField waitingTimeTextField;
    @FXML private TableColumn<Player, Integer> winsColumn;
    @FXML private Button logoutButton;

    private ClientCallback callback = Client.callback;
    private AdminService adminService = Client.adminService;
    private Admin admin = PlayerLogin.admin;
    private ObservableList<Player> playersList = FXCollections.observableArrayList();
    private Player selectedPlayer;
    private Stage stage;
    private static List<Player> leaderboards = new ArrayList<>();
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize(AdminService adminService, Admin admin) {
        this.adminService = adminService;
        this.admin = admin;
        adminNameLabel.setText("Welcome " + admin.username);
        playersTable.setOnMouseClicked(event -> {
            System.out.println("Mouse clicked");
        });
        initializeButtons();
        refreshTable();
    }

    private void initializeButtons() {
        addPlayerButton.setOnAction(this::handleAddPlayer);
        deleteButton.setOnAction(this::handleDeletePlayer);
        saveButton.setOnAction(this::handleSavePlayer);
        logoutButton.setOnAction(this::handleLogout);
        setGameTimeButton.setOnAction(this::handleSetGameTime);
        setWaitingTimeButton.setOnAction(this::handleSetWaitingTime);
        refreshButton.setOnAction(this::handleRefresh);
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        Platform.runLater(this::refreshTable);
    }

    private void refreshTable() {
        Admin admin = PlayerLogin.admin;
        adminService.request(AdminRequestType.GET_PLAYER_DETAILS, admin, callback);
    }

    @FXML
    void handleAddPlayer(ActionEvent event) {
        String username = addUsernameTextField.getText();
        String password = addPasswordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or Password cannot be empty. Try again.");
            return;
        }

        Admin playerToBeCreated = new Admin(0, username, password, 0,0);
        adminService.request(AdminRequestType.CREATE_NEW_PLAYER, playerToBeCreated, callback);
        refreshTable();
        addUsernameTextField.clear();
        addPasswordField.clear();
    }

    @FXML
    void handleDeletePlayer(ActionEvent event) {
        if (selectedPlayer == null) {
            showAlert("Error", "Player not selected. Try again.");
            return;
        }

        selectedPlayer.username = usernameTextField.getText();
        Admin playerToBeDeleted = new Admin(0, selectedPlayer.username, selectedPlayer.password, 0, 0);
        adminService.request(AdminRequestType.DELETE_PLAYER, playerToBeDeleted, callback);
        refreshTable();
    }

    @FXML
    void handleSavePlayer(ActionEvent event) {
        if (selectedPlayer == null) {
            showAlert("Error", "Player not selected. Try again.");
            return;
        }

        Admin adminTobeSent = new Admin(0, selectedPlayer.username, usernameTextField.getText(), 0, 0);
        adminService.request(AdminRequestType.UPDATE_PLAYER_DETAILS, adminTobeSent, callback);
        refreshTable();
    }

    @FXML
    void handleSetWaitingTime(ActionEvent event) {
        if (waitingTimeTextField.getText().isEmpty()) {
            showAlert("Error", "Waiting time cannot be empty. Try again.");
            return;
        }

        admin.waitingTime = Integer.parseInt(waitingTimeTextField.getText());
        adminService.request(AdminRequestType.SET_LOBBY_WAITING_TIME, admin, callback);
    }

    @FXML
    void handleSetGameTime(ActionEvent event) {
        if (gameTimeTextField.getText().isEmpty()) {
            showAlert("Error", "Game time cannot be empty. Try again.");
            return;
        }

        admin.gameTime = Integer.parseInt(gameTimeTextField.getText());
        adminService.request(AdminRequestType.SET_ROUND_TIME, admin, callback);
    }
    @FXML
    void handleLogout(ActionEvent event) {
        adminService.request(AdminRequestType.ADMIN_LOGOUT, PlayerLogin.admin, callback);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Client/Player/view/PlayerLogin.fxml"));
            Parent root = loader.load();
            PlayerLogin playerLoginController = loader.getController();
            Client.callbackImpl.removeAllObservers();
            playerLoginController.setStage(stage);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleSearch(KeyEvent event) {
        String query = searchTextField.getText().toLowerCase();
        ObservableList<Player> filteredList = FXCollections.observableArrayList();
        for (Player p : playersList) {
            if (p.username.toLowerCase().startsWith(query)) {
                filteredList.add(p);
            }
        }
        playersTable.setItems(filteredList);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void update(ValuesList list) {
        if (checkValue(list.values[0])) {
            String message = getStringFromList(list);
            if (message.equals("PLAYER_USERNAME_ALREADY_EXISTS")) {
                Platform.runLater(() -> showAlert("Error", "Player's username already exists!"));
            } else if (message.equals("PLAYER_CREATED_SUCCESSFULLY")) {
                Platform.runLater(() -> showAlert("Success", "New player created!"));
            } else if (message.contains("PLAYER_USERNAME_UPDATE_SUCCESS")) {
                Platform.runLater(() -> showAlert("Success", "Player's username updated!"));
            } else if (message.contains("PLAYER_USERNAME_UPDATE_FAILED")) {
                Platform.runLater(() -> showAlert("Error", "Player's username cannot be updated!"));
            } else if (message.equals("PLAYER_DELETED")) {
                Platform.runLater(() -> showAlert("Success", "Player successfully deleted!"));
            } else if (message.equals("PLAYER_NOT_FOUND")) {
                Platform.runLater(() -> showAlert("Error", "Player could not be found!"));
            } else if (message.equals("WAITING_TIME_UPDATED")) {
                Platform.runLater(() -> showAlert("Success", "Waiting time successfully updated!"));
            } else if (message.equals("GAME_TIME_UPDATED")) {
                Platform.runLater(() -> showAlert("Success", "Game time successfully updated!"));
            }
        } else {
            leaderboards = decodePlayers(list.values);

            Platform.runLater(() -> {
                userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().username));
                winsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().wins).asObject());

                playersList = FXCollections.observableArrayList(
                        leaderboards
                );
                playersTable.setItems(playersList);
                searchTextField.setOnKeyReleased(this::handleSearch);
                playersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        selectedPlayer = newSelection;
                        usernameTextField.setText(newSelection.username);
                    }
                });
            });
        }
    }

    public static List<Player> decodePlayers(Any[] encodedContacts) {
        List<Player> result = new ArrayList<>();

        for (Any outerAny : encodedContacts) {
            Any[] contactData = extractInnerAnyArray(outerAny);

            String username = contactData[0].extract_string();
            int wins = contactData[1].extract_long();

            result.add(new Player(0, username, "", wins, 0, 0, true));
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

    public boolean checkValue(Any object) {
        try {
            if (object.extract_string() != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }



}
