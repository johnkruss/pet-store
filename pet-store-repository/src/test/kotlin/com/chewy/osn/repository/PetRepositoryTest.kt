package com.chewy.osn.repository

import com.chewy.osn.TestRepositoryInitializer
import com.chewy.osn.domain.Species
import com.chewy.osn.generatePet
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.util.*

class PetRepositoryTest: FreeSpec({

    lateinit var petRepository: PetRepository
    lateinit var config: DynamoConfig

    beforeTest {
        config = DynamoConfig("http://localhost:4566", "pet_table")

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

    "No pet is found after being adopted(deleted)" {
        //setup
        val pet = generatePet()
        petRepository.writePet(pet)

        //execution
        val doesExist = petRepository.getPet(pet.species, pet.name)

        //assertion
        doesExist shouldBe pet

        //execution
        petRepository.adoptPet(pet.species, pet.name)
        val doesNotExist = petRepository.getPet(pet.species, pet.name)

        //assertion
        doesNotExist shouldBe null
    }
})