package battleship;

import battleship.game.Board;
import battleship.game.Game;
import battleship.infra.Constants;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        Scanner scanner = new Scanner(System.in);
        Board board = new Board();
        System.out.printf(Constants.PLAYER_WELCOME_MESSAGE, 1);
        board.field_1.print();
        Game.setUpBattleShips(board.field_1, scanner);
        System.out.println(Constants.PRESS_ENTER);
        scanner.nextLine();

        System.out.printf(Constants.PLAYER_WELCOME_MESSAGE, 2);
        board.field_2.print();
        Game.setUpBattleShips(board.field_2, scanner);
        System.out.println(Constants.PRESS_ENTER);
        scanner.nextLine();

        Game.playTheGame(board, scanner);
        scanner.close();
    }
}
