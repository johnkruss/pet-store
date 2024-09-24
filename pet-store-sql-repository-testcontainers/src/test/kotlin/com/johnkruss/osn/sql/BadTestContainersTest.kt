package com.johnkruss.osn.sql

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import com.johnkruss.osn.generatePet
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import org.jooq.exception.DataAccessException
import org.jooq.exception.NoDataFoundException
import org.testcontainers.containers.PostgreSQLContainer
import java.util.UUID

class BadTestContainersTest : FreeSpec({

    lateinit var repository: PetSqlRepository
    val container =
        PostgreSQLContainer<Nothing>("postgres:16").apply {
            withDatabaseName("pet_Store")
            withUsername("username")
            withPassword("password")
            // Not normally needed, just used to avoid port collision between the 3 different Postgres Containers
            withCreateContainerCmdModifier {
                HostConfig().apply {
                    withPortBindings(PortBinding(Ports.Binding.bindPort(9876), ExposedPort(5432)))
                }
            }
        }

    /*
     *  This is spinning up and then killing a database container for each test in the file.
     *  It will also open/close a new connection, as well as run flyway
     * This is going to be very slow and expensive, but people do it by accident all the time
     * */
    beforeTest {
        container.start()

        val config =
            HikariConfig().apply {
                jdbcUrl = container.jdbcUrl
                username = container.username
                password = container.password
            }
        val dataSource = HikariDataSource(config)

        FlywayMigrator(dataSource).run()

        repository = PetSqlRepository(dataSource)
    }

    afterTest {
        container.stop()
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
