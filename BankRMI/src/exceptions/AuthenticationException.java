/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.rmi.RemoteException;

/**
 *
 * @author vitorgreati
 */
public class AuthenticationException extends RemoteException {
    
    public AuthenticationException() {
        super("Invalid account/password");
    }
    
}
