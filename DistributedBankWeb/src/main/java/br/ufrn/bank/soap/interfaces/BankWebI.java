/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.soap.interfaces;

import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AccountAuthenticationException;
import br.ufrn.bank.exceptions.AuthenticationFailedException;
import br.ufrn.bank.exceptions.InconvenientUserException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.MissingAuthenticationException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;
import br.ufrn.bank.exceptions.UserAlreadyExistsException;
import br.ufrn.bank.model.User;
import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * Web interface for the distributed bank.
 * 
 * @author vitorgreati
 */
@WebService
@SOAPBinding(style = Style.RPC)
public interface BankWebI {
    
    /**
     * Creates a new user.
     * 
     * @param username
     * @param password
     * @return username of the new user
     * @throws UserAlreadyExistsException 
     * @throws br.ufrn.bank.exceptions.InconvenientUserException 
     */
    @WebMethod
    public String createUser (String username, String password) throws UserAlreadyExistsException, InconvenientUserException;
    
    /**
     * Authenticates user in the server.
     * 
     * @param username
     * @param password
     * @return 
     * @throws br.ufrn.bank.exceptions.AuthenticationFailedException
     */
    @WebMethod 
    public String authenticate (String username, String password) throws AuthenticationFailedException;
    
    /**
     * Creates an account, given an account number and a password.
     * 
     * @param id
     * @param password
     * @return the account id
     * @throws br.ufrn.bank.exceptions.AccountAlreadyExistsException 
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException 
     */
    @WebMethod
    public Long createAccount(Long id, String password) throws AccountAlreadyExistsException, MissingAuthenticationException;
    
    /**
     * Deposit an amount in an account.
     * 
     * @param value
     * @param account
     * @param updateOperation
     * @throws br.ufrn.bank.exceptions.InvalidAccountException
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException
     */
    @WebMethod
    public void deposit(Double value, Long account, Boolean updateOperation) throws IllegalArgumentException, InvalidAccountException, MissingAuthenticationException;
    
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
     * @throws br.ufrn.bank.exceptions.InvalidAccountException
     * @throws br.ufrn.bank.exceptions.AccountAuthenticationException
     * @throws br.ufrn.bank.exceptions.NotEnoughBalanceException
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException
     */
    @WebMethod
    public void withdraw(Long account, String password, Double value, Boolean updateOperation) throws InvalidAccountException, AccountAuthenticationException, NotEnoughBalanceException, MissingAuthenticationException;
    
    /**
     * Transfers an amount from an account to another.
     * 
     * @param account
     * @param password
     * @param value
     * @param anotherAccount
     * @throws br.ufrn.bank.exceptions.InvalidAccountException
     * @throws br.ufrn.bank.exceptions.AccountAuthenticationException
     * @throws br.ufrn.bank.exceptions.NotEnoughBalanceException
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException
     */
    @WebMethod
    public void transfer(Long account, String password, Double value, Long anotherAccount) throws IllegalArgumentException, InvalidAccountException, AccountAuthenticationException, NotEnoughBalanceException, MissingAuthenticationException;
    
    /**
     * Returns an account statement (operations performed).
     * 
     * @param account
     * @param password
     * @return the account statement
     * @throws br.ufrn.bank.exceptions.InvalidAccountException
     * @throws br.ufrn.bank.exceptions.AccountAuthenticationException
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException
     */
    @WebMethod
    public ArrayList<String> statement(Long account, String password) throws InvalidAccountException, AccountAuthenticationException, MissingAuthenticationException;
    
    /**
     * Check account balance.
     * 
     * @param account
     * @param password
     * @return the balance
     * @throws br.ufrn.bank.exceptions.InvalidAccountException
     * @throws br.ufrn.bank.exceptions.AccountAuthenticationException
     * @throws br.ufrn.bank.exceptions.MissingAuthenticationException
     */
    @WebMethod
    public Double getBalance(Long account, String password) throws InvalidAccountException, AccountAuthenticationException, MissingAuthenticationException;
}

