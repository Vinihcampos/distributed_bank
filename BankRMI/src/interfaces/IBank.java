package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IBank extends Remote{
    public Long createAccount(Long id, String password) throws RemoteException;
    public Boolean deposit(Double value, Long account) throws RemoteException;
    public Boolean withdraw(Long account, String password, Double value) throws RemoteException;
    public Boolean transfer(Long account, String password, Double value, Long anotherAccount) throws RemoteException;
    public String statement(Long account, String password) throws RemoteException;	
}
