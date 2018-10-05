package br.ufrn.bank.model;

import java.util.Date;

/**
 * Represents a withdraw operation.
 * 
 * @author viniciuscampos
 */
public class Withdraw extends Operation{
	
    public Withdraw(Double value) {
        this.date = new Date();
        this.type = "SAQUE";
        this.value = value;
    }
}
