package com.xfrog.framework.common;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public final class DateTimeUtils {
    private static final ZoneId UTC_ZONE = ZoneId.of("UTC");
    public static LocalDateTime utcNow() {
        return ZonedDateTime.now(UTC_ZONE).toLocalDateTime();
    }

    public static Date utcNowDate() {
        return Date.from(utcNow().atZone(UTC_ZONE).toInstant());
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null) {
            return null;
        }
        return instant.atZone(UTC_ZONE).toLocalDateTime();
    }

    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return localDateTime.atZone(UTC_ZONE).toInstant();
    }
}
