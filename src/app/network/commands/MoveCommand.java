package app.network.commands;

import app.game.Move;

/**
 * @author Joël Hoekstra
 */
public class MoveCommand implements Command {
    private Move move;

    public MoveCommand(Move move) {
        this.move = move;
    }

    @Override
    public String toString() {
        return "move " + move.getPosition();
    }
}
