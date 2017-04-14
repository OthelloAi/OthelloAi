package app;

import java.util.ArrayList;

/**
 * @author Joël Hoekstra
 */
public class History {
    private ArrayList<Match> history = new ArrayList<>();

    public void addMatch(Match match) {
        history.add(match);
    }

    public ArrayList<Match> getHistory() {
        return history;
    }

    public Match getMatch(int m) {
        return history.get(m);
    }
}
