package server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;


/**
 * Setup bank RMI server.
 * 
 * @author vitorgreati
 */
public class BankEngine {
	
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        LocateRegistry.createRegistry(1099);		
        Bank bank = new Bank();

        Naming.rebind("rmi://localhost/Bank", bank);
        System.out.println("Servidor pronto e registrado no RMI Registry.");
    }
	
}
