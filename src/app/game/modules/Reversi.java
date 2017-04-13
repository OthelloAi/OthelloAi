package app.game.modules;

import app.game.Move;
import app.Token;

/**
 * @author Joël Hoekstra
 */
public class Reversi implements Module {
    @Override
    public boolean isValidMove(Move move, Token token) {
        return false;
    }
}
