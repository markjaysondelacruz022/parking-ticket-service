package com.mjdc.pts.util;

import com.mjdc.pts.AppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Optional;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class EnvironmentProp {

    public static String DEFAULT_TIME_ZONE_CONFIG;

    @Value("${application.default-time-zone}")
    public void setDefaultTimeZone(final String defaultTimeZoneConfig) {
        DEFAULT_TIME_ZONE_CONFIG = defaultTimeZoneConfig;
    }
}
