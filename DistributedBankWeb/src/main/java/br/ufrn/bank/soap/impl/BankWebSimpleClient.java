package br.ufrn.bank.soap.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.ufrn.bank.exceptions.AuthenticationFailedException;
import br.ufrn.bank.exceptions.UserAlreadyExistsException;
import br.ufrn.bank.soap.interfaces.BankWebI;
import javax.xml.ws.BindingProvider;

public class BankWebSimpleClient {

    public static void main(String[] args) throws UserAlreadyExistsException, AuthenticationFailedException {
        try {
            URL url = new URL("http://localhost:8080/DistributedBankWeb/BankImplService?wsdl");
            QName qname = new QName("http://impl.soap.bank.ufrn.br/", "BankImplService");
            Service ws = Service.create(url, qname);
            BankWebI bank = ws.getPort(BankWebI.class);
            ((BindingProvider) bank).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
            
            String username = bank.createUser("greati", "123");
            
            System.out.println("Created user: " + username);
            
            //Long accId = bank.createAccount(null, 10L, "123");

            //System.out.println("Created account: " + accId);
            
            
            String username2 = bank.authenticate("greati", "123");
            
            System.out.println("Create: " + username2);


        } catch (MalformedURLException e) {
            //
        } catch (AuthenticationFailedException e) {
            e.printStackTrace();
        }
    }
	
}
