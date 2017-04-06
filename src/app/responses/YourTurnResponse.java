package app.responses;

import app.Game;
import app.Move;
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
//        game.getMatch()
        System.out.println("It's your turn");
        // get next move and then send it
//        game.requestNewMove()
//        game.handleCommand(new MoveCommand(game.getNextMove()));
    }
}
