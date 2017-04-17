package app.utils;

/**
 * @author Joël Hoekstra
 */
public class Debug {
    private boolean _canPrint = false;
    public static Debug instance = new Debug();

    private Debug() {}

    public static void print(String msg) {
        if (instance._canPrint) {
            System.out.print(msg);
        }
    }

    public static void print(Object x) {
        if (instance._canPrint) {
            System.out.print(x);
        }
    }
    
    public static void println(String msg) {
        if (instance._canPrint) {
            System.out.println(msg);
        }
    }

    public static void println() {
        if (instance._canPrint) {
            System.out.println();
        }
    }

    public static void println(Object x) {
        if (instance._canPrint) {
            System.out.println(x);
        }
    }

    public static void canPrint(boolean _canPrint) {
        instance._canPrint = _canPrint;
    }
}
