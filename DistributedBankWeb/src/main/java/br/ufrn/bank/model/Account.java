package br.ufrn.bank.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a bank account.
 * 
 * @author viniciuscampos
 */
public class Account {

    private User user;
    private Long id;
    private String password;
    private Double balance;
    private List<Operation> operations;

    public Account(User user, Long id, String password) {
        this.user = user;
        this.id = id;
        this.password = password;
        this.balance = 0.0;
        this.operations = Collections.synchronizedList(new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Double getBalance() {
        return balance;
    }

    public void updateBalance(Double value) {
        this.balance += value;
    }

    public void updateOperations(Operation operation) {
        operations.add(operation);
    }
    
    public ArrayList<String> getStatement() {
        ArrayList<String> stmt = new ArrayList<>();
        for(Operation o : operations) 
            stmt.add(o.toString() + "\n");
        return stmt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
		
}
