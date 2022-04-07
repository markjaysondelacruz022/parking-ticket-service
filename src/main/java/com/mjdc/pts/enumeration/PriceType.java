package com.mjdc.pts.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum PriceType {
    FLAT_RATE("Flat Rate"), PER_HOUR("Per hour"), DAY_RATE("Day Rate"), DEDUCTION("Deduction");

    @Getter
    private final String value;

    public static PriceType of(final String value) {
        return Stream.of(PriceType.values()).filter(type -> type.getValue().equalsIgnoreCase(value))
            .findFirst().orElseThrow(IllegalAccessError::new);
    }

}
