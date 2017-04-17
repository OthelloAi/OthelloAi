package app;

import app.game.Game;
import app.game.Move;

import java.util.Stack;

/**
 * @author Joël Hoekstra
 */
public class MoveHandler implements Runnable {

    private static Stack<Move> moveStack = new Stack<>();
    private Game game;

    public MoveHandler(Game game) {
        this.game = game;
    }

    public static void addMove(Move move) {
        moveStack.push(move);
    }

    public static void clear() {
        moveStack.clear();
    }

    @Override
    public void run() {
        try {
            // process here
            while (true) {
                if (!moveStack.empty()) {
                    if (!game.isProcessingMove()) {
                        Move move = moveStack.pop();
                        game.processMove(move);
                    }
                } else {
                    if (game.isYourTurn()) {
                        if (game.usesAI()) {
                            int nextMove = game.getActor().getNext(game.getPossibleMoves());
                            game.handleMove(new Move(nextMove, game.getLoggedInPlayer()));
                        }
                    }
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
