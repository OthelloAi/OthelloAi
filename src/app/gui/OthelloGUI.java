package app.gui;

import app.Token;

/**
 * @author Joël Hoekstra
 */
public class OthelloGUI extends GameGUI {

    public OthelloGUI(Token[][] board, GUI gui) {
        super(board, gui);
        setGUI(gui);
        setBoard(board);
    }

//    @Override
//    public void render() {
//        getChildren().clear();
//        if (board != null) {
//            for (int y = 0; y < board.length; y++) {
//                for (int x = 0; x < board.length; x++) {
//                    if (board[y][x] != null) {
//                        GridNode node = new GridNode(board[y][x]);
//                        int posX = x;
//                        int posY = y;
//                        node.setOnMouseClicked(e -> gui.handleMouseClick(node, posX, posY));
//                        add(node, x, y);
//                    }
//                }
//            }
//        }
//    }
}
