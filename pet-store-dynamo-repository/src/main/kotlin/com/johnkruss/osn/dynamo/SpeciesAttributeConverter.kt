package com.johnkruss.osn.dynamo

import com.johnkruss.osn.domain.Species
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter
import software.amazon.awssdk.enhanced.dynamodb.AttributeValueType
import software.amazon.awssdk.enhanced.dynamodb.EnhancedType
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

class SpeciesAttributeConverter : AttributeConverter<Species> {
    override fun transformFrom(input: Species): AttributeValue {
        return AttributeValue.builder()
            .s(input.toString())
            .build()
    }

    override fun transformTo(input: AttributeValue): Species {
        return Species.valueOf(input.s())
    }

    override fun type(): EnhancedType<Species> {
        return EnhancedType.of(Species::class.java)
    }

    override fun attributeValueType(): AttributeValueType {
        return AttributeValueType.S
    }
}
