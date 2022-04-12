package com.mjdc.pts.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
public enum PaymentStatus {
    PAID("Paid"), VOID("Void");

    @Getter
    private final String value;

    public static PaymentStatus of(final String value) {
        return Stream.of(PaymentStatus.values()).filter(s -> s.getValue().equalsIgnoreCase(value))
            .findFirst().orElseThrow(IllegalAccessError::new);
    }
}
