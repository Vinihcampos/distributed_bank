package br.ufrn.bank.rmi.impl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


/**
 * Setup bank RMI server.
 * 
 * @author viniciuscampos
 */
public class BankRMIServer {
	
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);		
        BankImpl bank = new BankImpl();

        Naming.rebind("rmi://localhost/Bank", bank);
        System.out.println("Servidor pronto e registrado no RMI Registry.");
    }
	
}
