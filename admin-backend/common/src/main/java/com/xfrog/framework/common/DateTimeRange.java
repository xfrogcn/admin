package com.xfrog.framework.common;


import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
public class DateTimeRange extends Range<LocalDateTime> {

    public DateTimeRange() {
    }

    public DateTimeRange(LocalDateTime begin, LocalDateTime end) {
        super(begin, end);
    }

    public static DateTimeRange of(LocalDateTime begin, LocalDateTime end) {
        return DateTimeRange.builder().begin(begin).end(end).build();
    }
}
