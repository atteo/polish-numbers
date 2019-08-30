package org.atteo.polish_numbers;

import java.time.LocalDate;
import java.util.Random;

public class Dates {
    public static LocalDate random(LocalDate minInclusive, LocalDate maxExclusive) {
        int minDay = (int) minInclusive.toEpochDay();
        int maxDay = (int) maxExclusive.toEpochDay();
        long randomDay = minDay = new Random().nextInt(maxDay - minDay);

        return LocalDate.ofEpochDay(randomDay);
    }
}
