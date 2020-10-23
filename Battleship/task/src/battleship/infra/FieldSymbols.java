package battleship.infra;

public enum FieldSymbols {
    FOG("~"),
    SHIP("O"),
    SHOT("X"),
    MISS("M");

    String symbol;

    FieldSymbols(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
