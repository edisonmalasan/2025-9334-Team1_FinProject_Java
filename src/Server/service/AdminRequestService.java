package Server.service;

import common.client.ClientCallback;
import common.client.admin.AdminRequestType;
import common.client.admin.AdminServicePOA;
import common.referenceClasses.Admin;

public class AdminRequestService extends AdminServicePOA {
    @Override
    public void request(AdminRequestType type, Admin admin, ClientCallback callback) {

    }
}
