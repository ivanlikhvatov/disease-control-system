package ru.example.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter {

    public static Date toDate(LocalDateTime localDateTime){
        return Date.from(localDateTime
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
        );
    }

    public static LocalDateTime toLocalDateTime(Date date){
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
