package org.atteo.polish_numbers;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Pesel {
    private int numbers[] = new int[11];

    private Pesel(Builder builder) {
        setFromBirthDate(builder.getBirthDate());
        setRandomSerial(builder.getSex());
        setChecksum();
    }

    private void setFromBirthDate(LocalDate date) {
        int[] yearDigits = toDigits(date.getYear(), 4);
        numbers[0] = yearDigits[2];
        numbers[1] = yearDigits[3];

        int[] monthDigits = toDigits(date.getMonthValue(), 2);

        numbers[2] = (char) (monthDigits[0] + monthCorrection(yearDigits));
        numbers[3] = monthDigits[1];

        int[] dayDigits = toDigits(date.getDayOfMonth(), 2);
        numbers[4] = dayDigits[0];
        numbers[5] = dayDigits[1];
    }

    private void setRandomSerial(Sex sex) {
        Random random = new Random();
        int serial = random.nextInt(1000);
        int[] serialDigits = toDigits(serial, 3);

        numbers[6] = serialDigits[0];
        numbers[7] = serialDigits[1];
        numbers[8] = serialDigits[2];
        numbers[9] = 2 * random.nextInt(5) + (sex == Sex.MALE ? 1 : 0);
    }

    int CHECKSUM_WEIGHTS[] = { 9, 7, 3, 1, 9, 7, 3, 1, 9, 7 };

    private int calculateChecksum() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += CHECKSUM_WEIGHTS[i] * numbers[i];
        }

        return sum % 10;
    }

    private void setChecksum() {
        numbers[10] = calculateChecksum();
    }

    private char monthCorrection(int[] digits) {
        int century = digits[0] * 10 + digits[1];
        switch (century) {
            case 18 : return 8;
            case 19 : return 0;
            case 20 : return 2;
            case 21 : return 4;
            case 22 : return 6;
        }
        throw new IllegalStateException();
    }

    public static Pesel random() {
        return builder().get();
    }

    @Override
    public String toString() {
        Arrays.stream(numbers)
            .map(ch -> '0' + ch)
            .collect(Collectors.toList());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private LocalDate birthDate = null;
        private LocalDate bornAfter = LocalDate.of(1800, Month.JANUARY, 1);
        private LocalDate bornBefore = LocalDate.now();
        private Sex sex = null;

        private Builder() {
        }

        private LocalDate getBirthDate() {
            if (birthDate != null) {
                return birthDate;
            }

            return Dates.random(this.bornBefore, this.bornAfter);
        }

        public Builder birthDate(LocalDate date) {
            this.birthDate = date;
            return this;
        }

        public Builder bornAfter(LocalDate minInclusive) {
            this.bornAfter = minInclusive;
            return this;
        }

        public Builder bornBefore(LocalDate maxExclusive) {
            this.bornBefore = maxExclusive;
            return this;
        }

        public Builder adult() {
            this.bornBefore = LocalDate.now().minusYears(18);
            return this;
        }

        public Builder sex(Sex sex) {
            this.sex = sex;
            return this;
        }

        public Pesel get() {
            return new Pesel(this);
        }

        public Stream<Pesel> stream() {
            // TODO: duplicate builder
            return Stream.generate(this::get);
        }

        public Sex getSex() {
            if (sex != null) {
                return sex;
            }
            return Sex.random();
        }
    }

    private static int[] toDigits(int number, int length) {
        char[] chars = Integer.toString(number).toCharArray();

        int[] result = new int[chars.length];

        int zeroPrefixLength = length - chars.length;

        for (int i = 0; i < zeroPrefixLength; i++) {
            result[i] = 0;
        }

        for (int i = zeroPrefixLength; i < length; i++) {
            result[i] = chars[i - zeroPrefixLength] - '0';
        }
        return result;
    }

}
