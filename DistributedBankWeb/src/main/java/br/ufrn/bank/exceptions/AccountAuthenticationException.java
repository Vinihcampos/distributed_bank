/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.exceptions;

/**
 * Thrown when the authentication fails.
 * 
 * @author vitorgreati
 */
public class AccountAuthenticationException extends Exception {
    
    public AccountAuthenticationException() {
        super("Invalid account/password");
    }
    
}
