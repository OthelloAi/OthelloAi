package app;

/**
 * @author Joël Hoekstra
 */
public class Move {
    private int position;
    private Player player;

    public Move(int position, Player player) {
        this.position = position;
        this.player = player;
        System.out.println("Move: " + position);
    }

    public Player getPlayer() {
        return player;
    }

    public int getPosition() {
        return position;
    }
}
