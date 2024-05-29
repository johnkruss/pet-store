package com.johnkruss.osn.request

import com.fasterxml.jackson.module.kotlin.readValue
import com.johnkruss.osn.ObjectMapperBuilder
import com.johnkruss.osn.getJsonString
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CreatePetRequestTest : FreeSpec({

    val mapper = ObjectMapperBuilder().build()

    "Map a CreatePetRequest from JSON and back" {
        // setup
        val json = getJsonString("CreatePetRequest")

        val expected =
            CreatePetRequest(
                "Dale",
                100,
            )

        // execution
        val deserialized = mapper.readValue<CreatePetRequest>(json)

        // assertion
        deserialized shouldBe expected

        // execution
        val backToJson = mapper.writeValueAsString(expected)

        // assertion
        json shouldBe backToJson
    }
})
