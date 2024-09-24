package com.johnkruss.osn.sql

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import javax.sql.DataSource

class PetSqlRepository(val context: DSLContext) {
    constructor(datasource: DataSource) : this(DSL.using(datasource.connection, SQLDialect.POSTGRES))

    fun getPet(name: String): Pet {
        val result =
            context.selectFrom("pets")
                .where(DSL.field("name").eq(name))
                .fetchSingle()

        return Pet(
            species = Species.valueOf(result.get("species", String::class.java)),
            name = result.get("name", String::class.java),
            cuteness = result.get("cuteness", Int::class.java),
        )
    }

    fun addPet(pet: Pet): Int {
        val table = DSL.table("pets")
        val nameField = DSL.field("name", String::class.java)
        val speciesField = DSL.field("species", String::class.java)
        val cutenessField = DSL.field("cuteness", Int::class.java)

        // Execute the insert statement
        return context.insertInto(table)
            .set(nameField, pet.name)
            .set(speciesField, pet.species.name)
            .set(cutenessField, pet.cuteness)
            .execute()
    }

    fun totalCuteness(names: List<String>): Int {
        val result =
            context.select(DSL.sum(DSL.field("cuteness", Int::class.java)).`as`("total_cuteness"))
                .from(DSL.table("pets"))
                .where(DSL.field("name").`in`(names))
                .fetchOne()

        return result?.get("total_cuteness", Int::class.java) ?: 0
    }
}
