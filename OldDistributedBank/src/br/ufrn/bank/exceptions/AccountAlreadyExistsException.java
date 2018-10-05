/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.exceptions;

import java.rmi.RemoteException;

/**
 * Thrown when an account number already exists.
 * 
 * @author vitorgreati
 */
public class AccountAlreadyExistsException extends Exception {
    
    public AccountAlreadyExistsException(Long account) {
        super("Account number " + account + " already in use");
    }
    
}
