package com.chewy.pet

import io.kotest.core.spec.style.FreeSpec

fun getJsonString(name: String): String {
    return FreeSpec::class.java.getResource("/${name}.json")?.readText() ?: "{}"
}