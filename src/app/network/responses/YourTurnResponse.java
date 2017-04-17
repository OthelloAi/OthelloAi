package app.network.responses;

import app.App;
import app.utils.Debug;

/**
 * @author Joël Hoekstra
 */
public class YourTurnResponse implements Response {

    private final App app;

    public YourTurnResponse(App app) {
        this.app = app;
        Debug.println("yourturnresponse");
    }

    @Override
    public void handle() {
        String message = app.getUser().getUsername() + ", it's your turn";
        app.getGUI().setLeftStatusText(message);
        app.getGame().setYourTurn(true);
        app.getGame().sendAIMove();
    }
}
