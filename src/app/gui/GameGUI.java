package app.gui;

import javafx.scene.layout.GridPane;
import app.Token;

/**
 * @author Joël Hoekstra
 */
public abstract class GameGUI extends GridPane {
    protected Token[][] board;

    protected final void setBoard(Token[][] board) {
        this.board = board;
        render();
    }

    public abstract void render();
}
