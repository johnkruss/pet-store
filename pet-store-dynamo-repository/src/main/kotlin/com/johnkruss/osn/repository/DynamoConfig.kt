package com.johnkruss.osn.repository

open class DynamoConfig(
    var serviceUrl: String? = null,
    var region: String = "us-east-1",
    var tableName: String = "tableName",
)
