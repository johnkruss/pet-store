package com.johnkruss.osn.dynamo

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

class DynamoRepositoryInitializer(var config: DynamoConfig) {
    fun init(): DynamoDbEnhancedClient {
        val client = createClient(config)
        return createEnhancedClient(client)
    }

    fun createClient(config: DynamoConfig): DynamoDbClient {
        val builder = DynamoDbClient.builder()
        if (config.serviceUrl != null) {
            builder.endpointOverride(URI.create(config.serviceUrl))
            builder.region(Region.of(config.region))
        }
        return builder.build()
    }

    fun createEnhancedClient(dynamoClient: DynamoDbClient): DynamoDbEnhancedClient {
        return DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoClient)
            .build()
    }
}
