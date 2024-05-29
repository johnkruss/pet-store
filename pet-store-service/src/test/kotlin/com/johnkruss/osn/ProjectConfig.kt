package com.johnkruss.osn

import io.kotest.core.config.AbstractProjectConfig
import io.micronaut.test.extensions.kotest5.MicronautKotest5Extension

@Suppress("unused")
object ProjectConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(MicronautKotest5Extension)

    override suspend fun beforeProject() {
        TestRepositoryInitializer()
            .withServiceUrl("http://localhost:4566")
            .withTableName("pet_table")
            .withHashKey("species")
            .withSortKey("name")
            .build()
    }
}
