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
//        System.out.println("Othello render");
        getChildren().clear();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                add(new GridNode(board[y][x]), x, y);
            }
        }
    }
}
