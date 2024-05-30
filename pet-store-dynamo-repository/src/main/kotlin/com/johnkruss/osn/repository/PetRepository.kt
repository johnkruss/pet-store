package com.johnkruss.osn.repository

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.TableSchema

class PetRepository(config: DynamoConfig, schema: TableSchema<Pet>) {
    protected var client: DynamoDbEnhancedClient
    protected var table: DynamoDbTable<Pet>
    protected var schema: TableSchema<Pet>

    init {
        val initializer = DynamoRepositoryInitializer(config)
        client = initializer.init()
        table = client.table(config.tableName, schema)
        this.schema = schema
    }

    fun getPet(
        species: Species,
        name: String,
    ): Pet? {
        val key =
            Key.builder()
                .partitionValue(species.toString())
                .sortValue(name)
                .build()
        return table.getItem(key)
    }

    fun writePet(pet: Pet) {
        table.putItem(pet)
    }

    fun adoptPet(
        species: Species,
        name: String,
    ) {
        val key =
            Key.builder()
                .partitionValue(species.toString())
                .sortValue(name)
                .build()
        table.deleteItem(key)
    }
}
