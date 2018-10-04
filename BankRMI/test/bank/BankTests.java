/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank;

import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.exceptions.AuthenticationException;
import br.ufrn.bank.exceptions.InvalidAccountException;
import br.ufrn.bank.exceptions.NotEnoughBalanceException;
import br.ufrn.bank.rmi.interfaces.IBank;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import br.ufrn.bank.model.Account;
import br.ufrn.bank.rmi.impl.BankImpl;
import br.ufrn.bank.model.Deposit;
import br.ufrn.bank.model.Operation;
import br.ufrn.bank.model.Transfer;
import br.ufrn.bank.model.Withdraw;


/**
 * Tests for the remote bank.
 * 
 * @author vitorgreati
 */
public class BankTests {
    
    private final IBank bank;
    
    public BankTests() throws NotBoundException, MalformedURLException, RemoteException {
        bank = (IBank) Naming.lookup("rmi://localhost/Bank");
    }
    
    @Test
    public void createAccountTest() throws RemoteException, AccountAlreadyExistsException, InvalidAccountException, AuthenticationException {
        bank.createAccount(123L, "123456789");
        assertEquals(new ArrayList<>(), bank.statement(123L, "123456789"));
    }
    
    @Test
    public void sameNumberAccountTest() {
        try {
            bank.createAccount(1234L, "123456789");
            bank.createAccount(1234L, "123456789");
            fail("Accounts with the same name");
        } catch (RemoteException | AccountAlreadyExistsException ex) {
            //Logger.getLogger(BankTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void depositTest() throws RemoteException, AccountAlreadyExistsException, InvalidAccountException, AuthenticationException {
        bank.createAccount(12345L, "123456789");
        assertEquals(new Double(0.0), bank.getBalance(12345L, "123456789"));
        bank.deposit(100.0, 12345L, true);
        assertEquals(new Double(100.0), bank.getBalance(12345L, "123456789"));
        try {
            bank.deposit(-100.0, 12345L, true);
            fail("Negative values not allowed");
        } catch (IllegalArgumentException e) {}
    }
    
    @Test
    public void withdrawTest() throws RemoteException, AccountAlreadyExistsException, IllegalArgumentException, InvalidAccountException, AuthenticationException, NotEnoughBalanceException {
        bank.createAccount(123456L, "123456789");
        bank.deposit(1000.0, 123456L, true);
        bank.withdraw(123456L, "123456789", 200.0, true);
        assertEquals(new Double(800.0), bank.getBalance(123456L, "123456789"));
        bank.withdraw(123456L, "123456789", 800.0, true);
        assertEquals(new Double(0.0), bank.getBalance(123456L, "123456789"));

    }
    
    @Test
    public void withdrawExceptionsTest() {
        try {
            bank.withdraw(123456L, "123456789", -100.0, true);
            fail("Negative values not allowed");

            bank.withdraw(123456L, "123456789", 10.0, true);
            fail("Not enough balance");
            
            bank.withdraw(123456L, "12345678", 10.0, true);
            fail("Authentication fail");
            
        } catch (RemoteException | IllegalArgumentException | InvalidAccountException | AuthenticationException | NotEnoughBalanceException ex) {
            Logger.getLogger(BankTests.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @Test
    public void transferTest() throws RemoteException, AccountAlreadyExistsException, InvalidAccountException, AuthenticationException, NotEnoughBalanceException {
        Long acc1 = bank.createAccount(2L, "123");
        Long acc2 = bank.createAccount(1L, "123");
        assertEquals(new Double(0.0), bank.getBalance(acc1, "123"));
        assertEquals(new Double(0.0), bank.getBalance(acc2, "123"));
        bank.deposit(300.0, acc1, true);
        bank.transfer(acc1, "123", 100.0, acc2);
        assertEquals(new Double(200.0), bank.getBalance(acc1, "123"));
        assertEquals(new Double(100.0), bank.getBalance(acc2, "123"));
    }
    
    @Test
    public void transferExceptionsTest() {
        
        Long acc1 = null, acc2 = null;
        
        try {
            
            acc1 = bank.createAccount(3L, "123");
            acc2 = bank.createAccount(4L, "123");
            
            bank.deposit(1000.0, acc1, true);
            
            bank.transfer(acc1, "123", -100.0, acc2);
            fail("Value can't be negative");
            
            bank.transfer(acc1, "123", 1001.0, acc2);
            fail("Not enough balance to transfer");
            
            bank.transfer(acc1, "1234", 1001.0, acc2);
            fail("Authentication fail");
            
            bank.transfer(acc1, "123", 1001.0, acc1);
            fail("Authentication fail");
            
        } catch(RemoteException | IllegalArgumentException e) {            
            try {
                assertEquals(new Double(1000.0), bank.getBalance(acc1, "123"));
                assertEquals(new Double(0.0), bank.getBalance(acc2, "123"));
            } catch (RemoteException | InvalidAccountException | AuthenticationException ex) {
                //Logger.getLogger(BankTests.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (AccountAlreadyExistsException | InvalidAccountException | AuthenticationException | NotEnoughBalanceException ex) {
            //Logger.getLogger(BankTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
