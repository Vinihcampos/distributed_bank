package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import interfaces.IBank;

public class Bank extends UnicastRemoteObject implements IBank, Serializable{
		
    private static final long serialVersionUID = 1L;

    private Map<Long, Account> accounts;

    public Bank() throws RemoteException {
        accounts = new HashMap<>();
    }

    @Override
    public Long createAccount(Long id, String password) {
        if(accounts.containsKey(id)) {
            return null;
        }else {
            accounts.put(id, new Account(id, password));			
            return id;
        }
    }

    @Override
    public Boolean deposit(Double value, Long account) {
        if(value <= 0 || !accounts.containsKey(account)) {
            return false;
        }else {
            accounts.get(account).updateBalance(value);
            accounts.get(account).updateOperations(new Deposit(value));
            return true;
        }
    }

    @Override
    public Boolean withdraw(Long account, String password, Double value) {
        if(!accounts.containsKey(account)) {
            return false;
        }else {
            if(!accounts.get(account).getPassword().equals(password) || value <= 0 || accounts.get(account).getBalance() < value) {
                return false;
            }

            accounts.get(account).updateBalance(-value);
            accounts.get(account).updateOperations(new Withdraw(-value));

            return true;
        }
    }

    @Override
    public Boolean transfer(Long account, String password, Double value, Long anotherAccount) {
        if(!accounts.containsKey(account) || !accounts.containsKey(anotherAccount)) {
            return false;
        }else {
            if(!accounts.get(account).getPassword().equals(password) || value <= 0 || accounts.get(account).getBalance() < value) {
                return false;
            }

            accounts.get(account).updateBalance(-value);
            accounts.get(account).updateOperations(new Transfer(anotherAccount, -value));
            accounts.get(anotherAccount).updateBalance(value);
            accounts.get(anotherAccount).updateOperations(new Transfer(account, value));

            return true;
        }
    }

    @Override
    public String statement(Long account, String password) {
        if(!accounts.containsKey(account) || !accounts.get(account).getPassword().equals(password)) {
            return null;
        }else {
            return accounts.get(account).toString();
        }
    }

}
