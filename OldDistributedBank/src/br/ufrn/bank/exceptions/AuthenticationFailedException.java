/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.exceptions;

/**
 * Thrown when authentication fails.
 * 
 * @author vitorgreati
 */
public class AuthenticationFailedException extends Exception {
    public AuthenticationFailedException(){
        super("Invalid username and/or password");
    }
}
