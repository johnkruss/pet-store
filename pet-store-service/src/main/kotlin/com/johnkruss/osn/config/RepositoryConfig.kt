package com.johnkruss.osn.config

import com.johnkruss.osn.dynamo.DynamoConfig
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("repository")
class RepositoryConfig : DynamoConfig()
