package com.johnkruss.osn

import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.Projection
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import java.net.URI

class TestRepositoryInitializer {
    lateinit var dynamoDb: DynamoDbClient
    var hashKeySchemaElementBuilder: KeySchemaElement.Builder
    var sortKeySchemaElementBuilder: KeySchemaElement.Builder
    var dynamoDbClientBuilder: DynamoDbClientBuilder
    var attributeDefinitionBuilder: AttributeDefinition.Builder
    var createTableRequestBuilder: CreateTableRequest.Builder
    var provisionedThroughputBuilder: ProvisionedThroughput.Builder
    var projectionBuilder: Projection.Builder
    var definitions: MutableList<AttributeDefinition> = ArrayList()
    var keySchemaElements: MutableList<KeySchemaElement> = ArrayList()
    var tableName: String? = null

    // default values that can be overwritten
    var serviceUrl = "http://localhost:4566"
    var region = "us-east-1"
    var readCapacityUnits = 10L
    var writeCapacityUnits = 5L

    init {
        hashKeySchemaElementBuilder = KeySchemaElement.builder()
        sortKeySchemaElementBuilder = KeySchemaElement.builder()
        dynamoDbClientBuilder = DynamoDbClient.builder()
        attributeDefinitionBuilder = AttributeDefinition.builder()
        createTableRequestBuilder = CreateTableRequest.builder()
        provisionedThroughputBuilder = ProvisionedThroughput.builder()
        projectionBuilder = Projection.builder()
    }

    fun build() {
        create()
    }

    fun create() {
        dynamoDb =
            dynamoDbClientBuilder
                .endpointOverride(URI.create(serviceUrl))
                .region(Region.of(region))
                .build()
        if (!tableExists(tableName)) {
            createTableRequestBuilder
                .provisionedThroughput(
                    provisionedThroughputBuilder
                        .readCapacityUnits(readCapacityUnits)
                        .writeCapacityUnits(writeCapacityUnits)
                        .build(),
                )
                .attributeDefinitions(definitions)
                .keySchema(keySchemaElements)
            dynamoDb.createTable(createTableRequestBuilder.build())
        }
    }

    fun withHashKey(hashKey: String): TestRepositoryInitializer {
        keySchemaElements.add(
            hashKeySchemaElementBuilder
                .attributeName(hashKey)
                .keyType(KeyType.HASH)
                .build(),
        )
        if (definitionsNeedsKey(hashKey)) {
            definitions.add(
                attributeDefinitionBuilder
                    .attributeName(hashKey)
                    .attributeType(ScalarAttributeType.S)
                    .build(),
            )
        }
        return this
    }

    fun withSortKey(sortKey: String): TestRepositoryInitializer {
        keySchemaElements.add(
            sortKeySchemaElementBuilder
                .attributeName(sortKey)
                .keyType(KeyType.RANGE)
                .build(),
        )
        if (definitionsNeedsKey(sortKey)) {
            definitions.add(
                attributeDefinitionBuilder
                    .attributeName(sortKey)
                    .attributeType(ScalarAttributeType.S)
                    .build(),
            )
        }
        return this
    }

    fun withServiceUrl(serviceUrl: String): TestRepositoryInitializer {
        this.serviceUrl = serviceUrl
        return this
    }

    fun withRegion(region: String): TestRepositoryInitializer {
        this.region = region
        return this
    }

    fun withTableName(tableName: String?): TestRepositoryInitializer {
        createTableRequestBuilder.tableName(tableName)
        this.tableName = tableName
        return this
    }

    fun tableExists(tableName: String?): Boolean {
        val tableRequest =
            DescribeTableRequest.builder()
                .tableName(tableName)
                .build()
        try {
            dynamoDb.describeTable(tableRequest)
        } catch (e: ResourceNotFoundException) {
            return false
        }
        return true
    }

    fun definitionsNeedsKey(key: String): Boolean {
        return definitions.stream().noneMatch { d: AttributeDefinition -> d.attributeName() == key }
    }
}
