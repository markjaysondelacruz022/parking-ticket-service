package com.mjdc.pts.util;

import com.mjdc.pts.AppContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DateUtil {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static TimeZone getDefaultTimeZone() {
        final AtomicReference<TimeZone> timeZoneAr = new AtomicReference<>(null);
        Optional.ofNullable(getDefaultZoneId())
            .ifPresent(z -> timeZoneAr.set(TimeZone.getTimeZone(z)));
        return timeZoneAr.get();
    }

    public static ZoneId getDefaultZoneId() {
        final AtomicReference<ZoneId> zoneIdAr = new AtomicReference<>(null);
        Optional.ofNullable(EnvironmentProp.DEFAULT_TIME_ZONE_CONFIG)
            .ifPresent(tz -> zoneIdAr.set(ZoneId.of(tz)));
        return zoneIdAr.get();
    }

    public static Date getCurrentDateTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(getDefaultTimeZone());
        return calendar.getTime();
    }
    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(getDefaultZoneId());
    }



    public static BigDecimal getHoursDiff(final LocalDateTime dateStart, final LocalDateTime dateTo) {
        long timeToMillis = dateTo.atZone(DateUtil.getDefaultZoneId()).toInstant().toEpochMilli();
        long timeFromMillis = dateStart.atZone(DateUtil.getDefaultZoneId()).toInstant().toEpochMilli();
        long timeDiff = timeToMillis - timeFromMillis;

        long hrs = TimeUnit.MILLISECONDS.toHours(timeDiff);
        long minute = TimeUnit.MILLISECONDS.toMinutes(timeDiff);

        final BigDecimal hoursBig = new BigDecimal(hrs);
        BigDecimal excessMinute = new BigDecimal(((minute - (hrs * 60)) * 60));
        excessMinute = excessMinute.divide(new BigDecimal(60*60), 2, RoundingMode.CEILING);
        return hoursBig.add(excessMinute);
    }

//    public static Integer getHoursDiff(final LocalDateTime dateStart, final LocalDateTime dateTo) {
//        long timeToMillis = dateTo.atZone(DateUtil.getDefaultZoneId()).toInstant().toEpochMilli();
//        long timeFromMillis = dateStart.atZone(DateUtil.getDefaultZoneId()).toInstant().toEpochMilli();
//        long timeDiff = timeToMillis - timeFromMillis;
//        long minute = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
//        long hrs = TimeUnit.MILLISECONDS.toHours(timeDiff);
//
//        final BigDecimal hoursBig = new BigDecimal(hrs);
//        BigDecimal excessMinute = new BigDecimal(((minute - (hrs * 60)) * 60));
//        excessMinute = excessMinute.divide(new BigDecimal(60*60), 2, RoundingMode.CEILING);
//        BigDecimal addedMinute = hoursBig.add(excessMinute).setScale(0, RoundingMode.HALF_UP);
//
//        return addedMinute.toBigInteger().intValue();
//    }

    public static LocalDateTime modifiedDateHour(final LocalDateTime date, int amount) {
        return date.plusHours(amount);
    }
}
