package com.mjdc.pts.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum ParkingStatus {
    PARKED("Parked"), CHECKOUT("Checkout");

    @Getter
    private final String value;

    public static ParkingStatus of(final String value) {
        return Stream.of(ParkingStatus.values()).filter(s -> s.getValue().equalsIgnoreCase(value))
            .findFirst().orElseThrow(IllegalAccessError::new);
    }

}
