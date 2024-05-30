package com.johnkruss.osn.domain

import com.fasterxml.jackson.module.kotlin.readValue
import com.johnkruss.osn.ObjectMapperBuilder
import com.johnkruss.osn.getJsonString
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PetTest : FreeSpec({

    val mapper = ObjectMapperBuilder().build()

    "Map a Pet from JSON and back" {
        // setup
        val json = getJsonString("Pet")

        val expected =
            Pet(
                "Otis",
                Species.CAPYBARA,
                78,
            )

        // execution
        val deserialized = mapper.readValue<Pet>(json)

        // assertion
        deserialized shouldBe expected

        // execution
        val backToJson = mapper.writeValueAsString(expected)

        // assertion
        json shouldBe backToJson
    }
})
