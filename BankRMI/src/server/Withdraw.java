package server;

import java.util.Date;

/**
 * Represents a withdraw operation.
 * 
 * @author vitorgreati
 */
public class Withdraw extends Operation{
	
    public Withdraw(Double value) {
        this.date = new Date();
        this.type = "WITHDRAW";
        this.value = value;
    }
}
