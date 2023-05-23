package com.chewy.pet.repository

import com.chewy.pet.domain.Species
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.util.*

class PetRepositoryTest: FreeSpec({

    lateinit var petRepository: PetRepository
    lateinit var config: DynamoConfig

    beforeTest {
        config = DynamoConfig("http://localhost:4566", "ledger_table")

        TestRepositoryInitializer()
                .withServiceUrl(config.serviceUrl)
                .withTableName(config.tableName)
                .withHashKey("species")
                .withSortKey("name")
                .build()

        petRepository = PetRepository(config, SchemaDefinition.PET_TABLE_SCHEMA)
    }

    "Write a pet to the database, then get it back out" {
        //setup
        val petName = UUID.randomUUID().toString()
        val pet = generatePet(name = petName)

        //execution
        petRepository.writePet(pet)
        val returned = petRepository.getPet(Species.BIRD, petName)

        //assertion
        returned shouldBe pet
    }

    "Null is returned when no pets are found" {
        //setup
        val petName = UUID.randomUUID().toString()

        //execution
        val returned = petRepository.getPet(Species.CAT, petName)

        //assertion
        returned shouldBe null
    }

    "Get back a list of pets for a given species" {
        //setup
        val allTheCats  = listOf(
            generatePet(species = Species.CAT),
            generatePet(species = Species.CAT),
            generatePet(species = Species.CAT),
            generatePet(species = Species.CAT)
        )

        allTheCats.forEach { petRepository.writePet(it) }

        //execution
        val returned = petRepository.getPets(Species.CAT)

        //assertion
        returned shouldBe allTheCats
    }
})