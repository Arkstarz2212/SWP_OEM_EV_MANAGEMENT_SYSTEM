package org.example.ulti;

import java.util.regex.Pattern;

public final class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\n]+@[^@\\n]+\\.[^@\\n]+$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+\\-() ]{6,20}$");
    private static final Pattern VIN = Pattern.compile("^[A-HJ-NPR-Z0-9]{11,17}$");

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }

    public static boolean isValidVin(String vin) {
        return vin != null && VIN.matcher(vin).matches();
    }
}
