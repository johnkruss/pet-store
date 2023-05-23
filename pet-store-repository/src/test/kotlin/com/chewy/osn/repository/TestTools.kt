package com.chewy.osn.repository

import com.chewy.osn.domain.Pet
import com.chewy.osn.domain.Species
import java.util.*

fun generatePet(name: String = UUID.randomUUID().toString(),
                species: Species = Species.BIRD,
                cuteness: Int = 9001): Pet {
    return Pet(name, species, cuteness)
}