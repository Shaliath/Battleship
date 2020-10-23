package battleship.game;

import battleship.infra.Field;
import battleship.infra.Constants;
import battleship.infra.FieldSymbols;
import battleship.infra.Rows;
import battleship.infra.ShipType;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Ships {

    public static void placeShip(Field field, Scanner scanner, ShipType ship) {
        boolean run = true;
        while (run) {
            String[] coordinates = scanner.nextLine().split(" ");
            if (coordinates.length == 2) {
                try {
                    String[] normalizedCoordinates = normalizeCoordinates(coordinates);
                    int[] startCoordinate = convertCoordinates(normalizedCoordinates[0]);
                    int[] endCoordinate = convertCoordinates(normalizedCoordinates[1]);
                    validateShipLength(startCoordinate, endCoordinate, ship.getSize(), ship.getName());
                    checkSpace(field, startCoordinate, endCoordinate);
                    putShip(field, startCoordinate, endCoordinate);
                    run = false;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("Error! 2 coordinates are required. Try again:\n");
            }
        }
    }

    private static String[] normalizeCoordinates(String[] coordinates) {
        if (coordinates[0].length() == 2 && coordinates[1].length() == 2) {
            Arrays.sort(coordinates);
        } else {
            Arrays.sort(coordinates, Comparator.comparingInt(String::length));
        }
        return coordinates;
    }

    public static int[] convertCoordinates(String coordinate) throws IllegalArgumentException {
        if (coordinate.length() < 2 || coordinate.length() > 3) {
            throw new IllegalArgumentException(Constants.WRONG_COORDINATE);
        }
        int[] coordinates = new int[2];
        try {
            coordinates[0] = Rows.getNumber(coordinate.substring(0, 1).toUpperCase());
            coordinates[1] = Integer.parseInt(coordinate.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error! Unable to parse coordinates. Try again:\n");
        }
        if (coordinates[0] == -1 || coordinates[1] < 0 || coordinates[1] > 10) {
            throw new IllegalArgumentException(Constants.WRONG_COORDINATE);
        }
        return coordinates;
    }

    private static void validateShipLength(int[] start, int[] end, int length, String shipName) {
        int actualLength;
        if (start[0] == end[0]) {
            actualLength = end[1] - start[1] + 1;
        } else if (start[1] == end[1]) {
            actualLength = end[0] - start[0] + 1;
        } else {
            throw new IllegalArgumentException("Error! Wrong ship location! Try again:\n");
        }
        if (length != actualLength) {
            throw new IllegalArgumentException(String.format("Error! Wrong length of the %s! Try again:\n", shipName));
        }
    }

    private static void putShip(Field field, int[] start, int[] end) {
        for (int i = start[0] - 1; i < end[0]; i++) {
            for (int j = start[1] - 1; j < end[1]; j++) {
                field.setCell(i, j, FieldSymbols.SHIP.getSymbol());
            }
        }
    }

    private static void checkSpace(Field field, int[] start, int[] end) {
        String ship = FieldSymbols.SHIP.getSymbol();
        for (int i = start[0] - 1; i < end[0]; i++) {
            for (int j = start[1] - 1; j < end[1]; j++) {
                if (ship.equals(field.getCell(i, j))) {
                    throw new IllegalArgumentException("Error! Wrong ship location! Try again:\n");
                }
                try {
                    if (ship.equals(field.getCell(i - 1, j)) ||
                            ship.equals(field.getCell(i + 1, j)) ||
                            ship.equals(field.getCell(i, j - 1)) ||
                            ship.equals(field.getCell(i, j + 1))) {
                        throw new IllegalArgumentException("Error! You placed it too close to another one. Try again:\n");
                    }

                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
    }
}
