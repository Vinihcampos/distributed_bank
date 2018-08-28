package server;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        final DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        if(getValue() > 0)
            return df.format(getDate()) + "," + getType() + " from " + id + ","  + String.valueOf(getValue());
        else
            return df.format(getDate()) + "," + getType() + " to " + id + ","  + String.valueOf(getValue());
    }
}
