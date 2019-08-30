package org.atteo.polish_numbers;

import java.util.Random;

public enum Sex {
    MALE, FEMALE;

    public static Sex random() {
        if (new Random().nextBoolean()) {
            return MALE;
        }
        return FEMALE;
    }
}
