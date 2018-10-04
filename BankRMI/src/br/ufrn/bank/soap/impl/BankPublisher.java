/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufrn.bank.soap.impl;

import javax.xml.ws.Endpoint;

/**
 *
 * @author vitorgreati
 */
public class BankPublisher {
    
    public static void main(String args[]) {

        Endpoint.publish("http://127.0.0.1:9876/BankWeb", 
                        new BankImpl());
        System.out.println("Bank published.");
        
    }
    
}
