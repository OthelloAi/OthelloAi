package app.responses;

import java.util.ArrayList;

/**
 * @author js
 */
public class Payload {
    private ArrayList<Object> payload;
    public Payload(ArrayList<Object> payload) {
        this.payload = payload;
    }

    public ArrayList<Object> getPayload() {
        return payload;
    }
}
