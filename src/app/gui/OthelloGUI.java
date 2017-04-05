package app.gui;

import app.Token;

/**
 * @author Joël Hoekstra
 */
public class OthelloGUI extends GameGUI {

    public OthelloGUI(Token[][] board) {
        setBoard(board);
    }

    @Override
    public void render() {
        getChildren().clear();
        if (board != null) {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    add(new GridNode(board[y][x]), x, y);
                }
            }
        }
    }
}
