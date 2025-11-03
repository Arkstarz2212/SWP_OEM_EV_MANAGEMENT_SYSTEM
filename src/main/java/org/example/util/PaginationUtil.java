package org.example.util;

public final class PaginationUtil {
    private PaginationUtil() {
    }

    public static int sanitizeLimit(Integer limit, int defaultLimit, int maxLimit) {
        int l = limit == null ? defaultLimit : limit;
        if (l <= 0)
            l = defaultLimit;
        return Math.min(l, maxLimit);
    }

    public static int sanitizeOffset(Integer offset) {
        return offset == null || offset < 0 ? 0 : offset;
    }
}
