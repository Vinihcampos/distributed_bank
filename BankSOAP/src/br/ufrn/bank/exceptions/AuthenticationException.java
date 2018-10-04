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
public class AuthenticationException extends Exception {
    
    public AuthenticationException() {
        super("Invalid account/password");
    }
    
}
