package com.mjdc.pts.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjdc.pts.AppContext;
import com.mjdc.pts.exception.ControllerException;
import com.mjdc.pts.exception.ParkingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class UtilityHelper {

    public static String addLeadingZero(final int num, final int count) {
        return  String.format("%0" + count + "d", num);
    }

    public static Integer roundUpWhole(final BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP).toBigInteger().intValue();
    }

    public static ObjectMapper getMapper() {
        return (ObjectMapper) AppContext.getBean("mapper");
    }

    public static void searchKeyChecker(final Map<String, String> body) {
        if (body.isEmpty()) {
            throw new ParkingException("No request body found", HttpStatus.BAD_REQUEST);
        } else if (body.size() > 1) {
            throw new ParkingException("Invalid request body", HttpStatus.BAD_REQUEST);
        } else if (Optional.ofNullable(body.get("searchKey")).isEmpty()) {
            throw new ParkingException("Invalid request body search key found", HttpStatus.BAD_REQUEST);
        }
    }
}
