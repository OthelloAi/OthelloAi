package app.network.responses;

/**
 * @author Joël Hoekstra
 */
public class NullResponse implements Response {

    @Override
    public void handle() {
        // does nothing because it's a null object
    }
}
