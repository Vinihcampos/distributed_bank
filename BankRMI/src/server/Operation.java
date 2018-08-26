package server;

import java.util.Date;

/**
 * Represents an operation to be performed
 * in the bank.
 * 
 * @author vitorgreati
 */
public abstract class Operation {
    protected Date date;
    protected String type;
    protected Double value;

    public Operation() {
        this.type = "";
        this.value = 0.0;
        this.date = new Date();
    }	

    @Override
    public String toString() {
        return String.valueOf(date) + ", " + type + ": R$ "  + String.valueOf(value);
    }
}
