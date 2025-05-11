package Client.Admin.controller;

import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.admin.AdminRequestType;
import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.referenceClasses.Admin;
import Client.WhatsTheWord.referenceClasses.Player;
import Client.main.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class AdminViewController {

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
    @FXML private TableColumn<Player, Long> winsColumn;
    @FXML private TableColumn<Player, Long> winsColumn1;

    private ClientCallback callback = Client.callback;
    private AdminService adminService;
    private Admin admin;
    private ObservableList<Player> playersList = FXCollections.observableArrayList();
    private Player selectedPlayer;

    public void initialize(AdminService adminService, Admin admin) {
        this.adminService = adminService;
        this.admin = admin;
        adminNameLabel.setText("Welcome " + admin.username);

        userNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().username));
        winsColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().wins).asObject());
        winsColumn1.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().noOfRoundWins).asObject());

        playersTable.setItems(playersList);

        playersTable.setOnMouseClicked(this::handleTableClick);
        searchTextField.setOnKeyReleased(this::handleSearch);
        initializeButtons();

        refreshTable();

    }

    private void initializeButtons() {
        addPlayerButton.setOnAction(this::handleAddPlayer);
        deleteButton.setOnAction(this::handleDeletePlayer);
        saveButton.setOnAction(this::handleSavePlayer);
        setGameTimeButton.setOnAction(this::handleSetGameTime);
        setWaitingTimeButton.setOnAction(this::handleSetWaitingTime);
        refreshButton.setOnAction(this::handleRefresh);
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        refreshTable();
    }

    private void refreshTable() {
    }

    @FXML
    void handleAddPlayer(ActionEvent event) {
        String username = addUsernameTextField.getText();
        String password = addPasswordField.getText();
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username or Password cannot be empty. Try again.");
            return;
        }
        Player newPlayer = new Player(0, username, password, 0, 0, 0, false);
        adminService.request(AdminRequestType.CREATE_NEW_PLAYER, admin, callback);
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

        adminService.request(AdminRequestType.CREATE_NEW_PLAYER, admin, callback);
        refreshTable();
    }

    @FXML
    void handleSavePlayer(ActionEvent event) {
        if (selectedPlayer == null) {
            showAlert("Error", "Player not selected. Try again.");
            return;
        }

        selectedPlayer.username = usernameTextField.getText();

        adminService.request(AdminRequestType.UPDATE_PLAYER_DETAILS, admin, callback);
        refreshTable();
    }

    @FXML
    void handleSetWaitingTime(ActionEvent event) {
        if (waitingTimeTextField.getText().isEmpty()) {
            showAlert("Error", "Waiting time cannot be empty. Try again.");
            return;
        }
        adminService.request(AdminRequestType.SET_LOBBY_WAITING_TIME, admin, callback);
    }

    @FXML
    void handleSetGameTime(ActionEvent event) {
        if (gameTimeTextField.getText().isEmpty()) {
            showAlert("Error", "Game time cannot be empty. Try again.");
            return;
        }
        adminService.request(AdminRequestType.SET_ROUND_TIME, admin, callback);
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

    private void handleTableClick(MouseEvent event) {
        selectedPlayer = playersTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            usernameTextField.setText(selectedPlayer.username);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
