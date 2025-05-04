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

        } else if (type.equals(AdminRequestType.GET_PLAYER_DETAILS)) {

        } else if (type.equals(AdminRequestType.UPDATE_PLAYER_DETAILS)) {

        } else if (type.equals(AdminRequestType.DELETE_PLAYER)) {

        } else if (type.equals(AdminRequestType.SEARCH_PLAYER)) {

        } else if (type.equals(AdminRequestType.SET_LOBBY_WAITING_TIME)) {

        } else if (type.equals(AdminRequestType.SET_ROUND_TIME)) {

        }
    }

    private void handleAdminLogin(Admin admin, ClientCallback callback) throws InvalidCredentialsException {

    }

    private void handleCreateNewPlayer(Admin admin, ClientCallback callback) throws InvalidCredentialsException {
        Player newPlayer = new Player();

        newPlayer.username = admin.username;
        newPlayer.password = admin.password;
        playerDao.save(newPlayer);
        list = buildList("PLAYER_ALREADY_EXIST");
        callback._notify(list);
    }
}

