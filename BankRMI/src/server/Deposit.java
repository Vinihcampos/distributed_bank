package server;

import java.util.Date;

/**
 * Represents a deposit operation.
 * 
 * @author viniciuscampos
 */
public class Deposit extends Operation {
    public Deposit(Double value) {
        this.date = new Date();
        this.type = "DEPOSIT";
        this.value = value;
    }
}
