package app.utils;

/**
 * @author Joël Hoekstra
 */
public class Debug {
    private boolean canLog = false;
    public static Debug instance = new Debug();

    private Debug() {}

    public static void log(String msg) {
        if (instance.canLog) {
            System.out.println(msg);
        }
    }

    public static void debug(boolean canLog) {
        instance.canLog = canLog;
    }
}
