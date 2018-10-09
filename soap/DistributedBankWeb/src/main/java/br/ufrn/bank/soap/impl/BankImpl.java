package br.ufrn.bank.soap.impl;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javax.jws.WebService;
import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AccountAuthenticationException;
import br.ufrn.bank.exceptions.AuthenticationFailedException;
import br.ufrn.bank.exceptions.InconvenientUserException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.InvalidArgumentException;
import br.ufrn.bank.exceptions.MissingAuthenticationException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;
import br.ufrn.bank.exceptions.UnauthorizedAccountOperation;
import br.ufrn.bank.exceptions.UserAlreadyExistsException;
import br.ufrn.bank.model.Account;
import br.ufrn.bank.model.Deposit;
import br.ufrn.bank.model.Transfer;
import br.ufrn.bank.model.User;
import br.ufrn.bank.model.Withdraw;
import br.ufrn.bank.soap.interfaces.BankWebI;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceException;
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
    
    private User getAuthenticatedUser() {
        MessageContext mc = wsContext.getMessageContext();
        HttpSession session = ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();

        if (session == null) {
            throw new WebServiceException("Missing HTTP session");
        } else { 
            if (session.getAttribute("authenticatedUser") != null)
                return (User) session.getAttribute("authenticatedUser");
            else 
                return null;
        }
    }
    
    @Override
    public void signOut() throws MissingAuthenticationException {
        MessageContext mc = wsContext.getMessageContext();
        HttpSession session = ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();
        
        if (session == null) {
            throw new WebServiceException("Missing HTTP session");
        } else {
            if (session.getAttribute("authenticatedUser") != null) {
                session.removeAttribute("authenticatedUser");
                session.invalidate();
            } else {
                throw new MissingAuthenticationException("");
            }
        }
    }
    
    private boolean isAuthenticated() {
        return getAuthenticatedUser() != null;
    }
    
    private boolean allowAccountOperation(Account a) throws MissingAuthenticationException {
        
        User u = getAuthenticatedUser();
        
        if (u != null) {
            return (a.getUser().equals(u));
        } else {
            throw new MissingAuthenticationException("");
        }    
    }

    @Override
    public String createUser(String username, String password) throws InvalidArgumentException, UserAlreadyExistsException, InconvenientUserException {
        
        if (username == null || password == null) 
            throw new InvalidArgumentException("");
        
        if (getAuthenticatedUser() != null)
            throw new InconvenientUserException("");
        
        if (users.containsKey(username))
            throw new UserAlreadyExistsException(username);
        
        users.put(username, new User(username, password));
        
        return username;
    }

    @Override
    public String authenticate(String username, String password) throws InvalidArgumentException, AuthenticationFailedException {

        if (username == null || password == null)
            throw new InvalidArgumentException("");
        
        if (users.containsKey(username)) {
        
            User u = users.get(username);
            
            if (!u.getPassword().equals(password))
                throw new AuthenticationFailedException("");
            
            MessageContext mc = wsContext.getMessageContext();
            
            if ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST) != null) {
            
                HttpSession session = ((HttpServletRequest) mc.get(MessageContext.SERVLET_REQUEST)).getSession();

                if (session == null) {
                    throw new WebServiceException("Missing HTTP session");
                } else { 
                    session.setAttribute("authenticatedUser", u);
                }
            
            } else {
                throw new WebServiceException("No context provided");
            }
                        
        } else {
            throw new AuthenticationFailedException("");
        }

        return username;
    }

    
    @Override
    public Long createAccount(Long id, String password) throws AccountAlreadyExistsException, MissingAuthenticationException, InvalidArgumentException {

        if (id == null || password == null)
            throw new InvalidArgumentException("");
        
        if (!isAuthenticated())
            throw new MissingAuthenticationException("");
        
        if(accounts.containsKey(id)) 
            throw new AccountAlreadyExistsException(String.valueOf(id));
        
        accounts.put(id, new Account(getAuthenticatedUser(), id, password));			
        
        return id;
    }

    @Override
    public void deposit(Double value, Long account, Boolean updateOperation) throws InvalidArgumentException, InvalidAccountException, MissingAuthenticationException {
        
        if (value == null || account == null || updateOperation == null)
            throw new InvalidArgumentException("");
        
        if (!isAuthenticated())
            throw new MissingAuthenticationException("");
        
        if (value <= 0)
            throw new InvalidArgumentException("You must deposit only positive values");
        
        if(!accounts.containsKey(account)) 
            throw new InvalidAccountException(String.valueOf(account));
        
        accounts.get(account).updateBalance(value);
        
        if(updateOperation){
            accounts.get(account).updateOperations(new Deposit(value));
        }
    }

    @Override
    public void withdraw(Long account, String password, Double value, Boolean updateOperation) throws InvalidArgumentException, InvalidAccountException, AccountAuthenticationException, NotEnoughBalanceException, MissingAuthenticationException, UnauthorizedAccountOperation {
         
        if (account == null || password == null || value == null || updateOperation == null)
            throw new InvalidArgumentException("");
        
        if (!isAuthenticated())
            throw new MissingAuthenticationException("");
                
        if(value <= 0)
            throw new InvalidArgumentException("O saque requer valores positivos!");
        
        if(!accounts.containsKey(account))
            throw new InvalidAccountException(String.valueOf(account));
            
        Account acc = accounts.get(account);
        
        if (allowAccountOperation(acc)) {
        
            if(!acc.getPassword().equals(password)) 
                throw new AccountAuthenticationException("");

            if(acc.getBalance() < value)
                throw new NotEnoughBalanceException(String.valueOf(account));

            acc.updateBalance(-value);

            if(updateOperation){
                acc.updateOperations(new Withdraw(-value));
            }
            
        } else {
            throw new UnauthorizedAccountOperation("");
        }
    }

    @Override
    public void transfer(Long account, String password, Double value, Long anotherAccount) throws InvalidArgumentException, InvalidAccountException, AccountAuthenticationException, NotEnoughBalanceException, MissingAuthenticationException, UnauthorizedAccountOperation {
        
       if (account == null || password == null || value == null || anotherAccount == null)
            throw new InvalidArgumentException("");
        
       if (!isAuthenticated())
            throw new MissingAuthenticationException("");        
        
        if(Objects.equals(account, anotherAccount)){
            throw new InvalidArgumentException("As contas precisam ser distintas!");
        }
    
        if(!accounts.containsKey(anotherAccount))
            throw new InvalidAccountException(String.valueOf(anotherAccount));
        
        withdraw(account, password, value, false);        
        deposit(value, anotherAccount, false);
        
        accounts.get(account).updateOperations(new Transfer(anotherAccount, -value));
        accounts.get(anotherAccount).updateOperations(new Transfer(account, value));
        
    }

    @Override
    public String[] statement(Long account, String password) throws InvalidArgumentException, InvalidAccountException, AccountAuthenticationException, MissingAuthenticationException, UnauthorizedAccountOperation {
        
        if (account == null || password == null)
            throw new InvalidArgumentException("");
        
        if (!isAuthenticated())
            throw new MissingAuthenticationException("");
        
        if (!accounts.containsKey(account))
            throw new InvalidAccountException(String.valueOf(account));
        
        Account acc = accounts.get(account);

        if (allowAccountOperation(acc)) {
        
            if (!acc.getPassword().equals(password))
                throw new AccountAuthenticationException("");

            ArrayList<String> output = acc.getStatement();

            return output.stream().toArray(String[]::new);
            
        } else {
            throw new UnauthorizedAccountOperation("");
        }
    }

    @Override
    public Double getBalance(Long account, String password) throws InvalidArgumentException, InvalidAccountException, AccountAuthenticationException, MissingAuthenticationException, UnauthorizedAccountOperation {
        
        if (account == null || password == null)
            throw new InvalidArgumentException("");
        
        if (!isAuthenticated())
            throw new MissingAuthenticationException("");
        
        if (!accounts.containsKey(account))
            throw new InvalidAccountException(String.valueOf(account));
        
        Account acc = accounts.get(account);
        
        if (allowAccountOperation(acc)) {
        
            if (!acc.getPassword().equals(password))
                throw new AccountAuthenticationException("");

            return acc.getBalance();
            
        } else {
            throw new UnauthorizedAccountOperation("");
        }
    }

}
