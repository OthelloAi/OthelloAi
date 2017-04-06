package app.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import app.Token;

/**
 * @author Joël Hoekstra
 */
public class GridNode extends StackPane {
    public GridNode(Token token) {
        setPrefSize(50, 50);
        Text text = new Text(token.toString());
        text.setStyle("-fx-font-size: 50px");
        getChildren().add(text);
        setStyle("-fx-border-color: black");
    }
}
