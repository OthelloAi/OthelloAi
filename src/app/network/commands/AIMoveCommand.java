package app.network.commands;

import app.game.Game;
import app.game.Move;

import java.util.concurrent.CountDownLatch;

/**
 * @author Joël Hoekstra
 */
public class AIMoveCommand implements Command {
    private Move move;
    private boolean wait;
    private final CountDownLatch sendMoveLatch = new CountDownLatch(1);
    private Game game;

    private static final int REQUEST_TIMEOUT_SECS = 1;

    public AIMoveCommand(Game game) {
        this.game = game;
        this.move = move;
        wait = true;
    }

    @Override
    public String toString() {
        // latch here before return
        if (wait) {
            long start = System.currentTimeMillis();
            Thread t = new Thread(() -> {
                while(true) {
                    long current = System.currentTimeMillis();
                    long diff = ((current - start) / 1);

//                    Debug.println(diff);
                    if (diff >= 500) {
                        game.redoLastMove();
                        sendMoveLatch.countDown();
                        break;
                    }
                }
            });
            t.setDaemon(true);
            t.start();

            try {
                sendMoveLatch.await();



                int movePos = game.getActor().getNext(game.getPossibleMoves());
                System.out.println("SEND AI move " + movePos + " from player " + game.getLoggedInPlayer().getUsername() + " with token " + game.getLoggedInPlayer().getToken());

                return "move " + movePos;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "";
//        return "move " + move.getPosition();
    }
}
