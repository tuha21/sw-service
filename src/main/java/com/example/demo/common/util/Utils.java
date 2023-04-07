package com.example.demo.common.util;

import com.example.demo.common.consts.TimeZoneConst;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import org.apache.commons.lang3.time.DateFormatUtils;

public class Utils {

    public static long getUTCTimestamp() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return Date.from(now.toInstant()).getTime() / 1000;
    }

    public static int getDateKey(long time) {
        Instant instant = Instant.ofEpochMilli(time * 1000);
        ZonedDateTime zonedDateTimeUTC = instant.atZone(ZoneId.of(TimeZoneConst.TimeOffset.UTC));
        return getDateTimeKey(zonedDateTimeUTC, "yyyyMMdd");
    }

    public static int getDateTimeKey(ZonedDateTime zonedDateTime, String pattern) {
        LocalDateTime localDateTime = zonedDateTime.toLocalDateTime();
        String timeKeyString = DateFormatUtils.format(
            Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant()), pattern
        );
        return Integer.valueOf(timeKeyString);
    }

    public static String getStringDateFromDateKey (int dateKey) {
        var stringKey = String.valueOf(dateKey);
        var year = stringKey.substring(0, 4);
        var month = stringKey.substring(4, 6);
        var date = stringKey.substring(6, 8);
        return date + "/" + month + "/" + year;
    }

}
