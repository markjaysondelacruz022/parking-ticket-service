package com.mjdc.pts.util;

import com.mjdc.pts.AppContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DateUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static TimeZone getDefaultTimeZone() {
        final AtomicReference<TimeZone> timeZoneAr = new AtomicReference<>(null);
        Optional.ofNullable(EnvironmentProp.DEFAULT_TIME_ZONE_CONFIG)
            .ifPresent(tz -> timeZoneAr.set(TimeZone.getTimeZone(ZoneId.of(tz))));
        return timeZoneAr.get();
    }

    public static Date getCurrentDateTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(getDefaultTimeZone());
        return calendar.getTime();
    }

    public static Integer getHoursDiff(final Date dateStart, final Date dateTo) {
        long timeDiff = dateTo.getTime() - dateStart.getTime();
        long minute = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
        long hrs = TimeUnit.MILLISECONDS.toHours(timeDiff);

        final BigDecimal hoursBig = new BigDecimal(hrs);
        BigDecimal excessMinute = new BigDecimal(((minute - (hrs * 60)) * 60));
        excessMinute = excessMinute.divide(new BigDecimal(60*60), 2, RoundingMode.CEILING);
        BigDecimal addedMinute = hoursBig.add(excessMinute).setScale(0, RoundingMode.HALF_UP);

        return addedMinute.toBigInteger().intValue();
    }

    public static Date modifiedDate(final Date date, final int dateField, final int amount) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTimeZone(getDefaultTimeZone());
        calendar.add(dateField, amount);
        return calendar.getTime();
    }
}
