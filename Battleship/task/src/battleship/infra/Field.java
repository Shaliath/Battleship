package battleship.infra;

import java.util.Arrays;

public class Field {

    private String[][] field;

    public Field() {
        this.field = initialiseField();
    }

    private String[][] initialiseField() {
        String[][] field = new String[10][10];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(field[i], FieldSymbols.FOG.getSymbol());
        }
        return field;
    }

    public void print() {
        System.out.println(Constants.FIRST_ROW);
        for (int i = 0; i < 10; i++) {
            System.out.print(Rows.getLetter(i + 1));
            for (int j = 0; j < 10; j++) {
                System.out.print(" " + this.field[i][j]);
            }
            System.out.println();
        }
    }

    public String getCell(int x, int y) {
        return this.field[x][y];
    }

    public void setCell(int x, int y, String value) {
        this.field[x][y] = value;
    }

    public boolean noShipInARow(int rowIndex) {
        return Arrays.stream(this.field[rowIndex])
                .filter(symbol -> symbol.equals(FieldSymbols.SHIP.getSymbol()))
                .count() == 0;
    }

}
