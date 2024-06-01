package com.johnkruss.osn.dynamo

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primaryPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags.primarySortKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema

val PET_TABLE_SCHEMA: TableSchema<Pet> =
    StaticTableSchema.builder(Pet::class.java)
        .newItemSupplier { Pet() }
        .addAttribute(Species::class.java) {
            it.name("species")
                .getter(Pet::species)
                .setter { p, s -> p.species = s }
                .attributeConverter(SpeciesAttributeConverter())
                .tags(primaryPartitionKey())
        }
        .addAttribute(String::class.java) {
            it.name("name")
                .getter(Pet::name)
                .setter { p, n -> p.name = n }
                .addTag(primarySortKey())
        }
        .addAttribute(Int::class.java) {
            it.name("cuteness")
                .getter(Pet::cuteness)
                .setter { p, c -> p.cuteness = c }
        }
        .build()
