package com.chewy.osn.repository;

import com.chewy.osn.domain.Species;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType;
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class SpeciesAttributeConverter implements AttributeConverter<Species> {

    @Override
    public AttributeValue transformFrom(Species input) {
        return AttributeValue.builder()
            .s(input.toString())
            .build();
    }

    @Override
    public Species transformTo(AttributeValue input) {
        return Species.valueOf(input.s());
    }

    @Override
    public EnhancedType<Species> type() {
        return EnhancedType.of(Species.class);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
