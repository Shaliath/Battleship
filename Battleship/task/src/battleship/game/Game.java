package battleship.game;

import battleship.infra.Field;
import battleship.infra.ShipType;
import battleship.infra.Constants;
import battleship.infra.FieldSymbols;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import static battleship.game.Ships.placeShip;

public class Game {

    private static final String methodPrintName = "print";
    private static final String fieldName = "field_";
    private static final String fieldFogName = "fieldFog_";


    public static void setUpBattleShips(Field field, Scanner scanner) {
        System.out.printf(Constants.ENTER_COORDINATES, ShipType.AIRCRAFT_CARRIER.getName(), ShipType.AIRCRAFT_CARRIER.getSize());
        placeShip(field, scanner, ShipType.AIRCRAFT_CARRIER);
        field.print();
        System.out.printf(Constants.ENTER_COORDINATES, ShipType.BATTLESHIP.getName(), ShipType.BATTLESHIP.getSize());
        placeShip(field, scanner, ShipType.BATTLESHIP);
        field.print();
        System.out.printf(Constants.ENTER_COORDINATES, ShipType.SUBMARINE.getName(), ShipType.SUBMARINE.getSize());
        placeShip(field, scanner, ShipType.SUBMARINE);
        field.print();
        System.out.printf(Constants.ENTER_COORDINATES, ShipType.CRUISER.getName(), ShipType.CRUISER.getSize());
        placeShip(field, scanner, ShipType.CRUISER);
        field.print();
        System.out.printf(Constants.ENTER_COORDINATES, ShipType.DESTROYER.getName(), ShipType.DESTROYER.getSize());
        placeShip(field, scanner, ShipType.DESTROYER);
        field.print();
    }

    public static void playTheGame(Board board, Scanner scanner) throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
        boolean run = true;
        int counter = 1;
        Class clazzBoard = board.getClass();
        Class clazzField = Field.class;
        while (run) {
            int activePlayer = counter % 2 == 0 ? 2 : 1;
            int passivePlayer = counter % 2 == 0 ? 1 : 2;
            clazzField.getMethod(methodPrintName).invoke(clazzBoard.getField(fieldFogName + passivePlayer).get(board));
            System.out.println(Constants.DELIMITER);
            clazzField.getMethod(methodPrintName).invoke(clazzBoard.getField(fieldName + activePlayer).get(board));
            System.out.printf("\nPlayer %d, it's your turn:\n%n", activePlayer);
            run = takeAShot((Field) clazzBoard.getField(fieldName + passivePlayer).get(board),
                    (Field) clazzBoard.getField(fieldFogName + passivePlayer).get(board), scanner);
            counter++;
            if (run) {
                System.out.println(Constants.PRESS_ENTER);
                scanner.nextLine();
            }
        }
    }

    private static boolean takeAShot(Field field, Field fieldWithFog, Scanner scanner) {
        boolean run = true;
        String coordinate = scanner.nextLine();
        try {
            int[] coords = Ships.convertCoordinates(coordinate);
            run = fire(field, fieldWithFog, coords);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        return run;
    }

    private static boolean fire(Field field, Field fieldWithFog, int[] coords) {
        String cell = field.getCell(coords[0] - 1, coords[1] - 1);
        String message;
        boolean lastShipSank = false;
        if (FieldSymbols.SHIP.getSymbol().equals(cell)) {
            field.setCell(coords[0] - 1, coords[1] - 1, FieldSymbols.SHOT.getSymbol());
            fieldWithFog.setCell(coords[0] - 1, coords[1] - 1, FieldSymbols.SHOT.getSymbol());
            if (shipDestroyed(field, coords)) {
                if (allShipsDestroyed(field)) {
                    message = "\nYou sank the last ship. You won. Congratulations!";
                    lastShipSank = true;
                } else {
                    message = "\nYou sank a ship! Specify a new target:\n";
                }
            } else {
                message = "\nYou hit a ship!\n";
            }
        } else if (FieldSymbols.FOG.getSymbol().equals(cell)) {
            field.setCell(coords[0] - 1, coords[1] - 1, FieldSymbols.MISS.getSymbol());
            fieldWithFog.setCell(coords[0] - 1, coords[1] - 1, FieldSymbols.MISS.getSymbol());
            message = "\nYou missed!\n";
        } else {
            message = "\nYou have already shot here!\n";
        }
        System.out.println(message);
        return !lastShipSank;
    }

    private static boolean allShipsDestroyed(Field field) {
        boolean allDead = true;
        for (int i = 0; i < 10; i++) {
            if (!field.noShipInARow(i)) {
                allDead = false;
                break;
            }
        }
        return allDead;
    }

    private static boolean shipDestroyed(Field field, int[] coords) {
        String ship = FieldSymbols.SHIP.getSymbol();
        int x = coords[0] - 1;
        int y = coords[1] - 1;
        return !ship.equals(field.getCell(x == 0 ? x : x - 1, y)) &&
                !ship.equals(field.getCell(x == 9 ? x : x + 1, y)) &&
                !ship.equals(field.getCell(x, y == 0 ? y : y - 1)) &&
                !ship.equals(field.getCell(x, y == 9 ? y : y + 1));
    }
}
