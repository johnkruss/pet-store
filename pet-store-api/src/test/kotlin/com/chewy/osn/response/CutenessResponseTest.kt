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

        //Running this test forward and back is super useful when working with someone else's contract
        //Hit their API, grab a sample response, then test like this to ensure all fields are ingested as expected
        //Going back to JSON ensures we didn't mutate/miss any fields in the payload
        //execution
        val backToJson = mapper.writeValueAsString(expected)

        //assertion
        json shouldBe backToJson
    }
})