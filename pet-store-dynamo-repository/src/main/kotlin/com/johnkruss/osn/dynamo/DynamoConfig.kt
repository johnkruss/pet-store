package com.johnkruss.osn.dynamo

open class DynamoConfig(
    var serviceUrl: String? = null,
    var region: String = "us-east-1",
    var tableName: String = "tableName",
)
