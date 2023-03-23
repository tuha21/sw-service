package com.example.demo.common.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

public class Utils {

    public static long getUTCTimestamp() {
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        return Date.from(now.toInstant()).getTime() / 1000;
    }

}
