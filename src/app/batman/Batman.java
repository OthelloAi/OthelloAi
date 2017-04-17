package app.batman;

import app.utils.Debug;

/**
 *
 * Every project needs it's hero.
 * Use toString to see Batman! <3
 *
 *        _N\
 *       /_ |
 *       B_||.
 *       /_ | '.
 *       0_ m.,,;
 *       //_|
 *
 * @author Joël Hoekstra
 */
public final class Batman {

    public Batman() {
        Debug.println(toString());
    }
    @Override
    public String toString() {
        String batman = "\n" +
                "       _N\\\n" +
                "      /_ |\n" +
                "      B_||.\n" +
                "      /_ | '.\n" +
                "      0_ m.,,;\n" +
                "      //_|";
        return batman;
    }
}
