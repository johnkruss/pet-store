package com.johnkruss.osn.response

import com.fasterxml.jackson.module.kotlin.readValue
import com.johnkruss.osn.ObjectMapperBuilder
import com.johnkruss.osn.getJsonString
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GetPetResponseTest : FreeSpec({

    val mapper = ObjectMapperBuilder().build()

    "Map a GetPetResponse to and from JSON" {
        // setup
        val json = getJsonString("GetPetResponse")

        val expected = GetPetResponse(400)

        // execution
        val deserialized = mapper.readValue<GetPetResponse>(json)

        // assertion
        deserialized shouldBe expected

        // execution
        val backToJson = mapper.writeValueAsString(expected)

        // assertion
        json shouldBe backToJson
    }
})
