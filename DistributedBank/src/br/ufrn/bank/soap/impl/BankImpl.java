package br.ufrn.bank.soap.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.jws.WebService;
import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AuthenticationException;
import br.ufrn.bank.exceptions.AuthenticationFailedException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;
import br.ufrn.bank.exceptions.UserAlreadyExistsException;
import br.ufrn.bank.model.Account;
import br.ufrn.bank.model.Deposit;
import br.ufrn.bank.model.Transfer;
import br.ufrn.bank.model.User;
import br.ufrn.bank.model.Withdraw;
import br.ufrn.bank.soap.interfaces.BankWebI;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * A remote bank and its operations.
 * 
 * @author vitorgreati, viniciuscampos
 */
@WebService(endpointInterface = "br.ufrn.bank.soap.interfaces.BankWebI")
public class BankImpl implements BankWebI {
		
    @Resource
    private WebServiceContext wsContext;
    
    private final Map<Long, Account> accounts;
    private final Map<String, User> users;
    
    public BankImpl() {
        accounts = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
    }

    @Override
    public String createUser(String username, String password) throws UserAlreadyExistsException {
        
        if (username == null || password == null) 
            throw new IllegalArgumentException();
        
        if (users.containsKey(username))
            throw new UserAlreadyExistsException(username);
        
        users.put(username, new User(username, password));
        
        return username;
    }

    @Override
    public String authenticate(String username, String password) throws AuthenticationFailedException {

        if (username == null || password == null)
            throw new IllegalArgumentException();
        
        if (users.containsKey(username)) {
        
            User u = users.get(username);
            
            if (!u.getPassword().equals(password))
                throw new AuthenticationFailedException();
            
            MessageContext mc = wsContext.getMessageContext();
            //HttpSession session = ((javax.servlet.http.HttpServletRequest)mc.get(MessageContext.SERVLET_REQUEST)).getSession();
            
        } else throw new AuthenticationFailedException();

        return username;
    }

    
    @Override
    public Long createAccount(User user, Long id, String password) throws AccountAlreadyExistsException {
        
        if(accounts.containsKey(id)) 
            throw new AccountAlreadyExistsException(id);
        
        accounts.put(id, new Account(user, id, password));			
        
        return id;
    }

    @Override
    public void deposit(Double value, Long account, Boolean updateOperation) throws IllegalArgumentException, InvalidAccountException {
        
        if (value <= 0)
            throw new IllegalArgumentException("Valor do depósito precisa ser positivo!");
        
        if(!accounts.containsKey(account)) 
            throw new InvalidAccountException(account);
        
        accounts.get(account).updateBalance(value);
        
        if(updateOperation){
            accounts.get(account).updateOperations(new Deposit(value));
        }
    }

    @Override
    public void withdraw(Long account, String password, Double value, Boolean updateOperation) throws InvalidAccountException, AuthenticationException, NotEnoughBalanceException {
                   
        if(value <= 0)
            throw new IllegalArgumentException("O saque requer valores positivos!");
        
        if(!accounts.containsKey(account))
            throw new InvalidAccountException(account);
            
        if(!accounts.get(account).getPassword().equals(password)) 
            throw new AuthenticationException();

        if(accounts.get(account).getBalance() < value)
            throw new NotEnoughBalanceException(account);
        
        accounts.get(account).updateBalance(-value);
        
        if(updateOperation){
            accounts.get(account).updateOperations(new Withdraw(-value));
        }
    }

    @Override
    public void transfer(Long account, String password, Double value, Long anotherAccount) throws IllegalArgumentException, InvalidAccountException, AuthenticationException, NotEnoughBalanceException {
        
        if(Objects.equals(account, anotherAccount)){
            throw new IllegalArgumentException("As contas precisam ser distintas!");
        }
    
        if(!accounts.containsKey(anotherAccount))
            throw new InvalidAccountException(anotherAccount);
        
        withdraw(account, password, value, false);        
        deposit(value, anotherAccount, false);
        
        accounts.get(account).updateOperations(new Transfer(anotherAccount, -value));
        accounts.get(anotherAccount).updateOperations(new Transfer(account, value));
        
    }

    @Override
    public ArrayList<String> statement(Long account, String password) throws InvalidAccountException, AuthenticationException {
        
        if (!accounts.containsKey(account))
            throw new InvalidAccountException(account);
        
        if (!accounts.get(account).getPassword().equals(password))
            throw new AuthenticationException();
        
        return accounts.get(account).getStatement();
    }

    @Override
    public Double getBalance(Long account, String password) throws InvalidAccountException, AuthenticationException {
        
        if (!accounts.containsKey(account))
            throw new InvalidAccountException(account);
        
        if (!accounts.get(account).getPassword().equals(password))
            throw new AuthenticationException();
        
        return accounts.get(account).getBalance();
    }

}
