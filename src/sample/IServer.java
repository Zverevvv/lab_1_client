package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface IServer extends Remote{

    /**
     * Приветсвует пользователя
     * @param text
     * @return
     * @throws java.rmi.RemoteException
     */
    public String SayHello(String text) throws RemoteException;

    /**
     * Добавляет в коллекцию элемент
     * @param ex
     * @throws java.rmi.RemoteException
     */
    public void AddData(Book ex) throws RemoteException;

    /**
     * Выводит всю коллекцию
     * @return
     * @throws java.rmi.RemoteException
     */
    public List<Book> print() throws RemoteException;

    /**
     * Удаляет элемент kol из коллекции
     * @param kol
     * @return
     * @throws java.rmi.RemoteException
     */
    public boolean DeleteData(int kol) throws RemoteException;

    /**
     * Ищет объект с содержащий ser в поле mode 
     * @param ser
     * @param mode
     * @return
     * @throws java.rmi.RemoteException
     */
    
    public void edit(int id, Book book) throws RemoteException;
    public  ArrayList<Book> search(String ser, int mode) throws RemoteException;
    //void registry(IClient client);
}