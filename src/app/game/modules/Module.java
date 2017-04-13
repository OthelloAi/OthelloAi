package app.game.modules;

import app.game.Move;
import app.Token;

/**
 * @author Joël Hoekstra
 */
public interface Module {
    public boolean isValidMove(Move move, Token token);
}
