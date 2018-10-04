package br.ufrn.bank.soap.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.jws.WebService;
import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AuthenticationException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;
import br.ufrn.bank.model.Account;
import br.ufrn.bank.model.Deposit;
import br.ufrn.bank.model.Transfer;
import br.ufrn.bank.model.Withdraw;
import br.ufrn.bank.soap.interfaces.BankWebI;

/**
 * A remote bank and its operations.
 * 
 * @author vitorgreati, viniciuscampos
 */
@WebService(endpointInterface = "br.ufrn.bank.soap.interfaces.BankWebI")
public class BankImpl implements BankWebI {
		
    private final Map<Long, Account> accounts;

    public BankImpl() {
        accounts = new HashMap<>();
    }

    @Override
    public Long createAccount(Long id, String password) throws AccountAlreadyExistsException {
        
        if(accounts.containsKey(id)) 
            throw new AccountAlreadyExistsException(id);
        
        accounts.put(id, new Account(id, password));			
        
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
