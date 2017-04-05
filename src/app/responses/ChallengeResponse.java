package app.responses;

import java.util.Map;

/**
 * @author js
 */
public class ChallengeResponse implements Response {

    public ChallengeResponse(Map<String, String> params) {

    }

    @Override
    public void handle() {
        System.out.println("challenge has been send");
    }
}
