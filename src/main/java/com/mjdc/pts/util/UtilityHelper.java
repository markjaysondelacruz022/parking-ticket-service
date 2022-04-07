package com.mjdc.pts.util;

import com.mjdc.pts.exception.ControllerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
public class UtilityHelper {

    public static String addLeadingZero(final int num, final int count) {
        return  String.format("%0" + count + "d", num);
    }
}
