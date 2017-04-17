package app.network.responses;

import app.App;
import app.game.Move;

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
        app.getGame().setYourTurn(true);

//        if (app.getGame().usesAI()) {
//            int aiMove = app.getGame().getActor().getNext(app.getGame().getPossibleMoves());
//            Move move = new Move(aiMove, app.getUser());
//            app.getGame().handleMove(aiMove);
////            app.getGame().handleMove(move);
//        }
        // if human actor then do nothing
        // else if ai then get best move and send
        // get next move and then send it
//        game.requestNewMove()
//        game.handleCommand(new MoveCommand(game.getNextMove()));
    }
}
