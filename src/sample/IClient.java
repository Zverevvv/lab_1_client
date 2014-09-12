package sample;

import java.rmi.Remote;

/**
 * Created by Василий on 12.09.2014.
 */
public interface IClient extends Remote{
    void Update();
}
