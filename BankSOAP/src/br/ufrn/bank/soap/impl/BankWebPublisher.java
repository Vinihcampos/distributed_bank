package br.ufrn.bank.soap.impl;

import javax.xml.ws.Endpoint;

public class BankWebPublisher {

	public static void main(String args[]) {
		
		Endpoint.publish("http://127.0.0.1:9876/BankWeb", 
				new BankWeb());
		
	}
	
}
