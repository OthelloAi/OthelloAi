package app.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import app.utils.Token;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * @author Joël Hoekstra
 * @author Martijn Snijder
 */
public class GameGUI extends GridPane {
    protected GUI gui;
    protected Token[][] board;
    private RowConstraints row = new RowConstraints();
    private ColumnConstraints col = new ColumnConstraints();

    public GameGUI(GUI gui) {
        setGUI(gui);
    }
    public GameGUI(Token[][] board, GUI gui) {
        setGUI(gui);
        setBoard(board);
    }

    protected final void setBoard(Token[][] board) {
        this.board = board;
        addConstraints();
        render();
    }

    public void addConstraints() {
        for (int x = 0; x < board.length; x++) {
            row.setVgrow(Priority.ALWAYS);
            getRowConstraints().add(row);
        }

        for (int y = 0; y < board.length; y++) {
            col.setHgrow(Priority.ALWAYS);
            getColumnConstraints().add(col);
        }
    }

    protected final void setGUI(GUI gui) {
        this.gui = gui;
    }

    public final void render() {
        getChildren().clear();
        if (board != null) {
            for (int y = 0; y < board.length; y++) {
                for (int x = 0; x < board.length; x++) {
                    if (board[y][x] != null) {
                        GridNode node = new GridNode(board[y][x]);
                        int posX = x;
                        int posY = y;
                        node.setOnMouseClicked(e -> gui.handleMouseClick(posX, posY));
                        add(node, x, y);
                    }
                }
            }
        }
    }
}
