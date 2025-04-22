package Server.service;

import common.client.ClientCallback;
import common.client.admin.AdminRequestType;
import common.client.admin.AdminServicePOA;
import common.referenceClasses.Admin;

public class AdminRequestService extends AdminServicePOA {
    // TODO: Implement DAO and other methods (Follow PlayerRequestService format)

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
}

