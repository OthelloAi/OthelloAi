package app.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import app.Token;
import javafx.scene.layout.RowConstraints;

/**
 * @author Joël Hoekstra
 */
public abstract class GameGUI extends GridPane {
    protected Token[][] board;
    private ColumnConstraints col = new ColumnConstraints(80);
    private RowConstraints row = new RowConstraints(80);

    protected final void setBoard(Token[][] board) {
        this.board = board;
        addConstraints();
        render();
    }

    public void addConstraints(){
        for (int y = 0; y < board.length; y++) {
            getColumnConstraints().add(col);
        }
            for (int x = 0; x < board.length; x++) {
            getRowConstraints().add(row);
            }
    }

    public abstract void render();
}
