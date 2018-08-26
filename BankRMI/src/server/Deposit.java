package server;

import java.util.Date;

/**
 * Represents a deposit operation.
 * 
 * @author vitorgreati
 */
public class Deposit extends Operation{
    public Deposit(Double value) {
        this.date = new Date();
        this.type = "DEPOSIT";
        this.value = value;
    }
}
