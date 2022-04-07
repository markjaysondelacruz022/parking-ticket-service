package com.mjdc.pts.converter;

import com.mjdc.pts.enumeration.PriceType;

import javax.persistence.AttributeConverter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class PriceTypeAttributeConverter implements AttributeConverter<PriceType, String> {
    @Override
    public String convertToDatabaseColumn(final PriceType priceType) {
        final AtomicReference<String> typeValueAr = new AtomicReference<>(null);
        Optional.ofNullable(priceType).ifPresent(pt -> typeValueAr.set(pt.getValue()));
        return typeValueAr.get();
    }

    @Override
    public PriceType convertToEntityAttribute(final String s) {
        return PriceType.of(s);
    }
}
