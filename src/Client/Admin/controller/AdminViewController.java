package Client.Admin.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

    @FXML
    void handleAddPlayer(ActionEvent event) {

    }

    @FXML
    void handleDeletePlayer(ActionEvent event) {

    }

    @FXML
    void handleRefresh(ActionEvent event) {

    }

    @FXML
    void handleSavePlayer(ActionEvent event) {

    }

    @FXML
    void handleSetGameTime(ActionEvent event) {

    }

    @FXML
    void handleSetWaitingTime(ActionEvent event) {

    }

    @FXML
    void sortByScore(ActionEvent event) {

    }

}
