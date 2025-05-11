package Client.Admin.controller;

import Client.WhatsTheWord.client.admin.AdminService;
import Client.WhatsTheWord.referenceClasses.Admin;
import Client.WhatsTheWord.referenceClasses.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class AdminViewController {

    @FXML
    private PasswordField addPasswordField;

    @FXML
    private Button addPlayerButton;

    @FXML
    private TextField addUsernameTextField;

    @FXML
    private Label adminNameLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField gameTimeTextField;

    @FXML
    private TableView<?> playersTable;

    @FXML
    private Button refreshButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField searchTextField;

    @FXML
    private Button setGameTimeButton;

    @FXML
    private Button setWaitingTimeButton;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField waitingTimeTextField;

    @FXML
    private TableColumn<?, ?> winsColumn;

    @FXML
    private TableColumn<?, ?> winsColumn1;


    private AdminService adminService;
    private Admin admin;
    private ObservableList<Player> playersData = FXCollections.observableArrayList();
    private ObservableList<Player> allPlayersData = FXCollections.observableArrayList();
    private Player selectedPlayer;

    public void initialize(AdminService adminService, Admin admin) {
        this.adminService = adminService;
        this.admin = admin;

    }

    @FXML
    void handleRefresh(ActionEvent event) {

    }


    @FXML
    void handleAddPlayer(ActionEvent event) {

    }

    @FXML
    void handleDeletePlayer(ActionEvent event) {

    }
    @FXML
    void handleSavePlayer(ActionEvent event) {

    }

    @FXML
    void handleSetWaitingTime(ActionEvent event) {

    }

    @FXML
    void sortByScore(ActionEvent event) {

    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void refreshTable() {}

}
