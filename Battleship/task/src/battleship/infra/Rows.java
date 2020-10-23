package battleship.infra;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rows {

    private static Map<Integer, String> rows = Stream.of(new Object[][]{
            {1, "A"},
            {2, "B"},
            {3, "C"},
            {4, "D"},
            {5, "E"},
            {6, "F"},
            {7, "G"},
            {8, "H"},
            {9, "I"},
            {10, "J"}
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));

    public static String getLetter(int number) {
        return rows.get(number);
    }

    public static int getNumber(String letter) {
        Optional<Integer> number = rows
                .entrySet()
                .stream()
                .filter(entry -> letter.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();
        return number.isEmpty() ? -1 : number.get();
    }

}
