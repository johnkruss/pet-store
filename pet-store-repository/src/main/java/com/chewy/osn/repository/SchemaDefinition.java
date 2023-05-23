package com.chewy.osn.repository;

import com.chewy.osn.domain.Pet;
import com.chewy.osn.domain.Species;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;

import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey;
import static software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primarySortKey;

public class SchemaDefinition {

    public static final TableSchema<Pet> PET_TABLE_SCHEMA =
        StaticTableSchema.builder(Pet.class)
            .newItemSupplier(Pet::new)
            .addAttribute(Species.class, s -> s.name("species")
                .getter(Pet::getSpecies)
                .setter(Pet::setSpecies)
                .tags(primaryPartitionKey())
            .attributeConverter(new SpeciesAttributeConverter()))
            .addAttribute(String.class, i -> i.name("name")
                .getter(Pet::getName)
                .setter(Pet::setName)
                .addTag(primarySortKey()))
            .addAttribute(Integer.class, n -> n.name("cuteness")
                .getter(Pet::getCuteness)
                .setter(Pet::setCuteness))
            .build();
}
