package app;

import javafx.application.Application;
import java.io.IOException;

/**
 * @author Gabe Witteveen
 * @author Joël Hoekstra
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to the AI of the future.");
        Application.launch(Game.class, args);
    }
}
