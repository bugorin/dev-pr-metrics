package com.devprmetrics.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class LocalDateTimeUtils {

    public static LocalDateTime toLocalDateTime(Date date) {
        return date
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
