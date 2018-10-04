package br.ufrn.bank.soap.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.ufrn.bank.exceptions.AccountAlreadyExistsException;
import br.ufrn.bank.soap.interfaces.IBankWeb;

public class BankWebSimpleClient {

	public static void main(String[] args) {
		try {
			URL url = new URL("http://127.0.0.1:9876/BankWeb?wsdl");
			QName qname = new QName("http://impl.soap.bank.ufrn.br/", "BankWebService");
			Service ws = Service.create(url, qname);
			IBankWeb bank = ws.getPort(IBankWeb.class);
			
			Long accId = bank.createAccount(10L, "123");
			
			System.out.println("Created account: " + accId);
			
		} catch (MalformedURLException e) {
			
		} catch (AccountAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
