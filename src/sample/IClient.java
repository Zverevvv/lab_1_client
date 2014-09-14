package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClient extends Remote{
    void Update() throws RemoteException;
}
