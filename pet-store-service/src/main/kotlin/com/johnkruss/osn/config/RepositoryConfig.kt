package com.johnkruss.osn.config

import com.johnkruss.osn.repository.DynamoConfig
import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("repository")
class RepositoryConfig : DynamoConfig()
