package com.johnkruss.osn.sql

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import org.testcontainers.containers.PostgreSQLContainer

/*
* Most modern testing frameworks expose some sort of lifecycle hooks to be able to do pre/post test setup and teardown
* Highly recommend using it to keep this infrastructure setup in 1 place
* */
@Suppress("unused")
object ProjectConfig : AbstractProjectConfig(), BeforeProjectListener, AfterProjectListener {
    val container =
        PostgreSQLContainer<Nothing>("postgres:16").apply {
            withDatabaseName("pet_Store")
            withUsername("username")
            withPassword("password")
            // Not normally needed, just used to avoid port collision between the 3 different Postgres Containers
            withCreateContainerCmdModifier {
                HostConfig().apply {
                    withPortBindings(PortBinding(Ports.Binding.bindPort(5678), ExposedPort(5432)))
                }
            }
        }

    lateinit var datasource: HikariDataSource

    /*
     * Handle our setup and cleanup in 1 spot - this ensures we aren't wasting cycles starting/stopping containers
     * It also gives us the luxury of managing connection config all in one place rather than per test
     * */
    override suspend fun beforeProject() {
        container.start()
        val config =
            HikariConfig().apply {
                // Just pull the configs straight off the container rather than piecing together strings
                jdbcUrl = container.jdbcUrl
                username = container.username
                password = container.password
            }
        datasource = HikariDataSource(config)
        FlywayMigrator(datasource).run()
    }

    override suspend fun afterProject() {
        container.stop()
        datasource.close()
    }
}
