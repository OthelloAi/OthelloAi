package app;

/**
 * @author Joël Hoekstra
 * Got this idea from @c00kiemon5ter.
 */
public enum TokenState {
    BLACK('●'),
    WHITE('○'),
    POSSIBLE('.'),
    EMPTY(' '),
    CROSS('X'),
    NOUGHT('O'),
    UP('↑'),
    DOWN('↓'),
    LEFT('←'),
    RIGHT('→'),
    LEFTUP('↖'),
    LEFTDOWN('↙'),
    RIGHTUP('↗'),
    RIGHTDOWN('↘');


    private final char symbol;

    TokenState(char symbol) {
        this.symbol = symbol;
    }

    public char symbol() {
        return this.symbol;
    }

    public TokenState opposite() {
        if (this == CROSS) return NOUGHT;
        if (this == NOUGHT) return CROSS;
        if (this == UP) return DOWN;
        if (this == DOWN) return UP;
        if (this == LEFT) return RIGHT;
        if (this == RIGHT) return LEFT;
        if (this == LEFTUP) return RIGHTDOWN;
        if (this == RIGHTDOWN) return LEFTUP;
        if (this == LEFTDOWN) return RIGHTUP;
        if (this == RIGHTUP) return LEFTDOWN;
        return this == BLACK ? WHITE : BLACK;
    }

    @Override
    public String toString() {
        return String.valueOf(symbol);
    }
}
