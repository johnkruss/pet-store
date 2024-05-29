package com.johnkruss.osn.controller

import com.johnkruss.osn.ObjectMapperBuilder
import com.johnkruss.osn.domain.Species
import com.johnkruss.osn.generatePet
import com.johnkruss.osn.request.CreatePetRequest
import com.johnkruss.osn.response.CutenessResponse
import com.johnkruss.osn.response.GetPetResponse
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.runtime.server.EmbeddedServer
import io.micronaut.test.extensions.kotest5.annotation.MicronautTest
import java.util.UUID

// What type of test is this? Unit? No. Integration? Maybe? Functional? Sure I guess
// It doesn't matter, it proves our app can spin up and our endpoints work

// Bonus round - create a portable library based API client using a tool like Retrofit
// Use it to test your endpoints, THEN publish it for other apps to consume
// Now you have an API client you can confidently use in other places because you dogfood your own code during testing

@MicronautTest
class PetControllerTest(val server: EmbeddedServer) : FreeSpec({

    lateinit var client: BlockingHttpClient
    val mapper = ObjectMapperBuilder().build()

    beforeTest {
        client = HttpClient.create(server.url).toBlocking()
    }

    "Successfully create a pet, then get it back" {
        // setup
        val species = Species.CAT
        val name = UUID.randomUUID().toString()
        val cuteness = 104
        val request = mapper.writeValueAsString(CreatePetRequest(name, cuteness))

        // execution
        val response = client.exchange(HttpRequest.POST("/v1/species/$species", request), Unit.javaClass)

        // assertions
        response.status() shouldBe HttpStatus.OK

        // execution
        val petResponse = client.exchange("/v1/species/$species/names/$name", GetPetResponse::class.java)

        // assertions
        petResponse.status() shouldBe HttpStatus.OK
        petResponse.body()?.cuteness shouldBe 104
    }

    "Not allowed to double create a pet!" {
        // setup
        val species = Species.CAT
        val name = UUID.randomUUID().toString()
        val cuteness = 104
        val request = mapper.writeValueAsString(CreatePetRequest(name, cuteness))

        // execution
        val response = client.exchange(HttpRequest.POST("/v1/species/$species", request), Unit.javaClass)

        // assertions
        response.status() shouldBe HttpStatus.OK

        // execution
        val exception =
            shouldThrow<HttpClientResponseException> {
                client.exchange(HttpRequest.POST("/v1/species/$species", request), Unit.javaClass)
            }

        // assertions
        exception.status shouldBe HttpStatus.CONFLICT
        exception.message shouldBe "We already have a CAT that's named $name"
    }

    "Add up all the cute!" {
        // setup
        val pets =
            listOf(
                generatePet(species = Species.DOG, cuteness = 10),
                generatePet(species = Species.DOG, cuteness = 20),
                generatePet(species = Species.DOG, cuteness = 0),
                generatePet(species = Species.CAT, cuteness = 10),
            )

        val nameString = pets.map { it.name }.joinToString(",")

        pets.forEach { pet ->
            val request = mapper.writeValueAsString(CreatePetRequest(pet.name, pet.cuteness!!))
            client.exchange(HttpRequest.POST("/v1/species/${pet.species}", request), Unit.javaClass)
        }

        // execution
        val petResponse = client.exchange("/v1/species/${Species.DOG}?names=$nameString", CutenessResponse::class.java)

        // assertions
        petResponse.status() shouldBe HttpStatus.OK
        petResponse.body()?.totalCuteness shouldBe 30
    }
})
