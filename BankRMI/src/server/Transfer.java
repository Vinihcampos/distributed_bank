package server;

import java.util.Date;

/**
 * Represents a transfer operation.
 * 
 * @author viniciuscampos
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
        if(getValue() > 0)
            return String.valueOf(getDate()) + ", " + getType() + " from " + id + ": R$ "  + String.valueOf(getValue());
        else
            return String.valueOf(getDate()) + ", " + getType() + " to " + id + ": R$ "  + String.valueOf(getValue());
    }
}
