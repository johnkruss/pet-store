package com.chewy.osn.repository;

import com.chewy.osn.domain.Pet;
import com.chewy.osn.domain.Species;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

public class PetRepository {
    protected DynamoDbEnhancedClient client;
    protected DynamoDbTable<Pet> table;
    protected TableSchema<Pet> schema;

    public PetRepository(DynamoConfig config, TableSchema<Pet> schema) {
        DynamoRepositoryInitializer initializer = new DynamoRepositoryInitializer(config);
        client = initializer.init();
        table = client.table(config.getTableName(), schema);
        this.schema = schema;
    }

    public Pet getPet(Species species, String name) {
        Key key = Key.builder()
                .partitionValue(species.toString())
                .sortValue(name)
                .build();
        return table.getItem(key);
    }
    public void writePet(Pet pet) {
        table.putItem(pet);
    }

    public void adoptPet(Species species, String name) {
        Key key = Key.builder()
                .partitionValue(species.toString())
                .sortValue(name)
                .build();
        table.deleteItem(key);
    }
}
