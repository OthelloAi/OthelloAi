package app.network.responses;

import app.App;

/**
 * @author Joël Hoekstra
 */
public class YourTurnResponse implements Response {

    private final App app;

    public YourTurnResponse(App app) {
        this.app = app;
    }

    @Override
    public void handle() {
        String message = app.getUser().getUsername() + ", it's your turn";
        app.getGUI().setLeftStatusText(message);

        // if human actor then do nothing
        // else if ai then get best move and send
        // get next move and then send it
//        game.requestNewMove()
//        game.handleCommand(new MoveCommand(game.getNextMove()));
    }
}
