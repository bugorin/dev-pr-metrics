package com.devprmetrics.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class LocalDateTimeUtils {

    private LocalDateTimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        return date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public static String toIsoInstantUtc(LocalDateTime localDateTime) {
        return DateTimeFormatter.ISO_INSTANT.format(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public static LocalDateTime toUtc(LocalDateTime localDateTime, ZoneId sourceZoneId) {
        return localDateTime
                .atZone(sourceZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
