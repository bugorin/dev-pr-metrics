package com.devprmetrics.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class LocalDateTimeUtils {

    private static final ZoneId SAO_PAULO_ZONE_ID = ZoneId.of("America/Sao_Paulo");

    private LocalDateTimeUtils() {
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) return null;
        return date
                .toInstant()
                .atZone(SAO_PAULO_ZONE_ID)
                .toLocalDateTime();
    }

    public static String toIsoInstantUtc(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return DateTimeFormatter.ISO_INSTANT.format(localDateTime.toInstant(ZoneOffset.UTC));
    }

    public static LocalDateTime toUtc(LocalDateTime localDateTime, ZoneId sourceZoneId) {
        return localDateTime
                .atZone(sourceZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }
}
