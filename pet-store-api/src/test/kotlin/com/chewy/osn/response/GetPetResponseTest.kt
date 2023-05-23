package com.chewy.osn.response

import com.chewy.osn.ObjectMapperBuilder
import com.chewy.osn.getJsonString
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GetPetResponseTest: FreeSpec({

    val mapper = ObjectMapperBuilder().build()

    "Map a GetPetResponse to and from JSON" {
        //setup
        val json = getJsonString("GetPetResponse")

        val expected = GetPetResponse(400)

        //execution
        val deserialized: GetPetResponse = mapper.readValue(json)

        //assertion
        deserialized shouldBe expected

        //execution
        val backToJson = mapper.writeValueAsString(expected)

        //assertion
        json shouldBe backToJson
    }
})