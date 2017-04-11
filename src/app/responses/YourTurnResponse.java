package app.responses;

import app.Game;
import app.Move;
import app.actors.Actor;
import app.actors.IterativeActor;
import app.commands.MoveCommand;

/**
 * @author Joël Hoekstra
 */
public class YourTurnResponse implements Response {

    private Game game;

    public YourTurnResponse(Game game) {
        this.game = game;
    }

    @Override
    public void handle() {
        String message = game.getLoggedInPlayer().getUsername() + ", it's your turn";
        // TODO: 11-4-2017 Create notification list with 'you turn' notification in it 
        // game.showNotification(message);
        System.out.println(message);

        // if human actor then do nothing
        // else if ai then get best move and send
        // get next move and then send it
//        game.requestNewMove()
//        game.handleCommand(new MoveCommand(game.getNextMove()));
    }
}
