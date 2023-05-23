package com.chewy.osn.domain

import com.chewy.osn.ObjectMapperBuilder
import com.chewy.osn.getJsonString
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PetTest: FreeSpec ({

    val mapper = ObjectMapperBuilder().build()

    "Map a Pet from JSON and back" {
        //setup
        val json = getJsonString("Pet")

        val expected = Pet(
            "Otis",
            Species.CAPYBARA,
            78
        )

        //execution
        val deserialized: Pet = mapper.readValue(json)

        //assertion
        deserialized shouldBe expected

        //execution
        val backToJson = mapper.writeValueAsString(expected)

        //assertion
        json shouldBe backToJson
    }
})