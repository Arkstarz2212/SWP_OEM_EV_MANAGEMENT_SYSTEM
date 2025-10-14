package org.example.ulti;

public final class SqlUtil {
    private SqlUtil() {
    }

    public static String likeContains(String value) {
        if (value == null || value.isEmpty())
            return "%";
        return "%" + value.replace("%", "[%]").replace("_", "[_]") + "%";
    }
}
