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
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }
}
