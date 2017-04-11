package app.actors;

import app.Token;

/**
 * @author Joël Hoekstra
 */
public class ScoredMove {
    public int score;
    public Token index;
    public int position = -1;

    public ScoredMove() {

    }

    public ScoredMove(Token index, int score) {
        this.index = index;
        this.score = score;
    }


}
