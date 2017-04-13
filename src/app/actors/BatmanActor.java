package app.actors;

import app.batman.*;
import java.util.ArrayList;



/**
 * @author Joël Hoekstra
 */
public class BatmanActor implements Actor {

    public BatmanActor() {
        Batman batman = new Batman();
        System.out.println(batman.toString());
    }

    @Override
    public int getNext(ArrayList<Integer> possibleMoves) {
        return possibleMoves.get(0);
    }
}
