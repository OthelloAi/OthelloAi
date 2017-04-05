package app.responses;

/**
 * @author js
 */
public class OkResponse implements Response {
    @Override
    public void handle() {
        System.out.println("[SVR] OKay!");
    }
}
