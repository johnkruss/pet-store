package com.chewy.pet.repository

import com.chewy.pet.domain.Pet
import com.chewy.pet.domain.Species
import java.util.*

fun generatePet(name: String = UUID.randomUUID().toString(),
                species: Species = Species.BIRD,
                cuteness: Int = 9001): Pet {
    return Pet(name, species, cuteness)
}