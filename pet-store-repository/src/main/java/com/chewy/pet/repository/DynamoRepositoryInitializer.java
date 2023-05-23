package com.chewy.pet.repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

import java.net.URI;

public class DynamoRepositoryInitializer {

    DynamoConfig config;

    public DynamoRepositoryInitializer(DynamoConfig config) {
        this.config = config;
    }

    public DynamoDbEnhancedClient init() {
        DynamoDbClient client = createClient(config);
        return createEnhancedClient(client);
    }

    public DynamoDbClient createClient(DynamoConfig config) {
        DynamoDbClientBuilder builder = DynamoDbClient.builder();
        if (config.getServiceUrl() != null) {
            builder.endpointOverride(URI.create(config.getServiceUrl()));
            builder.region(Region.of(config.getRegion()));
        }
        return builder.build();
    }

    public DynamoDbEnhancedClient createEnhancedClient(DynamoDbClient dynamoClient) {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoClient)
                .build();
    }
}