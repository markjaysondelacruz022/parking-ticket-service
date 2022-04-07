package com.mjdc.pts.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum Size {
    SMALL("Small"), MEDIUM("Medium"), LARGE("Large");

    @Getter
    private final String value;

    public static Size of(final String value) {
        return Stream.of(Size.values()).filter(s -> s.getValue().equalsIgnoreCase(value))
            .findFirst().orElseThrow(IllegalAccessError::new);
    }


}
