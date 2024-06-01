package com.johnkruss.osn.sql

import com.johnkruss.osn.generatePet
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.jooq.exception.DataAccessException
import org.jooq.exception.NoDataFoundException
import java.util.UUID

/*
* For more detailed notes about the tests themselves, check out pet-store-sql-repository
* */
class GoodTestContainersTest : FreeSpec({

    lateinit var repository: PetSqlRepository

    beforeTest {
        // Leverage the setup we did in ProjectConfig, our database is all ready to go
        repository = PetSqlRepository(ProjectConfig.datasource)
    }

    "Add a pet, then call it back" {
        val name = UUID.randomUUID().toString()
        val pet = generatePet(name = name)

        repository.addPet(pet)
        val returned = repository.getPet(name)

        returned shouldBe pet
    }

    "Exception thrown when duplicate pet is added" {
        val name = UUID.randomUUID().toString()
        val pet = generatePet(name = name)

        repository.addPet(pet)
        val exception = shouldThrow<DataAccessException> { repository.addPet(pet) }

        exception.message shouldBe
            """
            SQL [insert into pets (name, species, cuteness) values (?, ?, ?)]; ERROR: duplicate key value violates unique constraint "unique_species_name"
              Detail: Key (species, name)=(BIRD, $name) already exists.
            """.trimIndent()
    }

    "Exception thrown when no pet is found" {
        val name = UUID.randomUUID().toString()

        val exception = shouldThrow<NoDataFoundException> { repository.getPet(name) }

        exception.message shouldBe "Cursor returned no rows"
    }

    "Sum up the cuteness of some pets" {
        val name1 = UUID.randomUUID().toString()
        val name2 = UUID.randomUUID().toString()
        val name3 = UUID.randomUUID().toString()
        val name4 = UUID.randomUUID().toString()

        repository.addPet(generatePet(name = name1, cuteness = 85))
        repository.addPet(generatePet(name = name2, cuteness = 12))
        repository.addPet(generatePet(name = name3, cuteness = 105))
        repository.addPet(generatePet(name = name4, cuteness = 68))

        val returned = repository.totalCuteness(listOf(name1, name4))

        returned shouldBe 153
    }
})
