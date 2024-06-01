package com.johnkruss.osn.sql

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.listeners.AfterProjectListener
import io.kotest.core.listeners.BeforeProjectListener
import org.testcontainers.containers.PostgreSQLContainer

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

    override suspend fun beforeProject() {
        container.start()
    }

    override suspend fun afterProject() {
        container.stop()
    }
}
