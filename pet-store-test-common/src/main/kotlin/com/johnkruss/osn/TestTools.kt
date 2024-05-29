package com.johnkruss.osn

import com.johnkruss.osn.domain.Pet
import com.johnkruss.osn.domain.Species
import java.util.UUID

fun generatePet(
    name: String = UUID.randomUUID().toString(),
    species: Species = Species.BIRD,
    cuteness: Int? = 9001,
): Pet {
    return Pet(name, species, cuteness)
}

fun getJsonString(name: String): String {
    return object {}::class.java.getResource("/$name.json")?.readText() ?: "{}"
}
