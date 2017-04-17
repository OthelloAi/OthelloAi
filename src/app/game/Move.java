package app.game;

/**
 * @author Joël Hoekstra
 */
public class Move {
    private int position;
    private Player player;

    public Move(int position, Player player) {
        this.position = position;
        this.player = player;
        // TODO: 16-4-2017 Removed cause of console spam caused bij getpossiblemoves
        // Debug.println("Move: " + position);
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }
}
