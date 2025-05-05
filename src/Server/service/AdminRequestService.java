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
import Server.exception.InvalidCredentialsException;
import Server.WhatsTheWord.referenceClasses.ValuesList;
import org.omg.CORBA.Any;

import java.util.List;

import static Server.main.GameServer.orb;
import static Server.service.PlayerRequestService.buildList;

public class AdminRequestService extends AdminServicePOA {
    // TODO: Implement DAO and other methods (Follow PlayerRequestService format)
    private static AdminDAO adminDao;
    private static PlayerDAO playerDao;
    private static ValuesList list = new ValuesList();

    @Override
    public void request(AdminRequestType type, Admin admin, ClientCallback callback) {
        if (type.equals(AdminRequestType.ADMIN_LOGIN)) {

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

    }

    private void handleCreateNewPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        Player existingPlayer = PlayerDAO.findByUsername(admin.username);
        if (existingPlayer != null) {
            list = buildList("USERNAME_ALREADY_EXISTS");
            callback._notify(list);
        } else {
            Player newPlayer = new Player();
            newPlayer.username = admin.username;
            newPlayer.password = admin.password;
            playerDao.create(newPlayer);
            list = buildList("PLAYER_CREATED");
            callback._notify(list);
        }
    }

    private void handleGetPlayerDetails(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        List<Player> players = playerDao.findAll();
        Any[] anyArray = new Any[players.size() + 1];
        anyArray[0] = orb.create_any();
        anyArray[0].insert_string("PLAYER_DETAILS");

        for (int i = 0; i < players.size(); i++) {
            anyArray[i+1] = orb.create_any();
            anyArray[i+1].insert_string(players.get(i).toString());
        }

        callback._notify(new ValuesList(anyArray));
    }

    private void handleUpdatePlayerDetails(Admin admin, ClientCallback callback) throws InvalidCredentialsException {

    }

    private void handleDeletePlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {

    }

    private void handleSearchPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {

    }

    private void handleSetLobbyWaitingTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        int waitingTime = 0;
        PlayerRequestService.waitingTime = waitingTime;

    }

    private void handleSetRoundTime(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        int roundTime = 0;
        PlayerRequestService.gameTime = roundTime;
    }

    public static ValuesList addPlayerToList(ValuesList list, Player player) {
        Any[] anyArray = new Any[6];
        Any message = list.values[0];
        Any playerID = orb.create_any();
        Any username = orb.create_any();
        Any password = orb.create_any();
        Any wins = orb.create_any();
        Any hasPlayed = orb.create_any();

        playerID.insert_ulong(player.playerId);
        username.insert_string(player.username);
        password.insert_string(player.password);
        wins.insert_ulong(player.wins);
        hasPlayed.insert_boolean(player.hasPlayed);

        anyArray[0] = message;
        anyArray[1] = playerID;
        anyArray[2] = username;
        anyArray[3] = password;
        anyArray[4] = wins;
        anyArray[5] = hasPlayed;

        return new ValuesList(anyArray);
    }
}

