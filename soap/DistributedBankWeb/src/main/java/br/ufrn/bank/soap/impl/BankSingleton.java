/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.soap.impl;

import br.ufrn.bank.soap.interfaces.BankWebI;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

/**
 *
 * @author vinihcampos
 */
public final class BankSingleton {
    private static BankWebI BANK;
    
    public static BankWebI getInstance() throws Exception{
        if(BANK == null){
            URL url = new URL("http://localhost:8080/DistributedBankWeb/BankImplService?wsdl");
            QName qname = new QName("http://impl.soap.bank.ufrn.br/", "BankImplService");
            Service ws = Service.create(url, qname);
            BANK = ws.getPort(BankWebI.class);
            ((BindingProvider) BANK).getRequestContext().put(BindingProvider.SESSION_MAINTAIN_PROPERTY, true);
        }
        
        return BANK;
    }
    
    public static void renewInstance(){
        BANK = null;
    }
}
