package com.chewy.osn.config;

import com.chewy.osn.repository.DynamoConfig;
import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("repository")
public class RepositoryConfig extends DynamoConfig {}
