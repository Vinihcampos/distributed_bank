package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface for the Bank.
 * 
 * @author Vinícius Campos, Vitor Greati
 */
public interface IBank extends Remote {
    
    /**
     * Creates an account, given an account number and a password.
     * 
     * @param id
     * @param password
     * @return the account id
     * @throws RemoteException 
     */
    public Long createAccount(Long id, String password) throws RemoteException;
    
    /**
     * Deposit an amount in an account.
     * 
     * @param value
     * @param account
     * @throws RemoteException 
     */
    public void deposit(Double value, Long account) throws RemoteException;
    
    /**
     * Withdraw a value from an account.
     * 
     * Performs authentication and subtracts the value from the
     * account if possible.
     * 
     * @param account
     * @param password
     * @param value
     * @throws RemoteException 
     */
    public void withdraw(Long account, String password, Double value) throws RemoteException;
    
    /**
     * Transfers an amount from an account to another.
     * 
     * @param account
     * @param password
     * @param value
     * @param anotherAccount
     * @throws RemoteException 
     */
    public void transfer(Long account, String password, Double value, Long anotherAccount) throws RemoteException;
    
    /**
     * Returns an account statement (operations performed).
     * 
     * @param account
     * @param password
     * @return the account statement
     * @throws RemoteException 
     */
    public String statement(Long account, String password) throws RemoteException;	
}
