package sample;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by Василий on 12.09.2014.
 */

//UnicastRemoteObject
public class Client extends UnicastRemoteObject implements IClient{

    public Client() throws RemoteException {
        super();
    }

    @Override
    public void Update() {

    }
}
