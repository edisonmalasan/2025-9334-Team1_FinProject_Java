package Client.common.callback;

import Client.common.ClientControllerObserver;
import common.client.ClientCallbackPOA;
import common.referenceClasses.ValuesList;

import java.util.ArrayList;
import java.util.List;

public class ClientCallbackImpl extends ClientCallbackPOA {
    private static List<ClientControllerObserver> observers = new ArrayList<>();
    @Override
    public void _notify(ValuesList list) {
        for (ClientControllerObserver observer : observers) {
            observer.update(list);
        }
    }

    public void addObserver(ClientControllerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ClientControllerObserver observer) {
        observers.remove(observer);
    }
}
