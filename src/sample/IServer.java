package sample;

import javafx.collections.ObservableList;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IServer extends Remote{

    public void AddData(Book ex) throws RemoteException;

    public ArrayList<Book> print() throws RemoteException;

    public boolean DeleteData(int kol) throws RemoteException;

    
    public void edit(int id, Book book) throws RemoteException;
    public  ArrayList<Book> search(String ser, int mode) throws RemoteException;
    void registry(IClient client) throws RemoteException;
    void unregistry(IClient client) throws RemoteException;
}