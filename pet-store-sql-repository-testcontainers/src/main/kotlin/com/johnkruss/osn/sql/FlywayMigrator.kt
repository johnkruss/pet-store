package com.johnkruss.osn.sql

import org.flywaydb.core.Flyway
import javax.sql.DataSource

class FlywayMigrator(val dataSource: DataSource) {
    fun run() {
        Flyway.configure()
            .dataSource(dataSource)
            .load()
            .migrate()
    }
}
