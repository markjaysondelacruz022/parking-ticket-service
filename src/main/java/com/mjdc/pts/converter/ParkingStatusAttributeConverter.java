package com.mjdc.pts.converter;

import com.mjdc.pts.enumeration.ParkingStatus;

import javax.persistence.AttributeConverter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ParkingStatusAttributeConverter implements AttributeConverter<ParkingStatus, String> {
    @Override
    public String convertToDatabaseColumn(final ParkingStatus parkingStatus) {
        final AtomicReference<String> statusValueAr = new AtomicReference<>(null);
        Optional.ofNullable(parkingStatus).ifPresent(ps -> statusValueAr.set(ps.getValue()));
        return statusValueAr.get();
    }

    @Override
    public ParkingStatus convertToEntityAttribute(final String s) {
        return ParkingStatus.of(s);
    }
}
