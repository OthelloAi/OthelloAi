package app.game;

import app.*;
import app.actors.Actor;
import app.actors.MiniMaxActor;
import app.gui.alerts.CouldNotConnectAlert;
import app.gui.dialogs.ConnectionDialog;
import app.gui.alerts.AcceptDeclineAlert;
import app.gui.alerts.StartMatchAlert;
import app.network.CommandSender;
import app.network.Connection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import app.network.commands.*;
import app.gui.GUI;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Joël Hoekstra
 */
public class Game {
    private GUI gui;
    private Board board;
    private GameType gameType;
    private ArrayList<Player> playerList;
    private ArrayList<Challenge> pendingChallenges;

    private Actor actor;
    private Match match = null;
    private App app;


    public Game(App app) {
        this.app = app;
        pendingChallenges = new ArrayList<>();
        board = new Board(gameType);
        actor = new MiniMaxActor(this, board);
    }

    public void addGUI(GUI gui) {
        this.gui = gui;
    }

    public Actor getActor() {
        return actor;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
        board = new Board(gameType);
        gui.render();
    }

    public Player getLoggedInPlayer() {
        return app.getUser();
    }

    public boolean isInMatch() {
        return (match != null && match.isStarted() && !match.isFinished());
    }

    public ArrayList<Player> getPlayerList() {return playerList;}


    public Token[][] getBoard() {
        return board.getBoard();
    }

    public void setPlayers(ArrayList<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPendingChallenge(Challenge challenge) {
        this.pendingChallenges.add(challenge);
        //TODO With @Martijn alerts toevoegen.
        // show dialog here
        Platform.runLater(() -> {
            AcceptDeclineAlert dialog = new AcceptDeclineAlert();
            Optional<ButtonType> result = dialog.showAndWait();
            if (result.get() == ButtonType.OK) {
                CommandSender.addCommand(new ChallengeAcceptCommand(challenge));

            } else {
                this.pendingChallenges.remove(challenge);
            }
        });
    }
    
    public Match endMatch(EndState endState) {
        // TODO: 14/04/2017 add functionality for ending a game here
//        match.stop();
        return match;
    }

    public ArrayList<Integer> getPossibleMoves() {
        return board.getPossibleMoves();
    }

    public boolean isLoggedIn() {
        return (app.getUser() != null);
    }

    public void startMatch(Player playerOne, Player playerTwo, GameType gameType) {
        System.out.println("You're placed in a new match..");
        Platform.runLater(() -> {
            StartMatchAlert startMatchAlert = new StartMatchAlert();
            startMatchAlert.showAndWait();
        });
        setGameType(gameType);
//        board = new Board(gameType); // TODO: 14/04/2017 create unique board types for tic tac toe and for reversi
        gui.reset();
        match = new Match(gameType, playerOne, playerTwo);
        match.start();
        update();
        gui.setLeftStatusText("You have been placed in a new match. Good luck!");
    }

    public void placeMove(Move move) {
        if (isLoggedIn()) {
            if (match.canDoMove()) {
                match.addMove(move);
                board.addMove(move.getPosition(), match.getTokenByPlayer(move.getPlayer()));
                update();
            }
        }
        else {
            // perhaps the reset should happen here.
            // or an empty game gui should be rendered.
        }
    }
    
    public void forfeit() {
        match.forfeit();
        update();
    }

    public void handleMove(Integer movePosition){
        Move move = new Move(movePosition, app.getUser());
        if(board.isValidMove(move, match.getTokenByPlayer(move.getPlayer())))
        {
            CommandSender.addCommand(new MoveCommand(move));
            System.out.println("Nice one, valid move");
        }
        else
            {
                // return alert that move isn't valid
                System.out.println("Invalid move!");
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Invalid move");
                    alert.setContentText("Sorry, the move you chose isn't valid");
                    alert.showAndWait();
                });
            }
    }

    public void update() {
        gui.update();
    }
}
