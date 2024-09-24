package com.johnkruss.osn.sql

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import org.jooq.Condition
import org.jooq.DSLContext
import org.jooq.Field
import org.jooq.InsertSetMoreStep
import org.jooq.InsertSetStep
import org.jooq.Record
import org.jooq.SelectConditionStep
import org.jooq.SelectWhereStep
import org.jooq.Table
import org.jooq.exception.DataAccessException

/*
* Don't do this. Stop unit testing database code. When our mocking gets this heavy we're effectively just testing
* the test framework itself. There is virtually no value in writing tests that mock this hard and verify nothing
* about our actual queries or database interactions. Does your SQL even work? Who can say! Not these tests...
* */
class BadPetSqlRepositoryTest : FreeSpec({

    lateinit var context: DSLContext
    lateinit var repository: PetSqlRepository

    beforeTest {
        context = mockk()
        repository = PetSqlRepository(context)
    }

    "Call a pet by name!" {
        val name = "Finley"

        val select: SelectWhereStep<Record> = mockk()
        val selectCondition: SelectConditionStep<Record> = mockk()
        val record: Record = mockk()

        every { context.selectFrom(any<String>()) }.returns(select)
        every { select.where(any<Condition>()) }.returns(selectCondition)
        every { selectCondition.fetchSingle() }.returns(record)
        every { record.get("species", String::class.java) }.returns("DOG")
        every { record.get("name", String::class.java) }.returns("Finley")
        every { record.get("cuteness", Int::class.java) }.returns(9001)

        val result = repository.getPet(name)

        /*
         * By this point you're already frustrated from writing out mocks for the last 15 minutes, so you don't even
         * bother to assert anything meaningful about the test
         *
         * What if no pets with the name are found? Does that return null? Is an exception thrown? Who can say!
         * */
        result shouldNotBe null
    }

    "Add a pet!" {
        val name = "Finley"
        val species = Species.DOG
        val cuteness = 42

        val pet =
            Pet(
                name = name,
                species = species,
                cuteness = cuteness,
            )

        val insert: InsertSetStep<Record> = mockk()
        val insertMore: InsertSetMoreStep<Record> = mockk()

        every { context.insertInto(any<Table<Record>>()) }.returns(insert)
        every { insert.set(any<Field<String>>(), any<String>()) }.returns(insertMore)
        every { insertMore.set(any<Field<Int>>(), any<Int>()) }.returns(insertMore)
        every { insertMore.execute() }.returns(1)

        /*
         * There's nothing else we can check here other than the return, which I suppose is 1...
         * because I told the mock it should be
         * */
        repository.addPet(pet) shouldBe 1
    }

    "Adding a pet failed I guess ¯\\_(ツ)_/¯" {
        val name = "Finley"
        val species = Species.DOG
        val cuteness = 42

        val pet =
            Pet(
                name = name,
                species = species,
                cuteness = cuteness,
            )

        val insert: InsertSetStep<Record> = mockk()
        val insertMore: InsertSetMoreStep<Record> = mockk()

        every { context.insertInto(any<Table<Record>>()) }.returns(insert)
        every { insert.set(any<Field<String>>(), any<String>()) }.returns(insertMore)
        every { insertMore.set(any<Field<Int>>(), any<Int>()) }.returns(insertMore)
        every { insertMore.execute() }.throws(DataAccessException("You violated the uniqueness constraint!"))

        /*
         * When thing is done throw exception, assert exception thrown....
         * This is the error thrown when we violate our uniqueness constraint but there's no way of verifying that
         * via unit test
         * */
        shouldThrow<DataAccessException> {
            repository.addPet(pet)
        }
    }

    /*
     * And so on.... I could provide more examples, but I'm getting sick of typing out mocks
     * */
})
