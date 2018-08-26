package server;

import java.util.Date;

/**
 * Represents a transfer operation.
 * 
 * @author vitorgreati
 */
public class Transfer extends Operation{
    private Long id;

    public Transfer(Long id, Double value) {
        this.date = new Date();
        this.type = "TRANSFER";
        this.value = value;
        this.id = id;
    }

    @Override
    public String toString() {
        if(value > 0)
            return String.valueOf(date) + ", " + type + " from " + id + ": R$ "  + String.valueOf(value);
        else
            return String.valueOf(date) + ", " + type + " to " + id + ": R$ "  + String.valueOf(value);
    }
}
