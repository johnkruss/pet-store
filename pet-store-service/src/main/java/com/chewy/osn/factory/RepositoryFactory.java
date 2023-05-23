package com.chewy.osn.factory;

import com.chewy.osn.config.RepositoryConfig;
import com.chewy.osn.repository.PetRepository;
import com.chewy.osn.repository.SchemaDefinition;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class RepositoryFactory {

    @Bean
    @Singleton
    public PetRepository getPetRepository(RepositoryConfig config) {
        return new PetRepository(config, SchemaDefinition.PET_TABLE_SCHEMA);
    }
}
