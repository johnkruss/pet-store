package com.chewy.osn.response

import com.chewy.osn.ObjectMapperBuilder
import com.chewy.osn.domain.Species
import com.chewy.osn.getJsonString
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class CutenessResponseTest: FreeSpec({

    val mapper = ObjectMapperBuilder().build()

    "Map a CutenessResponse to and from JSON" {
        //setup
        val json = getJsonString("CutenessResponse")

        val expected = CutenessResponse(
            Species.BIRD,
            listOf("Zazu", "Tweety"),
            900000
        )

        //execution
        val deserialized: CutenessResponse = mapper.readValue(json)

        //assertion
        deserialized shouldBe expected

        //execution
        val backToJson = mapper.writeValueAsString(expected)

        //assertion
        json shouldBe backToJson
    }
})