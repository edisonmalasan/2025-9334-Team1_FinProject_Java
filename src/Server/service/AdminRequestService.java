package Server.service;

import Server.DataAccessObject.AdminDAO;
import Server.DataAccessObject.PlayerDAO;
import Server.WhatsTheWord.client.ClientCallback;
import Server.WhatsTheWord.client.admin.AdminRequestType;
import Server.WhatsTheWord.client.admin.AdminServicePOA;
import Server.WhatsTheWord.referenceClasses.Admin;
import Server.WhatsTheWord.referenceClasses.Player;
import Server.exception.InvalidCredentialsException;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import Server.util.PasswordHashUtility;
import org.omg.CORBA.Any;

import java.util.ArrayList;
import java.util.List;

import static Server.main.GameServer.orb;
import static Server.service.PlayerRequestService.encodePlayers;

public class AdminRequestService extends AdminServicePOA {
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
        } else if (type.equals(AdminRequestType.SEARCH_PLAYER)) {
            handleSearchPlayer(admin, callback);
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
                list = buildList("USER_ALREADY_LOGGED_IN");
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
        if (existingPlayer.username != null) {
            list = buildList("PLAYER_USERNAME_ALREADY_EXISTS");
            callback._notify(list);
        } else {
            Player playerToBeCreated = new Player(000, admin.username, admin.password, 0, 0, 0, false);
            PlayerDAO.create(playerToBeCreated);
            list = buildList("PLAYER_CREATED_SUCCESSFULLY");
            callback._notify(list);
        }
    }

    private void handleGetPlayerDetails(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        List<Player> playerList = PlayerDAO.findAllPlayers();
        list = encodePlayers(playerList);
        callback._notify(list);
    }

    private void handleUpdatePlayerDetails(Admin admin, ClientCallback callback) {
       try {
           Player existingPlayer = PlayerDAO.findByUsername(admin.username);
           if (existingPlayer.username == null) {
               list = buildList("PLAYER_NOT_FOUND");
               callback._notify(list);
               return;
           }

           boolean updateSuccess = AdminDAO.editPlayerName(
                   admin.username,
                   "username",
                   admin.password
           );

           if (updateSuccess) {
               list = buildList("PLAYER_USERNAME_ALREADY_SUCCESS");
           } else {
               list = buildList("PLAYER_USERNAME_UPDATE_FAILED");
           }
           callback._notify(list);

       } catch (Exception e) {
           list = buildList("PLAYER_USERNAME_UPDATE_FAILED: " + e.getMessage());
           callback._notify(list);
       }
    }

    private void handleDeletePlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        if (PlayerDAO.delete(admin.username)) {
            list = buildList("PLAYER_DELETED");
        } else {
            list = buildList("PLAYER_NOT_FOUND");
        }
        callback._notify(list);
    }

    private void handleSearchPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        try {
            List<Player> players = AdminDAO.searchPlayersByUsername(admin.username);

            if (players.isEmpty()) {
                list = buildList("PLAYER_NOT_FOUND");
                callback._notify(list);
                return;
            }

            Any[] anyArray = new Any[players.size() + 1];
            anyArray[0] = orb.create_any();
            anyArray[0].insert_string("PLAYER_SEARCH_RESULT");

            for (int i = 0; i < players.size(); i++) {
                anyArray[i + 1] = orb.create_any();
                anyArray[i + 1].insert_string(
                        players.get(i).playerId + ":" +
                                players.get(i).username + ":" +
                                players.get(i).wins
                        );
            }
            callback._notify(new ValuesList(anyArray));
        } catch (Exception e) {
            list = buildList("SEARCH_FAILED_FOR" + admin.username + "ERROR: " + e.getMessage());
            callback._notify(list);
        }
    }

    private void handleSetLobbyWaitingTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        PlayerRequestService.waitingTime = admin.waitingTime;
        list = buildList("WAITING_TIME_UPDATED");
        System.out.println("waiting time");
        callback._notify(list);
    }

    private void handleSetRoundTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        PlayerRequestService.gameTime = admin.gameTime;
        list = buildList("GAME_TIME_UPDATED");
        System.out.println("game time");
        callback._notify(list);
    }

    public static ValuesList buildList(Object object) {
        Any[] anyArray = new Any[1];
        Any anyString = orb.create_any();
        if (object instanceof String) {
            anyString.insert_string((String) object);
        }
        anyArray[0] = anyString;
        return new ValuesList(anyArray);
    }

}

