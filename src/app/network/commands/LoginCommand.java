package app.network.commands;

/**
 * @author js
 */
public class LoginCommand implements Command {
    String username = "";

    public LoginCommand(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "login " + username;
    }
}
