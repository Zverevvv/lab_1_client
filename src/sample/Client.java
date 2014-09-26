package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

//UnicastRemoteObject
class Client extends UnicastRemoteObject implements IClient{

    static ObservableList<Book> data = FXCollections.observableArrayList();
    Registry reg;
    IServer rmi;

    public Client() throws RemoteException {
        super();
    }

    public boolean connect(Client l) {
        try {
            reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
            rmi = (IServer) reg.lookup("server");
            System.out.println("Connected");
            rmi.registry(l);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } catch (NotBoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnetc(Client c){
        try {
            rmi.unregistry(c);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addData(Book b){
        try {
            data.add(b);
            rmi.AddData(b);
        }catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deleteDate(int n) throws RemoteException {
        rmi.DeleteData(n);
    }

    public void edit(int index, Book b){
        try {
            rmi.edit(index, b);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> search(String kategory, String what){
        try {
            if (kategory.equals("Name"))
                return rmi.search(what, 1);
            if (kategory.equals("Publisher"))
                return rmi.search(what, 2);
            if (kategory.equals("Date"))
                return rmi.search(what, 3);
            if (kategory.equals("Pages"))
                return rmi.search(what, 4);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void Update() throws RemoteException {
        data.clear();
        data.addAll(rmi.print());
    }
}