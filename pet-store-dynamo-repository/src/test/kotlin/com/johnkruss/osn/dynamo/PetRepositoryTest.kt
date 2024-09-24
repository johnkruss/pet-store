package com.johnkruss.osn.dynamo

import com.johnkruss.osn.TestRepositoryInitializer
import com.johnkruss.osn.domain.Species
import com.johnkruss.osn.generatePet
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.util.UUID

class PetRepositoryTest : FreeSpec({

    lateinit var petRepository: PetRepository
    lateinit var config: DynamoConfig

    beforeTest {
        config =
            DynamoConfig(
                serviceUrl = "http://localhost:4566",
                region = "us-east-1",
                tableName = "pet_table",
            )

        TestRepositoryInitializer()
            .withServiceUrl(config.serviceUrl!!)
            .withTableName(config.tableName)
            .withHashKey("species")
            .withSortKey("name")
            .build()

        petRepository =
            PetRepository(
                config,
                PET_TABLE_SCHEMA,
            )
    }

    "Write a pet to the database, then get it back out" {
        // setup
        val petName = UUID.randomUUID().toString()
        val pet = generatePet(name = petName)

        // execution
        petRepository.writePet(pet)
        val returned = petRepository.getPet(Species.BIRD, petName)

        // assertion
        returned shouldBe pet
    }

    "Null is returned when no pets are found" {
        // setup
        val petName = UUID.randomUUID().toString()

        // execution
        val returned = petRepository.getPet(Species.CAT, petName)

        // assertion
        returned shouldBe null
    }

    "No pet is found after being adopted(deleted)" {
        // setup
        val pet = generatePet()
        petRepository.writePet(pet)

        // execution
        val doesExist = petRepository.getPet(pet.species, pet.name)

        // assertion
        doesExist shouldBe pet

        // execution
        petRepository.adoptPet(pet.species, pet.name)
        val doesNotExist = petRepository.getPet(pet.species, pet.name)

        // assertion
        doesNotExist shouldBe null
    }
})
