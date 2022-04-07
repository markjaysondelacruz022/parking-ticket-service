package com.mjdc.pts.converter;

import com.mjdc.pts.enumeration.PriceType;
import com.mjdc.pts.enumeration.Size;

import javax.persistence.AttributeConverter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SizeAttributeConverter implements AttributeConverter<Size, String> {
    @Override
    public String convertToDatabaseColumn(final Size priceType) {
        final AtomicReference<String> sizeValueAr = new AtomicReference<>(null);
        Optional.ofNullable(priceType).ifPresent(pt -> sizeValueAr.set(pt.getValue()));
        return sizeValueAr.get();
    }

    @Override
    public Size convertToEntityAttribute(final String s) {
        return Size.of(s);
    }
}
