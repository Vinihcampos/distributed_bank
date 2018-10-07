/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.soap.tests;

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
import br.ufrn.bank.soap.interfaces.BankWebI;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for the bank web service.
 * 
 * @author vitorgreati
 */
public class BankImplTests {
    
    private static BankWebI bank;
    private static String username;
    private static String anotherUsername;
    
    @BeforeClass
    public static void setup() {
        try {
            BankImplTests.username = UUID.randomUUID().toString();
            
            BankImplTests.anotherUsername = UUID.randomUUID().toString();
            
            URL url = new URL("http://localhost:8080/DistributedBankWeb/BankImplService?wsdl");
            QName qname = new QName("http://impl.soap.bank.ufrn.br/", "BankImplService");
            Service ws = Service.create(url, qname);
            bank = ws.getPort(BankWebI.class);
            ((BindingProvider) bank).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
            
            bank.createUser(username, "123");
            bank.createUser(anotherUsername, "123");
            
            bank.authenticate(username, "123");
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserAlreadyExistsException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InconvenientUserException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AuthenticationFailedException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }
    
    @Test
    public void testAccountCreationAndBalance() throws InvalidArgumentException, AccountAlreadyExistsException, MissingAuthenticationException, InvalidAccountException, AccountAuthenticationException, UnauthorizedAccountOperation {
        Long acc = getRandAccountNumber();
        bank.createAccount(acc, "123");
        assertEquals(0.0, bank.getBalance(acc, "123"), 0.0);
    }
    
    @Test
    public void sameNumberAccountTest() {
        try {
            Long acc = getRandAccountNumber();
            bank.createAccount(acc, "123456789");
            bank.createAccount(acc, "123456789");
            fail("Accounts with the same name");
        } catch (InvalidArgumentException | AccountAlreadyExistsException | MissingAuthenticationException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    @Test
    public void depositTest() throws InvalidArgumentException, InvalidAccountException, AccountAlreadyExistsException, AccountAuthenticationException, MissingAuthenticationException, UnauthorizedAccountOperation {
        Long acc = getRandAccountNumber();
        bank.createAccount(acc, "123456789");
        assertEquals(new Double(0.0), bank.getBalance(acc, "123456789"));
        bank.deposit(100.0, acc, true);
        assertEquals(new Double(100.0), bank.getBalance(acc, "123456789"));
        try {
            bank.deposit(-100.0, 12345L, true);
            fail("Negative values not allowed");
        } catch (InvalidArgumentException e) {}
    }
    
    @Test
    public void withdrawTest() throws InvalidArgumentException, AccountAlreadyExistsException, InvalidAccountException, MissingAuthenticationException, AccountAuthenticationException, UnauthorizedAccountOperation, NotEnoughBalanceException {
        Long acc = getRandAccountNumber();        
        bank.createAccount(acc, "123456789");
        bank.deposit(1000.0, acc, true);
        bank.withdraw(acc, "123456789", 200.0, true);
        assertEquals(new Double(800.0), bank.getBalance(acc, "123456789"));
        bank.withdraw(acc, "123456789", 800.0, true);
        assertEquals(new Double(0.0), bank.getBalance(acc, "123456789"));
    }
    
    @Test
    public void withdrawExceptionsTest() {
        try {
            Long acc = getRandAccountNumber();       
            
            bank.withdraw(acc, "123456789", -100.0, true);
            fail("Negative values not allowed");

            bank.withdraw(acc, "123456789", 10.0, true);
            fail("Not enough balance");
            
            bank.withdraw(acc, "12345678", 10.0, true);
            fail("Authentication fail");
        } catch (InvalidArgumentException | InvalidAccountException | AccountAuthenticationException | NotEnoughBalanceException | MissingAuthenticationException | UnauthorizedAccountOperation ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Test
    public void transferTest() throws AccountAlreadyExistsException, InvalidAccountException, NotEnoughBalanceException, InvalidArgumentException, MissingAuthenticationException, AccountAuthenticationException, UnauthorizedAccountOperation {
        Long acc1 = getRandAccountNumber();
        Long acc2 = getRandAccountNumber(); 
        
        bank.createAccount(acc1, "123");
        bank.createAccount(acc2, "123");
        
        assertEquals(new Double(0.0), bank.getBalance(acc1, "123"));
        assertEquals(new Double(0.0), bank.getBalance(acc2, "123"));
        bank.deposit(300.0, acc1, true);
        bank.transfer(acc1, "123", 100.0, acc2);
        assertEquals(new Double(200.0), bank.getBalance(acc1, "123"));
        assertEquals(new Double(100.0), bank.getBalance(acc2, "123"));
    }
    
    @Test
    public void transferExceptionsTest() {
        
        Long acc1 = getRandAccountNumber(), acc2 = getRandAccountNumber();
        
        try {
            
            acc1 = bank.createAccount(acc1, "123");
            acc2 = bank.createAccount(acc2, "123");
            
            bank.deposit(1000.0, acc1, true);
            
            bank.transfer(acc1, "123", -100.0, acc2);
            fail("Value can't be negative");
            
            bank.transfer(acc1, "123", 1001.0, acc2);
            fail("Not enough balance to transfer");
            
            bank.transfer(acc1, "1234", 1001.0, acc2);
            fail("Authentication fail");
            
            bank.transfer(acc1, "123", 1001.0, acc1);
            fail("Authentication fail");
            
        } catch(InvalidArgumentException e) {            
            try {
                assertEquals(new Double(1000.0), bank.getBalance(acc1, "123"));
                assertEquals(new Double(0.0), bank.getBalance(acc2, "123"));
            } catch (InvalidArgumentException | AccountAuthenticationException | MissingAuthenticationException | UnauthorizedAccountOperation | InvalidAccountException ex) {
                Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (AccountAlreadyExistsException | InvalidAccountException | NotEnoughBalanceException ex) {
            //Logger.getLogger(BankTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccountAuthenticationException | MissingAuthenticationException | UnauthorizedAccountOperation ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    @Test
    public void logoffTest() throws AuthenticationFailedException {
    
        try {
            
            Long acc = getRandAccountNumber();
            bank.createAccount(acc, "123");
            
            bank.signOut();
            
            bank.getBalance(acc, "123");
            fail("Unauthorized user!");
                        
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccountAlreadyExistsException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MissingAuthenticationException ex) {
            try {
                bank.authenticate(username, "123");
            } catch (InvalidArgumentException ex1) {
                Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (InvalidAccountException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccountAuthenticationException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnauthorizedAccountOperation ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void authorizationAccountTest() throws InvalidAccountException, AuthenticationFailedException, AccountAuthenticationException {
        Long acc1 = getRandAccountNumber(), acc2 = getRandAccountNumber();
        try {
            bank.createAccount(acc1, "123");
            bank.createAccount(acc2, "123");   
            
            bank.deposit(10.0, acc1, Boolean.TRUE);
            
            bank.signOut();
            
            bank.authenticate(anotherUsername, "123");
            
            bank.getBalance(acc1, username);
            fail("Unauthorized user");
            
        } catch (InvalidArgumentException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (AccountAlreadyExistsException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MissingAuthenticationException ex) {
            Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnauthorizedAccountOperation ex) {
            try {
                bank.signOut();
                try {
                    bank.statement(acc1, "123");
                    fail("Unauthorized");
                } catch (UnauthorizedAccountOperation ex1) {
                    Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex1);
                }
            } catch (InvalidArgumentException ex1) {
                Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex1);
            } catch (MissingAuthenticationException ex1) {
                Logger.getLogger(BankImplTests.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
    
    }
    
    private Long getRandAccountNumber() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits());
    }
}
