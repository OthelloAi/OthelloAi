package app.network.commands;

import app.game.Move;
import app.utils.Debug;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Joël Hoekstra
 */
public class MoveCommand implements Command {
    private Move move;
    private boolean wait;
    private final CountDownLatch sendMoveLatch = new CountDownLatch(1);

    private static final int REQUEST_TIMEOUT_SECS = 1;

    public MoveCommand(Move move) {
        this.move = move;
        wait = false;
    }

    public MoveCommand(Move move, boolean wait) {
        this(move);
        this.wait = wait;
    }

    @Override
    public String toString() {
        // latch here before return
        if (wait) {
            long start = System.currentTimeMillis();
            Thread t = new Thread(() -> {
                while(true) {
                    long current = System.currentTimeMillis();
                    long diff = ((current - start) );

//                    Debug.println(diff);
                    if (diff >= 1000) {
                        sendMoveLatch.countDown();
                        break;
                    }
                }
            });
            t.setDaemon(true);
            t.start();

            try {
                sendMoveLatch.await();
                return "move " + move.getPosition();
//                if (sendMoveLatch.await(REQUEST_TIMEOUT_SECS, TimeUnit.SECONDS)) {
//
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "move " + move.getPosition();
    }
}
