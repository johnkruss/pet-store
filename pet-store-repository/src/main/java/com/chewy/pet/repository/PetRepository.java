package com.chewy.pet.repository;

import com.chewy.pet.domain.Pet;
import com.chewy.pet.domain.Species;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<Pet> getPets(Species species) {
        QueryConditional queryConditional = QueryConditional
                .keyEqualTo(Key.builder()
                        .partitionValue(species.toString())
                        .build());

        return table.query(queryConditional)
                .items().stream()
                .collect(Collectors.toList());
    }
    public void writePet(Pet pet) {
        table.putItem(pet);
    }

    public void deleteItem(Species species, String name) {
        Key key = Key.builder()
                .partitionValue(species.toString())
                .sortValue(name)
                .build();
        table.deleteItem(key);
    }
}
