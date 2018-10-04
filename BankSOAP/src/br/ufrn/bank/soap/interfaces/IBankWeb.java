package br.ufrn.bank.soap.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AuthenticationException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;

/**
 * Web service interface for the Bank.
 * 
 * @author Vinícius Campos, Vitor Greati
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface IBankWeb {
    
    /**
     * Creates an account, given an account number and a password.
     * 
     * @param id
     * @param password
     * @return the account id
     * @throws exceptions.AccountAlreadyExistsException
     * @throws RemoteException 
     */
	@WebMethod
	public Long createAccount(Long id, String password) throws AccountAlreadyExistsException;
    
    /**
     * Deposit an amount in an account.
     * 
     * @param value
     * @param account
     * @param updateOperation
     * @throws RemoteException 
     * @throws exceptions.InvalidAccountException 
     */
	@WebMethod
	public void deposit(Double value, Long account, Boolean updateOperation) throws IllegalArgumentException, InvalidAccountException;
    
    /**
     * Withdraw a value from an account.
     * 
     * Performs authentication and subtracts the value from the
     * account if possible.
     * 
     * @param account
     * @param password
     * @param value
     * @param updateOperation
     * @throws exceptions.InvalidAccountException
     * @throws exceptions.AuthenticationException
     * @throws RemoteException 
     * @throws exceptions.NotEnoughBalanceException 
     */
	@WebMethod
	public void withdraw(Long account, String password, Double value, Boolean updateOperation) throws InvalidAccountException, AuthenticationException, NotEnoughBalanceException;
    
    /**
     * Transfers an amount from an account to another.
     * 
     * @param account
     * @param password
     * @param value
     * @param anotherAccount
     * @throws RemoteException 
     * @throws exceptions.InvalidAccountException 
     * @throws exceptions.AuthenticationException 
     * @throws exceptions.NotEnoughBalanceException 
     */
	@WebMethod
    public void transfer(Long account, String password, Double value, Long anotherAccount) throws IllegalArgumentException, InvalidAccountException, AuthenticationException, NotEnoughBalanceException;
    
    /**
     * Returns an account statement (operations performed).
     * 
     * @param account
     * @param password
     * @return the account statement
     * @throws exceptions.InvalidAccountException
     * @throws exceptions.AuthenticationException
     * @throws RemoteException 
     */
	@WebMethod
    public ArrayList<String> statement(Long account, String password) throws InvalidAccountException, AuthenticationException;
    
    /**
     * Check account balance.
     * 
     * @param account
     * @param password
     * @return the balance
     * @throws exceptions.InvalidAccountException
     * @throws exceptions.AuthenticationException
     * @throws java.rmi.RemoteException
     */
	@WebMethod
    public Double getBalance(Long account, String password) throws InvalidAccountException, AuthenticationException;
}
