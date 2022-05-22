package antifraud.utils;

import java.util.stream.Stream;

public final class IpAddressValidator {

    public static boolean validate(String ipAddress) {
        if (ipAddress == null) {
            return false;
        }
        String[] numbers = ipAddress.split("\\.");
        if (numbers.length != 4) {
            return false;
        }
        return Stream.of(numbers)
                .map(Integer::valueOf)
                .allMatch(n -> n >= 0 && n <= 255);
    }

}
