package ra.Coursemanagement.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class InputValidator {

    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isNumeric(String input) {
        if (isEmpty(input)) return false;
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return false;
        // Số điện thoại Việt Nam: 10-11 số, bắt đầu bằng 0
        String phoneRegex = "^(0|\\+84)\\d{9,10}$";
        return phone.matches(phoneRegex);
    }

    public static boolean isValidDate(String dateStr) {
        if (isEmpty(dateStr)) return false;
        try {
            LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean isValidSex(String sex) {
        if (isEmpty(sex)) return false;
        return sex.equals("0") || sex.equals("1") ||
                sex.equalsIgnoreCase("Nam") || sex.equalsIgnoreCase("Nữ");
    }

    public static int parseSex(String sex) {
        if (sex.equalsIgnoreCase("Nam") || sex.equals("1")) {
            return 1;
        }
        return 0;
    }

    public static boolean isValidPassword(String password) {
        return !isEmpty(password) && password.length() >= 6;
    }

    public static boolean isPositiveNumber(String input) {
        if (!isNumeric(input)) return false;
        int num = Integer.parseInt(input);
        return num > 0;
    }

    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}