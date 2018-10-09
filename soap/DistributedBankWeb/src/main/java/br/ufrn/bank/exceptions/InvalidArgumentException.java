/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.exceptions;

/**
 * Thrown when an account number already exists.
 * 
 * @author vitorgreati
 */
public class InvalidArgumentException extends Exception {
    
    public InvalidArgumentException(String msg) {
        super(msg);
    }
        
}
