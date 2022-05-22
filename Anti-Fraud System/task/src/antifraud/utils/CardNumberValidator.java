package antifraud.utils;

public final class CardNumberValidator {

    public static boolean validateNumber(String number) {
        if (number == null) {
            return false;
        }
        int sum = 0;
        for (int i = number.length() - 2, j = 0; i >= 0 ; i--, j++) {
            var num = Integer.parseInt(number, i, i + 1, 10);
            if (j % 2 == 0) {
                num *= 2;
                num = num > 9 ? (num % 10) + (num / 10) : num;
            }
            sum += num;
        }
        int controllNumber = Integer.parseInt(number, number.length() - 1, number.length(), 10);
        return ((10 - (sum % 10)) % 10) == controllNumber;
    }

}
