package com.chewy.osn.controller

import com.chewy.osn.ObjectMapperBuilder
import com.chewy.osn.TestRepositoryInitializer
import com.chewy.osn.config.RepositoryConfig
import com.chewy.osn.domain.Species
import com.chewy.osn.request.CreatePetRequest
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import jakarta.inject.Inject
import java.util.*

@MicronautTest
class PetControllerTest(@Inject val server: EmbeddedServer, val config: RepositoryConfig): FreeSpec({

    lateinit var client: BlockingHttpClient
    val mapper = ObjectMapperBuilder().build()

    beforeTest {
        //ensure our database table is spun up and ready to go
        TestRepositoryInitializer()
            .withServiceUrl(config.serviceUrl)
            .withTableName(config.tableName)
            .withHashKey("species")
            .withSortKey("name")
            .build()

        client = HttpClient.create(server.url).toBlocking()
    }

    "Successfully create a pet, then get it back" {
        //setup
        val species = Species.CAT
        val name = UUID.randomUUID().toString()
        val cuteness = 104
        val request = mapper.writeValueAsString(CreatePetRequest(name, cuteness))

        //execution
        val response = client.exchange(HttpRequest.POST("/v1/species/$species", request), Unit.javaClass)

        //assertions
        response.code() shouldBe 200
    }
})