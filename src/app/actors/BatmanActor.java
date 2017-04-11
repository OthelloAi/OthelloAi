package app.actors;

import app.Move;
import app.batman.*;
import java.util.ArrayList;



/**
 * @author Joël Hoekstra
 */
public class BatmanActor implements Actor {

    public BatmanActor() {
        Batman batman = new Batman();
    }

    @Override
    public Move getNextMove(ArrayList<Move> possibleMoves) {
        return possibleMoves.get(0);
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(0);
    }
}
