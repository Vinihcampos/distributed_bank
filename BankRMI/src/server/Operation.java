package server;

import java.util.Date;

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
