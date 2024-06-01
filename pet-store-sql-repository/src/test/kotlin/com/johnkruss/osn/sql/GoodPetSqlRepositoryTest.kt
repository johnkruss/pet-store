package com.johnkruss.osn.sql

import com.johnkruss.osn.generatePet
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.jooq.exception.DataAccessException
import org.jooq.exception.NoDataFoundException
import java.util.UUID

/*
* Call this test whatever you want, I call it "proof your queries actually work"
* When we extract our code out into small working pieces like this we don't even need a full application framework to
* be able to run our tests. We can now effectively test this code in isolation! Of course this approach is still
* compatible with a larger application framework, but it isn't required
*
* A quick note: Don't forget to run docker compose up -d over in your terminal before trying to run this test. We need
* real database containers running!
*
* Goofed up your flyway migration and need to fix it? Good thing we tested here instead of against a live db right?
* Simply spin down your docker containers and bring them back up. Now try again!
*
* Another quick note: I find it easier to run containers without mounted data volumes so that every time they're spun
* down and back up, all the data in them is wiped out
* */
class GoodPetSqlRepositoryTest : FreeSpec({

    lateinit var repository: PetSqlRepository
    lateinit var dataSource: HikariDataSource

    beforeSpec {
        // Let's make a real connection, this could be parameterized for environment vars if needed
        val config =
            HikariConfig().apply {
                jdbcUrl = "jdbc:postgresql://localhost:5432/pet_Store"
                username = "username"
                password = "password"
            }
        dataSource = HikariDataSource(config)

        /*
         * Look here, now we're testing our ACTUAL database migration code against an ACTUAL database!
         * */
        FlywayMigrator(dataSource).run()

        repository = PetSqlRepository(dataSource)
    }

    afterSpec {
        dataSource.close()
    }

    "Add a pet, then call it back" {
        /*
         * Note that we need randomness here because we're working against a live db
         * If we used the same static strings we'd run into all sorts of wacky problems from duplicate data
         * */
        val name = UUID.randomUUID().toString()
        val pet = generatePet(name = name)

        repository.addPet(pet)
        val returned = repository.getPet(name)

        // Pet goes into and comes back out of the table as expected
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

    /*
     * So what does happen if we search for a pet that doesn't exist?
     * Well let's find out!
     * */
    "Exception thrown when no pet is found" {
        val name = UUID.randomUUID().toString()

        val exception = shouldThrow<NoDataFoundException> { repository.getPet(name) }

        exception.message shouldBe "Cursor returned no rows"
    }

    /*
     * Rather than attempting to spin through a lot of mock interacts and fake out what the database is doing
     * Let's just add some data and make sure our queries do what we expect
     * */
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
