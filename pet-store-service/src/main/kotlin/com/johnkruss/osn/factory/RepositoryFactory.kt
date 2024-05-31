package com.johnkruss.osn.factory

import com.johnkruss.osn.config.RepositoryConfig
import com.johnkruss.osn.dynamo.PET_TABLE_SCHEMA
import com.johnkruss.osn.dynamo.PetRepository
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton

@Factory
class RepositoryFactory {
    @Bean
    @Singleton
    fun getPetRepository(config: RepositoryConfig): PetRepository {
        return PetRepository(config, PET_TABLE_SCHEMA)
    }
}
