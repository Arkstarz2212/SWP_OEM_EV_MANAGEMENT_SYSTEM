package org.example.util;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {
    private DateTimeUtil() {
    }

    public static String formatIso(OffsetDateTime time) {
        return time == null ? null : time.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static OffsetDateTime parseIsoOffset(String iso) {
        return iso == null ? null : OffsetDateTime.parse(iso, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    public static String formatDate(LocalDate date) {
        return date == null ? null : date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDate parseDate(String date) {
        return date == null ? null : LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static OffsetDateTime now(String zoneId) {
        return OffsetDateTime.now(ZoneId.of(zoneId));
    }
}
