package Server.service;

import Server.DataAccessObject.AdminDAO;
import Server.DataAccessObject.PlayerDAO;
import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.client.admin.AdminRequestType;
import Server.WhatsTheWord.client.admin.AdminServicePOA;
import Server.WhatsTheWord.referenceClasses.Admin;
import Server.WhatsTheWord.referenceClasses.GameLobby;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.controller.GameLobbyHandler;
import Server.database.DatabaseConnection;
import Server.exception.InvalidCredentialsException;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import Server.util.PasswordHashUtility;
import org.omg.CORBA.Any;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static Server.main.GameServer.orb;
import static Server.service.PlayerRequestService.buildList;

public class AdminRequestService extends AdminServicePOA {
    // TODO: Implement DAO and other methods (Follow PlayerRequestService format)
    private static AdminDAO adminDao;
    private static PlayerDAO playerDao;

    private static List<String> loggedInAdmin = new ArrayList<>();
    private static ValuesList list = new ValuesList();

    @Override
    public void request(AdminRequestType type, Admin admin, ClientCallback callback) {
        if (type.equals(AdminRequestType.ADMIN_LOGIN)) {
            handleAdminLogin(admin, callback);
        } else if (type.equals(AdminRequestType.CREATE_NEW_PLAYER)) {
            handleCreateNewPlayer(admin, callback);
        } else if (type.equals(AdminRequestType.GET_PLAYER_DETAILS)) {
            handleGetPlayerDetails(admin, callback);
        } else if (type.equals(AdminRequestType.UPDATE_PLAYER_DETAILS)) {
            handleUpdatePlayerDetails(admin, callback);
        } else if (type.equals(AdminRequestType.DELETE_PLAYER)) {
            handleDeletePlayer(admin, callback);
//        } else if (type.equals(AdminRequestType.SEARCH_PLAYER)) {
//            handleSearchPlayer(admin, callback);
        } else if (type.equals(AdminRequestType.SET_LOBBY_WAITING_TIME)) {
            handleSetLobbyWaitingTime(admin, callback);
        } else if (type.equals(AdminRequestType.SET_ROUND_TIME)) {
            handleSetRoundTime(admin, callback);
        }
    }

    private void handleAdminLogin(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        try {
            // find admin
            Admin dbAdmin = AdminDAO.findByUsername(admin.username);

            // verify admin
            if (dbAdmin == null) {
                list = buildList("UNSUCCESSFUL_LOGIN");
                callback._notify(list);
                return;
            }

            if (!PasswordHashUtility.verify(admin.password, dbAdmin.password)) {
                list = buildList("UNSUCCESSFUL_LOGIN");
                callback._notify(list);
                return;
            }

            // check if already logged in
            if (loggedInAdmin.contains(admin.username)) {
                list = buildList("ADMIN_ALREADY_LOGGED_IN");
                callback._notify(list);
                return;
            }

            // success
            loggedInAdmin.add(admin.username);
            list = buildList("SUCCESSFUL_LOGIN");
            callback._notify(list);

        } catch (Exception e) {
            list = buildList("LOGIN_ERROR: " + e.getMessage());
            callback._notify(list);
        }
    }

    private void handleCreateNewPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        Player existingPlayer = PlayerDAO.findByUsername(admin.username);
        if (existingPlayer != null) {
            list = buildList("USERNAME_ALREADY_EXISTS");
            callback._notify(list);
        } else {
            AdminDAO.addPlayer(admin.username, admin.password);
            list = buildList("PLAYER_CREATED");
            callback._notify(list);
        }
    }

    private void handleGetPlayerDetails(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        List<Player> players = playerDao.findAllPlayers();
        Any[] anyArray = new Any[players.size() + 1];
        anyArray[0] = orb.create_any();
        anyArray[0].insert_string("PLAYER_DETAILS");

        for (int i = 0; i < players.size(); i++) {
            anyArray[i+1] = orb.create_any();
            anyArray[i+1].insert_string(players.get(i).toString());
        }

        callback._notify(new ValuesList(anyArray));
    }

    private void handleUpdatePlayerDetails(Admin admin, ClientCallback callback) {
        // TODO: handle palyer username update
    }

    private void handleDeletePlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        if (playerDao.delete(String.valueOf(admin.adminId))) {
            list = buildList("PLAYER_DELETED");
        } else {
            list = buildList("PLAYER_NOT_FOUND");
        }
        callback._notify(list);
    }

//    private void handleSearchPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
//        try {
//            List<Player> players = adminDao.searchPlayersByUsername(admin.username);
//
//            if (players.isEmpty()) {
//                list = buildList("PLAYER_NOT_FOUND");
//                callback._notify(list);
//                return;
//            }
//
//            Any[] anyArray = new Any[players.size() + 1];
//            anyArray[0] = orb.create_any();
//            anyArray[0].insert_string("PLAYER_SEARCH_RESULT");
//
//            for (int i = 0; i < players.size(); i++) {
//                anyArray[i + 1] = orb.create_any();
//                anyArray[i + 1].insert_string(
//                        players.get(i).playerId + ":" +
//                                players.get(i).username + ":" +
//                                players.get(i).wins
//                        );
//            }
//            callback._notify(new ValuesList(anyArray));
//        } catch (Exception e) {
//            list = buildList("SEARCH_FAILED_FOR" + admin.username + "ERROR: " + e.getMessage());
//            callback._notify(list);
//        }
//    }

    private void handleSetLobbyWaitingTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        int waitingTime = 0;
        PlayerRequestService.waitingTime = waitingTime;
    }

    private void handleSetRoundTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        int roundTime = 0;
        PlayerRequestService.gameTime = roundTime;
    }

}

